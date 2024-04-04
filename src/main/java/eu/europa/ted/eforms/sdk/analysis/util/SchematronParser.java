package eu.europa.ted.eforms.sdk.analysis.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.IError;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.SchematronHelper;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.IMicroNode;

import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronAssert;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;

public class SchematronParser {
  private static final Logger logger = LoggerFactory.getLogger(SchematronParser.class);

  private SchematronParser() {}

  public static SchematronFile loadSchematronFile(Path schematronFilePath) {
    logger.debug("Loading Schematron file " + schematronFilePath.toString());

    IReadableResource schematron = new FileSystemResource(schematronFilePath);
    // Resolve all included files, so that they also get loaded.
    final IMicroDocument doc = SchematronHelper.getWithResolvedSchematronIncludes(schematron,
        e -> handleError(e, schematronFilePath));

    if (doc == null) {
      logger.error("Schematron file %s could not be loaded as XML", schematronFilePath);
      return null;
    }

    List<SchematronAssert> asserts = new ArrayList<>();
    List<IMicroNode> allChildren = doc.getAllChildrenRecursive();
    if (allChildren == null) {
      logger.error("Schematron file %s does not have the expected content", schematronFilePath);
      return null;
    }

    for (IMicroNode node : allChildren) {
      if (node != null && node.isElement() && "assert".equals(node.getNodeName())) {
        IMicroElement element = (IMicroElement)node;
        SchematronAssert schAssert = new SchematronAssert(element.getAttributeValue("id"));
        asserts.add(schAssert);
      }
    }

    SchematronFile schematronFile = new SchematronFile(schematronFilePath, asserts);

    return schematronFile;
  }

  private static void handleError(IError error, Path schematronFilePath) {
    logger.error("Error loading schematron file %s : %s", schematronFilePath.toString(),
        error.getAsString(new Locale("en")));
  }
}
