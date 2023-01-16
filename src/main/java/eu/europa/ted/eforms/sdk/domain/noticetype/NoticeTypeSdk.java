package eu.europa.ted.eforms.sdk.domain.noticetype;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.europa.ted.eforms.sdk.domain.EFormsTrackableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain object to represent an SDK notice type for JSON in Java. Top level item.
 *
 * <p style="color: red;">
 * IMPORTANT: be careful when adding getters as those will end up in the JSON !!!
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_DEFAULT) // Avoids having xyz: false
public class NoticeTypeSdk extends EFormsTrackableEntity {

  private String noticeId;
  private final List<NoticeTypeContent> metadata = new ArrayList<>();
  private final List<NoticeTypeContent> content = new ArrayList<>();

  public String getNoticeId() {
    return noticeId;
  }

  public List<NoticeTypeContent> getMetadata() {
    return metadata;
  }

  public List<NoticeTypeContent> getContent() {
    return content;
  }
}
