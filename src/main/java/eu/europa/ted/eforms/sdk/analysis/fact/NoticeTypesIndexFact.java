package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
   * The standard notice subtypes for which a definition is always expected. Hardcoded because the
   * eForms standard set is not declared anywhere machine-readable. It is the baseline only; the full
   * set actually required is {@link #getRequiredNoticeSubtypes()}, which also covers whatever the
   * index lists.
   */
  public Set<String> getExpectedNoticeSubtypes() {
    return Stream.of("1", "2", "3", "4", "5", "6", "7", "8", "9",
          "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
          "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
          "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
          "40",
          "CEI", "T01", "T02", "X01", "X02",
          "E1", "E2", "E3", "E4", "E5", "E6")
        .collect(Collectors.toSet());
  }

  /**
   * Every notice subtype that must have a definition file: the standard set
   * ({@link #getExpectedNoticeSubtypes()}) together with every subtype the index actually lists. The
   * union catches both a standard subtype omitted from the index and a listed subtype whose file is
   * missing — and, being a set, never reports a subtype that is in both twice.
   */
  public Set<String> getRequiredNoticeSubtypes() {
    final Set<String> required = new HashSet<>(getExpectedNoticeSubtypes());
    noticeTypesForIndex.getNoticeSubTypes()
        .forEach(subType -> required.add(subType.getSubTypeId()));
    return required;
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
    return "notice-types/notice-types.json";
  }

  @Override
  public String getTypeName() {
    return "noticeTypesIndex";
  }
}
