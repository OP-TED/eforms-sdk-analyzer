package eu.europa.ted.eforms.sdk.analysis.report;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkSection;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Renders an {@link AnalysisResults} as a JSON string, for programmatic consumers — primarily the
 * Metadata Manager, which displays the findings in a table so a user can review the analysis before
 * exporting an SDK. The shape is a small {@code summary} (counts, plus per-section counts) and a flat
 * {@code findings} array — one self-describing object per finding (section, severity, rule, problem,
 * subject, referenced assets, message) so a UI can bind, sort and filter (including by section) without
 * re-deriving anything.
 *
 * <p>A deliberately simple starting contract; consumers are free to ask for more fields as the UI takes
 * shape. Within each section the findings are ordered by message, so the output is stable across runs.
 */
public class JsonReportRenderer {
  private final ObjectMapper mapper = new ObjectMapper();

  /** The analysis as a pretty-printed JSON string: {@code { summary, findings }}. */
  public String render(final AnalysisResults results) {
    final ObjectNode root = this.mapper.createObjectNode();
    root.set("summary", summary(results));
    root.set("findings", findings(results));
    try {
      return this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    } catch (final JsonProcessingException e) {
      throw new IllegalStateException("Could not render the analysis as JSON", e);
    }
  }

  /** Headline counts plus a per-section breakdown (only sections that carry a finding, in section order). */
  private ObjectNode summary(final AnalysisResults results) {
    final ObjectNode summary = this.mapper.createObjectNode();
    summary.put("errors", results.errorCount());
    summary.put("warnings", results.warningCount());
    summary.put("clean", results.isClean());

    final Map<SdkSection, List<Finding>> errors = results.errorFindingsBySection();
    final Map<SdkSection, List<Finding>> warnings = results.warningFindingsBySection();
    final ArrayNode sections = summary.putArray("sections");
    for (final SdkSection section : SdkSection.values()) {
      final int sectionErrors = sizeOf(errors.get(section));
      final int sectionWarnings = sizeOf(warnings.get(section));
      if (sectionErrors > 0 || sectionWarnings > 0) {
        final ObjectNode node = sections.addObject();
        node.put("section", section.getTitle());
        node.put("errors", sectionErrors);
        node.put("warnings", sectionWarnings);
      }
    }
    return summary;
  }

  /** Every finding as a flat array — errors first (by section), then warnings (by section). */
  private ArrayNode findings(final AnalysisResults results) {
    final ArrayNode findings = this.mapper.createArrayNode();
    appendFindings(findings, results.errorFindingsBySection());
    appendFindings(findings, results.warningFindingsBySection());
    return findings;
  }

  private void appendFindings(final ArrayNode array,
      final Map<SdkSection, List<Finding>> bySection) {
    for (final Map.Entry<SdkSection, List<Finding>> entry : bySection.entrySet()) {
      entry.getValue().stream()
          .sorted(Comparator.comparing(finding -> String.valueOf(finding.getResult().getMessage())))
          .forEach(finding -> array.add(findingNode(entry.getKey(), finding)));
    }
  }

  private ObjectNode findingNode(final SdkSection section, final Finding finding) {
    final ValidationResult result = finding.getResult();
    final ObjectNode node = this.mapper.createObjectNode();
    node.put("section", section.getTitle());
    node.put("severity", severity(result.getStatus()));
    node.put("rule", finding.getKind());
    node.put("problem", problemOf(finding));
    node.set("subject", assetNode(result.getSubject()));
    final ArrayNode references = node.putArray("references");
    result.getReferences().forEach(reference -> references.add(assetNode(reference)));
    node.put("message", result.getMessage());
    return node;
  }

  private ObjectNode assetNode(final AssetRef asset) {
    final ObjectNode node = this.mapper.createObjectNode();
    node.put("type", asset.getType());
    node.put("id", asset.getId());
    return node;
  }

  /** The display problem statement, never null (so the UI always has a column value to show). */
  private static String problemOf(final Finding finding) {
    final String problem = finding.getProblemOrKind();
    return problem != null ? problem : "(unknown)";
  }

  private static String severity(final ValidationStatusEnum status) {
    return status.name().toLowerCase(Locale.ROOT);
  }

  private static int sizeOf(final List<Finding> findings) {
    return findings == null ? 0 : findings.size();
  }
}
