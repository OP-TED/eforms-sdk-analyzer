package eu.europa.ted.eforms.sdk.analysis.validator;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
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
