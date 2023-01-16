package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;
import eu.europa.ted.eforms.sdk.analysis.ValidationResult;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypeFact;

public class SdkUnit implements RuleUnitData {
  private final DataStore<FieldFact> fields;
  private final DataStore<NoticeTypeFact> noticeTypes;

  private final Set<ValidationResult> results = new HashSet<>(); // Global variable to store
  // validations results

  public SdkUnit() {
    this(DataSource.createStore(), DataSource.createStore());
  }

  public SdkUnit(DataStore<FieldFact> fields, DataStore<NoticeTypeFact> noticeTypes) {
    this.fields = fields;
    this.noticeTypes = noticeTypes;
  }

  public DataStore<FieldFact> getFields() {
    return fields;
  }

  public DataStore<NoticeTypeFact> getNoticeTypes() {
    return noticeTypes;
  }

  public Set<ValidationResult> getResults() {
    return getResults(null);
  }

  public Set<ValidationResult> getResults(EnumSet<ValidationStatusEnum> statuses) {
    if (CollectionUtils.isEmpty(statuses)) {
      return results;
    }

    return results.stream()
        .filter((ValidationResult result) -> statuses.contains(result.getStatus()))
        .collect(Collectors.toSet());
  }

  public String[] getWarnings() {
    return getResults(EnumSet.of(ValidationStatusEnum.WARNING)).stream()
        .map(ValidationResult::toString).toArray(String[]::new);
  }

  public String[] getErrors() {
    return getResults(EnumSet.of(ValidationStatusEnum.ERROR)).stream()
        .map(ValidationResult::toString).toArray(String[]::new);
  }

  public boolean hasWarnings() {
    return ArrayUtils.isNotEmpty(getWarnings());
  }

  public boolean hasErrors() {
    return ArrayUtils.isNotEmpty(getErrors());
  }
}
