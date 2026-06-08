package eu.europa.ted.eforms.sdk.analysis.enums;

/**
 * How a missing label was detected.
 */
public enum MissingLabelKind {
  /** The result does not concern a missing label. */
  NONE,
  /** A label is referenced by the SDK but is not present in the export. */
  FOUND,
  /**
   * A label identifier was left inside another label's text by the exporter, which happens when the
   * label could not be resolved during export.
   */
  ASSUMED;
}
