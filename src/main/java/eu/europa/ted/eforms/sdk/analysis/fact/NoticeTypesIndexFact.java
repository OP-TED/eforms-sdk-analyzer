package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.Set;
import java.util.stream.Collectors;
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

  @Override
  public String getId() {
    return "noticeTypesIndex";
  }

  @Override
  public String getTypeName() {
    return "noticeTypesIndex";
  }
}
