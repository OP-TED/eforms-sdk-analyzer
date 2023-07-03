package eu.europa.ted.eforms.sdk.analysis.domain.noticetype;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.enums.NoticeTypeContentDisplayType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.enums.NoticeTypeContentType;

/**
 * Domain object to represent items of the SDK notice type for JSON in Java. This is not for the top
 * level item.
 *
 * <p style="color: red;">
 * IMPORTANT: be careful when adding getters as those will end up in the JSON !!!
 * </p>
 */
@JsonInclude(Include.NON_DEFAULT) // Avoids having xyz: false
@JsonPropertyOrder({"id", "contentType", "nodeId", "displayType", "description", "_label",
    "valueSource", "_idScheme", "_idSchemes", "_schemeName", "_identifierFieldId", "readOnly",
    "_repeatable", "_presetValue", "hidden", "collapsed",
    "unpublishGroupId", "unpublishFieldId", "unpublishCode",
    "content"})
public class NoticeTypeContent {
  /**
   * Unique identifier.
   */
  private String id;

  /**
   * Can be null if not a node.
   */
  private String nodeId;

  /**
   * Type of the tree item.
   */
  private NoticeTypeContentType contentType;

  /**
   * For the UI design and form inputs.
   */
  private NoticeTypeContentDisplayType displayType;

  /**
   * This is just for humans reading the JSON, the UI should use the label.
   */
  private String description;

  /**
   * For i18n. Translation id. The label id.
   */
  @JsonProperty("_label")
  private String label;

  /**
   * For the UI logic: a section is not pre opened if this is true.
   */
  private boolean collapsed;

  /**
   * For the UI logic. For hidden form fields like a hidden HTML input.
   */
  private boolean hidden;

  @JsonProperty("_presetValue")
  private String presetValue;

  private String unpublishGroupId;
  private String unpublishFieldId;
  private String unpublishCode;

  /**
   * For the UI logic. Read only form inputs.
   */
  private boolean readOnly;

  /**
   * Starts with underscore because it could be determined from node Id or field Id. We provide it
   * for convenience only.
   */
  @JsonProperty("_repeatable")
  private boolean repeatable;

  /**
   * This can point to the id of another field like "BT-...", that means: copy the value of that
   * field into this one. Which also means that the other field should already have been loaded.
   * <p>
   * See SDK docs for details. For example: "BT-137-LotsGroup"
   * </p>
   */
  private String valueSource;

  /**
   * List of child content (same type), it can be an empty list if there are no children. This is
   * where recursion happens.
   */
  private List<NoticeTypeContent> content = new ArrayList<>();

  private NoticeTypeContent parent;

  /**
   * See TEDEFO-1047. We provide this for convenience. Equivalent to TEDEN2 instanceList or
   * instanceFieldId.
   */
  @JsonProperty("_idScheme")
  private String idScheme;

  /**
   * See TEDEFO-1047. We provide this for convenience. Equivalent to TEDEN2 valueList.
   */
  @JsonProperty("_idSchemes")
  private final List<String> idSchemes = new ArrayList<>();

  @JsonProperty("_schemeName")
  private String schemeName;

  /**
   * For convenience. Set later by child.
   */
  @JsonProperty("_identifierFieldId")
  private String identifierFieldId;

  @JsonProperty("_captionFieldId")
  private String captionFieldId;

  public NoticeTypeContentType getContentTypeEnum() {
    return contentType;
  }

  public NoticeTypeContentType getContentType() {
    return contentType;
  }

  public NoticeTypeContentDisplayType getDisplayType() {
    return displayType;
  }

  public String getDescription() {
    return description;
  }

  public Stream<NoticeTypeContent> flattened() {
    return Stream.concat(
        Stream.of(this),
        content.stream()
            .flatMap(NoticeTypeContent::flattened));
  }

  public void setContent(List<NoticeTypeContent> content) {
    if (content != null) {
      content.forEach((NoticeTypeContent c) -> c.setParent(this));
    }

    this.content = content;
  }

  public NoticeTypeContent getFirstRepeatableAncestorGroup() {
    return getFirstRepeatableAncestor(NoticeTypeContentType.GROUP);
  }

  public NoticeTypeContent getFirstRepeatableAncestor() {
    return getFirstRepeatableAncestor(null);
  }

  public NoticeTypeContent getFirstRepeatableAncestor(NoticeTypeContentType type) {
    NoticeTypeContent result = null;
    NoticeTypeContent currentContent = parent;

    while (currentContent != null) {
      if (currentContent.isRepeatable()
          && (type == null || currentContent.getContentType().equals(type))) {
        result = currentContent;
        // First repeatable ancestor found
        break;
      }

      currentContent = currentContent.getParent();
    }

    return result;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNodeId() {
    return nodeId;
  }

  public boolean isRepeatable() {
    return repeatable;
  }

  public void setRepeatable(boolean repeatable) {
    this.repeatable = repeatable;
  }

  public String getValueSource() {
    return valueSource;
  }

  public String getLabel() {
    return label;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public boolean isHidden() {
    return hidden;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public NoticeTypeContent getParent() {
    return parent;
  }

  public NoticeTypeContent setParent(NoticeTypeContent parent) {
    this.parent = parent;
    return this;
  }

  public String getUnpublishGroupId() {
    return unpublishGroupId;
  }

  public String getUnpublishFieldId() {
    return unpublishFieldId;
  }

  public String getUnpublishCode() {
    return unpublishCode;
  }
}