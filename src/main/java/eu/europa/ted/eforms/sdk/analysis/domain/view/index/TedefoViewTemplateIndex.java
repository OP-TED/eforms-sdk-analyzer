package eu.europa.ted.eforms.sdk.analysis.domain.view.index;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TedefoViewTemplateIndex implements Serializable {
  private static final long serialVersionUID = -4884796492682953244L;

  private String id;
  private String filename;
  private String description;

  @JsonProperty("_label")
  private String labelId;

  @JsonProperty("_noticeSubtypeIds")
  private List<String> noticeSubtypeIds;

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getFilename() {
    return this.filename;
  }

  public void setFilename(final String filename) {
    this.filename = filename;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getLabelId() {
    return this.labelId;
  }

  public void setLabelId(final String labelId) {
    this.labelId = labelId;
  }

  public List<String> getNoticeSubtypeIds() {
    return this.noticeSubtypeIds;
  }

  public void setNoticeSubtypeIds(final List<String> noticeSubtypeIds) {
    this.noticeSubtypeIds = noticeSubtypeIds;
  }
}
