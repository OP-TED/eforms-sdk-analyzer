package eu.europa.ted.eforms.sdk.analysis.domain.noticetype;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.EFormsTrackableEntity;

/** Array of all the existing notice sub-types and associated document type. */
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase", "noticeSubTypes",
    "documentTypes"})
public class NoticeTypesForIndex extends EFormsTrackableEntity {
  private static final long serialVersionUID = -6549601786048458875L;

  private final List<DocumentType> documentTypes = new ArrayList<>();

  private final List<NoticeSubTypeForIndex> noticeSubTypes = new ArrayList<>();

  public List<NoticeSubTypeForIndex> getNoticeSubTypes() {
    return noticeSubTypes;
  }

  public List<DocumentType> getDocumentTypes() {
    return documentTypes;
  }
}
