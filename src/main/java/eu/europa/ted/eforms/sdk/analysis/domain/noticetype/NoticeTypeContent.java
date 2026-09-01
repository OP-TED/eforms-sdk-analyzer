package eu.europa.ted.eforms.sdk.analysis.domain.noticetype;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    return this.contentType;
  }

  public NoticeTypeContentType getContentType() {
    return this.contentType;
  }

  public void setContentType(final NoticeTypeContentType contentType) {
    this.contentType = contentType;
  }

  public NoticeTypeContentDisplayType getDisplayType() {
    return this.displayType;
  }

  public void setDisplayType(final NoticeTypeContentDisplayType displayType) {
    this.displayType = displayType;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public Stream<NoticeTypeContent> flattened() {
    return Stream.concat(
        Stream.of(this),
        content.stream()
            .flatMap(NoticeTypeContent::flattened));
  }

  public List<NoticeTypeContent> getContent() {
    return this.content;
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

    // visited guards against a cycle in the content hierarchy of a malformed notice type, which
    // would otherwise spin forever walking the parent chain.
    final Set<NoticeTypeContent> visited = new HashSet<>();
    while (currentContent != null && visited.add(currentContent)) {
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
    return this.nodeId;
  }

  public void setNodeId(final String nodeId) {
    this.nodeId = nodeId;
  }

  public boolean isRepeatable() {
    return this.repeatable;
  }

  public void setRepeatable(final boolean repeatable) {
    this.repeatable = repeatable;
  }

  public String getValueSource() {
    return this.valueSource;
  }

  public void setValueSource(final String valueSource) {
    this.valueSource = valueSource;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(final String label) {
    this.label = label;
  }

  public boolean isCollapsed() {
    return this.collapsed;
  }

  public void setCollapsed(final boolean collapsed) {
    this.collapsed = collapsed;
  }

  public boolean isHidden() {
    return this.hidden;
  }

  public void setHidden(final boolean hidden) {
    this.hidden = hidden;
  }

  public String getPresetValue() {
    return this.presetValue;
  }

  public void setPresetValue(final String presetValue) {
    this.presetValue = presetValue;
  }

  public boolean isReadOnly() {
    return this.readOnly;
  }

  public void setReadOnly(final boolean readOnly) {
    this.readOnly = readOnly;
  }

  public NoticeTypeContent getParent() {
    return this.parent;
  }

  public NoticeTypeContent setParent(final NoticeTypeContent parent) {
    this.parent = parent;
    return this;
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

  public String getBusinessEntityId() {
    return this.businessEntityId;
  }

  public void setBusinessEntityId(final String businessEntityId) {
    this.businessEntityId = businessEntityId;
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

  public List<String> getIdSchemes() {
    return this.idSchemes;
  }
}
