package eu.europa.ted.eforms.sdk.domain.field;

import lombok.Data;

/**
 * TEDEFO-546: Implements a range / interval constraint value.
 */
@Data
public class RangeNumericPropertyValue {
  /**
   * Since TEDEFO-354. String used because we may need to use references later on. Nullable.
   *
   * <p>
   * The name is "minNumber" which is what eNotices2 uses right now. But it could contain non
   * numbers later !
   * </p>
   */
  private String minNumber;

  /**
   * Since TEDEFO-354. String used because we may need to use references later on. Nullable.
   *
   * <p>
   * The name is "maxNumber" which is what eNotices2 uses right now. But it could contain non
   * numbers later !
   * </p>
   */
  private String maxNumber;
}
