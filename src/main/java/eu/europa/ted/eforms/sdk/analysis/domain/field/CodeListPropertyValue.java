package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import eu.europa.ted.eforms.sdk.analysis.domain.mdd.enums.CodeListType;

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

  /**
   * SDK 2: the {@code listName} written in the notice XML (replaces {@code parentId}, TEDEFO-1522).
   */
  private String listName;

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public CodeListType getType() {
    return this.type;
  }

  public void setType(final CodeListType type) {
    this.type = type;
  }

  public String getParentId() {
    return this.parentId;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  public String getListName() {
    return this.listName;
  }

  public void setListName(final String listName) {
    this.listName = listName;
  }
}
