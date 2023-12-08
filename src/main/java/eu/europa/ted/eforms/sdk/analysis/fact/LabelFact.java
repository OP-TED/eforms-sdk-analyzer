package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;

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

  public String getNormalizedId() {
    String id = label.getId();
    return id.strip().toLowerCase().replaceAll("[-_\\.]", "");
  }

  @Override
  public String getTypeName() {
    return "label";
  }
}
