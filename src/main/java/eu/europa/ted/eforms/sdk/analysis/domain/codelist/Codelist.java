package eu.europa.ted.eforms.sdk.analysis.domain.codelist;

import java.io.Serializable;
import java.util.List;

import eu.europa.ted.eforms.sdk.analysis.domain.xml.ColumnSet.Column;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.SimpleCodeList.Row;

public class Codelist implements Serializable {
  private static final long serialVersionUID = 9090505617139835976L;

  private String id;
  private String parentId;

  private List<String> codes;

  private List<Column> columnDefinitions;

  private List<Row> rows;

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

  public List<String> getCodes() {
    return codes;
  }

  public void setCodes(List<String> codes) {
    this.codes = codes;
  }

  public List<Column> getColumnDefinitions() {
    return columnDefinitions;
  }
  
  public void setColumnDefinitions(List<Column> columnDefinitions) {
    this.columnDefinitions = columnDefinitions;
  }

  public List<Row> getRows() {
    return rows;
  }

  public void setRows(List<Row> rows) {
    this.rows = rows;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }
}
