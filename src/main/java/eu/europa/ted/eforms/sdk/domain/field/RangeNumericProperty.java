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
}
