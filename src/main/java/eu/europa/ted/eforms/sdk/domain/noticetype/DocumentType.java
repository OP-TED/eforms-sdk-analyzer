package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class DocumentType implements Serializable {
  private static final long serialVersionUID = -6022318533541565505L;

  private String id;
  private String namespace;
  private String rootElement;
  private String schemaLocation;

  private List<DocumentTypeNamespace> additionalNamespaces;

  public String getId() {
    return id;
  }

  public String getSchemaLocation() {
    return schemaLocation;
  }
}
