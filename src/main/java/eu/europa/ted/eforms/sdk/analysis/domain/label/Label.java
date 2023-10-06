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
    return id;
  }

  public Map<Language, String> getTranslations() {
    return translations;
  }

  public void addTranslation(Language language, String text) {
    translations.put(language, text);
  }

  public String getText(Language lang) {
    return translations.get(lang);
  }
}
