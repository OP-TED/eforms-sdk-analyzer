package eu.europa.ted.eforms.sdk.analysis.domain.mdd.enums;

import eu.europa.ted.eforms.sdk.analysis.domain.ILiteral;

public enum FieldPrivacyCode implements ILiteral {
  CON_ESTI("con-esti"),
  CRI_AWRD("cri-awrd"),
  EOP_INFO("eop-info"),
  NOT_FRAM("not-fram"),
  NOT_VALU("not-valu"),
  ORI_INFO("ori-info"),
  REV_INFO("rev-info"),
  SUB_INFO("sub-info"),
  SUB_LOHI("sub-lohi"),
  SUB_STAT("sub-stat"),
  TEN_RANK("ten-rank"),
  TEN_VALU("ten-valu"),
  TEN_VARI("ten-vari");

  private final String literal;

  private FieldPrivacyCode(String literal) {
    this.literal = literal;
  }

  @Override
  public String getLiteral() {
    return literal;
  }
}
