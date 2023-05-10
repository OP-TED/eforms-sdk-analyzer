package eu.europa.ted.eforms.sdk.domain;

import java.util.Map;

public class Translation {
  private String comment;
  private Map<Label, String> labels;
  private Language language;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Map<Label, String> getLabels() {
    return labels;
  }

  public void setLabels(Map<Label, String> labels) {
    this.labels = labels;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }
}
