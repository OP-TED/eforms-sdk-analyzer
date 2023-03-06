package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.drools.ruleunits.api.conf.RuleConfig;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.event.rule.RuleEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesRunner {
  private static final Logger logger = LoggerFactory.getLogger(RulesRunner.class);

  private RulesRunner() {}

  public static <T extends RuleUnit> T execute(final T unit, String... rulesFilter) {
    RuleConfig rc = RuleUnitProvider.get().newRuleConfig();

    rc.getRuleEventListeners().add(new RuleEventListener() {
      @Override
      public void onAfterMatchFire(Match match) {
        RuleEventListener.super.onAfterMatchFire(match);
        unit.addFiredRule(match.getRule());
      }
    });

    try (RuleUnitInstance<T> instance =
        RuleUnitProvider.get().createRuleUnitInstance(unit, rc)) {
      logger.info("Run query. Rules are also fired");

      instance.fire((Match match) -> ArrayUtils.isEmpty(rulesFilter)
          || Arrays.asList(rulesFilter).contains(match.getRule().getName()));

      return instance.ruleUnitData();
    }
  }
}
