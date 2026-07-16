package eu.europa.ted.eforms.sdk.analysis.domain;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase"})
public class EFormsTrackableEntity implements Serializable {
  private static final long serialVersionUID = 1610996084241775417L;

  private String ublVersion;
  private String sdkVersion;
  private MetadataDatabase metadataDatabase;

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

  public MetadataDatabase getMetadataDatabase() {
    return this.metadataDatabase;
  }

  public void setMetadataDatabase(final MetadataDatabase metadataDatabase) {
    this.metadataDatabase = metadataDatabase;
  }
}
