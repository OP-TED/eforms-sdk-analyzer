package eu.europa.ted.eforms.sdk.analysis.vo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The logical sections of the SDK, used to group findings in the report. They line up with the seven
 * drools rule files and the SDK content areas. A finding's section is derived from its subject asset's
 * type (the fact's {@code getTypeName()} / {@link AssetRef#getType()}) — this is the single source of
 * that mapping.
 */
public enum SdkSection {
  // Leads the report when present: a validator that failed to run means the analysis is incomplete,
  // so it must not be buried below the content findings. Absent on a clean run (no "validator" facts).
  ANALYZER("Analyzer", "validator"),
  FIELDS_AND_NODES("Fields & Nodes", "field", "node", "businessEntity", "fieldsAndNodesMetadata"),
  NOTICE_TYPES("Notice Types", "noticeType", "noticeTypesIndex", "documentType"),
  CODELISTS("Codelists", "codelist", "codelistIndex"),
  TRANSLATIONS("Translations", "label"),
  VIEW_TEMPLATES("View Templates", "viewTemplate", "viewTemplatesIndex"),
  SCHEMATRON("Schematron", "schematron"),
  EXAMPLES("Examples", "xmlNotice", "svrlReport"),
  OTHER("Other");

  private static final Map<String, SdkSection> BY_TYPE = Stream.of(values())
      .flatMap(section -> section.assetTypes.stream()
          .map(type -> Map.entry(type, section)))
      .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

  private final String title;
  private final Set<String> assetTypes;

  SdkSection(final String title, final String... assetTypes) {
    this.title = title;
    this.assetTypes = Set.of(assetTypes);
  }

  public String getTitle() {
    return this.title;
  }

  /** The section an asset type belongs to; {@link #OTHER} for unrecognised types. */
  public static SdkSection forType(final String assetType) {
    return BY_TYPE.getOrDefault(assetType, OTHER);
  }
}
