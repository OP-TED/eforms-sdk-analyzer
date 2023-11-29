package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeSubTypeForIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeTypesForIndex;

public class NoticeTypesIndexFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 780561935375885850L;

  private NoticeTypesForIndex noticeTypesForIndex;

  public NoticeTypesIndexFact(NoticeTypesForIndex noticeTypesForIndex) {
    this.noticeTypesForIndex = noticeTypesForIndex;
  }

  /**
   * Return the set of notice subtypes for which we expect to have a notice type definition.
   * This is hardcoded here because there is no other place that has this information.
   */
  public Set<String> getExpectedNoticeSubtypes() {
    return Stream.of("1", "2", "3", "4", "5", "6", "7", "8", "9",
          "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
          "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
          "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
          "40",
          "CEI", "T01", "T02", "X01", "X02")
        .collect(Collectors.toSet());
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
