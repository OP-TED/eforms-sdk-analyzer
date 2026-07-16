package eu.europa.ted.eforms.sdk.analysis.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkSection;

/**
 * The report's lead: a per-section summary, then actionable items grouped by SDK section and, within
 * each section, by the <strong>problem statement</strong> (the rule's {@code @problem}, falling back
 * to its name). Grouping by the problem — not the rule — lets rules that report the same thing (e.g. a
 * field and a business entity both referencing a missing label) fuse into one group. The closing
 * headline total follows this via {@link DetailReportRenderer}; the full, unaggregated list of every
 * finding is written separately to {@code analyzer-report.txt}.
 *
 * <p>Each group is a one-line header (problem + the count), then one line per asset to act on: the
 * referenced asset (the thing to add or fix), shown once, with the facts that reference it. When a
 * group mixes subject types, each referrer carries its type.
 */
public class SummaryReportRenderer {
  private static final int MAX_FACTS_SHOWN = 8;

  private final PrintStream out;

  public SummaryReportRenderer() {
    this(System.out);
  }

  public SummaryReportRenderer(final PrintStream out) {
    this.out = out;
  }

  public void render(final AnalysisResults results) {
    if (results.isClean() && results.warningCount() == 0) {
      return;
    }
    if (!results.isClean()) {
      renderErrors(results);
    }
    if (results.warningCount() > 0) {
      renderWarnings(results);
    }
  }

  private void renderErrors(final AnalysisResults results) {
    final Map<SdkSection, List<Finding>> bySection = results.errorFindingsBySection();

    banner("Findings summary");
    renderSectionCounts(bySection, "Errors");
    renderProblems(results.errorsByProblem(), "Errors");

    banner("Actionable items");
    renderSections(bySection);
  }

  /**
   * Warnings are advisory: they are reported in their own block, after the actionable errors and
   * clearly separated from them, and never affect the exit code.
   */
  private void renderWarnings(final AnalysisResults results) {
    final Map<SdkSection, List<Finding>> bySection = results.warningFindingsBySection();

    banner("Warnings summary");
    renderSectionCounts(bySection, "Warnings");
    renderProblems(results.warningsByProblem(), "Warnings");

    banner("Advisory warnings");
    renderSections(bySection);
  }

  /** A top-level section banner: a blank line, {@code === title ===}, then a blank line. */
  private void banner(final String title) {
    this.out.println();
    this.out.println("=== " + title + " ===");
    this.out.println();
  }

  private void renderSectionCounts(final Map<SdkSection, List<Finding>> bySection,
      final String label) {
    this.out.println(label + " by SDK section:");
    bySection.forEach((section, findings) ->
        this.out.println(String.format("  %-18s %d", section.getTitle(), findings.size())));
  }

  private void renderProblems(final SortedMap<String, Long> byProblem, final String label) {
    this.out.println(label + " by problem (" + byProblem.size() + "):");
    byProblem.forEach((problem, count) -> this.out.println("  " + count + " ×  " + problem));
  }

  /** The per-SDK-section blocks, each headed by {@code [Section]}, separated by a single blank line. */
  private void renderSections(final Map<SdkSection, List<Finding>> bySection) {
    boolean first = true;
    for (final Map.Entry<SdkSection, List<Finding>> entry : bySection.entrySet()) {
      if (!first) {
        this.out.println();
      }
      first = false;
      this.out.println("[" + entry.getKey().getTitle() + "]");
      entry.getValue().stream()
          .collect(Collectors.groupingBy(SummaryReportRenderer::problemLabel, LinkedHashMap::new,
              Collectors.toList()))
          .forEach(this::renderProblemGroup);
    }
  }

