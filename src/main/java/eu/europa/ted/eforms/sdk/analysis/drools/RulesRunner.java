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
import eu.europa.ted.eforms.sdk.analysis.ValidationProfile;

public class RulesRunner {
  private static final Logger logger = LoggerFactory.getLogger(RulesRunner.class);

  private RulesRunner() {}

  /**
   * Fires rules on the given unit. The optional {@code profile} controls the
   * rule applicability filter: when non-null, only rules whose DRL metadata
   * annotations match the profile (per {@link ApplicabilityFilter}) are fired.
   * When null, the applicability filter is bypassed (used by tests that
   * exercise specific rules without a built profile). The optional
   * {@code rulesFilter} narrows further by rule name.
   */
  public static <T extends RuleUnit> T execute(final T unit, final ValidationProfile profile,
      final String... rulesFilter) {
    final RuleConfig rc = RuleUnitProvider.get().newRuleConfig();

    rc.getRuleEventListeners().add(new RuleEventListener() {
      @Override
      public void onBeforeMatchFire(final Match match) {
        RuleEventListener.super.onBeforeMatchFire(match);
        // Stamp the unit with the rule about to fire so the result sink can pair every result this
        // rule's consequence adds with the rule's name (its "kind").
        unit.setCurrentRule(match.getRule());
      }

      @Override
      public void onAfterMatchFire(final Match match) {
        RuleEventListener.super.onAfterMatchFire(match);
        unit.addFiredRule(match.getRule());
      }
    });

    try (RuleUnitInstance<T> instance = RuleUnitProvider.get().createRuleUnitInstance(unit, rc)) {
      logger.info("Run query. Rules are also fired");

      if (profile == null) {
        logger.warn("Rule applicability filter bypassed: no ValidationProfile supplied"
            + " (only happens in test code that fires rules without going through"
            + " SdkValidator.validate()). Production runs always supply a profile.");
      }

      instance.fire((Match match) -> {
        if (!ArrayUtils.isEmpty(rulesFilter)
            && !Arrays.asList(rulesFilter).contains(match.getRule().getName())) {
          return false;
        }
        if (profile != null && !ApplicabilityFilter.appliesTo(match, profile)) {
          return false;
        }
        return true;
      });

      return instance.ruleUnitData();
    }
  }
}
