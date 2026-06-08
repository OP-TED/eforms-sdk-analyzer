package eu.europa.ted.eforms.sdk.analysis.report;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.MissingLabelKind;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

class ConsoleReportRendererTest {

  private final LabelFact fact = new LabelFact(new Label("code|name|x"));

  private AnalysisResults sampleResults() {
    final ValidationResult found = new ValidationResult(this.fact,
        "Referenced label business-entity|name|UBO does not exist", ValidationStatusEnum.ERROR,
        List.of("business-entity|name|UBO"), MissingLabelKind.FOUND);
    final ValidationResult assumed = new ValidationResult(this.fact,
        "Label in EN contains label identifier(s): expression|name|906", ValidationStatusEnum.ERROR,
        List.of("expression|name|906"), MissingLabelKind.ASSUMED);
    final ValidationResult other = new ValidationResult(this.fact,
        "The value eforms-sdk-1.15.0 in sdkVersion is incorrect", ValidationStatusEnum.ERROR);
    final ValidationResult warning = new ValidationResult(this.fact, "a warning",
        ValidationStatusEnum.WARNING);
    return new AnalysisResults(List.of(found, assumed, other, warning));
  }

  private String render(final boolean verbose) {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    new ConsoleReportRenderer(new PrintStream(buffer, true, StandardCharsets.UTF_8))
        .render(sampleResults(), verbose);
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  void defaultReportSummarisesMissingLabelsAndOtherErrors() {
    final String output = render(false);

    assertTrue(output.contains("referenced by the SDK but not present"), output);
    assertTrue(output.contains("business-entity|name|UBO (1)"), output);
    assertTrue(output.contains("identifier left in label text by the exporter"), output);
    assertTrue(output.contains("expression|name|906 (1)"), output);
    assertTrue(output.contains("in sdkVersion is incorrect"), output);
    assertTrue(output.contains("Total number of validation errors: 3"), output);
    assertTrue(output.contains("--verbose"), output);
    // Warnings keep their detail in the default report (grouped), not just a count.
    assertTrue(output.contains("Warnings (1 unique):"), output);
    assertTrue(output.contains("a warning"), output);
    // The missing-label per-occurrence lines must not be listed individually in the default report.
    assertFalse(output.contains("All validation errors"), output);
  }

  @Test
  void verboseReportListsEveryFinding() {
    final String output = render(true);

    assertTrue(output.contains("All validation errors (3)"), output);
    assertTrue(output.contains("Referenced label business-entity|name|UBO does not exist"), output);
    assertTrue(output.contains("Label in EN contains label identifier(s): expression|name|906"),
        output);
    assertFalse(output.contains("--verbose"), output);
  }
}
