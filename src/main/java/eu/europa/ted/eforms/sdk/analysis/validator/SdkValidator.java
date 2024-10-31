package eu.europa.ted.eforms.sdk.analysis.validator;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.kie.api.definition.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.FactsLoader;
import eu.europa.ted.eforms.sdk.analysis.drools.RulesRunner;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;
import eu.europa.ted.eforms.sdk.analysis.util.SdkMetadataParser;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates the content of the SDK with a set of rules for the Drools engine.
 */
public class SdkValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(SdkValidator.class);

  private final Path sdkRoot;

  private SdkUnit sdkUnit;

  public SdkValidator(final Path sdkRoot) {
    this.sdkRoot = sdkRoot;
    this.sdkUnit = new SdkUnit().setSdkRoot(sdkRoot);
  }

  /**
   * For the unit tests, to have finer grained control on the facts loaded.
   */
  public SdkUnit getSdkUnit() {
    return sdkUnit;
  }

  public List<String> getFiredRulesNames() {
    return sdkUnit.getFiredRules().stream().map(Rule::getName).collect(Collectors.toList());
  }

  @Override
  public Validator validate() throws Exception {
    final FactsLoader factsLoader = new FactsLoader(sdkRoot);
    
    final SdkMetadata sdkMetadata = SdkMetadataParser.loadSdkMetadata(sdkRoot);

    logger.debug("Loading all facts in RuleUnit");
    sdkUnit.setSdkMetadata(sdkMetadata)
        .setCodelists(factsLoader.loadCodelists())
        .setCodelistsIndex(factsLoader.loadCodelistsIndex())
        .setDocumentTypes(factsLoader.loadDocumentTypes())
        .setFieldsAndNodesMetadata(factsLoader.loadFieldsAndNodesMetadata())
        .setFields(factsLoader.loadFields())
        .setBusinessEntities(factsLoader.loadBusinessEntities())
        .setNodes(factsLoader.loadNodes())
        .setNoticeTypes(factsLoader.loadNoticeTypes())
        .setNoticeTypesIndex(factsLoader.loadNoticeTypesIndex())
        .setLabels(factsLoader.loadLabels())
        .setViewTemplates(factsLoader.loadViewTemplates())
        .setViewTemplatesIndex(factsLoader.loadViewTemplatesIndex())
        .setXmlNotices(factsLoader.loadXmlNotices())
        .setSvrlReports(factsLoader.loadSvrlReports())
        .setSchematrons(factsLoader.loadSchematrons());

    fireAllRules();
    return this;
  }

  @Override
  public Set<ValidationResult> getResults() {
    return sdkUnit.getResults();
  }

  public SdkUnit fireAllRules() {
    return fireRules();
  }

  public SdkUnit fireRules(final String... rules) {
    RulesRunner.execute(sdkUnit, rules);

    return sdkUnit;
  }

}
