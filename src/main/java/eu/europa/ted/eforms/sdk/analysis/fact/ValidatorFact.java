package eu.europa.ted.eforms.sdk.analysis.fact;

/**
 * The subject of a finding that reports an analyzer validator failing to run, rather than an SDK
 * asset. It exists so an unexpected validator crash surfaces as a normal finding (in the
 * {@code Analyzer} section) instead of aborting the whole analysis — see {@code SdkAnalyzer}'s
 * per-validator safety net.
 */
public final class ValidatorFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = 1L;

  private final String validatorName;

  public ValidatorFact(final String validatorName) {
    this.validatorName = validatorName;
  }

  @Override
  public String getId() {
    return this.validatorName;
  }

  @Override
  public String getTypeName() {
    return "validator";
  }
}
