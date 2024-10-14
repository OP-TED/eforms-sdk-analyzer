package eu.europa.ted.eforms.sdk.analysis.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.IError;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.SchematronHelper;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.IMicroNode;
import com.helger.xml.microdom.serialize.MicroReader;

import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronAssert;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronDiagnostic;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronPattern;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronPhase;

public class SchematronParser {
  private static final Logger logger = LoggerFactory.getLogger(SchematronParser.class);

  private SchematronParser() {}

  public static SchematronFile loadSchematronFile(Path schematronFilePath) {
    logger.debug("Loading Schematron file " + schematronFilePath.toString());

    SchematronFile schematronFile = new SchematronFile(schematronFilePath);

    IReadableResource schematron = new FileSystemResource(schematronFilePath);

    final IMicroDocument doc = MicroReader.readMicroXML(schematron);
    if (doc == null || doc.getAllChildren() == null) {
      logger.error("Schematron file {} could not be loaded as XML", schematronFilePath);
      return null;
    }
    List<IMicroNode> children = doc.getAllChildrenRecursive();
    if (children == null) {
      logger.error("Schematron file {} has unexpected structure", schematronFilePath);
      return null;
    }

    // Parse phase definitions
    List<SchematronPhase> phases = new ArrayList<>();
    children.stream()
        .filter(node -> node.isElement() && "phase".equals(node.getNodeName()))
        .forEach(node -> {
          IMicroElement element = (IMicroElement)node;
          String id = element.getAttributeValue("id");
          SchematronPhase phase = new SchematronPhase(id);
          List<IMicroNode> phaseRefs = element.getAllChildren();
          if (phaseRefs == null) {
            logger.error("Incorrect definition for phase {}", id);
            return;
          }
          phaseRefs.stream()
              .filter(n -> n.isElement() && "active".equals(n.getNodeName()))
              .map(n -> ((IMicroElement)n).getAttributeValue("pattern"))
              .forEach(s -> phase.addActivePattern(s));

          phases.add(phase);
        });

    schematronFile.setPhases(phases);

    // Parse list diagnostic definitions
    List<SchematronDiagnostic> diagnostics = children.stream()
        .filter(node -> node.isElement() && "diagnostic".equals(node.getNodeName()))
        .map(n -> ((IMicroElement)n).getAttributeValue("id"))
        .map(s -> new SchematronDiagnostic(s))
        .collect(Collectors.toList());

    schematronFile.setDiagnostics(diagnostics);

    // Resolve all included files, so that they also get loaded.
    final IMicroDocument docResolved = SchematronHelper.getWithResolvedSchematronIncludes(schematron,
        e -> handleError(e, schematronFilePath));

    if (docResolved == null) {
      logger.error("Schematron file {} with resolved includes could not be loaded as XML",
          schematronFilePath);
      return null;
    }

    List<IMicroNode> allChildren = docResolved.getAllChildrenRecursive();
    if (allChildren == null) {
      logger.error("Schematron file {} does not have the expected content", schematronFilePath);
      return null;
    }
    
    // Parse all patterns
    List<SchematronPattern> patterns = allChildren.stream()
        .filter(node -> node.isElement() && "pattern".equals(node.getNodeName()))
        .map(n -> ((IMicroElement)n).getAttributeValue("id"))
        .map(s -> new SchematronPattern(s))
        .collect(Collectors.toList());

    schematronFile.setPatterns(patterns);

    // Parse all asserts
    List<SchematronAssert> asserts = new ArrayList<>();
    allChildren.stream()
        .filter(node -> node.isElement() && "assert".equals(node.getNodeName()))
        .forEach(n -> {
          IMicroElement element = (IMicroElement)n;
          String id = element.getAttributeValue("id");
          SchematronAssert schematronAssert = new SchematronAssert(id);
          String diag = element.getAttributeValue("diagnostics");
          schematronAssert.setDiagnostics(diag);
          asserts.add(schematronAssert);
        });

    schematronFile.setAsserts(asserts);

    return schematronFile;
  }

  private static void handleError(IError error, Path schematronFilePath) {
    logger.error("Error loading schematron file {} : {}", schematronFilePath.toString(),
        error.getAsString(new Locale("en")));
  }
}
