package eu.europa.ted.eforms.sdk.analysis.domain;

import java.io.Serializable;

import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;

public class Label implements Serializable {
  private static final long serialVersionUID = -24724237531309137L;

  private final String id;
  private final Language language;
  private final String text;


  public Label(String id, Language language, String text) {
    this.id = id;
    this.language = language;
    this.text = text;
  }


  public String getId() {
    return id;
  }


  public Language getLanguage() {
    return language;
  }


  public String getText() {
    return text;
  }
}
