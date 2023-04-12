package eu.europa.ted.eforms.sdk.analysis.drools;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drools.ruleunits.api.DataStore;
import org.kie.api.definition.rule.Rule;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.DocumentTypeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NodeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypesIndexFact;
import eu.europa.ted.eforms.sdk.analysis.fact.SdkProjectFact;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplateFact;
import eu.europa.ted.eforms.sdk.analysis.fact.XmlNoticeFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkUnit implements RuleUnit {
  private Path sdkRoot;

  private DataStore<FieldFact> fields;
  private DataStore<NodeFact> nodes;
  private DataStore<NoticeTypeFact> noticeTypes;
  private DataStore<NoticeTypesIndexFact> noticeTypesIndex;
  private DataStore<LabelFact> labels;
  private DataStore<ViewTemplateFact> viewTemplates;
  private DataStore<DocumentTypeFact> documentTypes;
  private DataStore<XmlNoticeFact> xmlNotices;
  private DataStore<SdkProjectFact> sdkProject;

  // Global variable to store validations results
  private final Set<ValidationResult> results = new HashSet<>();

  private final List<Rule> firedRules = new ArrayList<>();

  public SdkUnit() {}

  public Path getSdkRoot() {
    return sdkRoot;
  }

  public SdkUnit setSdkRoot(Path sdkRoot) {
    this.sdkRoot = sdkRoot;
    return this;
  }

  public SdkUnit setFields(DataStore<FieldFact> fields) {
    this.fields = fields;
    return this;
  }

  public DataStore<FieldFact> getFields() {
    return fields;
  }

  public SdkUnit setNodes(DataStore<NodeFact> nodes) {
    this.nodes = nodes;
    return this;
  }

  public DataStore<NodeFact> getNodes() {
    return nodes;
  }

  public SdkUnit setNoticeTypes(DataStore<NoticeTypeFact> noticeTypes) {
    this.noticeTypes = noticeTypes;
    return this;
  }

  public DataStore<NoticeTypeFact> getNoticeTypes() {
    return noticeTypes;
  }

  public SdkUnit setNoticeTypesIndex(DataStore<NoticeTypesIndexFact> noticeTypesIndex) {
    this.noticeTypesIndex = noticeTypesIndex;
    return this;
  }

  public DataStore<NoticeTypesIndexFact> getNoticeTypesIndex() {
    return noticeTypesIndex;
  }

  public SdkUnit setLabels(DataStore<LabelFact> labels) {
    this.labels = labels;
    return this;
  }

  public DataStore<LabelFact> getLabels() {
    return labels;
  }

  public SdkUnit setViewTemplates(DataStore<ViewTemplateFact> viewTemplates) {
    this.viewTemplates = viewTemplates;
    return this;
  }

  public DataStore<ViewTemplateFact> getViewTemplates() {
    return viewTemplates;
  }

  public DataStore<DocumentTypeFact> getDocumentTypes() {
    return documentTypes;
  }

  public SdkUnit setDocumentTypes(DataStore<DocumentTypeFact> documentTypes) {
    this.documentTypes = documentTypes;
    return this;
  }

  public DataStore<XmlNoticeFact> getXmlNotices() {
    return xmlNotices;
  }

  public SdkUnit setXmlNotices(DataStore<XmlNoticeFact> xmlExamples) {
    this.xmlNotices = xmlExamples;
    return this;
  }

  public DataStore<SdkProjectFact> getSdkProject() {
    return sdkProject;
  }

  public SdkUnit setSdkProject(DataStore<SdkProjectFact> sdkProject) {
    this.sdkProject = sdkProject;
    return this;
  }

  public Set<ValidationResult> getResults() {
    return getResults(null);
  }

  public Set<ValidationResult> getResults(EnumSet<ValidationStatusEnum> statuses) {
    if (CollectionUtils.isEmpty(statuses)) {
      return results;
    }

    return results.stream()
        .filter((ValidationResult result) -> statuses.contains(result.getStatus()))
        .collect(Collectors.toSet());
  }

  public String[] getWarnings() {
    return getResults(EnumSet.of(ValidationStatusEnum.WARNING)).stream()
        .map(ValidationResult::toString).toArray(String[]::new);
  }

  public String[] getErrors() {
    return getResults(EnumSet.of(ValidationStatusEnum.ERROR)).stream()
        .map(ValidationResult::toString).toArray(String[]::new);
  }

  public boolean hasWarnings() {
    return ArrayUtils.isNotEmpty(getWarnings());
  }

  public boolean hasErrors() {
    return ArrayUtils.isNotEmpty(getErrors());
  }

  @Override
  public List<Rule> getFiredRules() {
    return firedRules;
  }

  @Override
  public SdkUnit addFiredRule(Rule rule) {
    firedRules.add(rule);
    return this;
  }
}
