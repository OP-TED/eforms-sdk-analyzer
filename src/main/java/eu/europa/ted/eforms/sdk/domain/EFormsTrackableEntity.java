package eu.europa.ted.eforms.sdk.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase"})
public class EFormsTrackableEntity {
  private String ublVersion;
  private String sdkVersion;
  private MetadataDatabase metadataDatabase;

  public String getUblVersion() {
    return ublVersion;
  }

  public String getSdkVersion() {
    return sdkVersion;
  }

  public MetadataDatabase getMetadataDatabase() {
    return metadataDatabase;
  }
}
