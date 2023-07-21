package eu.europa.ted.eforms.sdk.analysis.util;

import java.util.regex.Pattern;

public class XPathUtils {
  private XPathUtils() {}

  private static Pattern ascendingAxes =
      Pattern.compile("^\\.\\.|(parent|ancestor|ancestor-or-self)::.*");

  public static boolean isAscendingStep(String step) {
    return ascendingAxes.matcher(step).matches();
  }
}
