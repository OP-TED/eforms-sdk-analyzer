package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.Validate;
import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaSequenceMember;
import org.apache.ws.commons.schema.XmlSchemaSimpleContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.analysis.domain.field.Field;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentTypeNamespace;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NodeFact;
import eu.europa.ted.eforms.sdk.analysis.util.XPathSplitter;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates the content of the XSD files in the SDK, and their consistency with other files.
 * It does not validate XML files against a schema.
 */
public class XmlSchemaValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(XmlSchemaValidator.class);

  private static final String ROOT_NODE_ID = "ND-Root";

  private final SdkLoader sdkLoader;

  private List<DocumentType> documentTypes;
  private Map<String, String> namespacePrefixToUri = new HashMap<>();
  private XmlSchemaCollection schemaCollection;

  private final Set<ValidationResult> results;

  private Set<QName> visited;

  public XmlSchemaValidator(Path sdkRoot) throws IOException {
    Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    this.sdkLoader = new SdkLoader(Path.of(sdkRoot.toString()));
    this.documentTypes = sdkLoader.getNoticeTypesForIndex().getDocumentTypes();

    documentTypes.forEach(dt -> {
      namespacePrefixToUri.putAll(dt.getAdditionalNamespaces().stream().collect(Collectors.toMap(
          DocumentTypeNamespace::getPrefix, DocumentTypeNamespace::getUri)));
    });

    logger.debug("Loading XML schemas");
    this.schemaCollection = sdkLoader.getXmlSchemas();

    this.results = new HashSet<>();
  }

  public XmlSchemaValidator validateXmlSchemas() throws IOException {
    final List<Field> fields = sdkLoader.getFieldsAndNodes().getFields();
    final List<XmlStructureNode> nodes = sdkLoader.getFieldsAndNodes().getNodes();

    fields.stream().forEach(field -> {
      checkFieldRepeatability(field);
    });

    nodes.stream().forEach(node -> {
      checkNodeRepeatability(node);
    });

    return this;
  }

  /*
   * Check that the schema allows the element corresponding to the field to occur several times
   * under its parent node.
   * If the parent node has the relative XPath A/B, and the field has C/D, then we need to
   * check that C can be repeated under B.
   */
  private void checkFieldRepeatability(Field field) {
    if (!field.getRepeatable().getValue()) {
      // Field is not repeatable, nothing to check;
      return;
    }

    // If the field's relative XPath has several steps, it's the first that must be repeatable
    final QName fieldQName = buildQName(getFirstElementName(field.getXpathRelative()));

    if (field.getParentNodeId().equals(ROOT_NODE_ID)) {
      // We don't know in which document root(s) the field can appear, so we look at all roots,
      // and if the element corresponding to the field can appear, it must be repeatable
      documentTypes.stream().forEach(dt -> {
        final QName root = new QName(dt.getNamespace(), dt.getRootElement());
        if (isDefinedUnder(fieldQName, root) && !canElementBeRepeatedUnder(fieldQName, root)) {
          results.add(new ValidationResult(new FieldFact(field),
              "Field is repeatable but this is not allowed by the schema", ValidationStatusEnum.ERROR));
        }
      });
    } else {
      final QName parent = buildQName(getLastElementName(field.getParentNode().getXpathRelative()));
      if (!canElementBeRepeatedUnder(fieldQName, parent)) {
        results.add(new ValidationResult(new FieldFact(field),
            "Field is repeatable but this is not allowed by the schema", ValidationStatusEnum.ERROR));
      }
    }
  }

  /*
   * Check that the schema allows the element corresponding to the node to occur several times
   * under its given parent.
   * If the node has the relative XPath C/D, and it's parent node has A/B, then we need to
   * check that C can be repeated under B.
   */
  private void checkNodeRepeatability(XmlStructureNode node) {
    if (!node.isRepeatable()) {
      // Node is not repeatable, nothing to check;
      return;
    }

    // If the node's relative XPath has several steps, it's the first that must be repeatable
    final QName nodeQName = buildQName(getFirstElementName(node.getXpathRelative()));

    XmlStructureNode parentNode = node.getParent();

    if (parentNode.getId().equals(ROOT_NODE_ID)) {
      documentTypes.stream().forEach(dt -> {
        final QName root = new QName(dt.getNamespace(), dt.getRootElement());
        if (isDefinedUnder(nodeQName, root) && !canElementBeRepeatedUnder(nodeQName, root)) {
          results.add(new ValidationResult(new NodeFact(node),
              "Field is repeatable but this is not allowed by the schema", ValidationStatusEnum.ERROR));
        }
      });
    } else {
      final QName parentQName = buildQName(getLastElementName(parentNode.getXpathRelative()));
      if (!canElementBeRepeatedUnder(nodeQName, parentQName)) {
        results.add(new ValidationResult(new NodeFact(node),
            "Node is repeatable but this is not allowed by the schema", ValidationStatusEnum.ERROR));
      }
    }
  }

  /**
   * Return the name of the element that is the first step in the given XPath,
   * removing any predicate.
   */
  private String getFirstElementName(String xpath) {
    List<String> names = XPathSplitter.getStepElementNames(xpath);
    return names.get(0);
  }

  /**
   * Return the name of the element that is the last step in the given XPath,
   * removing any predicate.
   */
  private String getLastElementName(String xpath) {
    List<String> names = XPathSplitter.getStepElementNames(xpath);
    return names.get(names.size() - 1);
  }

  /**
   * Create the qualified name for the given element name.
   * The element must have a known namespace prefix (for example "cbc:ID").
   */
  private QName buildQName(String elementName) {
    String[] parts = elementName.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Unexpected XML element: " + elementName);
    }
    final String prefix = parts[0];
    final String localPart = parts[1];
    String namespaceURI = namespacePrefixToUri.get(prefix);

    return new QName(namespaceURI, localPart, prefix);
  }

  /*
   * Returns true if the childQName element is referenced in the schema directly in the type
   * corresponding to the parentQName element
   */
  private boolean isDefinedUnder(final QName childQName, final QName parentQName) {
    XmlSchemaElement child = schemaCollection.getElementByQName(childQName);
    XmlSchemaElement parent = schemaCollection.getElementByQName(parentQName);

    XmlSchemaComplexType parentType = (XmlSchemaComplexType)parent.getSchemaType();
    if (parentType == null) {
      // Not a complex type, cannot have elements under it
      return false;
    }

    boolean found = false;

    XmlSchemaParticle particle = parentType.getParticle();

    if (particle instanceof XmlSchemaSequence) {
      final XmlSchemaSequence xmlSchemaSequence = (XmlSchemaSequence) particle;
      for (XmlSchemaSequenceMember item : xmlSchemaSequence.getItems()) {
        XmlSchemaElement elt = (XmlSchemaElement)item;
        XmlSchemaElement target = elt.getRef().getTarget();
        if (child.getQName().equals(target.getQName())) {
          found = true;
          break;
        }
      }
    } else {
      throw new UnsupportedOperationException("Type " + parentType.getName()
          + " contains unsupported particle: " + particle.getClass().getName());
    }

    return found;
  }

  /*
   * Returns true if the elementQName is referenced in the schema somewhere under the type of
   * parentQName, and this reference has maxOccurs > 1.
   * As an element can be at multiple locations in the descendants of a given element, this
   * will just look at the closest occurence of elementQName.
   */
  private boolean canElementBeRepeatedUnder(final QName elementQName, final QName parentQName) {
    visited = new HashSet<>();

    XmlSchemaElement parent = schemaCollection.getElementByQName(parentQName);

    XmlSchemaElement element = schemaCollection.getElementByQName(elementQName);
  
    long maxOccurs = findElementMaxOccursUnder(element, parent);

    if (maxOccurs < 0) {
      throw new RuntimeException("Element not found: " + elementQName);
    }

    return (maxOccurs > 1);
  }

  /*
   * Look for a reference to the "needle" element under "current" and return the "maxoccurs" of this
   * reference
   * We want to find the closest match, so we do a recursive breadth-first search.
   */
  private long findElementMaxOccursUnder(final XmlSchemaElement needle, final XmlSchemaElement current) {
    long maxOccurs = -1;

    if (visited.contains(current.getQName())) {
      // Already visited this
      return -1;
    }
    visited.add(current.getQName());

    XmlSchemaComplexType currentType = (XmlSchemaComplexType)current.getSchemaType();
    if (currentType == null) {
      // Not a complex type, so not element defined under it, nothing to do
      return -1;
    }
    if (currentType.getContentModel() instanceof XmlSchemaSimpleContent) {
      // No child element, nothing to do
      return -1;
    }

    XmlSchemaParticle particle = currentType.getParticle();

    List<XmlSchemaElement> children;
    if (particle instanceof XmlSchemaSequence) {
      final XmlSchemaSequence xmlSchemaSequence = (XmlSchemaSequence) particle;
      children = xmlSchemaSequence.getItems().stream().map(e -> (XmlSchemaElement)e)
          .collect(Collectors.toList());
    } else if (particle instanceof XmlSchemaChoice) {
      final XmlSchemaChoice xmlSchemaChoice = (XmlSchemaChoice) particle;
      children = xmlSchemaChoice.getItems().stream().map(e -> (XmlSchemaElement)e)
          .collect(Collectors.toList());
    } else {
      throw new UnsupportedOperationException("Type " + currentType.getName()
          + " contains unsupported particle: " + particle.getClass().getName());
    }

    for (XmlSchemaElement childElement : children) {
      XmlSchemaElement target = childElement.getRef().getTarget();

      if (needle.getQName().equals(target.getQName())) {
        // childElement references the "needle" element, return its "maxoccurs"
        return childElement.getMaxOccurs();
      }
    }

    // No match on the children themselves, recursively visit them and propagate
    // the highest "maxoccurs" value we find.
    for (XmlSchemaElement childElement : children) {
      XmlSchemaElement target = childElement.getRef().getTarget();
      long localMaxOccurs = target.getMaxOccurs();
      long childMaxOccurs = findElementMaxOccursUnder(needle, target);
      if (childMaxOccurs > 0) {
        // needle was found under target, keep the biggest maxOccurs
        maxOccurs = Math.max(localMaxOccurs, childMaxOccurs);
      }
    }

    return maxOccurs;
  }

  @Override
  public Set<ValidationResult> getResults() {
    return results;
  }
}
