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
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getLabelId() {
    return this.labelId;
  }

  public void setLabelId(final String labelId) {
    this.labelId = labelId;
  }

  public boolean isRepeatable() {
    return this.repeatable;
  }

  public void setRepeatable(final boolean repeatable) {
    this.repeatable = repeatable;
  }

  public String getRepeatsWithNodeId() {
    return this.repeatsWithNodeId;
  }

  public void setRepeatsWithNodeId(final String repeatsWithNodeId) {
    this.repeatsWithNodeId = repeatsWithNodeId;
  }

  public BusinessEntityChangeIdentification getChangeIdentification() {
    return this.changeIdentification;
  }

  public void setChangeIdentification(
      final BusinessEntityChangeIdentification changeIdentification) {
    this.changeIdentification = changeIdentification;
  }

  public BusinessEntityIdentifier getInstanceIdentifier() {
    return this.instanceIdentifier;
  }

  public void setInstanceIdentifier(final BusinessEntityIdentifier instanceIdentifier) {
    this.instanceIdentifier = instanceIdentifier;
  }
}
