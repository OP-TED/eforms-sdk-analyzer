package eu.europa.ted.eforms.sdk.domain;

import java.util.Set;
import lombok.Data;

@Data
public class Codelist {
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
