package eu.europa.ted.eforms.sdk.domain.field;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

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
@Data
@JsonPropertyOrder({"id", "parentNodeId", "name", "btId", "xpathAbsolute", "xpathRelative", "type",
    "presetValue", "idSchemes", "idScheme", "schemeName", "legalType", "maxLength",
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
  private String presetValue;

  private List<String> idSchemes;
  private String idScheme;
  private String schemeName;

  private String legalType;
  private Integer maxLength;

  /**
   * Related to TEDEFO-219.
   */
  private FieldPrivacy privacy;

  private BooleanProperty repeatable;
  private BooleanProperty forbidden;
  private BooleanProperty mandatory; // Since TEDEFO-433.

  private StringProperty pattern;
  private RangeNumericProperty rangeNumeric;
  private CodeListProperty codeList;

  /**
   * Since TEDEFO-1090. TEDEFO-1111. Can be null.
   */
  private ChangeableOrCpProperty inChangeNotice;

  /**
   * Since TEDEFO-1090. TEDEFO-1111. Can be null.
   */
  private ChangeableOrCpProperty inContinueProcedure;

  /**
   * Since TEDEFO-1180.
   */
  @JsonProperty("assert")
  private StringProperty assertion;

  public String getParentNodeId() {
    return parentNodeId;
  }

  public void setParentNode(XmlStructureNode parentNode) {
    this.parentNode = parentNode;
  }

  public XmlStructureNode getParentNode() {
    return parentNode;
  }

  public String getId() {
    return id;
  }

  public BooleanProperty getForbidden() {
    return forbidden;
  }

  public String getXpathAbsolute() {
    return xpathAbsolute;
  }
}
