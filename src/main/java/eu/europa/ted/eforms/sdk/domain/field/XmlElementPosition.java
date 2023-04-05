package eu.europa.ted.eforms.sdk.domain.field;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class XmlElementPosition {

  private String elementName;
  private Integer elementIndex;

  public String getElementName() {
    return elementName;
  }

  public Integer getElementIndex() {
    return elementIndex;
  }

  @JsonAnySetter
  public void setValue(String elementName, Integer elementIndex) {
    this.elementName = elementName;
    this.elementIndex = elementIndex;
  }
}
