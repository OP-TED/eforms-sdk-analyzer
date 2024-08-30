package eu.europa.ted.eforms.sdk.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.validator.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SchematronValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.TextValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.Validator;
import eu.europa.ted.eforms.sdk.analysis.validator.XmlSchemaValidator;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(final Path sdkRoot) throws Exception {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    List<ValidationResult> warnings = new ArrayList<>();
    List<ValidationResult> errors = new ArrayList<>();

    List<Validator> validators = List.of(
        new XmlSchemaValidator(sdkRoot),
        new TextValidator(sdkRoot),
        new SchematronValidator(sdkRoot),
        new SdkValidator(sdkRoot),
        new EfxValidator(sdkRoot));
    
    for (Validator validator : validators) {
      String validatorName = validator.getClass().getSimpleName();
      logger.info("Starting validation with {}", validatorName);
      validator.validate();

      Set<ValidationResult> foundWarnings = validator.getWarnings();
      if (!foundWarnings.isEmpty()) {
        logger.warn("Warnings from {}:\n{}", validatorName, StringUtils.join(foundWarnings, '\n'));
      }
      Set<ValidationResult> foundErrors = validator.getErrors();
      if (!foundErrors.isEmpty()) {
        logger.error("Errors from {}:\n{}", validatorName, StringUtils.join(foundErrors, '\n'));
      }

      warnings.addAll(foundWarnings);
      errors.addAll(foundErrors);
    }

    if (!warnings.isEmpty() && logger.isWarnEnabled()) {
      logger.warn("All validation warnings:\n{}", StringUtils.join(warnings, '\n'));
      logger.warn("Total number of validation warnings: {}", warnings.size());
    }

    if (!errors.isEmpty() && logger.isErrorEnabled()) {
      logger.error("All validation errors:\n{}", StringUtils.join(errors, '\n'));
      logger.error("Total number of validation errors: {}", errors.size());
    }

    return errors.isEmpty() ? 0 : 1;
  }
}
