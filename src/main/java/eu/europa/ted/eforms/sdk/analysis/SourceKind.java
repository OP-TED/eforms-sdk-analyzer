package eu.europa.ted.eforms.sdk.analysis;

/**
 * Whether file-format concerns apply to a given {@link SdkContentSource}.
 *
 * <p>A {@code FILE} source has filenames, file formats and a directory layout
 * that rules can validate. A {@code DATABASE} source has tables and entities;
 * "expected filename" or "format parity" are meaningless for it. Any future
 * {@code SdkContentSource} implementation lands in one of these two buckets
 * according to its nature; the enum is intentionally closed because adding a
 * new value implies framework-level implications and should require a
 * deliberate code change.
 */
public enum SourceKind {
  FILE,
  DATABASE
}
