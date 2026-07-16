package eu.europa.ted.eforms.sdk.analysis.domain.noticetype;

import java.io.Serializable;
import java.util.List;

public class DocumentType implements Serializable {
  private static final long serialVersionUID = -6022318533541565505L;

  private String id;
  private String namespace;
  private String rootElement;
  private String schemaLocation;

  private List<DocumentTypeNamespace> additionalNamespaces;

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getNamespace() {
    return this.namespace;
  }

  public void setNamespace(final String namespace) {
    this.namespace = namespace;
  }

  public String getRootElement() {
    return this.rootElement;
  }

  public void setRootElement(final String rootElement) {
    this.rootElement = rootElement;
  }

  public String getSchemaLocation() {
    return this.schemaLocation;
  }

  public void setSchemaLocation(final String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }

  public List<DocumentTypeNamespace> getAdditionalNamespaces() {
    return this.additionalNamespaces;
  }

  public void setAdditionalNamespaces(final List<DocumentTypeNamespace> additionalNamespaces) {
    this.additionalNamespaces = additionalNamespaces;
  }
}
