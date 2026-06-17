package eu.europa.ted.eforms.sdk.analysis.validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import com.helger.commons.error.IError;
import com.helger.commons.error.list.IErrorList;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.SchematronHelper;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.pure.errorhandler.CollectingPSErrorHandler;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.serialize.read.SAXReaderSettings;
import com.helger.xml.transform.TransformSourceFactory;

import eu.europa.ted.eforms.sdk.analysis.SdkLoader;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.SchematronFileFact;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates the content of Schematron files.
 * It does not use Schematron to validate something else.
 */
public class SchematronValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(SchematronValidator.class);

  private final SdkLoader sdkLoader;
  private final Set<ValidationResult> results;

  // Each finding carries a stable category (its Problem) as the report's problem statement, so the
  // summary groups Schematron failures by category instead of by their per-file, line-specific text.
  private final List<Finding> findings = new ArrayList<>();

  public SchematronValidator(Path sdkRoot) throws IOException {
    Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    this.sdkLoader = new SdkLoader(Path.of(sdkRoot.toString()));

    this.results = new HashSet<>();
  }

  @Override
  public Validator validate() throws Exception {
    logger.debug("Validating Schematron files");

    sdkLoader.getSchematronFilesPaths().forEach(file -> {
      if (file == null) {
        return;
      }

      // Create a fact for the file being validated, to use in the validation result
      SchematronFileFact schematronFileFact = new SchematronFileFact(new SchematronFile(file));

      IReadableResource schematron = new FileSystemResource(file);

      // Skip the execution check when the file is not even well-formed XML — it would only
      // produce a redundant parse failure for the same file.
      if (checkAgainstSchema(schematronFileFact, schematron)) {
        checkCanExecute(schematronFileFact, schematron);
      }
    });

    return this;
  }

  /** @return true if the file is well-formed XML (so the execution check is worth running). */
  private boolean checkAgainstSchema(SchematronFileFact schematronFileFact,
      IReadableResource schematron) {
    // Capture XML well-formedness errors instead of letting the parser log them.
    // A set so the same parse error reported via error() and fatalError() is listed once.
    final Set<String> saxErrors = new LinkedHashSet<>();
    final SAXReaderSettings saxSettings = new SAXReaderSettings().setErrorHandler(new ErrorHandler() {
      @Override
      public void warning(final SAXParseException e) {
        // Warnings are not reported.
      }

      @Override
      public void error(final SAXParseException e) {
        saxErrors.add(formatSaxError(e));
      }

      @Override
      public void fatalError(final SAXParseException e) {
        saxErrors.add(formatSaxError(e));
      }
    });

    // Resolve all included files, so that they also get validated.
    final IMicroDocument doc = SchematronHelper.getWithResolvedSchematronIncludes(schematron,
        saxSettings, e -> handleError(e, schematronFileFact, Problem.NOT_WELL_FORMED));

    if (doc == null) {
      final String message = saxErrors.isEmpty()
          ? "File is not well-formed XML"
          : "File is not well-formed XML: " + String.join("; ", saxErrors);
      record(schematronFileFact, message, Problem.NOT_WELL_FORMED);
      return false;
    }

    String resolved = MicroWriter.getNodeAsString(doc);
    if (resolved == null) {
      record(schematronFileFact, "Resolved schematron could not be processed",
          Problem.PROCESSING_FAILURE);
      return true;
    }
    Source source = TransformSourceFactory.create(resolved);
    // This will return an empty list if the schematron is valid.
    IErrorList errors = com.helger.schematron.validator.SchematronValidator.validateSchematron(source);

    if (errors != null) {
      errors.forEach(e -> handleError(e, schematronFileFact, Problem.SCHEMA_VIOLATION));
    } else {
      record(schematronFileFact, "Error while validating schematron", Problem.PROCESSING_FAILURE);
    }
    return true;
  }

  private void handleError(IError error, SchematronFileFact schematronFileFact, Problem problem) {
    if (error.getErrorLevel().isError()) {
      // Fixed locale: error text feeds the report, which must be stable across CI runners.
      record(schematronFileFact, error.getErrorText(Locale.ENGLISH), problem);
    }
  }

  
  private String formatSaxError(final SAXParseException e) {
    return String.format(Locale.ENGLISH, "line %d, column %d: %s",
        e.getLineNumber(), e.getColumnNumber(), e.getMessage());
  }

  private void checkCanExecute(SchematronFileFact schematronFileFact, IReadableResource schematron) {
    final SchematronResourcePure phSchematron = new SchematronResourcePure(schematron);
    // Collect the precise pre-compilation errors instead of letting the default
    // handler log them to the console; report one result per file with the
    // collected detail in its message.
    final CollectingPSErrorHandler errorHandler = new CollectingPSErrorHandler();
    phSchematron.setErrorHandler(errorHandler);
    try {
      // Execute the schematron on a dummy XML.
      // Having an XML that causes all rules to be executed is not possible anyway.
      Source source = new StreamSource(new StringReader("<a></a>"));
      phSchematron.applySchematronValidation(source);
    } catch (Exception e) {
      record(schematronFileFact,
          "Error during execution: " + describeCollectedErrors(errorHandler, e),
          Problem.CANNOT_COMPILE);
    }
  }

  private String describeCollectedErrors(final CollectingPSErrorHandler errorHandler,
      final Exception fallback) {
    final String detail = errorHandler.getAllErrors().stream()
        .filter(error -> error.getErrorLevel().isError())
        .map(error -> error.getErrorText(Locale.ENGLISH))
        .collect(Collectors.joining("; "));
    if (!detail.isBlank()) {
      return detail;
    }
    // Some exceptions carry no message; fall back to the type+message rather than "null".
    final String message = fallback.getMessage();
    return message == null || message.isBlank() ? fallback.toString() : message;
  }

  /**
   * Records a Schematron failure: the full, detailed message (which file, which line, which XPath)
   * goes on the {@link ValidationResult}, shown verbatim only in the detail report
   * ({@code analyzer-report.txt}); the
   * {@link Finding}'s problem is the stable {@link Problem} category, so the summary and actionable
   * items group failures by category rather than by their unique per-file text.
   */
  private void record(final SchematronFileFact fact, final String message, final Problem problem) {
    final ValidationResult result = new ValidationResult(fact, message, ValidationStatusEnum.ERROR);
    this.results.add(result);
    this.findings.add(new Finding(getClass().getSimpleName(), problem.statement(), result));
  }

  @Override
  public Set<ValidationResult> getResults() {
    return results;
  }

  /**
   * Overrides the default (message-as-problem) behaviour: each finding's problem is its stable
   * {@link Problem} category set in {@link #record}, so the report groups Schematron failures by
   * category, not by their raw per-file message.
   */
  @Override
  public List<Finding> getFindings() {
    return new ArrayList<>(this.findings);
  }

  /**
   * The stable categories of Schematron failure this validator reports. The detail (which file, which
   * line, which XPath) stays on the result message; the category is what the summary groups by.
   */
  private enum Problem {
    NOT_WELL_FORMED("Generated schematron is invalid: not well-formed XML"),
    SCHEMA_VIOLATION("Generated schematron is invalid: schema violation"),
    CANNOT_COMPILE("Generated schematron is invalid: compilation failure"),
    PROCESSING_FAILURE("Generated schematron is invalid: processing failure");

    private final String statement;

    Problem(final String statement) {
      this.statement = statement;
    }

    String statement() {
      return this.statement;
    }
  }
}
