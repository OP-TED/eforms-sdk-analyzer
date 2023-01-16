package eu.europa.ted.eforms.sdk.domain.field;

import eu.europa.ec.mdd.generated.enums.BusinessRuleSeverity;
import eu.europa.ec.mdd.generated.tables.interfaces.IBusinessRule;

/**
 * Introduced for TEDEFO-546.
 *
 * <p>
 * Can be used next to default value or inside a constraint.
 * </p>
 *
 * @author rouschr
 */
public enum PropertyOrConstraintSeverity {
  // IMPORTANT:
  // RENAMING THOSE BREAKS THE CODE.
  // ALIGN THESE WITH DB RULE SEVERITY VALUES !!!
  ERROR, WARN;

  public static PropertyOrConstraintSeverity buildFieldSeverity(final IBusinessRule businessRule) {
    return buildFieldSeverity(businessRule.getSeverity());
  }

  public static PropertyOrConstraintSeverity buildFieldSeverity(
      final BusinessRuleSeverity severity) {
    if (severity == null) {
      return null;
    }
    // See TEDEFO-546 on possible values. Also see TEDEFO-595.
    // For now only two values ERROR and WARN are of interest for the UI.
    // And at the point of writing this only ERROR and WARN are used in the DB.
    return BusinessRuleSeverity.ERROR == severity ? PropertyOrConstraintSeverity.ERROR
        : PropertyOrConstraintSeverity.WARN;
  }

}
