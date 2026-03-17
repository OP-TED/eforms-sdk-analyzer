package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

  /**
   * Return the list of assert identifiers that appear more than once in the same phase.
   * 
   * @return list of duplicate assert ids
   */
  public List<String> getDuplicateAssertIds() {
    // Mpa from the pattern id to the id of the phase it is part of
    Map<String, String> patternToPhase = new HashMap<>();
    getPhases().forEach(phase -> {
      phase.getActivePatterns()
          .forEach(patternId -> {
            patternToPhase.put(patternId, phase.getId());
          });
    });

    // Map from phase id to the set of assert ids it contains.
    Map<String, Set<String>> phaseToAssertIds = new HashMap<String, Set<String>>();
    return getAsserts().stream()
        .filter(assrt -> {
          String phaseId = patternToPhase.get(assrt.getPatternId());
          Set<String> asserts = 
              phaseToAssertIds.computeIfAbsent(phaseId, k -> new HashSet<String>());
          // Keep SchematronAssert if its is already present in the same phase
          return !asserts.add(assrt.getId());
        })
        .map(SchematronAssert::getId)
        .collect(Collectors.toList());
  }

  /**
   * Returns the list of diagnostic identifiers that are referenced in an assert but not defined by
   * a diagnostic element.
   * 
   * @return List of diagnostic identifiers missing a definition.
   */
  public List<String> getMissingDiagnostics() {
    Set<String> definedDiagnosticIds = getDiagnostics().stream()
        .map(SchematronDiagnostic::getId)
        .collect(Collectors.toSet());

    return getAsserts().stream()
        .map(SchematronAssert::getDiagnostics)
        .filter(id -> !definedDiagnosticIds.contains(id))
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
