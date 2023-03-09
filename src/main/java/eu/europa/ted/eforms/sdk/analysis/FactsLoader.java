package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypeFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NoticeTypesIndexFact;
import eu.europa.ted.eforms.sdk.domain.Label;
import eu.europa.ted.eforms.sdk.domain.field.Field;
import eu.europa.ted.eforms.sdk.domain.field.FieldsAndNodes;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypesForIndex;

public class FactsLoader {
  private static final Logger logger = LoggerFactory.getLogger(FactsLoader.class);

  private SdkLoader sdkLoader;

  public FactsLoader() {
    this(null);
  }

  public FactsLoader(Path sdkRoot) {
    sdkLoader = new SdkLoader(sdkRoot);
  }

  public DataStore<FieldFact> loadFields() throws IOException {
    logger.debug("Creating facts datastore for fields");

    FieldsAndNodes fieldsAndNodes = sdkLoader.getFieldsAndNodes();

    final DataStore<FieldFact> datastore = DataSource.createStore();

    fieldsAndNodes.getFields()
        .forEach((Field field) -> datastore.add(new FieldFact(field)));

    return datastore;
  }

  public DataStore<NoticeTypeFact> loadNoticeTypes() throws IOException {
    logger.debug("Creating facts datastore for notice types");

    Set<NoticeType> noticeTypes = sdkLoader.getNoticeTypes();

    final DataStore<NoticeTypeFact> datastore = DataSource.createStore();

    noticeTypes.forEach(
        (NoticeType noticeType) -> datastore.add(new NoticeTypeFact(noticeType)));

    return datastore;
  }

  public DataStore<NoticeTypesIndexFact> loadNoticeTypesIndex() throws IOException {
    logger.debug("Creating facts datastore for notice types index");

    NoticeTypesForIndex noticeTypesIndex = sdkLoader.getNoticeTypesForIndex();

    final DataStore<NoticeTypesIndexFact> datastore = DataSource.createStore();

    datastore.add(new NoticeTypesIndexFact(noticeTypesIndex));

    return datastore;
  }

  public DataStore<LabelFact> loadLabels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    logger.debug("Creating facts datastore for labels");

    Set<Label> labels = sdkLoader.getLabels();

    final DataStore<LabelFact> datastore = DataSource.createStore();

    labels.forEach((Label label) -> datastore.add(new LabelFact(label)));

    return datastore;
  }
}
