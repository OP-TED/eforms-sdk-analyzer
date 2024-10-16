package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.analysis.domain.EFormsTrackableEntity;
import eu.europa.ted.eforms.sdk.analysis.domain.SvrlReport;
import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.CodelistsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;
import eu.europa.ted.eforms.sdk.analysis.domain.field.Field;
import eu.europa.ted.eforms.sdk.analysis.domain.field.FieldsAndNodes;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.domain.label.LanguageFileInfo;
import eu.europa.ted.eforms.sdk.analysis.domain.label.TranslationsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeSubTypeForIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeTypeSdk;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeTypesForIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplatesIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.CodeList;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.Identification;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.Properties;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.SimpleCodeList.Row;
import eu.europa.ted.eforms.sdk.analysis.domain.xml.SimpleCodeList.Row.Value;
import eu.europa.ted.eforms.sdk.analysis.util.SchematronParser;
import eu.europa.ted.eforms.sdk.analysis.util.XmlDataExtractor;
import eu.europa.ted.eforms.sdk.analysis.util.XmlParser;

public class SdkLoader {
  private static final Logger logger = LoggerFactory.getLogger(SdkLoader.class);

  // Not in SdkResource, as it is not useful when you use the SDK in an app
  public static final Path EXAMPLE_NOTICES = Path.of("examples", "notices");
  public static final Path EXAMPLE_REPORTS = Path.of("examples", "reports");
  public static final Path SCHEMATRONS_DYNAMIC = Path.of("schematrons", "dynamic");
  public static final Path SCHEMATRONS_STATIC = Path.of("schematrons", "static");
  
  private final Path sdkRoot;
  private final ObjectMapper objectMapper;

  public SdkLoader(Path sdkRoot) {
    Validate.notNull(sdkRoot, "Undefined SDK root folder");
    Validate.isTrue(Files.isDirectory(sdkRoot),
        MessageFormat.format("SDK root [{0}] does not exist or is not a folder", sdkRoot));

    this.sdkRoot = sdkRoot;

    objectMapper = createObjectMapper();
  }

  private ObjectMapper createObjectMapper() {
    logger.debug("Initialising object mapper");

    final ObjectMapper om = new ObjectMapper();

    om.findAndRegisterModules();

    om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    // https://fasterxml.github.io/jackson-annotations/javadoc/2.7/com/fasterxml/jackson/annotation/JsonInclude.Include.html

    // Value that indicates that only properties with non-null values are to be included.
    om.setSerializationInclusion(Include.NON_NULL);

    // Value that indicates that only properties with null value, or what is considered empty, are
    // not to be included.
    om.setSerializationInclusion(Include.NON_EMPTY);

    return om;
  }

  private <T> T loadJsonFile(Class<T> clazz, Path jsonFilePath) throws IOException {
    logger.debug("Loading contents of type [{} from JSON file [{}]", clazz.getName(), jsonFilePath);

    if (!Files.isRegularFile(jsonFilePath)) {
      throw new FileNotFoundException(jsonFilePath.toString());
    }

    logger.debug("Getting data from JSON file [{}]", jsonFilePath);

    final T result = objectMapper.readValue(jsonFilePath.toFile(), clazz);
    Validate.notNull(result,
        MessageFormat.format("No data was loaded from JSON file [{0}]", jsonFilePath));

    return result;
  }

  private XmlStructureNode findNode(String nodeId, List<XmlStructureNode> nodes) {
    if (nodes == null) {
      return null;
    }

    return nodes.stream()
        .filter((XmlStructureNode n) -> n.getId().equals(nodeId))
        .collect(Collectors.collectingAndThen(
            Collectors.toList(),
            (List<XmlStructureNode> l) -> {
              if (l.size() != 1) {
                throw new IllegalArgumentException(
                    MessageFormat.format("Could not find node with id [{0}]", nodeId));
              }

              return l.get(0);
            }));
  }

