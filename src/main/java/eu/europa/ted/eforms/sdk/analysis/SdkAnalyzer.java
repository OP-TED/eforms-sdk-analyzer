package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.ValidatorFact;
import eu.europa.ted.eforms.sdk.analysis.report.DetailReportRenderer;
import eu.europa.ted.eforms.sdk.analysis.report.SummaryReportRenderer;
import eu.europa.ted.eforms.sdk.analysis.validator.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SchematronValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.Validator;
import eu.europa.ted.eforms.sdk.analysis.validator.XmlSchemaValidator;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  /** The full report is always written here (in the working directory), alongside analyzer.log. */
  private static final String REPORT_FILE = "analyzer-report.txt";

  private SdkAnalyzer() {}

  public static int analyze(final Path sdkRoot) throws Exception {
    return analyze(sdkRoot, false);
  }

  public static int analyze(final Path sdkRoot, final boolean verbose) throws Exception {
    return analyze(sdkRoot, verbose, false);
  }

  public static int analyze(final Path sdkRoot, final boolean verbose, final boolean skipEfx)
      throws Exception {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    // Each validator is built lazily inside the guarded run loop, so a constructor failure (e.g. a
    // malformed SDK that breaks metadata parsing) is caught and reported like any other validator
    // crash instead of aborting the whole run. Translation-text checks (invalid characters,
    // label-identifier leaks) run as drools rules inside SdkValidator, so there is no TextValidator.
    final Map<String, ValidatorFactory> validators = new LinkedHashMap<>();
    validators.put(XmlSchemaValidator.class.getSimpleName(), () -> new XmlSchemaValidator(sdkRoot));
    validators.put(SchematronValidator.class.getSimpleName(), () -> new SchematronValidator(sdkRoot));
    validators.put(SdkValidator.class.getSimpleName(), () -> new SdkValidator(sdkRoot));

    // EFX validation is independent of the rule engine and is by far the slowest pass; --skip-efx
    // omits it so rule debugging runs do not pay for it. It also walks node ancestry, which loops
    // forever on a cyclic node hierarchy — so we do not even start it when the SDK has node cycles
    // (those are reported separately by the node-hierarchy rules; resolve them and re-run for EFX).
    if (skipEfx) {
      logger.info("Skipping EFX validation (--skip-efx)");
    } else if (NodeCycleDetector.hasNodeCycle(sdkRoot)) {
      logger.warn("Skipping EFX validation: the SDK has cyclic node references (reported by the node "
          + "hierarchy checks). EFX translation cannot run until they are resolved.");
    } else {
      validators.put(EfxValidator.class.getSimpleName(), () -> new EfxValidator(sdkRoot));
    }

    final AnalysisResults results = AnalysisResults.of(runValidators(validators));
    // Lead with the summary and the actionable items; the full, unaggregated list of findings
    // follows only under --verbose. DetailReportRenderer carries that closing detail and the
    // headline total, so the summary is always what the reader sees first.
    new SummaryReportRenderer().render(results);
    new DetailReportRenderer().render(results, verbose);

    // Always write the full report (summary, actionable items and every finding) to a fixed file in
    // the working directory — like analyzer.log — so CI can upload it as an artifact while the console
    // keeps the concise summary. A write failure is logged, not fatal — the console report succeeded.
    writeFullReport(results, Path.of(REPORT_FILE));

    return results.exitCode();
  }

  /** Writes the complete report — summary, actionable items and every finding — to {@code reportFile}. */
  private static void writeFullReport(final AnalysisResults results, final Path reportFile) {
    try (PrintStream out =
        new PrintStream(Files.newOutputStream(reportFile), false, StandardCharsets.UTF_8)) {
      new SummaryReportRenderer(out).render(results);
      new DetailReportRenderer(out).render(results, true);
    } catch (final IOException e) {
      logger.warn("Could not write the report to [{}]: {}", reportFile, e.getMessage());
    }
  }

  /**
   * Constructs and runs every validator in turn and gathers their findings. A validator must never
   * abort the whole run: construction and execution are both guarded, so an unexpected failure —
   * whether in the constructor or in {@code validate()} — is caught, turned into a visible
   * {@link #validatorCrashFinding finding}, and the remaining validators still run. When a validator
   * crashes part-way through {@code validate()}, whatever it had already collected is still harvested,
   * so a half-finished pass does not silently discard the real problems it had already found.
   * Package-private so the safety net can be exercised in isolation.
   */
  static List<Finding> runValidators(final Map<String, ValidatorFactory> validators) {
    final List<Finding> findings = new ArrayList<>();
    for (final Map.Entry<String, ValidatorFactory> entry : validators.entrySet()) {
      final String name = entry.getKey();
      logger.info("Starting validation with {}", name);
      Validator validator = null;
      try {
        validator = entry.getValue().create();
        validator.validate();
        findings.addAll(validator.getFindings());
      } catch (final Exception e) {
        logger.error("Validator {} failed to run", name, e);
        // Preserve whatever the validator gathered before it failed — it is null only when the
        // constructor itself threw — then record the crash so the report shows the run was incomplete.
        if (validator != null) {
          harvestPartialFindings(name, validator, findings);
        }
        findings.add(validatorCrashFinding(name, e));
      }
    }
    return findings;
  }

  /** Best-effort harvest of a crashed validator's already-collected findings; never throws. */
  private static void harvestPartialFindings(final String name, final Validator validator,
      final List<Finding> findings) {
    try {
      findings.addAll(validator.getFindings());
    } catch (final RuntimeException e) {
      logger.warn("Could not harvest partial findings from {}", name, e);
    }
  }

  /** Builds a validator on demand, so a constructor failure is caught by the run loop, not fatal. */
  @FunctionalInterface
  interface ValidatorFactory {
    Validator create() throws Exception;
  }

  /** Records a validator failing to run as an error finding, so the crash shows up in the report. */
  private static Finding validatorCrashFinding(final String validatorName, final Exception error) {
    final ValidationResult result = new ValidationResult(new ValidatorFact(validatorName),
        validatorName + " did not complete: " + describe(error), ValidationStatusEnum.ERROR);
    return new Finding(validatorName, "A validator failed to run", result);
  }

  /** The exception's type and message (or just the type when it carries none), for the report. */
  private static String describe(final Throwable error) {
    final String message = error.getMessage();
    return message == null || message.isBlank()
        ? error.getClass().getSimpleName()
        : error.getClass().getSimpleName() + ": " + message;
  }
}
