package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.List;
import org.drools.ruleunits.api.RuleUnitData;
import org.kie.api.definition.rule.Rule;
import eu.europa.ted.eforms.sdk.analysis.Validator;

public interface RuleUnit extends RuleUnitData, Validator {
  List<Rule> getFiredRules();

  RuleUnit addFiredRule(Rule rule);
}
