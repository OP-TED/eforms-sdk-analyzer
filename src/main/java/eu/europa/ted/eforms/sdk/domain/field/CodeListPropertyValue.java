package eu.europa.ted.eforms.sdk.domain.field;

import eu.europa.ec.mdd.generated.enums.CodeListType;
import lombok.Data;

/**
 * TEDEFO-546: Implements a codelist constraint value.
 */
@Data
public class CodeListPropertyValue {
  /**
   * The code list id.
   */
  private String id;

  /**
   * Use the generated type enum.
   */
  private CodeListType type;

  /**
   * IMPORTANT: this is not always the parent of the tailored codelist.
   */
  private String parentId;
}
