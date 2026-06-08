package eu.europa.ted.eforms.sdk.analysis.validator;

import java.util.ArrayList;
import java.util.HashSet;
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
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates the content of Schematron files.
 * It does not use Schematron to validate something else.
 */
public class SchematronValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(SchematronValidator.class);

  private final SdkLoader sdkLoader;
  private final Set<ValidationResult> results;
  
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

      checkAgainstSchema(schematronFileFact, schematron);
      checkCanExecute(schematronFileFact, schematron);
    });

    return this;
  }

  private void checkAgainstSchema(SchematronFileFact schematronFileFact, IReadableResource schematron) {
    // Capture XML well-formedness errors instead of letting the parser log them.
    final List<String> saxErrors = new ArrayList<>();
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
        saxSettings, e -> handleError(e, schematronFileFact));

    if (doc == null) {
      final String message = saxErrors.isEmpty()
          ? "File is not well-formed XML"
          : "File is not well-formed XML: " + String.join("; ", saxErrors);
      results.add(new ValidationResult(schematronFileFact, message, ValidationStatusEnum.ERROR));
      return;
    }

    String resolved = MicroWriter.getNodeAsString(doc);
    if (resolved == null) {
      ValidationResult result = new ValidationResult(schematronFileFact,
          "Resolved schematron could not be processed", ValidationStatusEnum.ERROR);
      
      results.add(result);
      return;
    }
    Source source = TransformSourceFactory.create(resolved);
    // This will return an empty list if the schematron is valid.
    IErrorList errors = com.helger.schematron.validator.SchematronValidator.validateSchematron(source);

    if (errors != null) {
      errors.forEach(e -> handleError(e, schematronFileFact));
    } else {
      ValidationResult result = new ValidationResult(schematronFileFact,
          "Error while validating schematron", ValidationStatusEnum.ERROR);
      
      results.add(result);
    }
  }

  private void handleError(IError error, SchematronFileFact schematronFileFact) {
    if (error.getErrorLevel().isError()) {
      Locale locale = Locale.getDefault();
      if (locale == null) {
        locale = new Locale("en");
      }
      ValidationResult result = new ValidationResult(schematronFileFact,
          error.getErrorText(locale), ValidationStatusEnum.ERROR);

      results.add(result);
    }
  }

  
  private String formatSaxError(final SAXParseException e) {
    return String.format("line %d, column %d: %s",
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
      results.add(new ValidationResult(schematronFileFact,
          "Error during execution: " + describeCollectedErrors(errorHandler, e),
          ValidationStatusEnum.ERROR));
    }
  }

  private String describeCollectedErrors(final CollectingPSErrorHandler errorHandler,
      final Exception fallback) {
    final Locale locale = Locale.getDefault();
    final String detail = errorHandler.getAllErrors().stream()
        .filter(error -> error.getErrorLevel().isError())
        .map(error -> error.getErrorText(locale))
        .collect(Collectors.joining("; "));
    if (!detail.isBlank()) {
      return detail;
    }
    // Some exceptions carry no message; fall back to the type+message rather than "null".
    final String message = fallback.getMessage();
    return message == null || message.isBlank() ? fallback.toString() : message;
  }

  @Override
  public Set<ValidationResult> getResults() {
    return results;
  }    
}
