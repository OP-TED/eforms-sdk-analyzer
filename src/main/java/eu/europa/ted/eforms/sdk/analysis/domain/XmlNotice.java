package eu.europa.ted.eforms.sdk.analysis.domain;

import java.io.Serializable;

/**
 * Information in an XML notice, limited to what is required for the analysis.
 */
public class XmlNotice implements Serializable {
  private static final long serialVersionUID = 2167488422739343874L;

  private final String filename;
  private final String customizationId;

  public XmlNotice(String filename, String customizationId) {
    this.filename = filename;
    this.customizationId = customizationId;
  }

  public String getFilename() {
    return filename;
  }

  public String getCustomizationId() {
    return customizationId;
  }
}
