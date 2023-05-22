package eu.europa.ted.eforms.sdk.domain.field;

import java.io.Serializable;
import eu.europa.ted.eforms.sdk.domain.mdd.enums.CodeListType;

/**
 * TEDEFO-546: Implements a codelist constraint value.
 */
public class CodeListPropertyValue implements Serializable {
  private static final long serialVersionUID = -4115119604626820302L;

  /**
   * The code list id.
   */
  private String id;

  /**
   * Use the generated type enum.
   */
  private CodeListType type;

  /**
   * IMPORTANT: this is not always the parent of the tailored codelist.
   */
  private String parentId;

  public String getId() {
    return id;
  }

  public CodeListType getType() {
    return type;
  }

  public String getParentId() {
    return parentId;
  }
}
