package eu.europa.ted.eforms.sdk.domain.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * TEDEFO-546: An abstract constraint class to be extended by classes implementing constraint items.
 */
@Data
@JsonPropertyOrder({"noticeTypes", "condition", "value", "severity", "message"})
public abstract class AbstractConstraint<V extends Serializable> implements Serializable {
  private static final long serialVersionUID = -3977225324375780808L;

  private List<String> noticeTypes = new ArrayList<>();

  private String condition;

  private V value;

  private PropertyOrConstraintSeverity severity;

  private String message;

  public List<String> getNoticeTypes() {
    return noticeTypes;
  }

  public void setNoticeTypes(List<String> noticeTypes) {
    this.noticeTypes = noticeTypes;
  }

  public String getCondition() {
    return condition;
  }
}
