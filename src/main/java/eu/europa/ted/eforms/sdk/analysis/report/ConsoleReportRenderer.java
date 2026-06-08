package eu.europa.ted.eforms.sdk.analysis.report;

import java.io.PrintStream;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.enums.MissingLabelKind;
import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Renders {@link AnalysisResults} as a human-readable report on a print stream (stdout by default).
 *
 * <p>The default report is deduplicated: missing labels are listed once per identifier with a
 * reference count, and other errors are grouped. Verbose mode additionally lists every individual
 * finding. Writing the report here rather than through the logger keeps it free of log decoration
 * and separate from framework logging.
 */
public class ConsoleReportRenderer {
  private static final String FOUND_TITLE =
      "Labels found missing from the export, referenced by the SDK but not present";
  private static final String ASSUMED_TITLE =
      "Labels assumed missing during export, identifier left in label text by the exporter";

  private final PrintStream out;

  public ConsoleReportRenderer() {
    this(System.out);
  }

  public ConsoleReportRenderer(final PrintStream out) {
    this.out = out;
  }

  public void render(final AnalysisResults results, final boolean verbose) {
    renderWarnings(results, verbose);
    renderErrors(results, verbose);

    if (results.isClean() && results.warningCount() == 0) {
      this.out.println("No validation errors or warnings found.");
    }
  }

  private void renderWarnings(final AnalysisResults results, final boolean verbose) {
    if (results.warningCount() == 0) {
      return;
    }
    this.out.println("Total number of validation warnings: " + results.warningCount());
    if (verbose) {
      results.getWarnings().forEach(warning -> this.out.println("  " + warning));
    }
  }

  private void renderErrors(final AnalysisResults results, final boolean verbose) {
    if (results.isClean()) {
      return;
    }

    if (verbose) {
      this.out.println("All validation errors (" + results.errorCount() + "):");
      results.getErrors().forEach(error -> this.out.println("  " + error));
    } else {
      renderGroupedOtherErrors(results.otherErrors());
    }

    renderMissingLabels(FOUND_TITLE, results.missingLabels(MissingLabelKind.FOUND));
    renderMissingLabels(ASSUMED_TITLE, results.missingLabels(MissingLabelKind.ASSUMED));

    this.out.println("Total number of validation errors: " + results.errorCount());

    if (!verbose) {
      this.out.println("Re-run the analyzer with --verbose to see every individual finding.");
    }
  }

  private void renderGroupedOtherErrors(final List<ValidationResult> otherErrors) {
    if (otherErrors.isEmpty()) {
      return;
    }
    final SortedMap<String, Long> grouped = otherErrors.stream().collect(
        Collectors.groupingBy(ValidationResult::toString, TreeMap::new, Collectors.counting()));
    this.out.println("Errors (" + grouped.size() + " unique):");
    grouped.forEach((line, count) ->
        this.out.println(count > 1 ? "  " + line + " (x" + count + ")" : "  " + line));
  }

  private void renderMissingLabels(final String title, final SortedMap<String, Long> labels) {
    if (labels.isEmpty()) {
      return;
    }
    this.out.println(title + " (" + labels.size()
        + " unique, identifier and number of references):");
    labels.forEach((labelId, count) -> this.out.println("  " + labelId + " (" + count + ")"));
  }
}
