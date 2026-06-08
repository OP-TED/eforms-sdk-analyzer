package eu.europa.ted.eforms.sdk.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.enums.MissingLabelKind;
import eu.europa.ted.eforms.sdk.analysis.validator.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SchematronValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.Validator;
import eu.europa.ted.eforms.sdk.analysis.validator.XmlSchemaValidator;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(final Path sdkRoot) throws Exception {
    return analyze(sdkRoot, false);
  }

  public static int analyze(final Path sdkRoot, final boolean verbose) throws Exception {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    List<ValidationResult> warnings = new ArrayList<>();
    List<ValidationResult> errors = new ArrayList<>();

    // Translation-text checks (invalid characters, label-identifier leaks) now run as drools rules
    // inside SdkValidator, so TextValidator is no longer part of the list.
    List<Validator> validators = List.of(
        new XmlSchemaValidator(sdkRoot),
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
      List<ValidationResult> errorsToLog = forDisplay(foundErrors, verbose);
      if (!errorsToLog.isEmpty()) {
        logger.error("Errors from {}:\n{}", validatorName, StringUtils.join(errorsToLog, '\n'));
      }

      warnings.addAll(foundWarnings);
      errors.addAll(foundErrors);
    }

    if (!warnings.isEmpty() && logger.isWarnEnabled()) {
      logger.warn("All validation warnings:\n{}", StringUtils.join(warnings, '\n'));
      logger.warn("Total number of validation warnings: {}", warnings.size());
    }

    if (!errors.isEmpty() && logger.isErrorEnabled()) {
      List<ValidationResult> errorsToLog = forDisplay(errors, verbose);
      if (!errorsToLog.isEmpty()) {
        logger.error("All validation errors:\n{}", StringUtils.join(errorsToLog, '\n'));
      }
      logger.error("Total number of validation errors: {}", errors.size());
    }

    reportMissingLabels(errors, verbose);

    return errors.isEmpty() ? 0 : 1;
  }

  /**
   * The errors to print individually. Without verbose output the per-occurrence missing-label errors
   * are left out, since they are summarised by {@link #reportMissingLabels}; all other errors are
   * always printed.
   */
  private static List<ValidationResult> forDisplay(Collection<ValidationResult> results,
      boolean verbose) {
    if (verbose) {
      return new ArrayList<>(results);
    }
    return results.stream()
        .filter(result -> result.getMissingLabelIds().isEmpty())
        .collect(Collectors.toList());
  }

  /**
   * Logs a deduplicated report of the labels that have no text, split by how they were detected:
   * referenced by the SDK but absent, and assumed missing because the exporter left their identifier
   * inside another label's text. Each identifier is listed once with the number of references, so
   * the labels that actually need to be added stand out from the thousands of individual errors.
   */
  private static void reportMissingLabels(List<ValidationResult> errors, boolean verbose) {
    if (!logger.isErrorEnabled()) {
      return;
    }

    Map<String, Long> found = countMissingLabels(errors, MissingLabelKind.FOUND);
    Map<String, Long> assumed = countMissingLabels(errors, MissingLabelKind.ASSUMED);

    if (found.isEmpty() && assumed.isEmpty()) {
      return;
    }

    logMissingLabels("Labels found missing from the export, referenced by the SDK but not present",
        found);
    logMissingLabels(
        "Labels assumed missing during export, identifier left in label text by the exporter",
        assumed);

    if (!verbose) {
      logger.error("Re-run the analyzer with --verbose to see the individual occurrences behind the"
          + " missing label detection.");
    }
  }

  private static Map<String, Long> countMissingLabels(List<ValidationResult> errors,
      MissingLabelKind kind) {
    return errors.stream()
        .filter(error -> error.getMissingLabelKind() == kind)
        .flatMap(error -> error.getMissingLabelIds().stream())
        .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
  }

  private static void logMissingLabels(String title, Map<String, Long> missingLabels) {
    if (missingLabels.isEmpty()) {
      return;
    }
    StringBuilder report = new StringBuilder();
    missingLabels.forEach(
        (labelId, count) -> report.append(String.format("%n  %s (%d)", labelId, count)));
    logger.error("{} ({} unique, identifier and number of references):{}", title,
        missingLabels.size(), report);
  }
}
