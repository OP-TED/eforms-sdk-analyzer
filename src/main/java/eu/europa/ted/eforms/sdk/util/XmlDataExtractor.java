package eu.europa.ted.eforms.sdk.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.europa.ted.eforms.sdk.domain.XmlNotice;

/**
 * Extract some specific information from an XML file, by applying some XPaths.
 * This for XML files that we do not need or want to load as a whole.
 */
public class XmlDataExtractor {
  private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);

  private static final String XPATH_CUSTOMIZATIONID = "/*/cbc:CustomizationID/text()";

  private XmlDataExtractor() {}
  
  public static XmlNotice loadXmlNoticeFile(Path xmlFilePath)
      throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    logger.debug("Loading XML notice [{}]", xmlFilePath);

    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
    // Options for security
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    dbf.setXIncludeAware(false);
    // For the XPaths to work
    dbf.setNamespaceAware(true);

    Document xmlDoc = dbf.newDocumentBuilder().parse(xmlFilePath.toFile());
    
    String filename = xmlFilePath.getFileName().toString();

    XPath xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(new NamespaceContext() {
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
      if (prefix.equals("cbc")) {
        return "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
      }
      return null;
      }
    });
    String customizationId = xpath.evaluateExpression(XPATH_CUSTOMIZATIONID, xmlDoc, String.class);

    return new XmlNotice(filename, customizationId.trim());
  }
}
