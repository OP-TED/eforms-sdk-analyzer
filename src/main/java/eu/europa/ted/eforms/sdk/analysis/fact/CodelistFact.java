package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.ColumnSet.Column;

public class CodelistFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 597836162298039219L;

  private Codelist codelist;

  public CodelistFact(Codelist codelist) {
    this.codelist = codelist;
  }

  public String getParentId() {
    return codelist.getParentId();
  }

  public List<String> getCodes() {
    return codelist.getCodes();
  }

  public boolean isTailored() {
    return codelist.getParentId() != null;
  }

  public List<Column> getColumnDefinitions() {
    return codelist.getColumnDefinitions();
  }

  public String getFilename() {
    return codelist.getFilename();
  }

  public List<String> getDuplicateCodes() {
    Set<String> set = new HashSet<String>();
    return getCodes().stream().filter(c -> !set.add(c)).collect(Collectors.toList());
  }

  public String getExpectedFilename() {
    String basename;
    if (getParentId() == null) {
      basename = getId();
    } else {
      basename = getParentId() + "_" + getId();
    }
    return basename + ".gc";
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
