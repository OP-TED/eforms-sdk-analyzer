package eu.europa.ted.eforms.sdk.domain;

import java.io.Serializable;
import java.util.Set;

public class Codelist implements Serializable {
  private static final long serialVersionUID = 9090505617139835976L;

  private String id;

  private Set<String> codes;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Set<String> getCodes() {
    return codes;
  }

  public void setCodes(Set<String> codes) {
    this.codes = codes;
  }
}
