package eu.europa.ted.eforms.sdk.domain.mdd.enums;

import eu.europa.ted.eforms.sdk.domain.ILiteral;

public enum CodeListType implements ILiteral {
  FLAT("flat"), HIERARCHICAL("hierarchical");

  private String literal;

  CodeListType(String literal) {
    this.literal = literal;
  }

  @Override
  public String getLiteral() {
    return literal;
  }
}
