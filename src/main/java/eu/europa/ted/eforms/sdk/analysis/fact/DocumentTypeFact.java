package eu.europa.ted.eforms.sdk.analysis.fact;

import java.nio.file.Files;
import java.nio.file.Path;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;

public class DocumentTypeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 2293703290220188078L;

  private DocumentType documentType;

  public DocumentTypeFact(DocumentType documentType) {
    this.documentType = documentType;
  }

  public String getSchemaLocation() {
    return this.documentType.getSchemaLocation();
  }

  public boolean schemaLocationExists(final Path sdkRoot) {
    // Without a known SDK root (e.g. when running against a content source
    // that is not file-system backed) we cannot verify the schema location.
    // The rule is not applicable in that mode.
    if (sdkRoot == null) {
      return true;
    }
    return Files.exists(Path.of(sdkRoot.toString(), this.documentType.getSchemaLocation()));
  }

  @Override
  public String getId() {
    return this.documentType.getId();
  }

  @Override
  public String getTypeName() {
    return "documentType";
  }
}
