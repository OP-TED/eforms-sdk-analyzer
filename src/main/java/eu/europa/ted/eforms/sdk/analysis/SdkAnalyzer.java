package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import eu.europa.ted.eforms.sdk.analysis.drools.RulesRunner;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import eu.europa.ted.eforms.sdk.util.SdkMetadataParser;

public class SdkAnalyzer {
  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzer.class);

  private SdkAnalyzer() {}

  public static int analyze(final Path sdkRoot) throws XPathExpressionException, IOException,
      JAXBException, SAXException, ParserConfigurationException {
    logger.info("Analyzing SDK under folder [{}]", sdkRoot);

    final SdkMetadata sdkMetadata = SdkMetadataParser.loadSdkMetadata(sdkRoot);

    final Validator templatesValidator = analyzeTemplates(sdkRoot, sdkMetadata.getVersion());

    final Validator sdkValidator = analyzeSdk(sdkRoot, sdkMetadata);

    final String[] warnings =
        concatArrays(templatesValidator.getWarnings(), sdkValidator.getWarnings());
    final String[] errors = concatArrays(templatesValidator.getErrors(), sdkValidator.getErrors());

    if (ArrayUtils.isNotEmpty(warnings) && logger.isWarnEnabled()) {
      logger.warn("Validation warnings:\n{}", StringUtils.join(errors, '\n'));
    }

    if (ArrayUtils.isNotEmpty(errors) && logger.isErrorEnabled()) {
      logger.error("Validation errors:\n{}", StringUtils.join(errors, '\n'));
    }

    return ArrayUtils.isNotEmpty(errors) ? 1 : 0;
  }

  private static String[] concatArrays(String[]... arrays) {
    return Stream.of(arrays)
        .flatMap(Stream::of)
        .toArray(String[]::new);
  }

  private static Validator analyzeTemplates(final Path sdkRoot, final String sdkVersion)
      throws IOException {
    return new EfxValidator(sdkRoot, sdkVersion).validateTemplates();
  }

  private static Validator analyzeSdk(final Path sdkRoot, SdkMetadata sdkMetadata)
      throws XPathExpressionException,
      IOException, JAXBException, SAXException, ParserConfigurationException {
    final FactsLoader factsLoader = new FactsLoader(sdkRoot);

    logger.debug("Creating RuleUnit");
    SdkUnit sdkUnit = new SdkUnit()
        .setSdkRoot(sdkRoot)
        .setSdkMetadata(sdkMetadata)
        .setDocumentTypes(factsLoader.loadDocumentTypes())
        .setFieldsAndNodesMetadata(factsLoader.loadFieldsAndNodesMetadata())
        .setFields(factsLoader.loadFields())
        .setNodes(factsLoader.loadNodes())
        .setNoticeTypes(factsLoader.loadNoticeTypes())
        .setNoticeTypesIndex(factsLoader.loadNoticeTypesIndex())
        .setLabels(factsLoader.loadLabels())
        .setViewTemplates(factsLoader.loadViewTemplates())
        .setXmlNotices(factsLoader.loadXmlNotices())
        .setSvrlReports(factsLoader.loadSvrlReports());

    fireAllRules(sdkUnit);
    return sdkUnit;
  }

  public static SdkUnit fireAllRules(final SdkUnit sdkUnit) {
    return fireRules(sdkUnit);
  }

  public static SdkUnit fireRules(final SdkUnit sdkUnit, final String... rules) {
    RulesRunner.execute(sdkUnit, rules);

    return sdkUnit;
  }
}
