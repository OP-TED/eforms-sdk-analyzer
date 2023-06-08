package eu.europa.ted.eforms.sdk.analysis.fact;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;

public class DocumentTypeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 2293703290220188078L;

  private DocumentType documentType;

  public DocumentTypeFact(DocumentType documentType) {
    this.documentType = documentType;
  }

  public String getSchemaLocation() {
    return documentType.getSchemaLocation();
  }

  public boolean schemaLocationExists(Path sdkRoot) {
    return Files
        .exists(Path.of(Optional.ofNullable(sdkRoot).orElse(Path.of(StringUtils.EMPTY)).toString(),
            documentType.getSchemaLocation()));
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
