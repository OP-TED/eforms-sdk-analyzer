package eu.europa.ted.eforms.sdk.analysis.validator;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public interface Validator {

  Validator validate() throws Exception;

  Set<ValidationResult> getResults();

  /**
   * This validator's results as {@link Finding}s. The default — used by the external-engine validators
   * (XSD, Schematron, EFX) — tags each finding with this validator's class name as the {@code kind} (a
   * stable machine key) and uses the result's own message as the {@code problem}. Those engines emit a
   * specific problem statement per finding rather than firing a named, pre-labelled rule, so the
   * message <em>is</em> the problem; findings then group by problem in the report exactly like the
   * drools ones. {@code SdkValidator} overrides this to stamp each finding with its drools rule name
   * and {@code @problem}.
   */
  default List<Finding> getFindings() {
    final String kind = getClass().getSimpleName();
    return getResults().stream().map(result -> new Finding(kind, result.getMessage(), result))
        .collect(Collectors.toList());
  }

  default Set<ValidationResult> getResults(EnumSet<ValidationStatusEnum> statuses) {
    if (CollectionUtils.isEmpty(statuses)) {
      return getResults();
    }

    return getResults().stream()
        .filter((ValidationResult result) -> statuses.contains(result.getStatus()))
        .collect(Collectors.toSet());
  }

  default Set<ValidationResult> getWarnings() {
    return getResults(EnumSet.of(ValidationStatusEnum.WARNING));
  }

  default Set<ValidationResult> getErrors() {
    return getResults(EnumSet.of(ValidationStatusEnum.ERROR));
  }

  default boolean hasWarnings() {
    return CollectionUtils.isNotEmpty(getWarnings());
  }

  default boolean hasErrors() {
    return CollectionUtils.isNotEmpty(getErrors());
  }
}
