package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.ColumnSet.Column;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.SimpleCodeList.Row;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.SimpleCodeList.Row.Value;

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

  public List<Row> getRows() {
    return codelist.getRows();
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

  public Set<String> getInvalidColumnRefs() {
    Set<String> result = new HashSet<>();

    // All column identifiers in the column definitions
    Set<String> colIds = getColumnDefinitions().stream().map(Column::getId).collect(Collectors.toSet());

    getRows().forEach(r -> {
      // Find any column ref that is not in colIds 
      result.addAll(r.getValue().stream()
          .map(Value::getColumnRef)
          .filter(v -> !colIds.contains(v))
          .collect(Collectors.toSet())
      );
    });

    return result;
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
