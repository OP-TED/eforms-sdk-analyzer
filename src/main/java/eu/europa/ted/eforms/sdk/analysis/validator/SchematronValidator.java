package eu.europa.ted.eforms.sdk.analysis.validator;

import java.util.HashSet;
import java.util.Locale;
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

import com.helger.commons.error.IError;
import com.helger.commons.error.list.IErrorList;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.SchematronHelper;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.serialize.MicroWriter;
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
    // Resolve all included files, so that they also get validated.
    final IMicroDocument doc = SchematronHelper.getWithResolvedSchematronIncludes(schematron,
        e -> handleError(e, schematronFileFact));

    if (doc == null) {
      ValidationResult result = new ValidationResult(schematronFileFact,
          "File is not well-formed XML", ValidationStatusEnum.ERROR);

      results.add(result);
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

  
  private void checkCanExecute(SchematronFileFact schematronFileFact, IReadableResource schematron) {
    ISchematronResource phSchematron = new SchematronResourcePure(schematron);
    try {
      // Execute the schematron on a dummy XML.
      // Having an XML that causes all rules to be executed is not possible anyway.
      Source source = new StreamSource(new StringReader("<a></a>"));
      phSchematron.applySchematronValidation(source);
    } catch (Exception e) {
      ValidationResult result = new ValidationResult(schematronFileFact,
          "Error during execution: " + e.getMessage(), ValidationStatusEnum.ERROR);

      results.add(result);
    }
  }

  @Override
  public Set<ValidationResult> getResults() {
    return results;
  }    
}
