package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-1090: Change Notice or continue procedure related. Necessary because the constraint value
 * is not just a string.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeableOrCpConstraint extends AbstractConstraint<ChangeableOrCpPropertyValue> {
}
