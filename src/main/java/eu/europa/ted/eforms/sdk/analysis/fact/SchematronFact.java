package eu.europa.ted.eforms.sdk.analysis.fact;

import java.nio.file.Path;

public class SchematronFact implements SdkComponentFact<String> {

  private final Path path;

  public SchematronFact(Path path) {
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
