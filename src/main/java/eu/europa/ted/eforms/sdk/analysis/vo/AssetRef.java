package eu.europa.ted.eforms.sdk.analysis.vo;

import java.util.Objects;

/**
 * A typed reference to an SDK asset: its {@code type} and {@code id}. Recorded on a
 * {@link ValidationResult} to capture the other asset(s) a finding implicates (the target of a broken
 * reference, the field a constraint forbids, the label that is missing, …), so findings can be pivoted
 * by referenced asset as well as by subject.
 *
 * <p>The {@code type} vocabulary deliberately matches the {@code getTypeName()} of the producing fact
 * (e.g. {@code field}, {@code node}, {@code label}, {@code noticeType}, {@code businessEntity}), so a
 * reference and a subject of the same asset compare equal across the two ends of the pivot. The
 * factories below are the single place those strings live.
 */
public final class AssetRef implements Comparable<AssetRef> {
  private final String type;
  private final String id;

  public AssetRef(final String type, final String id) {
    this.type = type;
    this.id = id;
  }

  public static AssetRef field(final String id) {
    return new AssetRef("field", id);
  }

  public static AssetRef node(final String id) {
    return new AssetRef("node", id);
  }

  public static AssetRef label(final String id) {
    return new AssetRef("label", id);
  }

  public static AssetRef codelist(final String id) {
    return new AssetRef("codelist", id);
  }

  /** A single code (an entry within a codelist), identified by its value. */
  public static AssetRef code(final String id) {
    return new AssetRef("code", id);
  }

  /** A codelist column, identified by its column reference. */
  public static AssetRef column(final String id) {
    return new AssetRef("column", id);
  }

  public static AssetRef noticeType(final String id) {
    return new AssetRef("noticeType", id);
  }

  /**
   * A repeatable group inside a notice type, identified by its content id. Unlike the other types this
   * one has no fact of its own — a group is a content node within a {@code NoticeType} — so it appears
   * only as a reference (the broken group of a notice-type finding), never as a subject.
   */
  public static AssetRef noticeTypeGroup(final String id) {
    return new AssetRef("noticeTypeGroup", id);
  }

  /**
   * A pattern inside a schematron file, identified by its pattern id. Reference-only: a pattern is a
   * content element within a schematron file, so it appears as the implicated asset of a finding whose
   * subject is the schematron file, never as a subject of its own.
   */
  public static AssetRef schematronPattern(final String id) {
    return new AssetRef("schematronPattern", id);
  }

  /** An assert inside a schematron file, identified by its assert id. Reference-only. */
  public static AssetRef schematronAssert(final String id) {
    return new AssetRef("schematronAssert", id);
  }

  /** A diagnostic referenced by a schematron assert, identified by its id. Reference-only. */
  public static AssetRef schematronDiagnostic(final String id) {
    return new AssetRef("schematronDiagnostic", id);
  }

  public static AssetRef businessEntity(final String id) {
    return new AssetRef("businessEntity", id);
  }

  public static AssetRef viewTemplate(final String id) {
    return new AssetRef("viewTemplate", id);
  }

  public static AssetRef documentType(final String id) {
    return new AssetRef("documentType", id);
  }

  public static AssetRef svrlReport(final String id) {
    return new AssetRef("svrlReport", id);
  }

  public static AssetRef xmlNotice(final String id) {
    return new AssetRef("xmlNotice", id);
  }

  public String getType() {
    return this.type;
  }

  public String getId() {
    return this.id;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AssetRef)) {
      return false;
    }
    final AssetRef that = (AssetRef) other;
    return Objects.equals(this.type, that.type) && Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.type, this.id);
  }

  @Override
  public int compareTo(final AssetRef other) {
    final int byType = compareNullable(this.type, other.type);
    return byType != 0 ? byType : compareNullable(this.id, other.id);
  }

  /** Null-safe natural-order comparison (nulls first): a reference id can be absent (e.g. a node). */
  private static int compareNullable(final String left, final String right) {
    if (left == null || right == null) {
      return left == null ? (right == null ? 0 : -1) : 1;
    }
    return left.compareTo(right);
  }

  @Override
  public String toString() {
    return this.type + ":" + this.id;
  }
}
