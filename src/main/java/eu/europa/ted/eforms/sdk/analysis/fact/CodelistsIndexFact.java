package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eu.europa.ted.eforms.sdk.analysis.domain.codelist.CodelistsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.CodelistsIndexItem;

public class CodelistsIndexFact implements SdkComponentFact<String> {
  private CodelistsIndex codelistIndex;

  public CodelistsIndexFact(CodelistsIndex codelistIndex) {
    this.codelistIndex = codelistIndex;
  }
  
  public List<CodelistsIndexItem> getIndexItems() {
    return codelistIndex.getCodelists();
  }

  @Override
  public String getId() {
    return StringUtils.EMPTY;
  }
  
  @Override
  public String getTypeName() {
    return "codelistIndex";
  }
}
