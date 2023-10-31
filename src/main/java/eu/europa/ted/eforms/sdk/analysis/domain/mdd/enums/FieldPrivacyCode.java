package eu.europa.ted.eforms.sdk.analysis.domain.mdd.enums;

import eu.europa.ted.eforms.sdk.analysis.domain.ILiteral;

public enum FieldPrivacyCode implements ILiteral {
  NOT_VAL("not-val"),
  NOT_MAX_VAL("not-max-val"),
  GRO_MAX_IDE("gro-max-ide"),
  GRO_MAX_VAL("gro-max-val"),
  WIN_CHO("win-cho"),
  NO_AWA_REA("no-awa-rea"),
  MAX_VAL("max-val"),
  REC_SUB_COU("rec-sub-cou"),
  REC_SUB_TYP("rec-sub-typ"),
  TEN_VAL_LOW("ten-val-low"),
  TEN_VAL_HIG("ten-val-hig"),
  REV_REQ("rev-req"),
  BUY_REV_COU("buy-rev-cou"),
  BUY_REV_TYP("buy-rev-typ"),
  CON_REV_USE("con-rev-use"),
  CON_REV_BUY("con-rev-buy"),
  VAL_CON_DES("val-con-des"),
  COU_ORI("cou-ori"),
  WIN_ORG_IDE("win-org-ide"),
  WIN_TEN_VAL("win-ten-val"),
  TEN_RAN("ten-ran"),
  WIN_TEN_VAR("win-ten-var"),
  SUB_VAL_KNO("sub-val-kno"),
  SUB_PER_KNO("sub-per-kno"),
  SUB_VAL("sub-val"),
  SUB_PER("sub-per"),
  SUB_DES("sub-des"),
  SUB_CON("sub-con"),
  CRO_BOR_LAW("cro-bor-law"),
  PRO_TYP("pro-typ"),
  PRO_FEA("pro-fea"),
  PRO_ACC("pro-acc"),
  PRO_ACC_JUS("pro-acc-jus"),
  DIR_AWA_JUS("dir-awa-jus"),
  DIR_AWA_PRE("dir-awa-pre"),
  DIR_AWA_TEX("dir-awa-tex"),
  AWA_CRI_TYP("awa-cri-typ"),
  AWA_CRI_NAM("awa-cri-nam"),
  AWA_CRI_DES("awa-cri-des"),
  AWA_CRI_WEI("awa-cri-wei"),
  AWA_CRI_FIX("awa-cri-fix"),
  AWA_CRI_THR("awa-cri-thr"),
  AWA_CRI_NUM("awa-cri-num"),
  AWA_CRI_COM("awa-cri-com"),
  AWA_CRI_ORD("awa-cri-ord"),
  NOT_APP_VAL("not-app-val"),
  REE_VAL("ree-val"),
  NOT_REE_VAL("not-ree-val"),
  GRO_REE_VAL("gro-ree-val");

  private final String literal;

  private FieldPrivacyCode(String literal) {
    this.literal = literal;
  }

  @Override
  public String getLiteral() {
    return literal;
  }
}
