package eu.europa.ted.eforms.sdk.analysis.validator;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.vdurmont.semver4j.Semver;
import org.kie.api.definition.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.FactsLoader;
import eu.europa.ted.eforms.sdk.analysis.SdkContentSource;
import eu.europa.ted.eforms.sdk.analysis.SdkLoader;
import eu.europa.ted.eforms.sdk.analysis.ValidationProfile;
import eu.europa.ted.eforms.sdk.analysis.drools.RulesRunner;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;
import eu.europa.ted.eforms.sdk.analysis.util.SdkMetadataParser;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates the content of the SDK with a set of rules for the Drools engine.
 */
public class SdkValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(SdkValidator.class);

  private final SdkContentSource source;
  private final Callable<SdkMetadata> metadataLoader;
  private final SdkUnit sdkUnit;

  /**
   * Built lazily inside {@link #validate()} from {@code metadataLoader} and
   * {@code source.getSourceKind()}. While {@code null}, calls to
   * {@link #fireRules(String...)} bypass the rule applicability filter
   * (preserves the legacy behaviour exercised by tests that fire rules
   * without going through {@link #validate()}).
   *
   * <p>The contract — production runs always go through {@link #validate()},
   * so a profile is always built before any rule fires — is reinforced by a
   * {@code WARN} log emitted from {@code RulesRunner.execute} whenever it is
   * invoked with no profile. The Java type system does not enforce it because
   * the {@link #fireRules(String...)} test entry point is intentionally
   * profile-less (see its Javadoc).
   */
  private ValidationProfile profile;

  public SdkValidator(final Path sdkRoot) {
    this.source = new SdkLoader(sdkRoot);
    this.metadataLoader = () -> SdkMetadataParser.loadSdkMetadata(sdkRoot);
    this.sdkUnit = new SdkUnit().setSdkRoot(sdkRoot);
  }

  public SdkValidator(final SdkContentSource source, final SdkMetadata sdkMetadata) {
    this.source = source;
    this.metadataLoader = () -> sdkMetadata;
    this.sdkUnit = new SdkUnit();
  }

  /**
   * For the unit tests, to have finer grained control on the facts loaded.
   */
  public SdkUnit getSdkUnit() {
    return this.sdkUnit;
  }

  public List<String> getFiredRulesNames() {
    return this.sdkUnit.getFiredRules().stream().map(Rule::getName).collect(Collectors.toList());
  }

  @Override
  public Validator validate() throws Exception {
    final FactsLoader factsLoader = new FactsLoader(this.source);

    final SdkMetadata sdkMetadata = this.metadataLoader.call();
    this.profile = new ValidationProfile(
        new Semver(sdkMetadata.getVersion()).getMajor(),
        this.source.getSourceKind());

    logger.debug("Loading all facts in RuleUnit");
    this.sdkUnit.setSdkMetadata(sdkMetadata)
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
    return this.sdkUnit.getResults();
  }

  /** Findings of this run, each paired with the name of the rule (its kind) that produced it. */
  @Override
  public List<Finding> getFindings() {
    return this.sdkUnit.getFindings();
  }

  public SdkUnit fireAllRules() {
    return fireRules();
  }

  /**
   * Fires the rules whose name matches one of {@code rules} (or all rules when
   * {@code rules} is empty), filtered by the active {@link ValidationProfile}
   * if one has been built.
   *
   * <p><b>Test mode caveat:</b> when callers invoke this without first calling
   * {@link #validate()}, the {@code profile} field is {@code null} and the
   * rule applicability filter is bypassed entirely. That is intentional for
   * legacy step-by-step Cucumber tests (see {@code SdkValidationSteps}) but
   * means a test that fires a rule directly through this method may exercise
   * a code path that production runs would never see — the rule will fire
   * regardless of any {@code @sdkMajor} / {@code @source} annotations.
   * {@link RulesRunner#execute} emits a {@code WARN} log line whenever the
   * filter is bypassed for this reason.</p>
   */
  public SdkUnit fireRules(final String... rules) {
    RulesRunner.execute(this.sdkUnit, this.profile, rules);

    return this.sdkUnit;
  }

}
