package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.mdd.enums.FieldPrivacyCode;

@JsonPropertyOrder({"code", "unpublishedFieldId", "reasonCodeFieldId", "reasonDescriptionFieldId",
    "publicationDateFieldId"})
public class FieldPrivacy implements Serializable {
  private static final long serialVersionUID = -1318408566061305451L;

  private FieldPrivacyCode code;

  private String unpublishedFieldId;
  private String reasonCodeFieldId;
  private String reasonDescriptionFieldId;
  private String publicationDateFieldId;

  public FieldPrivacyCode getCode() {
    return this.code;
  }

  public void setCode(final FieldPrivacyCode code) {
    this.code = code;
  }

  public String getUnpublishedFieldId() {
    return this.unpublishedFieldId;
  }

  public void setUnpublishedFieldId(final String unpublishedFieldId) {
    this.unpublishedFieldId = unpublishedFieldId;
  }

  public String getReasonCodeFieldId() {
    return this.reasonCodeFieldId;
  }

  public void setReasonCodeFieldId(final String reasonCodeFieldId) {
    this.reasonCodeFieldId = reasonCodeFieldId;
  }

  public String getReasonDescriptionFieldId() {
    return this.reasonDescriptionFieldId;
  }

  public void setReasonDescriptionFieldId(final String reasonDescriptionFieldId) {
    this.reasonDescriptionFieldId = reasonDescriptionFieldId;
  }

  public String getPublicationDateFieldId() {
    return this.publicationDateFieldId;
  }

  public void setPublicationDateFieldId(final String publicationDateFieldId) {
    this.publicationDateFieldId = publicationDateFieldId;
  }
}
