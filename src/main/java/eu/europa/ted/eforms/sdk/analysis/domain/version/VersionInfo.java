package eu.europa.ted.eforms.sdk.analysis.domain.version;

import java.time.LocalDateTime;

/**
 * This groups the version information and pass it as one parameter. Generally for JSON export. This
 * information comes from the VersionHelper.
 */
public final class VersionInfo {
  private String ublVersion;
  private String sdkVersion;
  private String fullSdkVersion;
  private String versionId;
  private LocalDateTime versionUpdatedOn;

  public String getUblVersion() {
    return this.ublVersion;
  }

  public void setUblVersion(final String ublVersion) {
    this.ublVersion = ublVersion;
  }

  public String getSdkVersion() {
    return this.sdkVersion;
  }

  public void setSdkVersion(final String sdkVersion) {
    this.sdkVersion = sdkVersion;
  }

  public String getFullSdkVersion() {
    return this.fullSdkVersion;
  }

  public void setFullSdkVersion(final String fullSdkVersion) {
    this.fullSdkVersion = fullSdkVersion;
  }

  public String getVersionId() {
    return this.versionId;
  }

  public void setVersionId(final String versionId) {
    this.versionId = versionId;
  }

  public LocalDateTime getVersionUpdatedOn() {
    return this.versionUpdatedOn;
  }

  public void setVersionUpdatedOn(final LocalDateTime versionUpdatedOn) {
    this.versionUpdatedOn = versionUpdatedOn;
  }
}
