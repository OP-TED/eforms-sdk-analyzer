package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-1090: Change Notice related. This exists only because the value is not just a string.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeableOrCpProperty
    extends AbstractFieldProperty<ChangeableOrCpConstraint, ChangeableOrCpPropertyValue> {
}
