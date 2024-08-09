package eu.europa.ted.eforms.sdk.analysis.domain.schematron;

public class SchematronDiagnostic {
  private final String id;

  public SchematronDiagnostic(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
