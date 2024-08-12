package eu.europa.ted.eforms.sdk.analysis.domain.schematron;

import java.nio.file.Path;
import java.util.List;

public class SchematronFile {
  private final Path path;
  private List<SchematronPhase> phases;
  private List<SchematronPattern> patterns;
  private List<SchematronDiagnostic> diagnostics;
  private List<SchematronAssert> asserts;

  public SchematronFile(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return path;
  }

  public List<SchematronPhase> getPhases() {
    return phases;
  }

  public void setPhases(List<SchematronPhase> phases) {
    this.phases = phases;
  }

  public List<SchematronPattern> getPatterns() {
    return patterns;
  }

  public void setPatterns(List<SchematronPattern> patterns) {
    this.patterns = patterns;
  }

  public List<SchematronDiagnostic> getDiagnostics() {
    return diagnostics;
  }

  public void setDiagnostics(List<SchematronDiagnostic> diagnostics) {
    this.diagnostics = diagnostics;
  }

  public List<SchematronAssert> getAsserts() {
    return asserts;
  }

  public void setAsserts(List<SchematronAssert> asserts) {
    this.asserts = asserts;
  }
}
