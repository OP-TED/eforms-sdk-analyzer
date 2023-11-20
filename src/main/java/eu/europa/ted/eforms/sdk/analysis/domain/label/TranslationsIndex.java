package eu.europa.ted.eforms.sdk.analysis.domain.label;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"files", "languages"})
public class TranslationsIndex implements Serializable {

  private final List<LanguageFileInfo> files = new ArrayList<>();
  private final List<TranslationLanguage> languages = new ArrayList<>();

  public List<LanguageFileInfo> getFiles() {
    return files;
  }

  public List<TranslationLanguage> getLanguages() {
    return languages;
  }
}
