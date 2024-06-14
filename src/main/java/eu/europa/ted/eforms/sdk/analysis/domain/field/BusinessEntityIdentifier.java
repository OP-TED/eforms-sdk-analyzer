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
    return formatName;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public String getReferencedBusinessEntityId() {
    return referencedBusinessEntityId;
  }

  public String getIdentifierFieldId() {
    return identifierFieldId;
  }

  public String getCaptionFieldId() {
    return captionFieldId;
  }
}