  private void renderProblemGroup(final String problem, final List<Finding> group) {
    final boolean mixedSubjects = distinctSubjectTypeCount(group) > 1;
    final Map<AssetRef, List<AssetRef>> byReferenced = referencedToSubjects(group);
    // Findings with no referenced asset: the subject itself is the thing to act on. A group can mix
    // the two (the same @problem from a reference-carrying rule and an intrinsic one), so render both
    // — otherwise an intrinsic finding silently vanishes just because a sibling carries a reference,
    // while the section count still includes it.
    final List<AssetRef> intrinsicSubjects = group.stream()
        .filter(finding -> finding.getResult().getReferences().isEmpty())
        .map(finding -> finding.getResult().getSubject())
        .distinct().sorted().collect(Collectors.toList());

    this.out.println(
        "  " + problem + "   (" + (byReferenced.size() + intrinsicSubjects.size()) + "):");

    if (!byReferenced.isEmpty()) {
      // Cross-reference: one row per referenced asset (the topic, named by its id) "@" the subjects
      // where it was found (shown as their files). Ids are padded so every "@" separator lines up.
      final int idCol = byReferenced.keySet().stream()
          .mapToInt(referenced -> asset(referenced, false).length()).max().orElse(0);
      byReferenced.entrySet().stream().sorted(Map.Entry.comparingByKey())
          .forEach(entry -> this.out.println("    " + pad(asset(entry.getKey(), false), idCol)
              + "   @ " + joinSubjects(entry.getValue(), mixedSubjects)));
    }
    // Self-contained finding(s): the subject itself is the problem — single column, no legend.
    intrinsicSubjects.forEach(subject -> this.out.println("    " + asset(subject, mixedSubjects)));
  }

  /** The group key: the finding's problem statement (or rule name), or {@code "(unknown)"} when it
   * has neither — mirrors {@code AnalysisResults}'s by-problem pivot and keeps {@code groupingBy} from
   * rejecting a null key (legacy findings built without a kind/problem). */
  private static String problemLabel(final Finding finding) {
    final String label = finding.getProblemOrKind();
    return label == null ? "(unknown)" : label;
  }

  private static String pad(final String text, final int width) {
    return width <= 0 ? text : String.format("%-" + width + "s", text);
  }

  /** Maps each referenced asset to the subjects that reference it, preserving first-seen order. */
  private static Map<AssetRef, List<AssetRef>> referencedToSubjects(final List<Finding> group) {
    final Map<AssetRef, List<AssetRef>> byReferenced = new LinkedHashMap<>();
    for (final Finding finding : group) {
      for (final AssetRef referenced : finding.getResult().getReferences()) {
        byReferenced.computeIfAbsent(referenced, key -> new ArrayList<>())
            .add(finding.getResult().getSubject());
      }
    }
    return byReferenced;
  }

  private static long distinctSubjectTypeCount(final List<Finding> group) {
    return group.stream().map(finding -> finding.getResult().getSubject().getType()).distinct()
        .count();
  }

  private static String joinSubjects(final List<AssetRef> subjects, final boolean withType) {
    final List<String> ids = subjects.stream().distinct()
        .map(subject -> asset(subject, withType)).sorted().collect(Collectors.toList());
    final String shown = ids.stream().limit(MAX_FACTS_SHOWN).collect(Collectors.joining(", "));
    return ids.size() > MAX_FACTS_SHOWN ? shown + ", … (" + ids.size() + " total)" : shown;
  }

  /**
   * Renders an asset for the report. Subjects already carry their SDK-relative path (set by
   * {@code ValidationResult.getSubject()} from the fact), so the file a reader opens shows directly;
   * references carry their plain id. Either way the id is shown as-is, with a humanised type prefix
   * only when a group mixes subject types and the id is not already a self-describing path.
   */
  private static String asset(final AssetRef asset, final boolean withType) {
    final String id = asset.getId();
    if (id == null || id.isBlank()) {
      // Defensive fallback for a fact with no id of its own: name it by its type.
      return humanize(asset.getType());
    }
    // A path id (it contains a "/") already identifies what it is, so it never needs the type prefix
    // that disambiguates bare ids when a group mixes subject types.
    if (withType && !id.contains("/")) {
      return humanize(asset.getType()) + " " + id;
    }
    return id;
  }

  /** Turns a camelCase type name into spaced lower case, e.g. {@code businessEntity} → "business entity". */
  private static String humanize(final String type) {
    return type.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
  }
}
