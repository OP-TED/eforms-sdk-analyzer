package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A field used in the SDK fields.json generation.
 *
 * <p>
 * For JSON serialization the getter names matter, not the member or class names.
 * </p>
 *
 * <p>
 * For JSON serialization member order matters. You can force a given order using @JsonPropertyOrder
 * but we may not need that.
 * </p>
 */
@JsonPropertyOrder({"id", "parentNodeId", "name", "btId",
    "xpathAbsolute", "xpathRelative", "xsdSequenceOrder",
    "type",
    "attributeName", "attributeOf", "attributes",
    "presetValue", "businessEntityId", "idSchemes", "idScheme", "schemeName", "referencedBusinessEntityIds",
    "legalType", "maxLength", "description",
    "privacy", "repeatable", "forbidden", "mandatory", "pattern", "numericRange", "codeList",
    "inChangeNotice", "inContinueProcedure", "assert"})
public class Field implements Serializable {
  private static final long serialVersionUID = -1387933500392516298L;

  private String id;

  /**
   * It can be null if there is no parent.
   */
  private String parentNodeId;

  private XmlStructureNode parentNode;

  private String name;
  private String btId;
  private String xpathAbsolute;
  private String xpathRelative;

  private List<XmlElementPosition> xsdSequenceOrder;

  private String type;
  private String attributeOf;
  private String attributeName;
  private List<String> attributes;
  private String presetValue;

  private String businessEntityId;

  private List<String> idSchemes;
  private List<String> referencedBusinessEntityIds;

  private String idScheme;
  private String schemeName;

  private String legalType;
  private Integer maxLength;

  private FieldPrivacy privacy;

  private BooleanProperty repeatable;
  private BooleanProperty forbidden;
  private BooleanProperty mandatory;

  private StringProperty pattern;
  private RangeNumericProperty numericRange;
  private CodeListProperty codeList;

  private ChangeableOrCpProperty inChangeNotice;
  private ChangeableOrCpProperty inContinueProcedure;

  @JsonProperty("assert")
  private StringProperty assertion;

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getParentNodeId() {
    return this.parentNodeId;
  }

  public void setParentNodeId(final String parentNodeId) {
    this.parentNodeId = parentNodeId;
  }

  public XmlStructureNode getParentNode() {
    return this.parentNode;
  }

  public void setParentNode(final XmlStructureNode parentNode) {
    this.parentNode = parentNode;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getBtId() {
    return this.btId;
  }

  public void setBtId(final String btId) {
    this.btId = btId;
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

  public String getType() {
    return this.type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getAttributeOf() {
    return this.attributeOf;
  }

  public void setAttributeOf(final String attributeOf) {
    this.attributeOf = attributeOf;
  }

  public String getAttributeName() {
    return this.attributeName;
  }

  public void setAttributeName(final String attributeName) {
    this.attributeName = attributeName;
  }

  public List<String> getAttributes() {
    return this.attributes;
  }

  public void setAttributes(final List<String> attributes) {
    this.attributes = attributes;
  }

  public String getPresetValue() {
    return this.presetValue;
  }

  public void setPresetValue(final String presetValue) {
    this.presetValue = presetValue;
  }

  public String getBusinessEntityId() {
    return this.businessEntityId;
  }

  public void setBusinessEntityId(final String businessEntityId) {
    this.businessEntityId = businessEntityId;
  }

  public List<String> getIdSchemes() {
    return this.idSchemes;
  }

  public void setIdSchemes(final List<String> idSchemes) {
    this.idSchemes = idSchemes;
  }

  public List<String> getReferencedBusinessEntityIds() {
    return this.referencedBusinessEntityIds;
  }

  public void setReferencedBusinessEntityIds(final List<String> referencedBusinessEntityIds) {
    this.referencedBusinessEntityIds = referencedBusinessEntityIds;
  }

  public String getIdScheme() {
    return this.idScheme;
  }

  public void setIdScheme(final String idScheme) {
    this.idScheme = idScheme;
  }

  public String getSchemeName() {
    return this.schemeName;
  }

  public void setSchemeName(final String schemeName) {
    this.schemeName = schemeName;
  }

  public String getLegalType() {
    return this.legalType;
  }

  public void setLegalType(final String legalType) {
    this.legalType = legalType;
  }

  public Integer getMaxLength() {
    return this.maxLength;
  }

  public void setMaxLength(final Integer maxLength) {
    this.maxLength = maxLength;
  }

  public FieldPrivacy getPrivacy() {
    return this.privacy;
  }

  public void setPrivacy(final FieldPrivacy privacy) {
    this.privacy = privacy;
  }

  public BooleanProperty getRepeatable() {
    return this.repeatable;
  }

  public void setRepeatable(final BooleanProperty repeatable) {
    this.repeatable = repeatable;
  }

  public BooleanProperty getForbidden() {
    return this.forbidden;
  }

  public void setForbidden(final BooleanProperty forbidden) {
    this.forbidden = forbidden;
  }

  public BooleanProperty getMandatory() {
    return this.mandatory;
  }

  public void setMandatory(final BooleanProperty mandatory) {
    this.mandatory = mandatory;
  }

  public StringProperty getPattern() {
    return this.pattern;
  }

  public void setPattern(final StringProperty pattern) {
    this.pattern = pattern;
  }

  public RangeNumericProperty getNumericRange() {
    return this.numericRange;
  }

  public void setNumericRange(final RangeNumericProperty numericRange) {
    this.numericRange = numericRange;
  }

  public CodeListProperty getCodeList() {
    return this.codeList;
  }

  public void setCodeList(final CodeListProperty codeList) {
    this.codeList = codeList;
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

  public StringProperty getAssertion() {
    return this.assertion;
  }

  public void setAssertion(final StringProperty assertion) {
    this.assertion = assertion;
  }
}
