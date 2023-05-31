package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypeContent;
import eu.europa.ted.eforms.sdk.domain.noticetype.enums.NoticeTypeContentType;

public class NoticeTypeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 8298379634322672452L;

  private NoticeType noticeType;

  public NoticeTypeFact(NoticeType noticeType) {
    this.noticeType = noticeType;
  }

  public String getSdkVersion() {
    return noticeType.getSdkVersion();
  }

  public Set<String> getMetadataFieldsIds() {
    return noticeType.getMetadata().stream()
        .filter((NoticeTypeContent content) -> NoticeTypeContentType.FIELD == content
            .getContentTypeEnum())
        .map(NoticeTypeContent::getId)
        .collect(Collectors.toSet());
  }

  public Set<String> getContentFieldIds() {
    return noticeType.getContent()
        .stream()
        .flatMap(NoticeTypeContent::flattened)
        .filter((NoticeTypeContent content) -> NoticeTypeContentType.FIELD == content
            .getContentTypeEnum())
        .map(NoticeTypeContent::getId)
        .collect(Collectors.toSet());
  }

  public Set<String> getFieldIds() {
    return new HashSet<>(CollectionUtils.union(getMetadataFieldsIds(), getContentFieldIds()));
  }

  public Set<String> getNodeIds() {
    return noticeType.getContent()
        .stream()
        .flatMap(NoticeTypeContent::flattened)
        .filter((NoticeTypeContent content) -> NoticeTypeContentType.GROUP == content
            .getContentTypeEnum())
        .map(NoticeTypeContent::getNodeId)
        .collect(Collectors.toSet());
  }

  public Set<String> getLabelIds() {
    return noticeType.getContent()
        .stream()
        .flatMap(NoticeTypeContent::flattened)
        .map(NoticeTypeContent::getLabel)
        .collect(Collectors.toSet());
  }

  private Set<NoticeTypeContent> findContentByType(NoticeTypeContentType type) {
    if (type == null) {
      return Collections.emptySet();
    }
    return noticeType.getContent()
        .stream()
        .flatMap(NoticeTypeContent::flattened)
        .filter((NoticeTypeContent content) -> type == content.getContentTypeEnum())
        .collect(Collectors.toSet());
  }

  public Set<NoticeTypeContent> getGroups() {
    return findContentByType(NoticeTypeContentType.GROUP);
  }

  public Set<NoticeTypeContent> getFields() {
    return findContentByType(NoticeTypeContentType.FIELD);
  }

  @Override
  public String getId() {
    return noticeType.getNoticeId();
  }

  @Override
  public String getTypeName() {
    return "noticeType";
  }
}
