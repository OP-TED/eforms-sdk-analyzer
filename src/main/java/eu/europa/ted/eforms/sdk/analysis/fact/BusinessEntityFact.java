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
    if (businessEntity.getInstanceIdentifier() != null) {
      return businessEntity.getInstanceIdentifier().getIdentifierFieldId();
    } else {
      return null;
    }
  }

  public String getCaptionFieldId() {
    if (businessEntity.getInstanceIdentifier() != null) {
      return businessEntity.getInstanceIdentifier().getCaptionFieldId();
    } else {
      return null;
    }
  }

  public String getReferencedBusinessEntityId() {
    if (businessEntity.getInstanceIdentifier() != null) {
      return businessEntity.getInstanceIdentifier().getReferencedBusinessEntityId();
    } else {
      return null;
    }
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
