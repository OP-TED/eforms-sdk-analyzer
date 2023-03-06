package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.drools.RulesRunner;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(Path sdkRoot) throws IOException {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    FactsLoader factsLoader = new FactsLoader(sdkRoot);

    logger.debug("Creating RuleUnit");
    SdkUnit sdkUnit = new SdkUnit()
        .setFields(factsLoader.loadFields())
        .setNoticeTypes(factsLoader.loadNoticeTypes());

    RulesRunner.execute(sdkUnit);

    if (sdkUnit.hasWarnings()) {
      logger.warn("Validation warnings:\n{}", StringUtils.join(sdkUnit.getWarnings(), '\n'));
    }

    if (sdkUnit.hasErrors()) {
      logger.error("Validation errors:\n{}", StringUtils.join(sdkUnit.getErrors(), '\n'));
    }

    return sdkUnit.hasErrors() ? 1 : 0;
  }
}
