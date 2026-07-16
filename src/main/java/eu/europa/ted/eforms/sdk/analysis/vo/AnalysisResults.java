package eu.europa.ted.eforms.sdk.analysis.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;

/**
 * Presentation-neutral aggregate of a validation run: the errors and warnings, the pass/fail
 * contract, the deduplicated missing-label findings, and the by-kind / by-asset pivots. Renderers
 * (console today, JSON later) turn this into output; it performs no logging itself.
 */
public class AnalysisResults {
  private final List<Finding> findings;
  private final List<ValidationResult> errors;
  private final List<ValidationResult> warnings;

  /**
   * Builds from plain results (no kind). Preserved for current consumers (e.g. the MDM analysis
   * service). The by-kind pivot reports such findings under {@code "(unknown)"}.
   */
  public AnalysisResults(final Collection<ValidationResult> results) {
    this(results.stream().map(result -> new Finding(null, result)).collect(Collectors.toList()));
  }

  private AnalysisResults(final List<Finding> findings) {
    this.findings = Collections.unmodifiableList(new ArrayList<>(findings));
    final List<ValidationResult> results =
        findings.stream().map(Finding::getResult).collect(Collectors.toList());
    this.errors = filterByStatus(results, ValidationStatusEnum.ERROR);
    this.warnings = filterByStatus(results, ValidationStatusEnum.WARNING);
  }

  /** Builds from kind-stamped findings, enabling the by-kind pivot. */
  public static AnalysisResults of(final Collection<Finding> findings) {
    return new AnalysisResults(new ArrayList<>(findings));
  }

  public List<ValidationResult> getErrors() {
    return this.errors;
  }

  public List<ValidationResult> getWarnings() {
    return this.warnings;
  }

  public int errorCount() {
    return this.errors.size();
  }

  public int warningCount() {
    return this.warnings.size();
  }

  public boolean isClean() {
    return this.errors.isEmpty();
  }

  public int exitCode() {
    return this.errors.isEmpty() ? 0 : 1;
  }

  // --- Pivots over error findings -------------------------------------------------------------

  /**
   * Error findings grouped by problem statement (the rule's {@code @problem}, falling back to its
   * name), with counts. Rules that share a {@code @problem} merge. Unknown buckets under "(unknown)".
   */
  public SortedMap<String, Long> errorsByProblem() {
    return problemCounts(ValidationStatusEnum.ERROR);
  }

  /**
   * Warning findings grouped by problem statement, with counts. Mirrors {@link #errorsByProblem()}
   * so warnings can be reported alongside — but separately from — the actionable errors.
   */
  public SortedMap<String, Long> warningsByProblem() {
    return problemCounts(ValidationStatusEnum.WARNING);
  }

  private SortedMap<String, Long> problemCounts(final ValidationStatusEnum status) {
    return findingsByStatus(status)
        .collect(Collectors.groupingBy(AnalysisResults::problemOf, TreeMap::new,
            Collectors.counting()));
  }

  /** Referenced assets across error findings, with how many error findings reference each. */
  public SortedMap<AssetRef, Long> errorsByReferencedAsset() {
    return errorFindings()
        .flatMap(finding -> finding.getResult().getReferences().stream())
        .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
  }

  /** Subject assets of error findings, with counts. */
  public SortedMap<AssetRef, Long> errorsBySubject() {
    return errorFindings()
        .map(finding -> finding.getResult().getSubject())
        .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
  }

  /**
   * Error findings grouped by SDK section, in section (enum) order. The section is derived from each
   * finding's subject asset type via {@link SdkSection#forType(String)}.
   */
  public Map<SdkSection, List<Finding>> errorFindingsBySection() {
    return findingsBySection(ValidationStatusEnum.ERROR);
  }

  /**
   * Warning findings grouped by SDK section, in section order. Mirrors
   * {@link #errorFindingsBySection()}; the warnings are advisory and do not affect the exit code.
   */
  public Map<SdkSection, List<Finding>> warningFindingsBySection() {
    return findingsBySection(ValidationStatusEnum.WARNING);
  }

  private Map<SdkSection, List<Finding>> findingsBySection(final ValidationStatusEnum status) {
    return findingsByStatus(status).collect(Collectors.groupingBy(
        finding -> SdkSection.forType(finding.getResult().getSubject().getType()),
        () -> new EnumMap<>(SdkSection.class),
        Collectors.toList()));
  }

  private Stream<Finding> errorFindings() {
    return findingsByStatus(ValidationStatusEnum.ERROR);
  }

  private Stream<Finding> findingsByStatus(final ValidationStatusEnum status) {
    return this.findings.stream()
        .filter(finding -> finding.getResult().getStatus() == status);
  }

  private static String problemOf(final Finding finding) {
    final String label = finding.getProblemOrKind();
    return label == null ? "(unknown)" : label;
  }

  private static List<ValidationResult> filterByStatus(final Collection<ValidationResult> results,
      final ValidationStatusEnum status) {
    // Sort for deterministic output: validators return findings in unspecified (set) order.
    return results.stream()
        .filter(result -> result.getStatus() == status)
        .sorted(Comparator.comparing(ValidationResult::toString))
        .collect(Collectors.toUnmodifiableList());
  }
}
