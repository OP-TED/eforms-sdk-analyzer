package eu.europa.ted.eforms.sdk.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MetadataDatabase {
  private String version;
  private LocalDateTime createdOn;
}
