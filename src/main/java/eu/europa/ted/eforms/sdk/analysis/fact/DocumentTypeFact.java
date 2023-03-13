package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.domain.noticetype.DocumentType;
import lombok.Data;

@Data
public class DocumentTypeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 2293703290220188078L;

  private DocumentType documentType;

  public DocumentTypeFact(DocumentType documentType) {
    this.documentType = documentType;
  }

  @Override
  public String getId() {
    return documentType.getId();
  }

  @Override
  public String getTypeName() {
    return "documentType";
  }
}
