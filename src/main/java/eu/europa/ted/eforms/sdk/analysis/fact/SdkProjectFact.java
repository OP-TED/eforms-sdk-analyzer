package eu.europa.ted.eforms.sdk.analysis.fact;

import com.vdurmont.semver4j.Semver;

import eu.europa.ted.eforms.sdk.domain.SdkProject;

public class SdkProjectFact {

  private final SdkProject sdkProject;

  public SdkProjectFact(SdkProject sdkProject) {
    this.sdkProject = sdkProject;
  }

  public String getVersion() {
    return sdkProject.getVersion();
  }
  
  public String getVersionMajorMinor() {
    Semver version = new Semver(sdkProject.getVersion());

    return String.format("%s.%s", version.getMajor(), version.getMinor());
  }

  public String getCustomizationId() {
    return "eforms-sdk-" + getVersionMajorMinor();
  }
}
