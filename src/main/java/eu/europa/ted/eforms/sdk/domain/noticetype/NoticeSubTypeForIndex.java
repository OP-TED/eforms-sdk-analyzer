package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.mdd.generated.enums.NoticeFormType;
import eu.europa.ec.mdd.generated.enums.NoticeLegalBasis;
import eu.europa.ec.mdd.generated.enums.NoticeNoticeType;
import eu.europa.ted.eforms.sdk.util.EnumHelper;
import lombok.Data;

@Data
@JsonPropertyOrder({"documentType", "legalBasis", "formType", "type", "description", "subTypeId",
    "_label", "viewTemplateIds"})
public class NoticeSubTypeForIndex implements Serializable {
  private static final long serialVersionUID = 4324353708410064216L;

  private String documentType;
  private NoticeLegalBasis legalBasis;
  private String formType;
  private String type;
  private String description;
  private String subTypeId;

  @JsonProperty("_label")
  private String labelId;

  private List<String> viewTemplateIds = new ArrayList<>();

  public String getLabelId() {
    return labelId;
  }

  public List<String> getViewTemplateIds() {
    return viewTemplateIds;
  }

  public NoticeLegalBasis getLegalBasisEnum() {
    return legalBasis;
  }

  public String getLegalBasis() {
    return legalBasis != null ? legalBasis.getLiteral() : null;
  }

  public void setLegalBasis(final String code) {
    this.legalBasis = EnumHelper.getEnum(NoticeLegalBasis.class, code);
  }

  public String getFormType() {
    return formType;
  }

  public String getType() {
    return type;
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
