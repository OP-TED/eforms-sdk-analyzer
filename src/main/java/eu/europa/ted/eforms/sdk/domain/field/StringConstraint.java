package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-546: Implements a codelist constraint item.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StringConstraint extends AbstractConstraint<String> {
}
