package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-546: range / interval property of a field.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RangeNumericProperty
    extends AbstractFieldProperty<RangeNumericConstraint, RangeNumericPropertyValue> {
  private static final long serialVersionUID = 4028295904390541920L;
}
