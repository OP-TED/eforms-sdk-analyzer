package eu.europa.ted.eforms.sdk.analysis;

import java.text.MessageFormat;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.SdkComponentFact;

public class ValidationResult {
  private final SdkComponentFact<?> fact;
  private final String message;
  private final ValidationStatusEnum status;

  public ValidationResult(SdkComponentFact<?> fact, String message,
      ValidationStatusEnum status) {
    this.fact = fact;
    this.message = message;
    this.status = status;
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

  @Override
  public String toString() {
    return MessageFormat.format("{0}({1}) - {2}: {3}",fact.getTypeName(), fact.getId(), status, message);
  }
}
