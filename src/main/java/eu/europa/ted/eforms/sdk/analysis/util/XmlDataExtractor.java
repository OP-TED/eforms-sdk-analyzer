package eu.europa.ted.eforms.sdk.analysis.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathNodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import eu.europa.ted.eforms.sdk.analysis.domain.SvrlReport;
import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;

/**
 * Extract some specific information from an XML file, by applying some XPaths. This for XML files
 * that we do not need or want to load as a whole.
 */
public class XmlDataExtractor {
  private static final Logger logger = LoggerFactory.getLogger(XmlDataExtractor.class);

  private static final String XPATH_NOTICE_CUSTOMIZATIONID = "/*/cbc:CustomizationID/text()";
  private static final String XPATH_SVRL_ERRORS =
      "/svrl:schematron-output/svrl:failed-assert[@role='ERROR']";

  private XmlDataExtractor() {}

  public static XmlNotice loadXmlNoticeFile(Path xmlFilePath)
      throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    logger.debug("Loading XML notice [{}]", xmlFilePath);

    Document xmlDoc = loadXmlDocument(xmlFilePath);

    String filename = xmlFilePath.getFileName().toString();

    XPath xpath = getXPath();
    String customizationId =
        xpath.evaluateExpression(XPATH_NOTICE_CUSTOMIZATIONID, xmlDoc, String.class);

    return new XmlNotice(filename, customizationId.trim());
  }

  public static SvrlReport loadSvrlReportFile(Path svrlReportPath)
      throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    logger.debug("Loading SVRL report [{}]", svrlReportPath);

    Document xmlDoc = loadXmlDocument(svrlReportPath);

    String filename = svrlReportPath.getFileName().toString();

    XPath xpath = getXPath();
    XPathNodes nodes = xpath.evaluateExpression(XPATH_SVRL_ERRORS, xmlDoc, XPathNodes.class);
    int errorCount = nodes.size();

    return new SvrlReport(filename, errorCount);
  }

  private static XPath getXPath() {
    XPath xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(new SimpleNamespaceContext());
    return xpath;
  }

  private static Document loadXmlDocument(Path xmlFilePath)
      throws ParserConfigurationException, SAXException, IOException {
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
    // Options for security
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    dbf.setXIncludeAware(false);
    // For the XPaths to work
    dbf.setNamespaceAware(true);

    return dbf.newDocumentBuilder().parse(xmlFilePath.toFile());
  }

  private static final class SimpleNamespaceContext implements NamespaceContext {
    private Map<String, String> nsMap;

    public SimpleNamespaceContext() {
      nsMap = new HashMap<String, String>();
      // Only need the namespace prefixes used in the XPath expressions
      nsMap.put("cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
      nsMap.put("svrl", "http://purl.oclc.org/dsdl/svrl");
    }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
      return null;
    }

    @Override
    public String getPrefix(String namespaceURI) {
      return null;
    }

    @Override
    public String getNamespaceURI(String prefix) {
      return nsMap.get(prefix);
    }
  }
}
