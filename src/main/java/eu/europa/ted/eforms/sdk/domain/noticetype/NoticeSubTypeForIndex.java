package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.mdd.generated.enums.NoticeFormType;
import eu.europa.ec.mdd.generated.enums.NoticeLegalBasis;
import eu.europa.ec.mdd.generated.enums.NoticeNoticeType;
import eu.europa.ted.eforms.sdk.domain.util.EnumHelper;
import lombok.Data;

@Data
@JsonPropertyOrder({"documentType", "legalBasis", "formType", "type", "description", "subTypeId",
    "_label", "viewTemplateIds"})
public class NoticeSubTypeForIndex {
  private String documentType;
  private NoticeLegalBasis legalBasis;
  private NoticeFormType formType;
  private NoticeNoticeType type;
  private String description;
  private String subTypeId;

  @JsonProperty("_label")
  private String labelId;

  private List<String> viewTemplateIds = new ArrayList<>();

  public NoticeLegalBasis getLegalBasisEnum() {
    return legalBasis;
  }

  public String getLegalBasis() {
    return legalBasis != null ? legalBasis.getLiteral() : null;
  }

  public void setLegalBasis(final String code) {
    this.legalBasis = EnumHelper.getEnum(NoticeLegalBasis.class, code);
  }

  public NoticeFormType getFormTypeEnum() {
    return formType;
  }

  public String getFormType() {
    return formType != null ? formType.getLiteral() : null;
  }

  public void setFormType(final String code) {
    this.formType = EnumHelper.getEnum(NoticeFormType.class, code);
  }

  public NoticeNoticeType getTypeEnum() {
    return type;
  }

  public String getType() {
    return type != null ? type.getLiteral() : null;
  }

  public void setType(final String code) {
    this.type = EnumHelper.getEnum(NoticeNoticeType.class, code);
  }

  public String getDocumentType() {
    return documentType;
  }

  public String getDescription() {
    return description;
  }

  public String getSubTypeId() {
    return subTypeId;
  }
}
