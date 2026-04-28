package eu.europa.ted.eforms.sdk.analysis.fact;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;

class DocumentTypeFactTest {

  private static DocumentType documentTypeWithSchemaLocation(final String location) {
    return new DocumentType() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getSchemaLocation() {
        return location;
      }
    };
  }

  @Test
  void schemaLocationIsAssumedValidWhenSdkRootIsNull() {
    final DocumentTypeFact fact = new DocumentTypeFact(
        documentTypeWithSchemaLocation("schemas/notice-types/something.xsd"));

    assertTrue(fact.schemaLocationExists(null));
  }

  @Test
  void schemaLocationExistsAgainstSdkRoot(@TempDir final Path tmp) throws Exception {
    final Path schema = tmp.resolve("schemas").resolve("notice-types").resolve("X01.xsd");
    Files.createDirectories(schema.getParent());
    Files.createFile(schema);

    final DocumentTypeFact fact = new DocumentTypeFact(
        documentTypeWithSchemaLocation("schemas/notice-types/X01.xsd"));

    assertTrue(fact.schemaLocationExists(tmp));
  }

  @Test
  void schemaLocationDoesNotExistWhenFileMissing(@TempDir final Path tmp) {
    final DocumentTypeFact fact = new DocumentTypeFact(
        documentTypeWithSchemaLocation("schemas/notice-types/missing.xsd"));

    assertFalse(fact.schemaLocationExists(tmp));
  }
}
