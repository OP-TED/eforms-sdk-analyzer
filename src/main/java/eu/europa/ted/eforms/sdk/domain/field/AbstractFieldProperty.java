package eu.europa.ted.eforms.sdk.domain.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * TEDEFO-546: An abstract field property.
 *
 * @param <C> Type of the constraint, it uses V inside as the constraint has a value of the same
 *        type as the default value.
 * @param <V> Type of the default value, it is the same as the one used inside the constraint.
 */
@Data
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
}
