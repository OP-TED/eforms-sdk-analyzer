package eu.europa.ted.eforms.sdk.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MetadataDatabase implements Serializable {
  private static final long serialVersionUID = -3586173611946643080L;

  private String version;
  private LocalDateTime createdOn;
}
