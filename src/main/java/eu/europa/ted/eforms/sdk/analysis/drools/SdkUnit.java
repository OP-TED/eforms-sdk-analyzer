package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drools.ruleunits.api.DataStore;
import org.kie.api.definition.rule.Rule;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypesIndexFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkUnit implements RuleUnit {
  private DataStore<FieldFact> fields;
  private DataStore<NoticeTypeFact> noticeTypes;
  private DataStore<NoticeTypesIndexFact> noticeTypesIndex;

  // Global variable to store validations results
  private final Set<ValidationResult> results = new HashSet<>();

  private final List<Rule> firedRules = new ArrayList<>();

  public SdkUnit() {}

  public SdkUnit setFields(DataStore<FieldFact> fields) {
    this.fields = fields;
    return this;
  }

  public DataStore<FieldFact> getFields() {
    return fields;
  }

  public SdkUnit setNoticeTypes(DataStore<NoticeTypeFact> noticeTypes) {
    this.noticeTypes = noticeTypes;
    return this;
  }

  public DataStore<NoticeTypeFact> getNoticeTypes() {
    return noticeTypes;
  }

  public SdkUnit setNoticeTypesIndex(DataStore<NoticeTypesIndexFact> noticeTypesIndex) {
    this.noticeTypesIndex = noticeTypesIndex;
    return this;
  }

  public DataStore<NoticeTypesIndexFact> getNoticeTypesIndex() {
    return noticeTypesIndex;
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

  @Override
  public List<Rule> getFiredRules() {
    return firedRules;
  }

  @Override
  public SdkUnit addFiredRule(Rule rule) {
    firedRules.add(rule);
    return this;
  }
}
