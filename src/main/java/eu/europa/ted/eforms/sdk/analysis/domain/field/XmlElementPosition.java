package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class XmlElementPosition implements Serializable {
  private static final long serialVersionUID = 3484514100289348439L;

  private String elementName;
  private Integer elementIndex;

  public String getElementName() {
    return this.elementName;
  }

  public void setElementName(final String elementName) {
    this.elementName = elementName;
  }

  public Integer getElementIndex() {
    return this.elementIndex;
  }

  public void setElementIndex(final Integer elementIndex) {
    this.elementIndex = elementIndex;
  }

  @JsonAnySetter
  public void setValue(String elementName, Integer elementIndex) {
    this.elementName = elementName;
    this.elementIndex = elementIndex;
  }
}
