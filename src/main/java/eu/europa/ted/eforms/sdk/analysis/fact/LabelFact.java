package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.Set;

import eu.europa.ted.eforms.sdk.analysis.domain.Label;
import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;

public class LabelFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -8325643682910825716L;

  private Label label;

  public LabelFact(Label label) {
    this.label = label;
  }

  @Override
  public String getId() {
    return label.getId();
  }

  public Language getLanguage() {
    return label.getLanguage();
  }

  public String getText() {
    return label.getText();
  }

  private final boolean isInvalidCharacter(int codePoint) {
    switch (Character.getType(codePoint)) {
      case Character.CONTROL:
      case Character.FORMAT:
      case Character.PRIVATE_USE:
      case Character.SURROGATE:
      case Character.UNASSIGNED:
        return true;
      default:
        return false;
    }
  }
  
  public Set<String> getInvalidCharacters() {
    Set<String> invalidCharacters = new HashSet<>();

    label.getText().codePoints().filter(c -> isInvalidCharacter(c)).forEach(c -> {
      invalidCharacters.add(Character.toString(c));
    });

    return invalidCharacters;
  }

  @Override
  public String getTypeName() {
    return "label";
  }
}
