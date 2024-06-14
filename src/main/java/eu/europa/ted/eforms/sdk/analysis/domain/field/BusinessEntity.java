package eu.europa.ted.eforms.sdk.analysis.domain.field;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "description", "labelId", "repeatable", "repeatsWithNodeId",
    "changeIdentification", "instanceIdentifier"})
public class BusinessEntity {
  private String id;
  private String name;
  private String description;
  private String labelId;
  private boolean repeatable;
  private String repeatsWithNodeId;

  private BusinessEntityChangeIdentification changeIdentification;

  private BusinessEntityIdentifier instanceIdentifier;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getLabelId() {
    return labelId;
  }

  public boolean isRepeatable() {
    return repeatable;
  }

  public String getRepeatsWithNodeId() {
    return repeatsWithNodeId;
  }

  public BusinessEntityChangeIdentification getChangeIdentification() {
    return changeIdentification;
  }

  public BusinessEntityIdentifier getInstanceIdentifier() {
    return instanceIdentifier;
  }
}
