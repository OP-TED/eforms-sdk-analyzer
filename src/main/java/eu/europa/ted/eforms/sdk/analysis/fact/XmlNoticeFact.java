package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;

public class XmlNoticeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 7260419598936482554L;

  private final XmlNotice xmlNotice;

  public XmlNoticeFact(XmlNotice xmlNotice) {
    this.xmlNotice = xmlNotice;
  }

  public String getCustomizationId() {
    return xmlNotice.getCustomizationId();
  }

  /**
   * Returns true if the XML notice is intended to be a valid notice.
   */
  public boolean shouldBeValid() {
    return !xmlNotice.getFilename().startsWith("INVALID");
  }

  @Override
  public String getId() {
    int extensionIndex = xmlNotice.getFilename().lastIndexOf(".");
    return xmlNotice.getFilename().substring(0, extensionIndex);
  }

  @Override
  public String getTypeName() {
    return "xmlNotice";
  }
}
