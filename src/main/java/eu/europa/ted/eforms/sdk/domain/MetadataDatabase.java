package eu.europa.ted.eforms.sdk.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MetadataDatabase implements Serializable {
  private static final long serialVersionUID = -3586173611946643080L;

  private String version;
  private LocalDateTime createdOn;

  public String getVersion() {
    return version;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }
}
