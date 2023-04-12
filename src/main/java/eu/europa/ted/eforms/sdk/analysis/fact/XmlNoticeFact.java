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

	@Override
	public String getId() {
		return xmlNotice.getFilename();
	}
	
	@Override
	public String getTypeName() {
	  return "xmlNotice";
	}
}
