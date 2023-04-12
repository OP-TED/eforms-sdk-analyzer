package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import eu.europa.ted.eforms.sdk.analysis.drools.RulesRunner;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(Path sdkRoot)
      throws IOException, JAXBException, SAXException, ParserConfigurationException, XPathExpressionException {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    FactsLoader factsLoader = new FactsLoader(sdkRoot);

    logger.debug("Creating RuleUnit");
    SdkUnit sdkUnit = new SdkUnit()
        .setSdkRoot(sdkRoot)
        .setDocumentTypes(factsLoader.loadDocumentTypes())
        .setFields(factsLoader.loadFields())
        .setNodes(factsLoader.loadNodes())
        .setNoticeTypes(factsLoader.loadNoticeTypes())
        .setNoticeTypesIndex(factsLoader.loadNoticeTypesIndex())
        .setLabels(factsLoader.loadLabels())
        .setViewTemplates(factsLoader.loadViewTemplates())
        .setXmlNotices(factsLoader.loadXmlNotices())
        .setSdkProject(factsLoader.loadSdkProject());

    fireAllRules(sdkUnit);

    if (sdkUnit.hasWarnings()) {
      logger.warn("Validation warnings:\n{}", StringUtils.join(sdkUnit.getWarnings(), '\n'));
    }

    if (sdkUnit.hasErrors()) {
      logger.error("Validation errors:\n{}", StringUtils.join(sdkUnit.getErrors(), '\n'));
    }

    return sdkUnit.hasErrors() ? 1 : 0;
  }

  public static SdkUnit fireAllRules(SdkUnit sdkUnit) {
    return fireRules(sdkUnit);
  }

  public static SdkUnit fireRules(SdkUnit sdkUnit, String... rules) {
    RulesRunner.execute(sdkUnit, rules);

    return sdkUnit;
  }
}
