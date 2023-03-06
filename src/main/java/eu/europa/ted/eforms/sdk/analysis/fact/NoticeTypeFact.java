package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypeContent;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypeContentType;
import lombok.Data;

@Data
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

  public Set<String> getContentFieldsIds() {
    return noticeType.getContent()
        .stream()
        .flatMap(NoticeTypeContent::flattened)
        .filter((NoticeTypeContent content) -> NoticeTypeContentType.FIELD == content
            .getContentTypeEnum())
        .map(NoticeTypeContent::getId)
        .collect(Collectors.toSet());
  }

  public Set<String> getFieldsIds() {
    return new HashSet<>(CollectionUtils.union(getMetadataFieldsIds(), getContentFieldsIds()));
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
