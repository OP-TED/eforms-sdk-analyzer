package eu.europa.ted.eforms.sdk.analysis.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MetadataDatabase implements Serializable {
  private static final long serialVersionUID = -3586173611946643080L;

  private String version;
  private LocalDateTime createdOn;

  public String getVersion() {
    return this.version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public LocalDateTime getCreatedOn() {
    return this.createdOn;
  }

  public void setCreatedOn(final LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }
}
