package eu.europa.ted.eforms.sdk.analysis.domain.field;

import java.io.Serializable;

/**
 * TEDEFO-546: Implements a range / interval constraint value.
 */
public class RangeNumericPropertyValue implements Serializable {
  private static final long serialVersionUID = -5361635853614332303L;

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

  public String getMinNumber() {
    return minNumber;
  }

  public String getMaxNumber() {
    return maxNumber;
  }
}
