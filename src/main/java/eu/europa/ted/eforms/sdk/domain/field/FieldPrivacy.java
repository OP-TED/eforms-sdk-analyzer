package eu.europa.ted.eforms.sdk.domain.field;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.domain.mdd.enums.FieldPrivacyCode;
import eu.europa.ted.eforms.sdk.util.EnumHelper;
import lombok.Data;

@Data
@JsonPropertyOrder({"code", "unpublishedFieldId", "reasonCodeFieldId", "reasonDescriptionFieldId",
    "publicationDateFieldId"})
public class FieldPrivacy implements Serializable {
  private static final long serialVersionUID = -1318408566061305451L;

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
