package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eu.europa.ted.eforms.sdk.analysis.domain.label.LanguageFileInfo;
import eu.europa.ted.eforms.sdk.analysis.domain.label.TranslationsIndex;

public class TranslationsIndexFact implements SdkComponentFact<String>  {
  private TranslationsIndex translationsIndex;

  public TranslationsIndexFact(TranslationsIndex translationsIndex) {
    this.translationsIndex = translationsIndex;
  }

  public List<String> getTranslationFilenames() {
    return translationsIndex.getFiles().stream()
        .map(LanguageFileInfo::getFilename).collect(Collectors.toList());
  }
  
  @Override
  public String getId() {
    return StringUtils.EMPTY;
  }
    
}
