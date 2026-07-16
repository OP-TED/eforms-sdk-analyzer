package eu.europa.ted.eforms.sdk.analysis.domain.label;

public class LanguageFileInfo {

  private String twoLetterCode;
  private String threeLetterCode;
  private String assetType;
  private String filename;

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

  public String getAssetType() {
    return this.assetType;
  }

  public void setAssetType(final String assetType) {
    this.assetType = assetType;
  }

  public String getFilename() {
    return this.filename;
  }

  public void setFilename(final String filename) {
    this.filename = filename;
  }
}
