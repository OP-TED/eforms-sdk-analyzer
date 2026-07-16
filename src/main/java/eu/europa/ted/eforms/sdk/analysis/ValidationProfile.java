package eu.europa.ted.eforms.sdk.analysis;

import java.util.Objects;
import org.apache.commons.lang3.Validate;

/**
 * Immutable value describing a single validation run: which SDK major version
 * is being analysed and whether the content comes from a file-system source
 * or a non-file source (e.g. a database). Used by the rule applicability
 * framework to select which rules fire for the run.
 *
 * <p>{@code sdkMajor} is an {@code int} because the set of valid SDK majors
 * is open (SDK 3 will exist; the framework should not need to be re-released
 * to know it). {@code sourceKind} is an enum because the set of source kinds
 * is closed.
 */
public final class ValidationProfile {

  private final int sdkMajor;
  private final SourceKind sourceKind;

  public ValidationProfile(final int sdkMajor, final SourceKind sourceKind) {
    Validate.isTrue(sdkMajor > 0, "sdkMajor must be positive (got %d)", sdkMajor);
    this.sdkMajor = sdkMajor;
    this.sourceKind = Validate.notNull(sourceKind, "Undefined sourceKind");
  }

  public int getSdkMajor() {
    return this.sdkMajor;
  }

  public SourceKind getSourceKind() {
    return this.sourceKind;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ValidationProfile)) {
      return false;
    }
    final ValidationProfile other = (ValidationProfile) o;
    return this.sdkMajor == other.sdkMajor && this.sourceKind == other.sourceKind;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.sdkMajor, this.sourceKind);
  }

  @Override
  public String toString() {
    return "ValidationProfile{sdkMajor=" + this.sdkMajor + ", sourceKind=" + this.sourceKind + "}";
  }
}
