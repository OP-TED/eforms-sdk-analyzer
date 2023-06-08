package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import java.util.List;

/**
 * While this is called Xml... it is used to create a json object inside the fields.json. TEDEFO-552
 * xml structure node for addition in the SDK fields.json file. This represents something like a
 * field section or group of sections. See TEDEFO-555 for DB representation.
 * <p>
 * NOTE: Thiemo of enotices2 initially called this a group, but we renamed it to node.
 * </p>
 */
public class XmlStructureNode implements Serializable {
  private static final long serialVersionUID = 8566153444257534770L;

  private String id;

  /**
   * Is null if there is no parent.
   */
  private String parentId;

  private XmlStructureNode parent;

  /**
   * Human readable name.
   */
  private String name;
  /**
   * 
   * Looks like field context. See TEDEFO-555 for details.
   */
  private String xpathAbsolute;
  private String xpathRelative;

  private List<XmlElementPosition> xsdSequenceOrder;

  private boolean repeatable;

  /**
   * This was initially inferred from the parent/child relationship of node and field. A repeatable
   * parent having a field of type id, the parent indicates which field holds the identifier.
   */
  private String identifierFieldId;

  private String captionFieldId;

  /**
   * Since TEDEFO-1090. TEDEFO-1111. Can be null.
   */
  private ChangeableOrCpProperty inChangeNotice;

  /**
   * Since TEDEFO-1090. TEDEFO-1111. Can be null.
   */
  private ChangeableOrCpProperty inContinueProcedure;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public XmlStructureNode getParent() {
    return parent;
  }

  public void setParent(XmlStructureNode parent) {
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public String getXpathAbsolute() {
    return xpathAbsolute;
  }

  public String getXpathRelative() {
    return xpathRelative;
  }

  public List<XmlElementPosition> getXsdSequenceOrder() {
    return xsdSequenceOrder;
  }

  public boolean isRepeatable() {
    return repeatable;
  }

  public void setRepeatable(boolean repeatable) {
    this.repeatable = repeatable;
  }

  public String getIdentifierFieldId() {
    return identifierFieldId;
  }

  public String getCaptionFieldId() {
    return captionFieldId;
  }

  public ChangeableOrCpProperty getInChangeNotice() {
    return inChangeNotice;
  }

  public ChangeableOrCpProperty getInContinueProcedure() {
    return inContinueProcedure;
  }
}
