package eu.europa.ted.eforms.sdk.analysis.domain;

import java.util.Map;
import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;

/**
 * Represents the information in one file in the translations folder:
 * A set of labels, all in the same language.
 */
public class Translation {
  private String comment;
  private Map<String, Label> labels;
  private Language language;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Map<String, Label> getLabels() {
    return labels;
  }

  public void setLabels(Map<String, Label> labels) {
    this.labels = labels;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }
}
