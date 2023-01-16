package eu.europa.ted.eforms.sdk.domain.field;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.mdd.generated.enums.FieldPrivacyCode;
import eu.europa.ted.eforms.sdk.domain.util.EnumHelper;
import lombok.Data;

@Data
@JsonPropertyOrder({"code", "unpublishedFieldId", "reasonCodeFieldId", "reasonDescriptionFieldId",
    "publicationDateFieldId"})
public class FieldPrivacy {
  private FieldPrivacyCode code;

  private String unpublishedFieldId;
  private String reasonCodeFieldId;
  private String reasonDescriptionFieldId;
  private String publicationDateFieldId;

  public String getCode() {
    return code != null ? code.getLiteral() : null;
  }

  public void setCode(final String code) {
    this.code = EnumHelper.getEnum(FieldPrivacyCode.class, code);
  }
}
