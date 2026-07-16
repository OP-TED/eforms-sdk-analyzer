package eu.europa.ted.eforms.sdk.analysis.domain.field;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import eu.europa.ted.eforms.sdk.analysis.domain.field.enums.IdentifierSchemeFormatName;

@JsonPropertyOrder({"referencedBusinessEntityId", "prefix", "schemeName", "identifierFieldId",
    "captionFieldId"})
public class BusinessEntityIdentifier {
  private IdentifierSchemeFormatName formatName;
  private String prefix;
  private String schemeName;
  private String referencedBusinessEntityId;
  private String identifierFieldId;
  private String captionFieldId;

  public IdentifierSchemeFormatName getFormatName() {
    return this.formatName;
  }

  public void setFormatName(final IdentifierSchemeFormatName formatName) {
    this.formatName = formatName;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  public String getSchemeName() {
    return this.schemeName;
  }

  public void setSchemeName(final String schemeName) {
    this.schemeName = schemeName;
  }

  public String getReferencedBusinessEntityId() {
    return this.referencedBusinessEntityId;
  }

  public void setReferencedBusinessEntityId(final String referencedBusinessEntityId) {
    this.referencedBusinessEntityId = referencedBusinessEntityId;
  }

  public String getIdentifierFieldId() {
    return this.identifierFieldId;
  }

  public void setIdentifierFieldId(final String identifierFieldId) {
    this.identifierFieldId = identifierFieldId;
  }

  public String getCaptionFieldId() {
    return this.captionFieldId;
  }

  public void setCaptionFieldId(final String captionFieldId) {
    this.captionFieldId = captionFieldId;
  }
}
