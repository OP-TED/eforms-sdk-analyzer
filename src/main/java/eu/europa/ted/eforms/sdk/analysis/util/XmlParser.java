package eu.europa.ted.eforms.sdk.analysis.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {
  private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);

  private XmlParser() {

  }

  public static <T> T loadXmlFile(Class<T> clazz, Path xmlFilePath)
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    logger.debug("Loading contents of type [{} from XML file [{}]", clazz.getName(), xmlFilePath);

    if (!Files.isRegularFile(xmlFilePath)) {
      throw new FileNotFoundException(xmlFilePath.toString());
    }

    final JAXBContext context = JAXBContext.newInstance(clazz);

    // Disable XXE
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
    spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

    // Do unmarshall operation
    Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(),
        new InputSource(Files.newBufferedReader(xmlFilePath)));

    Unmarshaller unmarshaller = context.createUnmarshaller();

    @SuppressWarnings("unchecked")
    final T result = (T) unmarshaller.unmarshal(xmlSource);
    Validate.notNull(result,
        MessageFormat.format("No data was loaded from XML file [{0}]", xmlFilePath));

    return result;
  }
}
