package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  public static int analyze(Path sdkRoot)
      throws IOException, InstantiationException, ClassNotFoundException {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    FactsLoader factsLoader = new FactsLoader(sdkRoot);

    logger.debug("Creating RuleUnit");
    SdkUnit sdkUnit = new SdkUnit(factsLoader.loadFields(), factsLoader.loadNoticeTypes());

    try (RuleUnitInstance<SdkUnit> instance =
        RuleUnitProvider.get().createRuleUnitInstance(sdkUnit)) {

      logger.info("Run query. Rules are also fired");
      instance.fire();

      if (instance.ruleUnitData().hasWarnings()) {
        logger.warn("Validation warnings:\n{}",
            StringUtils.join(instance.ruleUnitData().getWarnings(), '\n'));
      }

      if (instance.ruleUnitData().hasErrors()) {
        logger.error("Validation errors:\n{}",
            StringUtils.join(instance.ruleUnitData().getErrors(), '\n'));
      }

      return instance.ruleUnitData().hasErrors() ? 1 : 0;
    }
  }
}
