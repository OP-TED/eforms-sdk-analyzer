package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import jakarta.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import eu.europa.ted.eforms.sdk.analysis.domain.SvrlReport;
import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.field.BusinessEntity;
import eu.europa.ted.eforms.sdk.analysis.domain.field.Field;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplatesIndex;
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
import eu.europa.ted.eforms.sdk.analysis.fact.TranslationsIndexFact;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplateFact;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplatesIndexFact;
import eu.europa.ted.eforms.sdk.analysis.fact.XmlNoticeFact;

public class FactsLoader {
  private static final Logger logger = LoggerFactory.getLogger(FactsLoader.class);

  private SdkLoader sdkLoader;

  public FactsLoader() {
    this(null);
  }

  public FactsLoader(Path sdkRoot) {
    sdkLoader = new SdkLoader(sdkRoot);
  }

  public DataStore<FieldsAndNodesMetadataFact> loadFieldsAndNodesMetadata() throws IOException {
    logger.debug("Creating facts datastore for metadata in fields.json");

    final DataStore<FieldsAndNodesMetadataFact> datastore = DataSource.createStore();

    datastore.add(new FieldsAndNodesMetadataFact(sdkLoader.getFieldsAndNodesMetadata()));

    return datastore;
  }

  public DataStore<FieldFact> loadFields() throws IOException {
    logger.debug("Creating facts datastore for fields");

    final DataStore<FieldFact> datastore = DataSource.createStore();

    sdkLoader.getFieldsAndNodes().getFields()
        .forEach((Field field) -> datastore.add(new FieldFact(field)));

    return datastore;
  }

  public DataStore<BusinessEntityFact> loadBusinessEntities() throws IOException {
    logger.debug("Creating facts datastore for business entities");

    final DataStore<BusinessEntityFact> datastore = DataSource.createStore();

    sdkLoader.getFieldsAndNodes().getBusinessEntities()
        .forEach((BusinessEntity entity) -> datastore.add(new BusinessEntityFact(entity)));

    return datastore;
  }

  public DataStore<NodeFact> loadNodes() throws IOException {
    logger.debug("Creating facts datastore for nodes");

    final DataStore<NodeFact> datastore = DataSource.createStore();
    List<XmlStructureNode> nodes = sdkLoader.getFieldsAndNodes().getNodes();

    nodes.forEach((XmlStructureNode node) -> datastore.add(new NodeFact(node)));

    return datastore;
  }

  public DataStore<NoticeTypeFact> loadNoticeTypes() throws IOException {
    logger.debug("Creating facts datastore for notice types");

    final DataStore<NoticeTypeFact> datastore = DataSource.createStore();
    sdkLoader.getNoticeTypes()
        .forEach((NoticeType noticeType) -> datastore.add(new NoticeTypeFact(noticeType)));

    return datastore;
  }

  public DataStore<NoticeTypesIndexFact> loadNoticeTypesIndex() throws IOException {
    logger.debug("Creating facts datastore for notice types index");

    final DataStore<NoticeTypesIndexFact> datastore = DataSource.createStore();
    datastore.add(new NoticeTypesIndexFact(sdkLoader.getNoticeTypesForIndex()));

    return datastore;
  }

  public DataStore<TranslationsIndexFact> loadTranslationsIndex() throws IOException {
    logger.debug("Creating facts datastore for translations index");

    final DataStore<TranslationsIndexFact> datastore = DataSource.createStore();
    datastore.add(new TranslationsIndexFact(sdkLoader.getTranslationsIndex()));

    return datastore;
  }

  public DataStore<LabelFact> loadLabels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    logger.debug("Creating facts datastore for labels");

    final DataStore<LabelFact> datastore = DataSource.createStore();

    sdkLoader.getLabels().forEach((Label label) -> datastore.add(new LabelFact(label)));

    return datastore;
  }

  public DataStore<ViewTemplateFact> loadViewTemplates() throws IOException {
    logger.debug("Creating facts datastore for view templates");

    TedefoViewTemplatesIndex viewTemplatesIndex = sdkLoader.getViewTemplatesIndex();

    final DataStore<ViewTemplateFact> datastore = DataSource.createStore();
    viewTemplatesIndex.getViewTemplates()
        .forEach((TedefoViewTemplateIndex viewTemplate) -> datastore
            .add(new ViewTemplateFact(viewTemplate)));

    return datastore;
  }

  public DataStore<ViewTemplatesIndexFact> loadViewTemplatesIndex() throws IOException {
    logger.debug("Creating facts datastore for view templates index");

    TedefoViewTemplatesIndex viewTemplatesIndex = sdkLoader.getViewTemplatesIndex();

    final DataStore<ViewTemplatesIndexFact> datastore = DataSource.createStore();
    datastore.add(new ViewTemplatesIndexFact(viewTemplatesIndex));

    return datastore;
  }

  public DataStore<DocumentTypeFact> loadDocumentTypes() throws IOException {
    logger.debug("Creating facts datastore for document types");

    final DataStore<DocumentTypeFact> datastore = DataSource.createStore();
    sdkLoader.getNoticeTypesForIndex().getDocumentTypes()
        .forEach((DocumentType documentType) -> datastore.add(new DocumentTypeFact(documentType)));

    return datastore;
  }

  public DataStore<CodelistFact> loadCodelists()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    logger.debug("Creating facts datastore for codelists");

    final DataStore<CodelistFact> datastore = DataSource.createStore();
    sdkLoader.getCodelists()
        .forEach((Codelist codelist) -> datastore.add(new CodelistFact(codelist)));

    return datastore;
  }

  public DataStore<CodelistsIndexFact> loadCodelistsIndex() throws IOException {
    logger.debug("Creating facts datastore for codelists index");

    final DataStore<CodelistsIndexFact> datastore = DataSource.createStore();
    datastore.add(new CodelistsIndexFact(sdkLoader.getCodelistsIndex()));

    return datastore;
  }

  public DataStore<XmlNoticeFact> loadXmlNotices()
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    logger.debug("Creating facts datastore for XML notice examples");

    final DataStore<XmlNoticeFact> datastore = DataSource.createStore();
    sdkLoader.getXmlNotices()
        .forEach((XmlNotice xmlNotice) -> datastore.add(new XmlNoticeFact(xmlNotice)));

    return datastore;
  }

  public DataStore<SvrlReportFact> loadSvrlReports()
      throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
    logger.debug("Creating facts datastore for SVRL reports examples");

    final DataStore<SvrlReportFact> datastore = DataSource.createStore();
    sdkLoader.getSvrlReports()
        .forEach((SvrlReport svrlReport) -> datastore.add(new SvrlReportFact(svrlReport)));

    return datastore;
  }

  public DataStore<SchematronFileFact> loadSchematrons() {
    logger.debug("Creating facts datastore for Schematron files");

    final DataStore<SchematronFileFact> datastore = DataSource.createStore();
    sdkLoader.getSchematronFiles().forEach(f -> datastore.add(new SchematronFileFact(f)));

    return datastore;
  }
}
