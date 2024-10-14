package eu.europa.ted.eforms.sdk.analysis.domain.schematron;

public class SchematronAssert {
  private final String id;
  private String diagnostics;

  public SchematronAssert(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getDiagnostics() {
    return diagnostics;
  }

  public void setDiagnostics(String diagnostics) {
    this.diagnostics = diagnostics;
  }
}
