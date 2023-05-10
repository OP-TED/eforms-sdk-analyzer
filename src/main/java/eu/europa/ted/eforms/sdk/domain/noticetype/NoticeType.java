package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.io.Serializable;
import java.util.List;
import eu.europa.ted.eforms.sdk.domain.MetadataDatabase;
import eu.europa.ted.eforms.sdk.domain.mdd.enums.NoticeLegalBasis;

public class NoticeType implements Serializable {
  private static final long serialVersionUID = 2166698175017975952L;

  private final NoticeSubTypeForIndex noticeSubTypeForIndex;
  private final NoticeTypeSdk noticeTypeSdk;

  public NoticeType(NoticeSubTypeForIndex noticeSubTypeForIndex, NoticeTypeSdk noticeTypeSdk) {
    this.noticeSubTypeForIndex = noticeSubTypeForIndex;
    this.noticeTypeSdk = noticeTypeSdk;
  }

  public String getDocumentType() {
    return noticeSubTypeForIndex.getDocumentType();
  }

  public NoticeLegalBasis getLegalBasis() {
    return noticeSubTypeForIndex.getLegalBasisEnum();
  }

  public String getFormType() {
    return noticeSubTypeForIndex.getFormType();
  }

  public String getType() {
    return noticeSubTypeForIndex.getType();
  }

  public String getDescription() {
    return noticeSubTypeForIndex.getDescription();
  }

  public String getSubTypeId() {
    return noticeSubTypeForIndex.getSubTypeId();
  }

  public String getUblVersion() {
    return noticeTypeSdk.getUblVersion();
  }

  public String getSdkVersion() {
    return noticeTypeSdk.getSdkVersion();
  }

  public MetadataDatabase getMetadataDatabase() {
    return noticeTypeSdk.getMetadataDatabase();
  }

  public String getNoticeId() {
    return noticeTypeSdk.getNoticeId();
  }

  public List<NoticeTypeContent> getMetadata() {
    return noticeTypeSdk.getMetadata();
  }

  public List<NoticeTypeContent> getContent() {
    return noticeTypeSdk.getContent();
  }
}
