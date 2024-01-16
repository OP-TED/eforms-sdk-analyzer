package eu.europa.ted.eforms.sdk.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.validator.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SchematronValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.TextValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.Validator;
import eu.europa.ted.eforms.sdk.analysis.validator.XmlSchemaValidator;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(final Path sdkRoot) throws Exception {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    List<String> warnings = new ArrayList<>();
    List<String> errors = new ArrayList<>();

    List<Validator> validators = List.of(
        new XmlSchemaValidator(sdkRoot),
        new EfxValidator(sdkRoot),
        new TextValidator(sdkRoot),
        new SchematronValidator(sdkRoot),
        new SdkValidator(sdkRoot));
    
    for (Validator validator : validators) {
      validator.validate();
      warnings.addAll(Arrays.asList(validator.getWarnings()));
      errors.addAll(Arrays.asList(validator.getErrors()));      
    }

    if (!warnings.isEmpty() && logger.isWarnEnabled()) {
      logger.warn("Validation warnings:\n{}", StringUtils.join(warnings, '\n'));
      logger.warn("Total number of validation warnings: {}", warnings.size());
    }

    if (!errors.isEmpty() && logger.isErrorEnabled()) {
      logger.error("Validation errors:\n{}", StringUtils.join(errors, '\n'));
      logger.error("Total number of validation errors: {}", errors.size());
    }

    return errors.isEmpty() ? 0 : 1;
  }
}
