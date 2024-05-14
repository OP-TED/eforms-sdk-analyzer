package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.analysis.domain.field.BusinessEntity;

public class BusinessEntityFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -8325643682910825716L;

  private BusinessEntity businessEntity;

  public BusinessEntityFact(BusinessEntity businessEntity) {
    this.businessEntity = businessEntity;
  }

  public String getLabelId() {
    return businessEntity.getLabelId();
  }

  public String getRepeatsWithNodeId() {
    return businessEntity.getRepeatsWithNodeId();
  }

  public String getIdentifierFieldId() {
    return businessEntity.getInstanceIdentifier().getIdentifierFieldId();
  }

  public String getCaptionFieldId() {
    return businessEntity.getInstanceIdentifier().getCaptionFieldId();
  }

  @Override
  public String getId() {
    return businessEntity.getId();
  }

  @Override
  public String getTypeName() {
    return "businessEntity";
  }
}
