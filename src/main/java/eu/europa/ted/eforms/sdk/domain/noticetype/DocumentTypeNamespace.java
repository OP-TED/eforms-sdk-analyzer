package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.io.Serializable;
import lombok.Data;

@Data
public class DocumentTypeNamespace implements Serializable {
  private static final long serialVersionUID = -2784549867558463539L;

  private String prefix;
  private String uri;
  private String schemaLocation;
}
