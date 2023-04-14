package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.domain.XmlNotice;

public class XmlNoticeFact implements SdkComponentFact<String> {

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
