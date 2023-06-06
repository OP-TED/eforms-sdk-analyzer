package eu.europa.ted.eforms.sdk.domain.codelist;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.domain.EFormsTrackableEntity;

@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase", "codelists"})
public class CodelistsIndex extends EFormsTrackableEntity {
  private final List<CodelistsIndexItem> codelists = new ArrayList<>();

  public List<CodelistsIndexItem> getCodelists() {
    return codelists;
  }
}
