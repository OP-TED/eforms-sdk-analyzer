package eu.europa.ted.eforms.sdk.domain.mdd.enums;

import eu.europa.ted.eforms.sdk.domain.ILiteral;

public enum NoticeLegalBasis implements ILiteral {
  _32001R2157("32001R2157"),

  _32014L0024("32014L0024"),

  _32014L0025("32014L0025"),

  _32014L0023("32014L0023"),

  _32009L0081("32009L0081"),

  _32007R1370("32007R1370"),

  _31985R2137("31985R2137"),

  _32018R1046("32018R1046"),

  other("other"),

  unknown("unknown");

  private final String literal;

  private NoticeLegalBasis(String literal) {
    this.literal = literal;
  }

  @Override
  public String getLiteral() {
    return literal;
  }
}
