package eu.europa.ted.eforms.sdk.analysis.domain.label;

import java.util.Objects;

public class TranslationLanguage implements Comparable<TranslationLanguage> {
  private String twoLetterCode;
  private String threeLetterCode;
  private String description;

  public String getTwoLetterCode() {
    return twoLetterCode;
  }

  public String getThreeLetterCode() {
    return threeLetterCode;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public int compareTo(TranslationLanguage o) {
    return this.twoLetterCode.compareTo(o.getTwoLetterCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(twoLetterCode);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TranslationLanguage other = (TranslationLanguage) obj;
    return Objects.equals(twoLetterCode, other.twoLetterCode);
  }
}
