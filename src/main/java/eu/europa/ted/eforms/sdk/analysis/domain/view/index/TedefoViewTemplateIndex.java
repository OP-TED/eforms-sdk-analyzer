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
    return id;
  }

  public String getFilename() {
    return filename;
  }

  public String getDescription() {
    return description;
  }

  public String getLabelId() {
    return labelId;
  }

  public List<String> getNoticeSubtypeIds() {
    return noticeSubtypeIds;
  }
}
