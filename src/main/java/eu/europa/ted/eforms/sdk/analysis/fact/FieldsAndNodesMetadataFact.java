package eu.europa.ted.eforms.sdk.analysis.fact;

import org.apache.commons.lang3.StringUtils;

import eu.europa.ted.eforms.sdk.analysis.domain.EFormsTrackableEntity;

public class FieldsAndNodesMetadataFact implements SdkComponentFact<String> {
  private EFormsTrackableEntity metadata;
  
  public FieldsAndNodesMetadataFact(EFormsTrackableEntity metadata) {
    this.metadata = metadata;
  }

  public String getSdkVersion() {
    return metadata.getSdkVersion();
  }

  @Override
  public String getId() {
    return StringUtils.EMPTY;
  }
  
  @Override
  public String getTypeName() {
    return "fieldsAndNodesMetadata";
  }
}
