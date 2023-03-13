package eu.europa.ted.eforms.sdk.domain.view.index;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.domain.EFormsTrackableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase", "viewTemplates"})
public class TedefoViewTemplatesIndex extends EFormsTrackableEntity {
  private static final long serialVersionUID = 350940090536145089L;

  private List<TedefoViewTemplateIndex> viewTemplates;

  public List<TedefoViewTemplateIndex> getViewTemplates() {
    return viewTemplates;
  }
}
