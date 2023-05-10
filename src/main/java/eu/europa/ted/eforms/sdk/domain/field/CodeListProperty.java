package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TEDEFO-546: Codelist property of a field.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeListProperty
    extends AbstractFieldProperty<CodeListConstraint, CodeListPropertyValue> {
  private static final long serialVersionUID = 7500997293090091589L;
}
