package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.util.List;

import lombok.Data;

@Data
public class DocumentType {
  private String id;
  private String namespace;
  private String rootElement;
  private String schemaLocation;

  private List<DocumentTypeNamespace> additionalNamespaces;
}
