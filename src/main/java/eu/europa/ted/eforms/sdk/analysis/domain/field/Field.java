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
    "privacy", "repeatable", "forbidden", "mandatory", "pattern", "rangeNumeric", "codeList",
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
  private RangeNumericProperty rangeNumeric;
  private CodeListProperty codeList;

  private ChangeableOrCpProperty inChangeNotice;
  private ChangeableOrCpProperty inContinueProcedure;

  @JsonProperty("assert")
  private StringProperty assertion;

  public String getId() {
    return id;
  }

  public String getParentNodeId() {
    return parentNodeId;
  }

  public XmlStructureNode getParentNode() {
    return parentNode;
  }

  public void setParentNode(XmlStructureNode parentNode) {
    this.parentNode = parentNode;
  }

  public String getName() {
    return name;
  }

  public String getBtId() {
    return btId;
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

  public String getType() {
    return type;
  }

  public String getAttributeOf() {
    return attributeOf;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public List<String> getAttributes() {
    return attributes;
  }

  public String getPresetValue() {
    return presetValue;
  }

  public String getBusinessEntityId() {
    return businessEntityId;
  }

  public List<String> getIdSchemes() {
    return idSchemes;
  }

  public List<String> getReferencedBusinessEntityIds() {
    return referencedBusinessEntityIds;
  }

  public String getIdScheme() {
    return idScheme;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public String getLegalType() {
    return legalType;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public FieldPrivacy getPrivacy() {
    return privacy;
  }

  public BooleanProperty getRepeatable() {
    return repeatable;
  }

  public BooleanProperty getForbidden() {
    return forbidden;
  }

  public BooleanProperty getMandatory() {
    return mandatory;
  }

  public StringProperty getPattern() {
    return pattern;
  }

  public RangeNumericProperty getRangeNumeric() {
    return rangeNumeric;
  }

  public CodeListProperty getCodeList() {
    return codeList;
  }

  public ChangeableOrCpProperty getInChangeNotice() {
    return inChangeNotice;
  }

  public ChangeableOrCpProperty getInContinueProcedure() {
    return inContinueProcedure;
  }

  public StringProperty getAssertion() {
    return assertion;
  }
}
