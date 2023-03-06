package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-546: Codelist property of a field.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BooleanProperty extends AbstractFieldProperty<BooleanConstraint, Boolean> {
  private static final long serialVersionUID = 7201002588535577527L;
}
