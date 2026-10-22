package eu.europa.ted.eforms.sdk.analysis.domain.enums;

import java.util.Locale;

public enum Language {
  // 24 official EU languages.
  BG(Locale.of("bg"), "Bulgarian", "bul_label"),
  CS(Locale.of("cs"), "Czech", "ces_label"),
  DA(Locale.of("da"), "Danish", "dan_label"),
  DE(Locale.of("de"), "German", "deu_label"),
  EL(Locale.of("el"), "Greek", "ell_label"),
  EN(Locale.of("en"), "English", "eng_label"),
  ES(Locale.of("es"), "Spanish", "spa_label"),
  ET(Locale.of("et"), "Estonian", "est_label"),
  FI(Locale.of("fi"), "Finnish", "fin_label"),
  FR(Locale.of("fr"), "French", "fra_label"),
  GA(Locale.of("ga"), "Irish", "gle_label"),
  HR(Locale.of("hr"), "Croatian", "hrv_label"),
  HU(Locale.of("hu"), "Hungarian", "hun_label"),
  IT(Locale.of("it"), "Italian", "ita_label"),
  LT(Locale.of("lt"), "Lithuanian", "lit_label"),
  LV(Locale.of("lv"), "Latvian", "lav_label"),
  MT(Locale.of("mt"), "Maltese", "mlt_label"),
  NL(Locale.of("nl"), "Dutch", "nld_label"),
  PL(Locale.of("pl"), "Polish", "pol_label"),
  PT(Locale.of("pt"), "Portuguese", "por_label"),
  RO(Locale.of("ro"), "Romanian", "ron_label"),
  SK(Locale.of("sk"), "Slovak", "slk_label"),
  SL(Locale.of("sl"), "Slovenian", "slv_label"),
  SV(Locale.of("sv"), "Swedish", "swe_label");

  public final Locale locale;
  public final String englishLanguage;
  public final String genericodeLanguage;

  private Language(Locale locale, String englishLanguage, String genericodeLanguage) {
    this.locale = locale;
    this.englishLanguage = englishLanguage;
    this.genericodeLanguage = genericodeLanguage;
  }
}
