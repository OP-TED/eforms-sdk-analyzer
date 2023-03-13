package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import eu.europa.ted.eforms.sdk.domain.noticetype.DocumentType;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeSubTypeForIndex;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypesForIndex;
import lombok.Data;

@Data
public class NoticeTypesIndexFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 780561935375885850L;

  private NoticeTypesForIndex noticeTypesForIndex;

  public NoticeTypesIndexFact(NoticeTypesForIndex noticeTypesForIndex) {
    this.noticeTypesForIndex = noticeTypesForIndex;
  }

  public String getSdkVersion() {
    return noticeTypesForIndex.getSdkVersion();
  }

  public Set<String> getLabelIds() {
    return noticeTypesForIndex.getNoticeSubTypes().stream()
        .map(NoticeSubTypeForIndex::getLabelId)
        .collect(Collectors.toSet());
  }

  public Set<String> getViewTemplateIds() {
    return noticeTypesForIndex.getNoticeSubTypes().stream()
        .flatMap(
            (NoticeSubTypeForIndex noticeSubType) -> noticeSubType.getViewTemplateIds().stream())
        .collect(Collectors.toSet());
  }

  public List<NoticeSubTypeForIndex> getNoticeSubTypes() {
    return noticeTypesForIndex.getNoticeSubTypes();
  }

  public List<DocumentType> getDocumentTypes() {
    return noticeTypesForIndex.getDocumentTypes();
  }

  @Override
  public String getId() {
    return StringUtils.EMPTY;
  }

  @Override
  public String getTypeName() {
    return "noticeTypesIndex";
  }
}
