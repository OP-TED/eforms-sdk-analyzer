package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.field.enums.PropertyOrConstraintSeverity;

/**
 * TEDEFO-546: An abstract field property.
 *
 * @param <C> Type of the constraint, it uses V inside as the constraint has a value of the same
 *        type as the default value.
 * @param <V> Type of the default value, it is the same as the one used inside the constraint.
 */
@JsonPropertyOrder({"value", "severity", "message", "constraints"})
public abstract class AbstractFieldProperty<C extends AbstractConstraint<V>, V extends Serializable>
    implements Serializable {
  private static final long serialVersionUID = 1342908486390503189L;

  /**
   * The generic default value, acts as fallback when no constraint value matches.
   */
  private V value;

  /**
   * Severity which is set next to a default value.
   */
  private PropertyOrConstraintSeverity severity;

  private String message;

  /**
   * The list can be empty but not null.
   */
  private final List<C> constraints = new ArrayList<>();

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

  public List<C> getConstraints() {
    return this.constraints;
  }

  /**
   * @return All notice type identifiers referenced in the constraints of this field property.
   */
  public Set<String> getAllNoticeTypeIds() {
    return getConstraints()
        .stream()
        .map(AbstractConstraint::getNoticeTypes)
        .flatMap(List::stream)
        .collect(Collectors.toSet());
  }

  public Set<String> getDuplicateNoticeTypeIds() {
    List<String> values = getConstraints()
        .stream()
        .map(AbstractConstraint::getNoticeTypes)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    
    return values.stream()
        .filter(i -> Collections.frequency(values, i) > 1)
        .collect(Collectors.toSet());
  }

  /**
   * @return All label identifiers referenced in the constraints of this field property.
   */
  public Set<String> getAllConstraintsLabelIds() {
    return getConstraints()
        .stream()
        .map(AbstractConstraint::getMessage)
        .collect(Collectors.toSet());
  }
}
