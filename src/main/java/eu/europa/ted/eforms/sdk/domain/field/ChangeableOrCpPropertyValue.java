package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;

/**
 * Triple boolean. TEDEFO-1090 Change Notice related. Special object because the value is not just a
 * string.
 */
@Data
public class ChangeableOrCpPropertyValue {
  private Boolean canAdd;
  private Boolean canModify;
  private Boolean canRemove;
}
