package eu.europa.ted.eforms.sdk.analysis.drools;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.drools.ruleunits.api.DataStore;
import org.kie.api.definition.rule.Rule;

import eu.europa.ted.eforms.sdk.analysis.fact.BusinessEntityFact;
import eu.europa.ted.eforms.sdk.analysis.fact.CodelistFact;
import eu.europa.ted.eforms.sdk.analysis.fact.CodelistsIndexFact;
import eu.europa.ted.eforms.sdk.analysis.fact.DocumentTypeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldsAndNodesMetadataFact;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NodeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypesIndexFact;
import eu.europa.ted.eforms.sdk.analysis.fact.SchematronFileFact;
import eu.europa.ted.eforms.sdk.analysis.fact.SvrlReportFact;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplateFact;
import eu.europa.ted.eforms.sdk.analysis.fact.XmlNoticeFact;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class SdkUnit implements RuleUnit {
  private Path sdkRoot;

  private DataStore<CodelistFact> codelists;
  private DataStore<CodelistsIndexFact> codelistsIndex;
  private DataStore<DocumentTypeFact> documentTypes;
  private DataStore<XmlNoticeFact> xmlNotices;
  private DataStore<FieldsAndNodesMetadataFact> fieldsAndNodesMetadata;
  private DataStore<FieldFact> fields;
  private DataStore<BusinessEntityFact> businessEntities;
  private DataStore<LabelFact> labels;
  private DataStore<NodeFact> nodes;
  private DataStore<NoticeTypesIndexFact> noticeTypesIndex;
  private DataStore<NoticeTypeFact> noticeTypes;
  private DataStore<ViewTemplateFact> viewTemplates;
  private DataStore<SvrlReportFact> svrlReports;
  private DataStore<SchematronFileFact> schematrons;

  private SdkMetadata sdkMetadata;

  // Global variable to store validations results
  private final Set<ValidationResult> results = new HashSet<>();

  private final List<Rule> firedRules = new ArrayList<>();

  public SdkUnit() {
    // Default constructor
  }

  public Path getSdkRoot() {
    return sdkRoot;
  }

  public SdkUnit setSdkRoot(Path sdkRoot) {
    this.sdkRoot = sdkRoot;
    return this;
  }

  public DataStore<CodelistFact> getCodelists() {
    return codelists;
  }

  public SdkUnit setCodelists(DataStore<CodelistFact> codelists) {
    this.codelists = codelists;
    return this;
  }

  public DataStore<CodelistsIndexFact> getCodelistsIndex() {
    return codelistsIndex;
  }

  public SdkUnit setCodelistsIndex(DataStore<CodelistsIndexFact> codelistsIndex) {
    this.codelistsIndex = codelistsIndex;
    return this;
  }

  public DataStore<DocumentTypeFact> getDocumentTypes() {
    return documentTypes;
  }

  public SdkUnit setDocumentTypes(DataStore<DocumentTypeFact> documentTypes) {
    this.documentTypes = documentTypes;
    return this;
  }

  public DataStore<FieldsAndNodesMetadataFact> getFieldsAndNodesMetadata() {
    return fieldsAndNodesMetadata;
  }

  public SdkUnit setFieldsAndNodesMetadata(DataStore<FieldsAndNodesMetadataFact> metadata) {
    this.fieldsAndNodesMetadata = metadata;
    return this;
  }

  public DataStore<FieldFact> getFields() {
    return fields;
  }

  public SdkUnit setFields(DataStore<FieldFact> fields) {
    this.fields = fields;
    return this;
  }

  public DataStore<BusinessEntityFact> getBusinessEntities() {
    return businessEntities;
  }

  public SdkUnit setBusinessEntities(DataStore<BusinessEntityFact> businessEntities) {
    this.businessEntities = businessEntities;
    return this;
  }

  public DataStore<LabelFact> getLabels() {
    return labels;
  }

  public SdkUnit setLabels(DataStore<LabelFact> labels) {
    this.labels = labels;
    return this;
  }

  public DataStore<NodeFact> getNodes() {
    return nodes;
  }

  public SdkUnit setNodes(DataStore<NodeFact> nodes) {
    this.nodes = nodes;
    return this;
  }

  public DataStore<NoticeTypesIndexFact> getNoticeTypesIndex() {
    return noticeTypesIndex;
  }

  public SdkUnit setNoticeTypesIndex(DataStore<NoticeTypesIndexFact> noticeTypesIndex) {
    this.noticeTypesIndex = noticeTypesIndex;
    return this;
  }

  public DataStore<NoticeTypeFact> getNoticeTypes() {
    return noticeTypes;
  }

  public SdkUnit setNoticeTypes(DataStore<NoticeTypeFact> noticeTypes) {
    this.noticeTypes = noticeTypes;
    return this;
  }

  public DataStore<ViewTemplateFact> getViewTemplates() {
    return viewTemplates;
  }

  public SdkUnit setViewTemplates(DataStore<ViewTemplateFact> viewTemplates) {
    this.viewTemplates = viewTemplates;
    return this;
  }

  public DataStore<XmlNoticeFact> getXmlNotices() {
    return xmlNotices;
  }

  public SdkUnit setXmlNotices(DataStore<XmlNoticeFact> xmlExamples) {
    this.xmlNotices = xmlExamples;
    return this;
  }

  public DataStore<SvrlReportFact> getSvrlReports() {
    return svrlReports;
  }

  public SdkUnit setSvrlReports(DataStore<SvrlReportFact> svrlReports) {
    this.svrlReports = svrlReports;
    return this;
  }

  public DataStore<SchematronFileFact> getSchematrons() {
    return schematrons;
  }

  public SdkUnit setSchematrons(DataStore<SchematronFileFact> schematrons) {
    this.schematrons = schematrons;
    return this;
  }

  public SdkMetadata getSdkMetadata() {
    return sdkMetadata;
  }

  public SdkUnit setSdkMetadata(SdkMetadata sdkMetadata) {
    this.sdkMetadata = sdkMetadata;
    return this;
  }

  public Set<ValidationResult> getResults() {
    return results;
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
