package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.field.enums.PropertyOrConstraintSeverity;

/**
 * TEDEFO-546: An abstract constraint class to be extended by classes implementing constraint items.
 */
@JsonPropertyOrder({"noticeTypes", "condition", "value", "severity", "message"})
public abstract class AbstractConstraint<V extends Serializable> implements Serializable {
  private static final long serialVersionUID = -3977225324375780808L;

  private List<String> noticeTypes = new ArrayList<>();

  private String condition;

  private V value;

  private PropertyOrConstraintSeverity severity;

  private String message;

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public List<String> getNoticeTypes() {
    return this.noticeTypes;
  }

  public void setNoticeTypes(final List<String> noticeTypes) {
    this.noticeTypes = noticeTypes;
  }

  public String getCondition() {
    return this.condition;
  }

  public void setCondition(final String condition) {
    this.condition = condition;
  }

  public V getValue() {
    return this.value;
  }

  public void setValue(final V value) {
    this.value = value;
  }

  public PropertyOrConstraintSeverity getSeverity() {
    return this.severity;
  }

  public void setSeverity(final PropertyOrConstraintSeverity severity) {
    this.severity = severity;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
