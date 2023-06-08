package eu.europa.ted.eforms.sdk.analysis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;

/**
 * Utility class to load metadata on the SDK from its POM file.
 */
public class SdkMetadataParser {
  private static final Logger logger = LoggerFactory.getLogger(SdkMetadataParser.class);

  private SdkMetadataParser() {}

  public static SdkMetadata loadSdkMetadata(Path sdkRoot) throws IOException {
    return loadSdkMetadataFromPomFile(sdkRoot.resolve("pom.xml"));
  }

  private static SdkMetadata loadSdkMetadataFromPomFile(Path pomFilePath) throws IOException {
    logger.debug("Loading SDK metadata from file [{}]", pomFilePath);

    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model = null;
    try {
      model = reader.read(Files.newInputStream(pomFilePath));
    } catch (XmlPullParserException e) {
      throw new IOException("Error reading POM from " + pomFilePath, e);
    }

    return new SdkMetadata(model.getVersion());
  }
}
