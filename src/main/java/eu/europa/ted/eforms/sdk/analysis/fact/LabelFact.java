package eu.europa.ted.eforms.sdk.analysis.fact;

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

  @Override
  public String getTypeName() {
    return "label";
  }
}
