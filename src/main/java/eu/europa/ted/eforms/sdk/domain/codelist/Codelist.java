package eu.europa.ted.eforms.sdk.domain.codelist;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import eu.europa.ted.eforms.sdk.domain.xml.ColumnSet.Column;

public class Codelist implements Serializable {
  private static final long serialVersionUID = 9090505617139835976L;

  private String id;
  private String parentId;

  private Set<String> codes;

  private List<Column> columnDefinitions;

  // Name of the file that contains the codelist.
  // This is rather metadata for the codelist, but its simpler to have it here.
  private String filename;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public Set<String> getCodes() {
    return codes;
  }

  public void setCodes(Set<String> codes) {
    this.codes = codes;
  }

  public List<Column> getColumnDefinitions() {
    return columnDefinitions;
  }
  
  public void setColumnDefinitions(List<Column> columnDefinitions) {
    this.columnDefinitions = columnDefinitions;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }
}
