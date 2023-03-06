package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.drools.ruleunits.api.RuleUnitData;
import org.kie.api.definition.rule.Rule;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public interface RuleUnit extends RuleUnitData {
  Set<ValidationResult> getResults();

  Set<ValidationResult> getResults(EnumSet<ValidationStatusEnum> statuses);

  String[] getWarnings();

  String[] getErrors();

  public boolean hasWarnings();

  boolean hasErrors();

  List<Rule> getFiredRules();

  RuleUnit addFiredRule(Rule rule);
}
