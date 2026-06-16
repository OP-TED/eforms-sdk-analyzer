package eu.europa.ted.eforms.sdk.analysis.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

class JsonReportRendererTest {

  private final ObjectMapper mapper = new ObjectMapper();
  private final LabelFact fact = new LabelFact(new Label("code|name|x"));

  private AnalysisResults sampleResults() {
    final Finding missingLabel = new Finding("Field references an existing label",
        "Label is referenced but is missing",
        new ValidationResult(this.fact, "Referenced label business-entity|name|UBO does not exist",
            ValidationStatusEnum.ERROR, List.of(AssetRef.label("business-entity|name|UBO"))));
    final Finding warning = new Finding("Some advisory rule", "An advisory problem",
        new ValidationResult(this.fact, "a warning", ValidationStatusEnum.WARNING));
    return AnalysisResults.of(List.of(missingLabel, warning));
  }

  private JsonNode render() throws Exception {
    return this.mapper.readTree(new JsonReportRenderer().render(sampleResults()));
  }

  @Test
  void summaryCarriesCountsAndPerSectionBreakdown() throws Exception {
    final JsonNode summary = render().path("summary");

    assertEquals(1, summary.path("errors").asInt());
    assertEquals(1, summary.path("warnings").asInt());
    assertFalse(summary.path("clean").asBoolean());

    // Both findings have a label subject, so they fall under one section with one error and one warning.
    final JsonNode sections = summary.path("sections");
    assertEquals(1, sections.size());
    assertEquals("Translations", sections.get(0).path("section").asText());
    assertEquals(1, sections.get(0).path("errors").asInt());
    assertEquals(1, sections.get(0).path("warnings").asInt());
  }

  @Test
  void findingsAreAFlatSelfDescribingArrayErrorsFirst() throws Exception {
    final JsonNode findings = render().path("findings");
    assertEquals(2, findings.size());

    // Errors are listed before warnings.
    final JsonNode error = findings.get(0);
    assertEquals("Translations", error.path("section").asText());
    assertEquals("error", error.path("severity").asText());
    assertEquals("Field references an existing label", error.path("rule").asText());
    assertEquals("Label is referenced but is missing", error.path("problem").asText());
    assertEquals("label", error.path("subject").path("type").asText());
    assertTrue(error.path("subject").hasNonNull("id"), error.toString());
    assertTrue(error.path("message").asText().contains("does not exist"), error.toString());

    // The implicated asset (the thing to add/fix) is carried as a typed reference.
    final JsonNode reference = error.path("references").get(0);
    assertEquals("label", reference.path("type").asText());
    assertEquals("business-entity|name|UBO", reference.path("id").asText());

    final JsonNode warning = findings.get(1);
    assertEquals("warning", warning.path("severity").asText());
    // An intrinsic finding (no implicated asset) still has the field, as an empty array.
    assertTrue(warning.path("references").isArray(), warning.toString());
    assertEquals(0, warning.path("references").size());
  }
}
