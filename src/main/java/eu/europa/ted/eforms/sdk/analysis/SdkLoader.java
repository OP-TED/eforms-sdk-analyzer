package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.domain.field.FieldsAndNodes;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeSubTypeForIndex;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypeSdk;
import eu.europa.ted.eforms.sdk.domain.noticetype.NoticeTypesForIndex;

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

    Validate.isTrue(Files.isRegularFile(jsonFilePath),
        MessageFormat.format("JSON file [{0}] does not exist", jsonFilePath));

    logger.debug("Getting data from JSON file [{}]", jsonFilePath);

    T result = objectMapper.readValue(jsonFilePath.toFile(), clazz);
    Validate.notNull(result,
        MessageFormat.format("No data was loaded from JSON file [{0}]", jsonFilePath));

    return result;
  }

  public FieldsAndNodes getFieldsAndNodes() throws IOException {
    return loadJsonFile(FieldsAndNodes.class,
        Path.of(sdkRoot.toString(), SdkResource.FIELDS_JSON.getPath().toString()));
  }

  public Set<NoticeType> getNoticeTypes() throws IOException {
    Set<NoticeType> result = new HashSet<>();

    NoticeTypesForIndex noticeTypesForIndex = getNoticeTypesForIndex();

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
}
