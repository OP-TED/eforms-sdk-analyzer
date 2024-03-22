package eu.europa.ted.eforms.sdk.analysis.domain.field.enums;

public enum IdentifierSchemeFormatName {
  fixedText("fixedText"),
  prefixedNumber("prefixedNumber"),
  reference("reference");

  private final String literal;

  private IdentifierSchemeFormatName(String literal) {
     this.literal = literal;
  }

  public String getLiteral() {
    return this.literal;
  }
}
