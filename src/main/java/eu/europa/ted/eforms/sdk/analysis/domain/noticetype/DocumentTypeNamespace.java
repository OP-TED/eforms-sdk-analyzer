package eu.europa.ted.eforms.sdk.analysis.domain.noticetype;

import java.io.Serializable;

public class DocumentTypeNamespace implements Serializable {
  private static final long serialVersionUID = -2784549867558463539L;

  private String prefix;
  private String uri;
  private String schemaLocation;

  public String getPrefix() {
    return this.prefix;
  }

  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  public String getUri() {
    return this.uri;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  public String getSchemaLocation() {
    return this.schemaLocation;
  }

  public void setSchemaLocation(final String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }
}
