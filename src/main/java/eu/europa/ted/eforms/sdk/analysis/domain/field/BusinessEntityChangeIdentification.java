package eu.europa.ted.eforms.sdk.analysis.domain.field;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"identifyInChangeNotice", "useInstanceIdentifier", "changeIdentifier"})
public class BusinessEntityChangeIdentification {
  private boolean identifyInChangeNotice;
  private boolean useInstanceIdentifier;
  private String changeIdentifier;

  public boolean isIdentifyInChangeNotice() {
    return identifyInChangeNotice;
  }

  public boolean isUseInstanceIdentifier() {
    return useInstanceIdentifier;
  }

  public String getChangeIdentifier() {
    return changeIdentifier;
  }
}
