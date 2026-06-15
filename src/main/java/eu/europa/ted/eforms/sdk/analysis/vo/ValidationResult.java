package eu.europa.ted.eforms.sdk.analysis.vo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import eu.europa.ted.eforms.sdk.analysis.Identifiable;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.SdkComponentFact;

public class ValidationResult {
  private final SdkComponentFact<?> fact;
  private final String message;
  private final ValidationStatusEnum status;
  private final List<AssetRef> references;

  public ValidationResult(SdkComponentFact<?> fact, String message, ValidationStatusEnum status) {
    this(fact, message, status, Collections.<AssetRef>emptyList());
  }

  /** A finding that references a single other asset (e.g. the missing/incorrect target). */
  public ValidationResult(SdkComponentFact<?> fact, String message, ValidationStatusEnum status,
      AssetRef reference) {
    this(fact, message, status, Collections.singletonList(reference));
  }

  public ValidationResult(SdkComponentFact<?> fact, String message, ValidationStatusEnum status,
      List<AssetRef> references) {
    this.fact = fact;
    this.message = message;
    this.status = status;
    // De-duplicate while preserving order (mirrors the previous LinkedHashSet behaviour).
    this.references = Collections.unmodifiableList(new ArrayList<>(new LinkedHashSet<>(references)));
  }

  public Identifiable<?> getFact() {
    return fact;
  }

  /**
   * The subject of this finding (where it was found) as a typed asset reference. A file-backed fact
   * carries its SDK-relative path, so the subject reads as the file a reader opens; otherwise the id.
   */
  public AssetRef getSubject() {
    final String path = fact.getSdkPath();
    return new AssetRef(fact.getTypeName(), path != null ? path : String.valueOf(fact.getId()));
  }

  public String getMessage() {
    return message;
  }

  public ValidationStatusEnum getStatus() {
    return status;
  }

  /** The other asset(s) this finding implicates (empty for intrinsic findings). */
  public List<AssetRef> getReferences() {
    return references;
  }

  @Override
  public String toString() {
    return MessageFormat.format("{0}({1}) - {2}: {3}", fact.getTypeName(), fact.getId(), status,
        message);
  }
}
