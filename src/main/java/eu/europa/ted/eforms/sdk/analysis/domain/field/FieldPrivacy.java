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
    return code;
  }

  public String getUnpublishedFieldId() {
    return unpublishedFieldId;
  }

  public String getReasonCodeFieldId() {
    return reasonCodeFieldId;
  }

  public String getReasonDescriptionFieldId() {
    return reasonDescriptionFieldId;
  }

  public String getPublicationDateFieldId() {
    return publicationDateFieldId;
  }
}
