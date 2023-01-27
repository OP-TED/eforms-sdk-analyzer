package eu.europa.ted.eforms.sdk.domain.noticetype;

import lombok.Data;

@Data
public class DocumentTypeNamespace {
  private String prefix;
  private String uri;
  private String schemaLocation;
}
