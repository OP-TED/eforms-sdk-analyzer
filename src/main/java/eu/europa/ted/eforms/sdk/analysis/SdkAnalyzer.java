package eu.europa.ted.eforms.sdk.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.report.ConsoleReportRenderer;
import eu.europa.ted.eforms.sdk.analysis.validator.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SchematronValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.Validator;
import eu.europa.ted.eforms.sdk.analysis.validator.XmlSchemaValidator;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(final Path sdkRoot) throws Exception {
    return analyze(sdkRoot, false);
  }

  public static int analyze(final Path sdkRoot, final boolean verbose) throws Exception {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    // Translation-text checks (invalid characters, label-identifier leaks) now run as drools rules
    // inside SdkValidator, so TextValidator is no longer part of the list.
    final List<Validator> validators = List.of(
        new XmlSchemaValidator(sdkRoot),
        new SchematronValidator(sdkRoot),
        new SdkValidator(sdkRoot),
        new EfxValidator(sdkRoot));

    final List<ValidationResult> all = new ArrayList<>();
    for (final Validator validator : validators) {
      logger.info("Starting validation with {}", validator.getClass().getSimpleName());
      validator.validate();
      all.addAll(validator.getResults());
    }

    final AnalysisResults results = new AnalysisResults(all);
    new ConsoleReportRenderer().render(results, verbose);

    return results.exitCode();
  }
}
