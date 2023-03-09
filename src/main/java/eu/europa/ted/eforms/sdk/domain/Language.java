package eu.europa.ted.eforms.sdk.domain;

import static eu.europa.ec.mdd.generated.Tables.LABEL;
import java.util.Arrays;
import java.util.Locale;
import org.jooq.TableField;
import eu.europa.ec.mdd.generated.tables.records.LabelRecord;

public enum Language {

  // 24 official EU languages.

  BG(new Locale("bg"), LABEL.BG, "Bulgarian", "bul_label"), //
  CS(new Locale("cs"), LABEL.CS, "Czech", "ces_label"), //
  DA(new Locale("da"), LABEL.DA, "Danish", "dan_label"), //
  DE(new Locale("de"), LABEL.DE, "German", "deu_label"), //
  EL(new Locale("el"), LABEL.EL, "Greek", "ell_label"), //

  EN(new Locale("en"), LABEL.EN, "English", "eng_label"), //
  ES(new Locale("es"), LABEL.ES, "Spanish", "spa_label"), //
  ET(new Locale("et"), LABEL.ET, "Estonian", "est_label"), //
  FI(new Locale("fi"), LABEL.FI, "Finnish", "fin_label"), //
  FR(new Locale("fr"), LABEL.FR, "French", "fra_label"), //

  GA(new Locale("ga"), LABEL.GA, "Galician", "gle_label"), //
  HR(new Locale("hr"), LABEL.HR, "Croatian", "hrv_label"), //
  HU(new Locale("hu"), LABEL.HU, "Hungarian", "hun_label"), //
  IT(new Locale("it"), LABEL.IT, "Italian", "ita_label"), //
  LT(new Locale("lt"), LABEL.LT, "Lithuanian", "lit_label"), //

  LV(new Locale("lv"), LABEL.LV, "Latvian", "lav_label"), //
  MT(new Locale("mt"), LABEL.MT, "Maltese", "mlt_label"), //
  NL(new Locale("nl"), LABEL.NL, "Dutch", "nld_label"), //
  PL(new Locale("pl"), LABEL.PL, "Polish", "pol_label"), //
  PT(new Locale("pt"), LABEL.PT, "Portuguese", "por_label"), //

  RO(new Locale("ro"), LABEL.RO, "Romanian", "ron_label"), //
  SK(new Locale("sk"), LABEL.SK, "Slovak", "slk_label"), //
  SL(new Locale("sl"), LABEL.SL, "Slovene", "slv_label"), //
  SV(new Locale("sv"), LABEL.SV, "Swedish", "swe_label");

  public final Locale locale;
  public final TableField<LabelRecord, String> tableField;
  public final String englishLanguage;
  public final String genericodeLanguage;

  private Language(Locale locale, TableField<LabelRecord, String> tableField,
      String englishLanguage, String genericodeLanguage) {
    this.locale = locale;
    this.tableField = tableField;
    this.englishLanguage = englishLanguage;
    this.genericodeLanguage = genericodeLanguage;
  }

  public Locale getLocale() {
    return locale;
  }

  public TableField<LabelRecord, String> getTableField() {
    return tableField;
  }

  public String getEnglishLanguage() {
    return englishLanguage;
  }

  public static Language valueOfFromSkos(final String value) {
    return Arrays.stream(values())//
        .filter(event -> event.locale.getLanguage().equals(value))//
        .findFirst()//
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("Unknown skos value: '%s'", value)));
  }

  public static Language valueOfFromGenericode(final String value) {
    return Arrays.stream(values())//
        .filter(event -> event.genericodeLanguage.equals(value))//
        .findFirst()//
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("Unknown genericode: '%s'", value)));
  }

  public static Language valueOfFromLocale(final Locale value) {
    return Arrays.stream(values())//
        .filter(event -> event.locale == value)//
        .findFirst()//
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("Unknown locale: '%s':", value)));
  }
}
