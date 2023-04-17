package eu.europa.ted.eforms.sdk.analysis.vo;

import com.vdurmont.semver4j.Semver;

public class SdkMetadata {

  private final String sdkVersion;

  public SdkMetadata(String sdkVersion) {
    this.sdkVersion = sdkVersion;
  }

  public String getVersion() {
    return sdkVersion;
  }
  
  public String getVersionMajorMinor() {
    Semver version = new Semver(sdkVersion);

    return String.format("%s.%s", version.getMajor(), version.getMinor());
  }

  public String getCustomizationId() {
    return "eforms-sdk-" + getVersionMajorMinor();
  }
}
