package eu.europa.ted.eforms.sdk.analysis.domain.field;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"identifyInChangeNotice", "useInstanceIdentifier", "changeIdentifier"})
public class BusinessEntityChangeIdentification {
  private boolean identifyInChangeNotice;
  private boolean useInstanceIdentifier;
  private String changeIdentifier;

  public boolean isIdentifyInChangeNotice() {
    return this.identifyInChangeNotice;
  }

  public void setIdentifyInChangeNotice(final boolean identifyInChangeNotice) {
    this.identifyInChangeNotice = identifyInChangeNotice;
  }

  public boolean isUseInstanceIdentifier() {
    return this.useInstanceIdentifier;
  }

  public void setUseInstanceIdentifier(final boolean useInstanceIdentifier) {
    this.useInstanceIdentifier = useInstanceIdentifier;
  }

  public String getChangeIdentifier() {
    return this.changeIdentifier;
  }

  public void setChangeIdentifier(final String changeIdentifier) {
    this.changeIdentifier = changeIdentifier;
  }
}
