package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import java.util.List;

public class XmlStructureNode implements Serializable {
  private static final long serialVersionUID = 8566153444257534770L;

  private String id;

  private String parentId;

  private XmlStructureNode parent;

  private String name;

  private String xpathAbsolute;
  private String xpathRelative;

  private List<XmlElementPosition> xsdSequenceOrder;

  private boolean repeatable;

  private String identifierFieldId;

  private String captionFieldId;

  private String businessEntityId;

  private ChangeableOrCpProperty inChangeNotice;
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

  public String getBusinessEntityId() {
    return businessEntityId;
  }

  public ChangeableOrCpProperty getInChangeNotice() {
    return inChangeNotice;
  }

  public ChangeableOrCpProperty getInContinueProcedure() {
    return inContinueProcedure;
  }
}
