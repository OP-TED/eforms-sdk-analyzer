package eu.europa.ted.eforms.sdk.domain.mdd.enums;

import eu.europa.ted.eforms.sdk.domain.ILiteral;

public enum FieldPrivacyCode implements ILiteral {
  not_val("not-val"),

  not_max_val("not-max-val"),

  gro_max_ide("gro-max-ide"),

  gro_max_val("gro-max-val"),

  win_cho("win-cho"),

  no_awa_rea("no-awa-rea"),

  max_val("max-val"),

  rec_sub_cou("rec-sub-cou"),

  rec_sub_typ("rec-sub-typ"),

  ten_val_low("ten-val-low"),

  ten_val_hig("ten-val-hig"),

  rev_req("rev-req"),

  buy_rev_cou("buy-rev-cou"),

  buy_rev_typ("buy-rev-typ"),

  con_rev_use("con-rev-use"),

  con_rev_buy("con-rev-buy"),

  val_con_des("val-con-des"),

  cou_ori("cou-ori"),

  win_org_ide("win-org-ide"),

  win_ten_val("win-ten-val"),

  ten_ran("ten-ran"),

  win_ten_var("win-ten-var"),

  sub_val_kno("sub-val-kno"),

  sub_per_kno("sub-per-kno"),

  sub_val("sub-val"),

  sub_per("sub-per"),

  sub_des("sub-des"),

  sub_con("sub-con"),

  cro_bor_law("cro-bor-law"),

  pro_typ("pro-typ"),

  pro_fea("pro-fea"),

  pro_acc("pro-acc"),

  pro_acc_jus("pro-acc-jus"),

  dir_awa_jus("dir-awa-jus"),

  dir_awa_pre("dir-awa-pre"),

  dir_awa_tex("dir-awa-tex"),

  awa_cri_typ("awa-cri-typ"),

  awa_cri_nam("awa-cri-nam"),

  awa_cri_des("awa-cri-des"),

  awa_cri_wei("awa-cri-wei"),

  awa_cri_fix("awa-cri-fix"),

  awa_cri_thr("awa-cri-thr"),

  awa_cri_num("awa-cri-num"),

  awa_cri_com("awa-cri-com"),

  awa_cri_ord("awa-cri-ord"),

  not_app_val("not-app-val"),

  ree_val("ree-val"),

  gro_ree_val("gro-ree-val");

  private final String literal;

  private FieldPrivacyCode(String literal) {
    this.literal = literal;
  }

  // @Override
  // public Catalog getCatalog() {
  // return null;
  // }
  //
  // @Override
  // public Schema getSchema() {
  // return null;
  // }
  //
  // @Override
  // public String getName() {
  // return "field_privacy_code";
  // }
  //
  @Override
  public String getLiteral() {
    return literal;
  }
}
