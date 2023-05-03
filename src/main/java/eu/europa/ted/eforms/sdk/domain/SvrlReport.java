package eu.europa.ted.eforms.sdk.domain;

/**
 * Information in an SVRL validation report, limited to what is needed for analysis
 */
public class SvrlReport {
  private final String filename;
  // Number of failed asserts with role="ERROR"
  private final int errorCount;

  public SvrlReport(String filename, int errorCount) {
    this.filename = filename;
    this.errorCount = errorCount;
  }

  public String getFilename() {
    return filename;
  }

  public int getErrorCount() {
    return errorCount;
  }
}
