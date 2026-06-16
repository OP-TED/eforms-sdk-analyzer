package eu.europa.ted.eforms.sdk.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.SdkAnalyzer.ValidatorFactory;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.validator.Validator;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkSection;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/** The safety net: neither a crashing constructor nor a crashing validate() may abort the run. */
class SdkAnalyzerTest {

  /** Always fails in validate(), with a recognisable cause, to stand in for an unexpected crash. */
  static final class ThrowingValidator implements Validator {
    @Override
    public Validator validate() {
      throw new IllegalStateException("boom");
    }

    @Override
    public Set<ValidationResult> getResults() {
      return Set.of();
    }
  }

  /** Completes normally and produces one error finding, to prove it survives another's crash. */
  static final class PassingValidator implements Validator {
    @Override
    public Validator validate() {
      return this;
    }

    @Override
    public Set<ValidationResult> getResults() {
      return Set.of(new ValidationResult(new LabelFact(new Label("code|name|x")), "a real finding",
          ValidationStatusEnum.ERROR));
    }
  }

  /** Has a finding collected, then crashes in validate() — to prove partial findings are kept. */
  static final class PartialThenThrowValidator implements Validator {
    @Override
    public Validator validate() {
      throw new IllegalStateException("late boom");
    }

    @Override
    public Set<ValidationResult> getResults() {
      return Set.of(new ValidationResult(new LabelFact(new Label("code|name|y")), "partial finding",
          ValidationStatusEnum.ERROR));
    }
  }

  private static Map<String, ValidatorFactory> factories(final ValidatorFactory... created) {
    final Map<String, ValidatorFactory> map = new LinkedHashMap<>();
    for (int i = 0; i < created.length; i++) {
      map.put("V" + i, created[i]);
    }
    return map;
  }

  @Test
  void aCrashingValidatorBecomesAFindingAndDoesNotAbortTheRun() {
    final Map<String, ValidatorFactory> validators = new LinkedHashMap<>();
    // Throwing validator first: if it aborted the run, the passing one's finding would be lost.
    validators.put(ThrowingValidator.class.getSimpleName(), ThrowingValidator::new);
    validators.put(PassingValidator.class.getSimpleName(), PassingValidator::new);

    final List<Finding> findings = SdkAnalyzer.runValidators(validators);

    // The validator that completed still contributes its finding.
    assertTrue(findings.stream().anyMatch(f -> "a real finding".equals(f.getProblemOrKind())),
        findings.toString());

    // The crash is reported as one error finding, grouped under a single category.
    final Finding crash = findings.stream()
        .filter(f -> "A validator failed to run".equals(f.getProblem())).findFirst()
        .orElseThrow(() -> new AssertionError("no crash finding in " + findings));
    assertEquals(ValidationStatusEnum.ERROR, crash.getResult().getStatus());

    // Its subject is the validator, so it lands in the Analyzer section, named by the failed validator.
    assertEquals("validator", crash.getResult().getSubject().getType());
    assertEquals(SdkSection.ANALYZER, SdkSection.forType(crash.getResult().getSubject().getType()));
    assertEquals("ThrowingValidator", crash.getResult().getSubject().getId());

    // The reason (exception type and message) is preserved on the result for the detail report.
    final String message = crash.getResult().getMessage();
    assertTrue(message.contains("ThrowingValidator did not complete"), message);
    assertTrue(message.contains("IllegalStateException"), message);
    assertTrue(message.contains("boom"), message);
  }

  @Test
  void aValidatorThatFailsToConstructIsReportedAndDoesNotAbortTheRun() {
    final Map<String, ValidatorFactory> validators = new LinkedHashMap<>();
    validators.put("ExplodingConstructor", () -> {
      throw new IllegalStateException("ctor boom");
    });
    validators.put(PassingValidator.class.getSimpleName(), PassingValidator::new);

    final List<Finding> findings = SdkAnalyzer.runValidators(validators);

    // A validator that cannot even be constructed must not prevent the others from running.
    assertTrue(findings.stream().anyMatch(f -> "a real finding".equals(f.getProblemOrKind())),
        findings.toString());

    // The construction failure surfaces as a crash finding named for the validator that could not load.
    final Finding crash = findings.stream()
        .filter(f -> "A validator failed to run".equals(f.getProblem())).findFirst()
        .orElseThrow(() -> new AssertionError("no crash finding in " + findings));
    assertEquals("ExplodingConstructor", crash.getResult().getSubject().getId());
    assertTrue(crash.getResult().getMessage().contains("ctor boom"), crash.getResult().getMessage());
  }

  @Test
  void findingsCollectedBeforeACrashAreNotDiscarded() {
    final List<Finding> findings =
        SdkAnalyzer.runValidators(factories(PartialThenThrowValidator::new));

    // The finding the validator had already collected survives the crash...
    assertTrue(findings.stream().anyMatch(f -> "partial finding".equals(f.getProblemOrKind())),
        findings.toString());
    // ...alongside the crash finding that records the run was incomplete.
    assertTrue(findings.stream().anyMatch(f -> "A validator failed to run".equals(f.getProblem())),
        findings.toString());
  }

  @Test
  void theSummaryPointsToTheDetailReportWhenThereAreFindings() {
    final LabelFact fact = new LabelFact(new Label("code|name|x"));
    final String pointer = "The full list of findings is in analyzer-report.txt";

    // A warning-only run has no errors (isClean() is true), but the detail report still lists those
    // warnings — so the summary must point to it.
    final String warningOnly = summaryOf(new AnalysisResults(
        List.of(new ValidationResult(fact, "a warning", ValidationStatusEnum.WARNING))));
    assertTrue(warningOnly.contains(pointer), warningOnly);

    final String withError = summaryOf(new AnalysisResults(
        List.of(new ValidationResult(fact, "an error", ValidationStatusEnum.ERROR))));
    assertTrue(withError.contains(pointer), withError);

    // A fully clean run has nothing to investigate, so it gives no pointer.
    final String clean = summaryOf(new AnalysisResults(List.of()));
    assertFalse(clean.contains(pointer), clean);
  }

  /** Renders the console summary to a string, so the detail-report pointer can be asserted. */
  private static String summaryOf(final AnalysisResults results) {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    SdkAnalyzer.renderSummary(new PrintStream(buffer, true, StandardCharsets.UTF_8), results,
        "title", "subtitle");
    return buffer.toString(StandardCharsets.UTF_8);
  }
}
