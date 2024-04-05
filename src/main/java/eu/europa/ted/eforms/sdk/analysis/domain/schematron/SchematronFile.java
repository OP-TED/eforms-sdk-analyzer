package eu.europa.ted.eforms.sdk.analysis.domain.schematron;

import java.nio.file.Path;
import java.util.List;

public class SchematronFile {
  private final Path path;
  private final List<SchematronAssert> asserts;

  public SchematronFile(Path path) {
    this(path, null);
  }

  public SchematronFile(Path path, List<SchematronAssert> asserts) {
    this.path = path;
    this.asserts = asserts;
  }

  public Path getPath() {
    return path;
  }

  public List<SchematronAssert> getAsserts() {
    return asserts;
  }
}
