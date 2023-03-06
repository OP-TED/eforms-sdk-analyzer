package eu.europa.ted.eforms.sdk.analysis.drools;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.drools.ruleunits.api.conf.RuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesRunner {
  private static final Logger logger = LoggerFactory.getLogger(RulesRunner.class);

  private RulesRunner() {}

  public static <T extends RuleUnit> T execute(final T unit) {
    RuleConfig rc = RuleUnitProvider.get().newRuleConfig();

    try (RuleUnitInstance<T> instance =
        RuleUnitProvider.get().createRuleUnitInstance(unit, rc)) {
      logger.info("Run query. Rules are also fired");

      instance.fire();

      return instance.ruleUnitData();
    }
  }
}
