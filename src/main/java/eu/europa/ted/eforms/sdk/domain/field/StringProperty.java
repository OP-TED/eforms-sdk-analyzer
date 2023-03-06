package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-546: Implements a constraint string value. Reusable in the constraint value type
 * definitions.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StringProperty extends AbstractFieldProperty<StringConstraint, String>{
  private static final long serialVersionUID = 3758614977387886822L;
}
