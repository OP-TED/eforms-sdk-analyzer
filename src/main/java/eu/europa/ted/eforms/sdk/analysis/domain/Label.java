package eu.europa.ted.eforms.sdk.analysis.domain;

import java.io.Serializable;

public class Label implements Serializable {
  private static final long serialVersionUID = -24724237531309137L;

  private final String id;

  public Label(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
