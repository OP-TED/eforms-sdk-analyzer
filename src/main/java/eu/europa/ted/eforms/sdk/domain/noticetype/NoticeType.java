package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.util.List;
import eu.europa.ec.mdd.generated.enums.NoticeFormType;
import eu.europa.ec.mdd.generated.enums.NoticeLegalBasis;
import eu.europa.ec.mdd.generated.enums.NoticeNoticeType;
import eu.europa.ted.eforms.sdk.domain.MetadataDatabase;
import lombok.Data;

@Data
public class NoticeType {
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

  public NoticeFormType getFormType() {
    return noticeSubTypeForIndex.getFormTypeEnum();
  }

  public NoticeNoticeType getType() {
    return noticeSubTypeForIndex.getTypeEnum();
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
