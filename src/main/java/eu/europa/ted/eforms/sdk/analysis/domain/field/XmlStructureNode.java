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
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getParentId() {
    return this.parentId;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  public XmlStructureNode getParent() {
    return this.parent;
  }

  public void setParent(final XmlStructureNode parent) {
    this.parent = parent;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getXpathAbsolute() {
    return this.xpathAbsolute;
  }

  public void setXpathAbsolute(final String xpathAbsolute) {
    this.xpathAbsolute = xpathAbsolute;
  }

  public String getXpathRelative() {
    return this.xpathRelative;
  }

  public void setXpathRelative(final String xpathRelative) {
    this.xpathRelative = xpathRelative;
  }

  public List<XmlElementPosition> getXsdSequenceOrder() {
    return this.xsdSequenceOrder;
  }

  public void setXsdSequenceOrder(final List<XmlElementPosition> xsdSequenceOrder) {
    this.xsdSequenceOrder = xsdSequenceOrder;
  }

  public boolean isRepeatable() {
    return this.repeatable;
  }

  public void setRepeatable(final boolean repeatable) {
    this.repeatable = repeatable;
  }

  public String getIdentifierFieldId() {
    return this.identifierFieldId;
  }

  public void setIdentifierFieldId(final String identifierFieldId) {
    this.identifierFieldId = identifierFieldId;
  }

  public String getCaptionFieldId() {
    return this.captionFieldId;
  }

  public void setCaptionFieldId(final String captionFieldId) {
    this.captionFieldId = captionFieldId;
  }

  public String getBusinessEntityId() {
    return this.businessEntityId;
  }

  public void setBusinessEntityId(final String businessEntityId) {
    this.businessEntityId = businessEntityId;
  }

  public ChangeableOrCpProperty getInChangeNotice() {
    return this.inChangeNotice;
  }

  public void setInChangeNotice(final ChangeableOrCpProperty inChangeNotice) {
    this.inChangeNotice = inChangeNotice;
  }

  public ChangeableOrCpProperty getInContinueProcedure() {
    return this.inContinueProcedure;
  }

  public void setInContinueProcedure(final ChangeableOrCpProperty inContinueProcedure) {
    this.inContinueProcedure = inContinueProcedure;
  }
}
