package eu.europa.ted.eforms.sdk.analysis.vo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.enums.MissingLabelKind;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;

/**
 * Presentation-neutral aggregate of a validation run: the errors and warnings, the pass/fail
 * contract, and the deduplicated missing-label findings. Renderers (console today, JSON later) turn
 * this into output; it performs no logging itself.
 */
public class AnalysisResults {
  private final List<ValidationResult> errors;
  private final List<ValidationResult> warnings;

  public AnalysisResults(final Collection<ValidationResult> results) {
    this.errors = filterByStatus(results, ValidationStatusEnum.ERROR);
    this.warnings = filterByStatus(results, ValidationStatusEnum.WARNING);
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

  /**
   * Errors that are not about a missing label. The missing-label errors are summarised separately by
   * {@link #missingLabels(MissingLabelKind)} so that thousands of per-occurrence lines collapse into
   * one entry per identifier.
   */
  public List<ValidationResult> otherErrors() {
    return this.errors.stream()
        .filter(error -> error.getMissingLabelIds().isEmpty())
        .collect(Collectors.toUnmodifiableList());
  }

  /** Missing-label identifiers of the given kind, each with how many times it was referenced. */
  public SortedMap<String, Long> missingLabels(final MissingLabelKind kind) {
    return this.errors.stream()
        .filter(error -> error.getMissingLabelKind() == kind)
        .flatMap(error -> error.getMissingLabelIds().stream())
        .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
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
