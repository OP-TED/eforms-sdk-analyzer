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
    return ublVersion;
  }

  public String getSdkVersion() {
    return sdkVersion;
  }

  public String getFullSdkVersion() {
    return fullSdkVersion;
  }

  public String getVersionId() {
    return versionId;
  }

  public LocalDateTime getVersionUpdatedOn() {
    return versionUpdatedOn;
  }
}