  public EFormsTrackableEntity getFieldsAndNodesMetadata() throws IOException {
    // Load fields.json but keep only the metadata, not the fields and nodes
    EFormsTrackableEntity metadata = loadJsonFile(FieldsAndNodes.class,
        Path.of(sdkRoot.toString(), SdkResource.FIELDS_JSON.getPath().toString()));

    return metadata;
  }

  public FieldsAndNodes getFieldsAndNodes() throws IOException {
    FieldsAndNodes fieldsAndNodes = loadJsonFile(FieldsAndNodes.class,
        Path.of(sdkRoot.toString(), SdkResource.FIELDS_JSON.getPath().toString()));

    fieldsAndNodes.getNodes().forEach((XmlStructureNode node) -> {
      if (StringUtils.isNotEmpty(node.getParentId())) {
        node.setParent(findNode(node.getParentId(), fieldsAndNodes.getNodes()));
      }
    });

    fieldsAndNodes.getFields().forEach((Field field) -> {
      if (StringUtils.isNotEmpty(field.getParentNodeId())) {
        field.setParentNode(findNode(field.getParentNodeId(), fieldsAndNodes.getNodes()));
      }
    });

    return fieldsAndNodes;
  }

  public Set<NoticeType> getNoticeTypes() throws IOException {
    final Set<NoticeType> result = new HashSet<>();

    final NoticeTypesForIndex noticeTypesForIndex = getNoticeTypesForIndex();

    for (NoticeSubTypeForIndex noticeSubType : noticeTypesForIndex.getNoticeSubTypes()) {
      result.add(
          new NoticeType(noticeSubType, getNoticeTypeSdk(noticeSubType.getSubTypeId(), sdkRoot)));
    }

    return result;
  }

  public NoticeTypesForIndex getNoticeTypesForIndex() throws IOException {
    return loadJsonFile(NoticeTypesForIndex.class,
        Path.of(sdkRoot.toString(), SdkResource.NOTICE_TYPES_JSON.getPath().toString()));
  }

  public NoticeTypeSdk getNoticeTypeSdk(String noticeId, Path sdkRoot) throws IOException {
    return loadJsonFile(NoticeTypeSdk.class, Path.of(sdkRoot.toString(),
        SdkResource.NOTICE_TYPES.getPath().toString(), MessageFormat.format("{0}.json", noticeId)));
  }

  public TranslationsIndex getTranslationsIndex() throws IOException {
    return loadJsonFile(TranslationsIndex.class,
        Path.of(sdkRoot.toString(), SdkResource.TRANSLATIONS_JSON.getPath().toString()));
  }

  public Set<Label> getLabels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    final Map<String, Label> labels = new HashMap<>();

    Properties translationProperties = null;

    TranslationsIndex translationsIndex = getTranslationsIndex();

    for (LanguageFileInfo info : translationsIndex.getFiles()) {
      Path path = Path.of(sdkRoot.toString(),
          SdkResource.TRANSLATIONS.getPath().toString(),
          info.getFilename());
      if (!Files.isDirectory(path)) {
        translationProperties = XmlParser.loadXmlFile(Properties.class, path);

        Language language = Language.valueOf(
            path.getFileName().toString().replaceAll("^.*?_(.*?).xml$", "$1").toUpperCase());

        translationProperties.getEntry().stream().forEach(e -> {
          Label label = labels.computeIfAbsent(e.getKey(), k -> new Label(k));
          label.addTranslation(language, e.getValue());
        });          
      }
    }

