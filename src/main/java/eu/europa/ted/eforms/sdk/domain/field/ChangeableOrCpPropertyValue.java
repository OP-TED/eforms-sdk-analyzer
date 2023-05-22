package eu.europa.ted.eforms.sdk.domain.field;

import java.io.Serializable;

/**
 * Triple boolean. TEDEFO-1090 Change Notice related. Special object because the value is not just a
 * string.
 */
public class ChangeableOrCpPropertyValue implements Serializable {
  private static final long serialVersionUID = 2643730327470961465L;

  private Boolean canAdd;
  private Boolean canModify;
  private Boolean canRemove;

  public Boolean getCanAdd() {
    return canAdd;
  }

  public Boolean getCanModify() {
    return canModify;
  }

  public Boolean getCanRemove() {
    return canRemove;
  }
}
