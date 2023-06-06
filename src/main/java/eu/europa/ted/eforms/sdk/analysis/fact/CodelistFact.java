package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.Set;
import eu.europa.ted.eforms.sdk.domain.codelist.Codelist;

public class CodelistFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 597836162298039219L;

  private Codelist codelist;

  public CodelistFact(Codelist codelist) {
    this.codelist = codelist;
  }

  public String getParentId() {
    return codelist.getParentId();
  }

  public Set<String> getCodes() {
    return codelist.getCodes();
  }

  public boolean isTailored() {
    return codelist.getParentId() != null;
  }

  @Override
  public String getId() {
    return codelist.getId();
  }

  @Override
  public String getTypeName() {
    return "codelist";
  }
}
