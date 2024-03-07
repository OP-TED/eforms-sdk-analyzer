package eu.europa.ted.eforms.sdk.analysis.fact;

import java.nio.file.Path;

public class SchematronFileFact implements SdkComponentFact<String> {

  private final Path path;

  public SchematronFileFact(Path path) {
      this.path = path;
  }

  @Override
  public String getId() {
      return path.toString();
  }

  @Override
  public String getTypeName() {
    return "schematron";
  }
}
