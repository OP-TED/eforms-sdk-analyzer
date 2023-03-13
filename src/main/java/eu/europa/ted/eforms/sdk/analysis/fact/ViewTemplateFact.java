package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.domain.view.index.TedefoViewTemplateIndex;
import lombok.Data;

@Data
public class ViewTemplateFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 591669979811179909L;

  private TedefoViewTemplateIndex viewTemplate;

  public ViewTemplateFact(TedefoViewTemplateIndex viewTemplate) {
    this.viewTemplate = viewTemplate;
  }

  @Override
  public String getId() {
    return viewTemplate.getId();
  }

  @Override
  public String getTypeName() {
    return "viewTemplate";
  }
}
