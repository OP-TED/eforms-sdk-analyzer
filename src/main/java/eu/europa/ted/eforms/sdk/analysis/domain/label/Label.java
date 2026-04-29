package eu.europa.ted.eforms.sdk.analysis.domain.label;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;

/**
 * A specific label, with all its translations in all languages.
 */
public class Label implements Serializable {
  private static final long serialVersionUID = -24724237531309137L;

  private final String id;
  private Map<Language, String> translations;

  public Label(String id) {
    this.id = id;
    this.translations = new HashMap<>();
  }


  public String getId() {
    return this.id;
  }

  public Map<Language, String> getTranslations() {
    return this.translations;
  }

  public void setTranslations(final Map<Language, String> translations) {
    this.translations = translations;
  }

  public void addTranslation(final Language language, final String text) {
    this.translations.put(language, text);
  }

  public String getText(final Language lang) {
    return this.translations.get(lang);
  }
}
