package eu.europa.ted.eforms.sdk.analysis.domain.label;

import java.util.Objects;

public class TranslationLanguage implements Comparable<TranslationLanguage> {
  private String twoLetterCode;
  private String threeLetterCode;
  private String description;

  public String getTwoLetterCode() {
    return this.twoLetterCode;
  }

  public void setTwoLetterCode(final String twoLetterCode) {
    this.twoLetterCode = twoLetterCode;
  }

  public String getThreeLetterCode() {
    return this.threeLetterCode;
  }

  public void setThreeLetterCode(final String threeLetterCode) {
    this.threeLetterCode = threeLetterCode;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
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
