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
 */
@JsonInclude(Include.NON_DEFAULT)
@JsonPropertyOrder({"id", "contentType", "nodeId", "displayType", "description", "_label",
    "valueSource",
    "_idScheme", "_idSchemes", "_schemeName", "businessEntityId",
    "_identifierFieldId", "_captionFieldId",
    "readOnly",
    "_repeatable", "_presetValue", "hidden", "collapsed",
    "unpublishGroupId", "unpublishFieldId", "unpublishCode",
    "content"})
public class NoticeTypeContent {
  private String id;

  private String nodeId;

  private NoticeTypeContentType contentType;

  private NoticeTypeContentDisplayType displayType;

  private String description;

  @JsonProperty("_label")
  private String label;

  private boolean collapsed;
  private boolean hidden;

  @JsonProperty("_presetValue")
  private String presetValue;

  private String unpublishGroupId;
  private String unpublishFieldId;
  private String unpublishCode;

  private boolean readOnly;

  @JsonProperty("_repeatable")
  private boolean repeatable;

  private String valueSource;

  private List<NoticeTypeContent> content = new ArrayList<>();

  private NoticeTypeContent parent;

  @JsonProperty("_idScheme")
  private String idScheme;

  @JsonProperty("_idSchemes")
  private final List<String> idSchemes = new ArrayList<>();

  @JsonProperty("_schemeName")
  private String schemeName;

  private String businessEntityId;

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

  public String getBusinessEntityId() {
    return businessEntityId;
  }
}
