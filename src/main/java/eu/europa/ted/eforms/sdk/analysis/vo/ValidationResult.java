package eu.europa.ted.eforms.sdk.analysis.vo;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import eu.europa.ted.eforms.sdk.analysis.Identifiable;
import eu.europa.ted.eforms.sdk.analysis.enums.MissingLabelKind;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.SdkComponentFact;

public class ValidationResult {
  private final SdkComponentFact<?> fact;
  private final String message;
  private final ValidationStatusEnum status;
  private final Set<String> missingLabelIds;
  private final MissingLabelKind missingLabelKind;

  public ValidationResult(SdkComponentFact<?> fact, String message, ValidationStatusEnum status) {
    this(fact, message, status, Collections.emptySet(), MissingLabelKind.NONE);
  }

  public ValidationResult(SdkComponentFact<?> fact, String message, ValidationStatusEnum status,
      Collection<String> missingLabelIds) {
    this(fact, message, status, missingLabelIds, MissingLabelKind.FOUND);
  }

  public ValidationResult(SdkComponentFact<?> fact, String message, ValidationStatusEnum status,
      Collection<String> missingLabelIds, MissingLabelKind missingLabelKind) {
    this.fact = fact;
    this.message = message;
    this.status = status;
    this.missingLabelIds = Collections.unmodifiableSet(new LinkedHashSet<>(missingLabelIds));
    this.missingLabelKind = missingLabelKind;
  }

  public Identifiable<?> getFact() {
    return fact;
  }

  public String getMessage() {
    return message;
  }

  public ValidationStatusEnum getStatus() {
    return status;
  }

  /**
   * The label identifiers that this result reports as missing (referenced but without text). Empty
   * for results unrelated to missing labels.
   */
  public Set<String> getMissingLabelIds() {
    return missingLabelIds;
  }

  /**
   * How the missing labels were detected: {@link MissingLabelKind#FOUND} when referenced by the SDK
   * but not present, {@link MissingLabelKind#ASSUMED} when the exporter left the identifier inside
   * another label's text, or {@link MissingLabelKind#NONE} when this result is not about missing
   * labels.
   */
  public MissingLabelKind getMissingLabelKind() {
    return missingLabelKind;
  }

  @Override
  public String toString() {
    return MessageFormat.format("{0}({1}) - {2}: {3}", fact.getTypeName(), fact.getId(), status,
        message);
  }
}
