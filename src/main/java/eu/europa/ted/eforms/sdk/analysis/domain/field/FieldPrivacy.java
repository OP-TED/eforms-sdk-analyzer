package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.mdd.enums.FieldPrivacyCode;

@JsonPropertyOrder({"code", "unpublishedFieldId", "reasonCodeFieldId", "reasonDescriptionFieldId",
    "publicationDateFieldId", "condition", "selector"})
public class FieldPrivacy implements Serializable {
  private static final long serialVersionUID = -1318408566061305451L;

  private FieldPrivacyCode code;

  private String unpublishedFieldId;
  private String reasonCodeFieldId;
  private String reasonDescriptionFieldId;
  private String publicationDateFieldId;

  /**
   * Optional EFX condition under which the field is withheld (TEDEFO-5128), evaluated in the
   * context of the field itself: {@code {fieldId} $&#123;conditionEfx&#125;}. Null when the field
   * is withheld unconditionally. Goes together with {@link #selector}.
   */
  private String condition;

  /**
   * Optional EFX selector locating the withheld field from the document root, with the condition
   * as a predicate. The {@code ND-Root} context is always present. Null when the field is
   * withheld unconditionally. Goes together with {@link #condition}.
   */
  private String selector;

  public FieldPrivacyCode getCode() {
    return this.code;
  }

  public void setCode(final FieldPrivacyCode code) {
    this.code = code;
  }

  public String getCondition() {
    return this.condition;
  }

  public void setCondition(final String condition) {
    this.condition = condition;
  }

  public String getSelector() {
    return this.selector;
  }

  public void setSelector(final String selector) {
    this.selector = selector;
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
