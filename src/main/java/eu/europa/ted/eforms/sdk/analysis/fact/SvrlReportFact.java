package eu.europa.ted.eforms.sdk.analysis.fact;

import org.apache.commons.lang3.Validate;

import eu.europa.ted.eforms.sdk.domain.SvrlReport;

public class SvrlReportFact implements SdkComponentFact<String> {

	private final SvrlReport svrlReport;

	public SvrlReportFact(SvrlReport svrlReport) {
		Validate.notNull(svrlReport, "SvrlReport is null");
		this.svrlReport = svrlReport;
	}

	public int getErrorCount() {
		return svrlReport.getErrorCount();
	}

	/**
	 * Returns true if the report is for an XML notice that is intended to be valid.
	 */
	public boolean shouldBeValid() {
		return !svrlReport.getFilename().startsWith("INVALID");
	}

	@Override
	public String getId() {
		int extensionIndex = svrlReport.getFilename().lastIndexOf(".");
		return svrlReport.getFilename().substring(0, extensionIndex);
	}

	@Override
	public String getTypeName() {
	  return "svrlReport";
	}
}
