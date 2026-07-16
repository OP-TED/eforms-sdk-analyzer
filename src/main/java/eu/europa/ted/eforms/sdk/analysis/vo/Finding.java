package eu.europa.ted.eforms.sdk.analysis.vo;

/**
 * A {@link ValidationResult} paired with the {@code kind} that produced it. The kind is a drools rule
 * name for rule-engine findings (stamped from {@code currentRule} by {@code SdkUnit}'s result sink) or
 * the validator's class name for the external-engine validators (XSD, Schematron, EFX) and for a
 * validator-crash finding. It is captured at collection time, so findings can later be grouped by kind
 * without re-deriving it from the message text.
 */
public final class Finding {
  private final String kind;
  private final String problem;
  private final ValidationResult result;

  public Finding(final String kind, final ValidationResult result) {
    this(kind, null, result);
  }

  public Finding(final String kind, final String problem, final ValidationResult result) {
    this.kind = kind;
    this.problem = problem;
    this.result = result;
  }

  /** Stable key: the drools rule name, or the validator's class name for external-engine and crash findings. */
  public String getKind() {
    return this.kind;
  }

  /**
   * The human problem statement: a drools rule's {@code @problem}, the EFX/Schematron failure category,
   * or the validator-crash statement; {@code null} when none was set.
   */
  public String getProblem() {
    return this.problem;
  }

  /** Display label: the problem statement when present, else the kind. */
  public String getProblemOrKind() {
    return this.problem != null ? this.problem : this.kind;
  }

  public ValidationResult getResult() {
    return this.result;
  }

  @Override
  public String toString() {
    return "[" + getProblemOrKind() + "] " + this.result;
  }
}
