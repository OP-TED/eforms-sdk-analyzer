package eu.europa.ted.eforms.sdk.domain.field;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ted.eforms.sdk.domain.EFormsTrackableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * All nodes (xmlStructure). All fields used in the SDK fields.json generation.
 * https://docs.ted.europa.eu/eforms/X.X.X/fields/index.html
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"ublVersion", "sdkVersion", "metadataDatabase", "xmlStructure", "fields"})
public class FieldsAndNodes extends EFormsTrackableEntity {
  private static final long serialVersionUID = 148801586584043714L;

  /**
   * TEDEFO-552: node list (aka sections).
   */
  private final List<XmlStructureNode> xmlStructure = new ArrayList<>();

  private final List<Field> fields = new ArrayList<>();

  public List<XmlStructureNode> getNodes() {
    return xmlStructure;
  }

  public List<Field> getFields() {
    return fields;
  }
}
