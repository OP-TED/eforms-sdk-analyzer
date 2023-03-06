package eu.europa.ted.eforms.sdk.domain;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase"})
public class EFormsTrackableEntity implements Serializable {
  private static final long serialVersionUID = 1610996084241775417L;

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
