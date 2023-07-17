package eu.europa.ted.eforms.sdk.analysis.validator;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public interface Validator {

  Validator validate() throws Exception;

  Set<ValidationResult> getResults();

  default Set<ValidationResult> getResults(EnumSet<ValidationStatusEnum> statuses) {
    if (CollectionUtils.isEmpty(statuses)) {
      return getResults();
    }

    return getResults().stream()
        .filter((ValidationResult result) -> statuses.contains(result.getStatus()))
        .collect(Collectors.toSet());
  }

  default String[] getWarnings() {
    return getResults(EnumSet.of(ValidationStatusEnum.WARNING)).stream()
        .map(ValidationResult::toString)
        .toArray(String[]::new);
  }

  default String[] getErrors() {
    return getResults(EnumSet.of(ValidationStatusEnum.ERROR)).stream()
        .map(ValidationResult::toString)
        .toArray(String[]::new);
  }

  default boolean hasWarnings() {
    return ArrayUtils.isNotEmpty(getWarnings());
  }

  default boolean hasErrors() {
    return ArrayUtils.isNotEmpty(getErrors());
  }
}
