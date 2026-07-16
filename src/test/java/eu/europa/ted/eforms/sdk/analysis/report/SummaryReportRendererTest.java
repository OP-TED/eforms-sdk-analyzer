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
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

class SummaryReportRendererTest {

  private final LabelFact fact = new LabelFact(new Label("code|name|x"));

  @Test
  void rendersPivotsByKindReferencedAssetAndSubject() {
    final ValidationResult missingLabel = new ValidationResult(this.fact,
        "Referenced label business-entity|name|UBO does not exist", ValidationStatusEnum.ERROR,
        AssetRef.label("business-entity|name|UBO"));
    final ValidationResult forbidden10 = new ValidationResult(this.fact,
        "Notice sub type 10 is listed in the unconditional forbidden constraint of field OPT-090-Lot",
        ValidationStatusEnum.ERROR, AssetRef.field("OPT-090-Lot"));
    final ValidationResult forbidden11 = new ValidationResult(this.fact,
        "Notice sub type 11 is listed in the unconditional forbidden constraint of field OPT-090-Lot",
        ValidationStatusEnum.ERROR, AssetRef.field("OPT-090-Lot"));

    final String forbiddenRule = "Notice sub type is not unconditionally forbidden for any field";
    final AnalysisResults results = AnalysisResults.of(List.of(
        new Finding("The labels referenced in field properties exist", missingLabel),
        new Finding(forbiddenRule, forbidden10),
        new Finding(forbiddenRule, forbidden11)));

    final String output = render(results);
    // Columns are padded for alignment, so compare with runs of spaces collapsed to one.
    final String norm = output.replaceAll(" +", " ");

    // all subjects are labels, so everything is under the Translations section.
    assertTrue(output.contains("[Translations]"), output);
    // the rule heads its group (no @problem in the fixtures, so the rule name shows).
    assertTrue(output.contains(forbiddenRule), output);
    // rows render directly under the problem header, with no legend line.
    assertFalse(output.contains("referenced label"), output);
    // the referenced asset (the thing to fix) is listed once, joined by "@" to the referring subject.
    assertTrue(norm.contains("OPT-090-Lot @ code|name|x"), output);
    assertTrue(norm.contains("business-entity|name|UBO @ code|name|x"), output);
    // type: prefixes are dropped on the asset lines.
    assertFalse(output.contains("field:OPT-090-Lot"), output);
  }

  private static String render(final AnalysisResults results) {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    new SummaryReportRenderer(new PrintStream(buffer, true, StandardCharsets.UTF_8)).render(results);
    return buffer.toString(StandardCharsets.UTF_8);
  }
}
