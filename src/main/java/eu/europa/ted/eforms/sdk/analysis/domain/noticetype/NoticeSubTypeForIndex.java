package eu.europa.ted.eforms.sdk.analysis.domain.noticetype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.mdd.enums.NoticeLegalBasis;

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
    return this.labelId;
  }

  public void setLabelId(final String labelId) {
    this.labelId = labelId;
  }

  public List<String> getViewTemplateIds() {
    return this.viewTemplateIds;
  }

  public void setViewTemplateIds(final List<String> viewTemplateIds) {
    this.viewTemplateIds = viewTemplateIds;
  }

  public NoticeLegalBasis getLegalBasis() {
    return this.legalBasis;
  }

  public void setLegalBasis(final NoticeLegalBasis legalBasis) {
    this.legalBasis = legalBasis;
  }

  public String getFormType() {
    return this.formType;
  }

  public void setFormType(final String formType) {
    this.formType = formType;
  }

  public String getType() {
    return this.type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getDocumentType() {
    return this.documentType;
  }

  public void setDocumentType(final String documentType) {
    this.documentType = documentType;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getSubTypeId() {
    return this.subTypeId;
  }

  public void setSubTypeId(final String subTypeId) {
    this.subTypeId = subTypeId;
  }
}
