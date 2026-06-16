package eu.europa.ted.eforms.sdk.analysis.report;

import java.io.PrintStream;

import eu.europa.ted.eforms.sdk.analysis.vo.AnalysisResults;

/**
 * Renders the closing headline — the grand total of errors (or, on a clean run, the all-clear
 * message) — and, when {@code full}, the complete list of every individual finding. The summary view
 * uses it without the list (just the headline); the detail file uses it with the full list. Writing
 * this here rather than through the logger keeps it free of log decoration and separate from framework
 * logging.
 */
public class DetailReportRenderer {
  private final PrintStream out;

  public DetailReportRenderer() {
    this(System.out);
  }

  public DetailReportRenderer(final PrintStream out) {
    this.out = out;
  }

  public void render(final AnalysisResults results, final boolean full) {
    if (results.isClean() && results.warningCount() == 0) {
      this.out.println("No validation errors or warnings found.");
      return;
    }

    // The summary view shows only the headline; the detail view (full) lists every individual finding.
    if (full) {
      listEveryFinding(results);
    }

    // Closing footer, set off by a blank line from whatever precedes it.
    this.out.println();
    if (!results.isClean()) {
      this.out.println("Total number of validation errors: " + results.errorCount());
    }
  }

  /** Every finding in full, errors before warnings — matching the order of the summary above. */
  private void listEveryFinding(final AnalysisResults results) {
    if (!results.isClean()) {
      banner("All validation errors (" + results.errorCount() + ")");
      results.getErrors().forEach(error -> this.out.println("  " + error));
    }
    if (results.warningCount() > 0) {
      banner("All validation warnings (" + results.warningCount() + ")");
      results.getWarnings().forEach(warning -> this.out.println("  " + warning));
    }
  }

  /** A section banner matching SummaryReportRenderer: a blank line, {@code === title ===}, a blank line. */
  private void banner(final String title) {
    this.out.println();
    this.out.println("=== " + title + " ===");
    this.out.println();
  }
}
