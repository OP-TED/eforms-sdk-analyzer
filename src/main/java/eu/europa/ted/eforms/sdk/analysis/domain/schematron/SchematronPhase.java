package eu.europa.ted.eforms.sdk.analysis.domain.schematron;

import java.util.ArrayList;
import java.util.List;

public class SchematronPhase {
  private final String id;
  private final List<String> activePatterns = new ArrayList<>();

  public SchematronPhase(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public List<String> getActivePatterns() {
    return activePatterns;
  }

  public void addActivePattern(String patternId) {
    activePatterns.add(patternId);
  }
}
