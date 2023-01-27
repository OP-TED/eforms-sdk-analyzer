package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.domain.EFormsTrackableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Array of all the existing notice sub-types and associated document type. */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase", "noticeSubTypes",
    "documentTypes"})
public class NoticeTypesForIndex extends EFormsTrackableEntity {
  private final List<DocumentType> documentTypes = new ArrayList<>();

  private final List<NoticeSubTypeForIndex> noticeSubTypes = new ArrayList<>();

  public List<NoticeSubTypeForIndex> getNoticeSubTypes() {
    return noticeSubTypes;
  }
}
