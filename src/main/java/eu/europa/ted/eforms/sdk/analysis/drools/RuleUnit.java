package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.List;
import org.drools.ruleunits.api.RuleUnitData;
import org.kie.api.definition.rule.Rule;

public interface RuleUnit extends RuleUnitData {
  List<Rule> getFiredRules();

  RuleUnit addFiredRule(Rule rule);
}
