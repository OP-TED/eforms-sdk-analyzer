package eu.europa.ted.eforms.sdk.domain;

import java.io.Serializable;

/**
 * Information in an SVRL validation report, limited to what is needed for analysis
 */
public class SvrlReport implements Serializable {
  private static final long serialVersionUID = 5047594203212205807L;

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
