package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplatesIndex;

public class ViewTemplatesIndexFact implements SdkComponentFact<String> {

  private final TedefoViewTemplatesIndex viewTemplatesIndex;

  public ViewTemplatesIndexFact(TedefoViewTemplatesIndex viewTemplatesIndex) {
    this.viewTemplatesIndex = viewTemplatesIndex;
  }

  public Set<String> getLabelIds() {
    return viewTemplatesIndex.getViewTemplates().stream()
        .map(TedefoViewTemplateIndex::getLabelId)
        .collect(Collectors.toSet());
  }

  @Override
  public String getId() {
    return StringUtils.EMPTY;
  }

  @Override
  public String getTypeName() {
    return "viewTemplatesIndex";
  }
}
