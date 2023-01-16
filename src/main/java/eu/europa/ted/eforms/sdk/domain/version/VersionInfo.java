package eu.europa.ted.eforms.sdk.domain.version;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * This groups the version information and pass it as one parameter. Generally for JSON export. This
 * information comes from the VersionHelper.
 */
@Data
public final class VersionInfo {
  private String ublVersion;
  private String sdkVersion;
  private String fullSdkVersion;
  private String versionId;
  private LocalDateTime versionUpdatedOn;
}
