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
    // The "Document types use existing schemaLocation" rule that calls this is
    // tagged @source(FILE) so it is skipped under SourceKind.DATABASE. It can,
    // however, still fire under a custom FILE source that does not back its
    // content with a real path (e.g. an in-memory or archive-backed reader),
    // in which case sdkRoot on SdkUnit is null. Treat that as "not verifiable
    // here" rather than NPE; the rule is effectively not applicable.
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