    return new HashSet<>(labels.values());
  }

  public TedefoViewTemplatesIndex getViewTemplatesIndex() throws IOException {
    return loadJsonFile(TedefoViewTemplatesIndex.class,
        Path.of(sdkRoot.toString(), SdkResource.VIEW_TEMPLATES_JSON.getPath().toString()));
  }

  public Set<TedefoViewTemplateIndex> getViewTemplates() throws IOException {
    return new HashSet<>(getViewTemplatesIndex().getViewTemplates());
  }

  public Set<Codelist> getCodelists()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    final Set<Codelist> result = new HashSet<>();

    CodeList codelistXmlPojo = null;

    DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
      public boolean accept(Path file) throws IOException {
        // Only load *.gc files
        return file.getFileName().toString().endsWith(".gc");
      }
    };

    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
        Path.of(sdkRoot.toString(), SdkResource.CODELISTS.getPath().toString()), filter)) {

      for (Path path : dirStream) {
        if (!Files.isDirectory(path)) {
          codelistXmlPojo = XmlParser.loadXmlFile(CodeList.class, path);

          final Codelist codelist = new Codelist();

          codelist.setFilename(path.getFileName().toString());

          codelist.setId(codelistXmlPojo
              .getIdentification()
              .getLongName().stream()
              .filter((Identification.LongName longName) -> longName.getIdentifier() == null)
              .findFirst()
              .get()
              .getValue());

          codelistXmlPojo
              .getIdentification()
              .getLongName().stream()
              .filter(longName -> "eFormsParentId".equals(longName.getIdentifier()))
              .findFirst()
              .ifPresent(l -> codelist.setParentId(l.getValue()));

          codelist.setCodes(codelistXmlPojo
              .getSimpleCodeList()
              .getRow().stream()
              .map((Row row) -> row.getValue().stream()
                  .filter((Value rowValue) -> rowValue.getColumnRef().equals("code"))
                  .map(Value::getSimpleValue).findFirst()
                  .get())
              .collect(Collectors.toList()));

          codelist.setColumnDefinitions(codelistXmlPojo.getColumnSet().getColumn());

          codelist.setRows(codelistXmlPojo.getSimpleCodeList().getRow());

          result.add(codelist);
        }
      }
    }

    return result;
  }

  public CodelistsIndex getCodelistsIndex() throws IOException {
    return loadJsonFile(CodelistsIndex.class,
        Path.of(sdkRoot.toString(), SdkResource.CODELISTS_JSON.getPath().toString()));
  }

  public Set<XmlNotice> getXmlNotices()
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    final Set<XmlNotice> result = new HashSet<>();

    try (DirectoryStream<Path> dirStream =
        Files.newDirectoryStream(Path.of(sdkRoot.toString(), EXAMPLE_NOTICES.toString()))) {

      for (Path path : dirStream) {
        if (!Files.isDirectory(path)) {
          XmlNotice xmlNotice = XmlDataExtractor.loadXmlNoticeFile(path);

          result.add(xmlNotice);
        }
      }
    }

    return result;
  }

  public Set<SvrlReport> getSvrlReports()
      throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
    final Set<SvrlReport> result = new HashSet<>();

    try (DirectoryStream<Path> dirStream =
        Files.newDirectoryStream(Path.of(sdkRoot.toString(), EXAMPLE_REPORTS.toString()))) {

      for (Path path : dirStream) {
        if (!Files.isDirectory(path)) {
          SvrlReport svrlReport = XmlDataExtractor.loadSvrlReportFile(path);

          result.add(svrlReport);
        }
      }
    }

    return result;
  }

  public XmlSchemaCollection getXmlSchemas() throws IOException {
    XmlSchemaCollection schemaCol = new XmlSchemaCollection();

    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
        Path.of(sdkRoot.toString(), SdkResource.SCHEMAS_MAINDOC.getPath().toString()))) {

      for (Path path : dirStream) {
        if (!Files.isDirectory(path)) {
          schemaCol.read(new StreamSource(path.toFile()));
        }
      }
    }

    return schemaCol;
  }

  public List<Path> getSchematronFilesPaths() {
    String startFile = "complete-validation.sch";

    return List.of(
        sdkRoot.resolve(SCHEMATRONS_DYNAMIC).resolve(startFile),
        sdkRoot.resolve(SCHEMATRONS_STATIC).resolve(startFile));
  }

  public Set<SchematronFile> getSchematronFiles() {
    final Set<SchematronFile> result = new HashSet<>();
    for (Path file : getSchematronFilesPaths()) {
      result.add(SchematronParser.loadSchematronFile(file));
    }

    return result;
  }
}
