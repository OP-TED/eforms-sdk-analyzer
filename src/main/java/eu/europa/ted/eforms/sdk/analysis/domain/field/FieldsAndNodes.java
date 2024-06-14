package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.analysis.domain.EFormsTrackableEntity;

/**
 * All nodes (xmlStructure). All fields used in the SDK fields.json generation.
 * https://docs.ted.europa.eu/eforms/X.X.X/fields/index.html
 */
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase",
    "businessEntities", "xmlStructure", "fields"})
public class FieldsAndNodes extends EFormsTrackableEntity {
  private static final long serialVersionUID = 148801586584043714L;

  private List<BusinessEntity> businessEntities;

  private final List<XmlStructureNode> xmlStructure = new ArrayList<>();

  private final List<Field> fields = new ArrayList<>();

  public List<BusinessEntity> getBusinessEntities() {
    return businessEntities;
  }

  @JsonProperty("xmlStructure")
  public List<XmlStructureNode> getNodes() {
    return xmlStructure;
  }

  public List<Field> getFields() {
    return fields;
  }
}
