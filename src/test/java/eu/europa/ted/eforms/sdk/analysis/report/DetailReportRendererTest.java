package eu.europa.ted.eforms.sdk.analysis.report;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

class DetailReportRendererTest {

  private final LabelFact fact = new LabelFact(new Label("code|name|x"));

  private AnalysisResults sampleResults() {
    final ValidationResult found = new ValidationResult(this.fact,
        "Referenced label business-entity|name|UBO does not exist", ValidationStatusEnum.ERROR,
        List.of(AssetRef.label("business-entity|name|UBO")));
    final ValidationResult assumed = new ValidationResult(this.fact,
        "Label in EN contains label identifier(s): expression|name|906", ValidationStatusEnum.ERROR,
        List.of(AssetRef.label("expression|name|906")));
    final ValidationResult other = new ValidationResult(this.fact,
        "The value eforms-sdk-1.15.0 in sdkVersion is incorrect", ValidationStatusEnum.ERROR);
    final ValidationResult warning = new ValidationResult(this.fact, "a warning",
        ValidationStatusEnum.WARNING);
    return new AnalysisResults(List.of(found, assumed, other, warning));
  }

  private String render(final boolean verbose) {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    new DetailReportRenderer(new PrintStream(buffer, true, StandardCharsets.UTF_8))
        .render(sampleResults(), verbose);
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @Test
  void defaultReportShowsOnlyTheHeadlineAndVerboseHint() {
    final String output = render(false);

    // The default run leads with the summary and actionable items (rendered by SummaryReportRenderer);
    // this renderer adds only the closing headline, so no individual finding text appears here.
    assertTrue(output.contains("Total number of validation errors: 3"), output);
    assertTrue(output.contains("--verbose"), output);
    // No per-finding list and no per-message grouping in the default console output.
    assertFalse(output.contains("All validation errors"), output);
    assertFalse(output.contains("in sdkVersion is incorrect"), output);
    assertFalse(output.contains("unique"), output);
    assertFalse(output.contains("a warning"), output);
  }

  @Test
  void verboseReportListsEveryFinding() {
    final String output = render(true);

    assertTrue(output.contains("All validation errors (3)"), output);
    assertTrue(output.contains("Referenced label business-entity|name|UBO does not exist"), output);
    assertTrue(output.contains("Label in EN contains label identifier(s): expression|name|906"),
        output);
    // Warnings are listed in full too, after the errors.
    assertTrue(output.contains("All validation warnings (1)"), output);
    assertTrue(output.contains("a warning"), output);
    assertFalse(output.contains("--verbose"), output);
  }
}
