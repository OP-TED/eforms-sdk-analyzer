package eu.europa.ted.eforms.sdk.domain;

import java.util.Map;
import lombok.Data;

@Data
public class Translation {
  private String comment;
  private Map<Label, String> labels;
  private Language language;

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setLabels(Map<Label, String> labels) {
    this.labels = labels;
  }

  public Map<Label, String> getLabels() {
    return labels;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }
}
