package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.domain.Label;
import eu.europa.ted.eforms.sdk.domain.Language;
import eu.europa.ted.eforms.sdk.domain.Translation;
import eu.europa.ted.eforms.sdk.domain.field.FieldsAndNodes;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeSubTypeForIndex;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypeSdk;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypesForIndex;
import eu.europa.ted.eforms.sdk.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.domain.view.index.TedefoViewTemplatesIndex;
import eu.europa.ted.eforms.sdk.domain.xml.Properties;
import eu.europa.ted.eforms.sdk.domain.xml.Properties.Entry;
import eu.europa.ted.eforms.sdk.util.XmlParser;

public class SdkLoader {
  private static final Logger logger = LoggerFactory.getLogger(SdkLoader.class);

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

  public FieldsAndNodes getFieldsAndNodes() throws IOException {
    return loadJsonFile(FieldsAndNodes.class,
        Path.of(sdkRoot.toString(), SdkResource.FIELDS_JSON.getPath().toString()));
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
    return loadJsonFile(NoticeTypeSdk.class,
        Path.of(sdkRoot.toString(), SdkResource.NOTICE_TYPES.getPath().toString(),
            MessageFormat.format("{0}.json", noticeId)));
  }

  public Set<Translation> getTranslations()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    final Set<Translation> result = new HashSet<>();

    Translation translation = null;
    Properties translationProperties = null;

    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
        Path.of(sdkRoot.toString(), SdkResource.TRANSLATIONS.getPath().toString()))) {

      for (Path path : dirStream) {
        if (!Files.isDirectory(path)) {
          translation = new Translation();
          translationProperties = XmlParser.loadXmlFile(Properties.class, path);

          translation.setComment(translationProperties.getComment());
          translation.setLanguage(Language.valueOf(
              path.getFileName().toString().replaceAll("^.*?_(.*?).xml$", "$1").toUpperCase()));

          translation.setLabels(translationProperties.getEntry().stream()
              .collect(
                  Collectors.toMap((Entry entry) -> new Label(entry.getKey()), Entry::getValue)));

          result.add(translation);
        }
      }
    }

    return result;
  }

  public Set<Label> getLabels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    return getTranslations().stream()
        .flatMap((Translation translation) -> translation.getLabels().keySet().stream())
        .collect(Collectors.toSet());
  }

  public TedefoViewTemplatesIndex getViewTemplatesIndex() throws IOException {
    return loadJsonFile(TedefoViewTemplatesIndex.class,
        Path.of(sdkRoot.toString(), SdkResource.VIEW_TEMPLATES_JSON.getPath().toString()));
  }

  public Set<TedefoViewTemplateIndex> getViewTemplates() throws IOException {
    return new HashSet<>(getViewTemplatesIndex().getViewTemplates());
  }
}
