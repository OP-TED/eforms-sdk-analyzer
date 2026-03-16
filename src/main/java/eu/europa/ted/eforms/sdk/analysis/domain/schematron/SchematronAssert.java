package eu.europa.ted.eforms.sdk.analysis.domain.schematron;

public class SchematronAssert {
  private final String id;
  private final String patternId;
  private String diagnostics;

  public SchematronAssert(String id, String patternId) {
    this.id = id;
    this.patternId = patternId;
  }

  public String getId() {
    return id;
  }

  public String getPatternId() {
    return patternId;
  }

  public String getDiagnostics() {
    return diagnostics;
  }

  public void setDiagnostics(String diagnostics) {
    this.diagnostics = diagnostics;
  }
}
