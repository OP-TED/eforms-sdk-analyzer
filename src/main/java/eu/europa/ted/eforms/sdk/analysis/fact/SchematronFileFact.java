package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronAssert;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronDiagnostic;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronPattern;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronPhase;

/**
 * Represents a complete set of schematron rules.
 * This corresponds to one file with the 'schema' root element, along with any
 * included partial files.
 */
public class SchematronFileFact implements SdkComponentFact<String> {

  private final SchematronFile schematronFile;

  public SchematronFileFact(SchematronFile schematronFile) {
      this.schematronFile = schematronFile;
  }

  public List<SchematronAssert> getAsserts() {
    return schematronFile.getAsserts();
  }

  public List<SchematronDiagnostic> getDiagnostics() {
    return schematronFile.getDiagnostics();
  }

  public List<SchematronPhase> getPhases() {
    return schematronFile.getPhases();
  }

  public List<SchematronPattern> getPatterns() {
    return schematronFile.getPatterns();
  }

  public List<String> getDuplicateAssertIds() {
    Set<String> set = new HashSet<String>();
    return getAsserts().stream().map(SchematronAssert::getId).filter(id -> !set.add(id))
        .collect(Collectors.toList());
  }

  @Override
  public String getId() {
      return schematronFile.getPath().toString();
  }

  @Override
  public String getTypeName() {
    return "schematron";
  }
}
