package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.ValidatorFact;
import eu.europa.ted.eforms.sdk.analysis.report.DetailReportRenderer;
import eu.europa.ted.eforms.sdk.analysis.report.SummaryReportRenderer;
import eu.europa.ted.eforms.sdk.analysis.util.SdkMetadataParser;
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

  /** The consumable summary is always written here (in the working directory), alongside analyzer.log. */
  private static final String SUMMARY_FILE = "analyzer-summary.txt";

  /** The full per-finding detail is always written here (in the working directory). */
  private static final String REPORT_FILE = "analyzer-report.txt";

  /** Maven stamps the build version here in every jar; read for the analyser version on the banner. */
  private static final String POM_PROPERTIES =
      "/META-INF/maven/eu.europa.ted.eforms/eforms-sdk-analyzer/pom.properties";

  private SdkAnalyzer() {}

  /** Runs the full analysis of the SDK at {@code sdkRoot} — every validator, including EFX. */
  public static int analyze(final Path sdkRoot) throws Exception {
    return analyze(sdkRoot, false);
  }

  /**
   * Runs the analysis of the SDK at {@code sdkRoot}, optionally skipping EFX. Package-private: the
   * public entry point is {@link #analyze(Path)} (a full analysis); skipping EFX is an internal
   * debugging option, exposed on the command line via {@code --skip-efx}.
   *
   * @param skipEfx when {@code true}, skips the slow EFX translation pass; the other validators are
   *        unaffected.
   */
  static int analyze(final Path sdkRoot, final boolean skipEfx) throws Exception {
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
    // The banner is built once so its timestamp is identical on the console and in both files: the SDK
    // being analysed on the title line, the analyser's own version and the run time on the subtitle.
    final String title = reportTitle(sdkRoot);
    final String subtitle = reportSubtitle();

    // The console shows the consumable summary (per-section/per-problem counts and actionable items).
    // The same summary and the full per-finding detail are written to separate fixed files in the
    // working directory — like analyzer.log — so CI can upload them as separate artifacts: open
    // whichever you need. A write failure is logged, not fatal.
    renderSummary(System.out, results, title, subtitle);
    writeFile(Path.of(SUMMARY_FILE), out -> renderSummary(out, results, title, subtitle));
    writeFile(Path.of(REPORT_FILE), out -> renderDetail(out, results, title, subtitle));

    return results.exitCode();
  }

  /** Writes {@code render}'s output to {@code file}; a write failure is logged, not fatal. */
  private static void writeFile(final Path file, final Consumer<PrintStream> render) {
    try (PrintStream out =
        new PrintStream(Files.newOutputStream(file), false, StandardCharsets.UTF_8)) {
      render.accept(out);
    } catch (final IOException e) {
      logger.warn("Could not write [{}]: {}", file, e.getMessage());
    }
  }

  /**
   * The consumable summary: the title banner, the per-section and per-problem counts and the actionable
   * items, then a pointer to the detail file (so a reader who needs every finding knows where it is).
   * Package-private so the detail-report pointer can be exercised in isolation.
   */
  static void renderSummary(final PrintStream out, final AnalysisResults results,
      final String title, final String subtitle) {
    renderTitle(out, title, subtitle);
    new SummaryReportRenderer(out).render(results);
    new DetailReportRenderer(out).render(results, false);
    // Point to the detail report whenever it has something to show — warning-only runs (no errors,
    // so isClean() is true) still list those warnings in the detail file.
    if (!results.isClean() || results.warningCount() > 0) {
      out.println("The full list of findings is in " + REPORT_FILE + ".");
    }
  }

  /** The full detail: the title banner and every individual finding — no summary (that is its own file). */
  private static void renderDetail(final PrintStream out, final AnalysisResults results,
      final String title, final String subtitle) {
    renderTitle(out, title, subtitle);
    new DetailReportRenderer(out).render(results, true);
  }

  /**
   * The title banner: a rule, the title line, a rule, then the subtitle beneath. The rules are sized to
   * the wider of the two lines so both sit within the banner.
   */
  private static void renderTitle(final PrintStream out, final String title, final String subtitle) {
    final String rule = "-".repeat(Math.max(title.length(), subtitle.length()));
    out.println(rule);
    out.println(title);
    out.println(rule);
    out.println(subtitle);
  }

  /** The banner's title line: the SDK being analysed (version from its metadata) and the report name. */
  private static String reportTitle(final Path sdkRoot) {
    return "eForms SDK " + sdkVersion(sdkRoot) + "  -  SDK Analyser Report";
  }

  /** The banner's subtitle line: the analyser's own version and the run time, in UTC. */
  private static String reportSubtitle() {
    final String when = ZonedDateTime.now(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'"));
    return "Analyser " + analyzerVersion() + "  -  " + when;
  }

  /**
   * The analyser's own build version, read from the Maven {@code pom.properties} bundled in the jar and
   * formatted as {@code vX.Y.Z}. Falls back to a placeholder when it cannot be read (e.g. running from
   * unpackaged classes in the IDE or tests, where {@code pom.properties} is absent).
   */
  private static String analyzerVersion() {
    try (InputStream in = SdkAnalyzer.class.getResourceAsStream(POM_PROPERTIES)) {
      if (in != null) {
        final Properties properties = new Properties();
        properties.load(in);
        final String version = properties.getProperty("version");
        if (version != null && !version.isBlank()) {
          return "v" + version;
        }
      }
    } catch (final IOException e) {
      logger.debug("Could not read the analyser version: {}", e.getMessage());
    }
    return "(version unknown)";
  }

  /** The SDK version from its metadata, or a placeholder when it cannot be read. */
  private static String sdkVersion(final Path sdkRoot) {
    try {
      return SdkMetadataParser.loadSdkMetadata(sdkRoot).getVersion();
    } catch (final Exception e) {
      logger.debug("Could not read the SDK version: {}", e.getMessage());
      return "(unknown version)";
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
