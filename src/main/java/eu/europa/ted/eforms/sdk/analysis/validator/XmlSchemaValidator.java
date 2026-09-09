package eu.europa.ted.eforms.sdk.analysis.validator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;
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

import eu.europa.ted.eforms.sdk.analysis.SdkLoader;
import eu.europa.ted.eforms.sdk.analysis.domain.field.Field;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentTypeNamespace;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.NodeFact;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;
import eu.europa.ted.eforms.xpath.XPathProcessor;
import eu.europa.ted.eforms.xpath.XPathStep;

/**
 * Validates the content of the XSD files in the SDK, and their consistency with other files.
 * It does not validate XML files against a schema.
 */
public class XmlSchemaValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(XmlSchemaValidator.class);

  private static final String ROOT_NODE_ID = "ND-Root";

  /**
   * The namespace prefixes of the eForms extension, the only ones whose schemas are authored for
   * eForms and therefore tailored to it. The UBL schemas are the untailored OASIS ones, so "declared
   * in the schema" says nothing about eForms there: an element being absent from the metadata is the
   * normal case for the vast majority of UBL, not a finding.
   */
  private static final Set<String> EXTENSION_PREFIXES = Set.of("efac", "efbc");

  /** Separates the two halves of a "parent element to child element" key. */
  private static final String PAIR_SEPARATOR = ">";

  /**
   * Stands for the notice root in place of an element name. The root node's relative XPath is "/*",
   * which names no element: each notice type has its own root element, resolved per document type.
   */
  private static final String DOCUMENT_ROOT = "/*";

  private final SdkLoader sdkLoader;

  private List<DocumentType> documentTypes;
  private Map<String, String> namespacePrefixToUri = new HashMap<>();
  private Map<String, String> namespaceUriToPrefix = new HashMap<>();
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

    // The schema gives us an element's namespace URI; findings have to name it the way the metadata
    // does, with a prefix, so we need the reverse lookup too.
    namespacePrefixToUri.forEach((prefix, uri) -> namespaceUriToPrefix.putIfAbsent(uri, prefix));

    logger.debug("Loading XML schemas");
    this.schemaCollection = sdkLoader.getXmlSchemas();

    this.results = new HashSet<>();
  }

  public XmlSchemaValidator validate() throws IOException {
    final List<Field> fields = sdkLoader.getFieldsAndNodes().getFields();
    final List<XmlStructureNode> nodes = sdkLoader.getFieldsAndNodes().getNodes();

    fields.stream().forEach(field -> {
      checkFieldRepeatability(field);
    });

    nodes.stream().forEach(node -> {
      checkNodeRepeatability(node);
    });

    checkExtensionElementsAreCovered(fields, nodes);

    return this;
  }

  /*
   * Invariant: every eForms extension element allowed by the schemas is covered by a field or a node.
   *
   * An element the schemas allow but no field and no node covers can legitimately appear in a notice
   * while being invisible to the metadata, so nothing can validate, translate or display it.
   *
   * Reported as a WARNING: the finding says an element is unmodelled, which may well be deliberate,
   * and the decision to add a field belongs to the metadata owners.
   *
   * Scope, in two parts.
   *
   * Only the efac: and efbc: namespaces are considered. The UBL schemas are the untailored OASIS
   * ones, so "allowed by the schema" carries no eForms meaning there: an element being absent from
   * the metadata is the normal case for the vast majority of UBL, not a finding.
   *
   * Only elements inside a parent the metadata reaches are examined — an element a node stands for,
   * an element a relative path passes through, or a notice root. So the invariant is enforced a
   * little more narrowly than its wording: the children of an aggregate that has no node AND appears
   * in no relative path are not examined, because there is no node to hang a field on yet. Such an
   * aggregate is itself reported, so every gap is either reported or has its parent reported; model
   * the parent and the children surface on the next run.
   *
   * Coverage is compared at ELEMENT-NAME level ("is efbc:X ever a child of efac:Y?") rather than by
   * full XPath. The schemas permit the same aggregate in several places while the SDK models one
   * canonical placement, so comparing full paths reports every alternative placement as missing —
   * noise, not findings. The trade-off is that an element covered at one legitimate placement but
   * absent at another is not reported.
   */
  private void checkExtensionElementsAreCovered(final List<Field> fields,
      final List<XmlStructureNode> nodes) {

    // What the metadata covers, as "parent element > child element" pairs.
    final Set<String> covered = new HashSet<>();
    nodes.forEach(node -> collectCoveredPairs(node.getParent(), node.getXpathRelative(), covered));
    fields.forEach(field -> collectCoveredPairs(field.getParentNode(), field.getXpathRelative(),
        covered));

    // Which elements to look inside. Not only the ones a node stands for: an aggregate reached solely
    // through a multi-step relative path has no node of its own, yet the elements it contains still
    // need metadata, so it has to be inspected too.
    final Map<String, InspectedElement> inspected = new HashMap<>();
    for (final XmlStructureNode node : nodes) {
      rememberInspected(inspected, elementNameOfNode(node), node.getXpathAbsolute(), node);
      collectInspectedElements(inspected, node.getParent(), node.getXpathRelative());
    }
    fields.forEach(field -> collectInspectedElements(inspected, field.getParentNode(),
        field.getXpathRelative()));

    // One finding per "parent element > child element" pair: the document root resolves to several
    // root elements, and an element can be reachable from more than one place.
    final Set<String> reported = new HashSet<>();

    for (final Map.Entry<String, InspectedElement> entry : inspected.entrySet()) {
      final String parentElementName = entry.getKey();
      final InspectedElement place = entry.getValue();

      for (final XmlSchemaElement childRef : childElementRefsOf(parentElementName)) {
        final XmlSchemaElement target = childRef.getRef().getTarget();
        if (target == null) {
          continue;
        }

        final String prefix = namespaceUriToPrefix.get(target.getQName().getNamespaceURI());
        if (prefix == null || !EXTENSION_PREFIXES.contains(prefix)) {
          continue;
        }

        final String childElementName = prefix + ":" + target.getQName().getLocalPart();
        final String pair = parentElementName + PAIR_SEPARATOR + childElementName;

        if (covered.contains(pair) || !reported.add(pair)) {
          continue;
        }

        results.add(new ValidationResult(new NodeFact(place.subject),
            "eForms extension element allowed by the schemas is covered by no field and no node",
            ValidationStatusEnum.WARNING,
            AssetRef.xmlElement(place.xpathAbsolute + "/" + childElementName)));
      }
    }
  }

  /**
   * The element references inside the named element. The document root is not a named element: every
   * notice type has its own root, so the union of their children is used — an element allowed by any
   * of them needs metadata. BRIN reaches {@code efac:} elements that way, outside ext:UBLExtensions.
   */
  private List<XmlSchemaElement> childElementRefsOf(final String elementName) {
    if (DOCUMENT_ROOT.equals(elementName)) {
      final List<XmlSchemaElement> refs = new ArrayList<>();
      for (final DocumentType documentType : documentTypes) {
        final XmlSchemaElement root = schemaCollection.getElementByQName(
            new QName(documentType.getNamespace(), documentType.getRootElement()));
        if (root != null) {
          refs.addAll(getChildElementRefs(root));
        }
      }
      return refs;
    }

    final XmlSchemaElement element = schemaCollection.getElementByQName(buildQName(elementName));
    // A null element is already reported by the repeatability checks above.
    return element == null ? Collections.emptyList() : getChildElementRefs(element);
  }

  /*
   * Record the "parent element > child element" pair for every step of a relative XPath, walking down
   * from the parent node. A relative XPath can have several steps (a field's can be
   * "efac:NoticeSubType/cbc:SubTypeCode"), and each step covers its own parent, so the intermediate
   * elements count as covered even though no node of their own exists for them.
   */
  private void collectCoveredPairs(final XmlStructureNode parentNode, final String xpathRelative,
      final Set<String> covered) {
    if (StringUtils.isBlank(xpathRelative)) {
      return;
    }

    String parentElementName = parentNode == null ? null : elementNameOfNode(parentNode);

    for (final XPathStep step : XPathProcessor.parse(xpathRelative).getSteps()) {
      final String stepText = step.getStepText();
      if (stepText == null || !stepText.contains(":")) {
        // An attribute, or a step we cannot name: neither can be a parent of anything.
        continue;
      }
      if (parentElementName != null) {
        covered.add(parentElementName + PAIR_SEPARATOR + stepText);
      }
      parentElementName = stepText;
    }
  }

  /*
   * Register every element a relative XPath passes through on its way down, except the last step,
   * which is the field or node itself. Without this, an aggregate that only ever appears as an
   * intermediate step is never looked inside: efac:NoticeSubType is reached only by OPP-070-notice's
   * "efac:NoticeSubType/cbc:SubTypeCode", so efbc:SubTypeDescription beside it stayed invisible.
   *
   * The accumulated XPath is built from the parent node's absolute XPath plus the steps walked, so it
   * carries the parent's predicates but not any on the intermediate steps themselves.
   */
  private void collectInspectedElements(final Map<String, InspectedElement> inspected,
      final XmlStructureNode parentNode, final String xpathRelative) {
    if (parentNode == null || StringUtils.isBlank(xpathRelative)
        || parentNode.getXpathAbsolute() == null) {
      return;
    }

    final StringBuilder xpath = new StringBuilder(parentNode.getXpathAbsolute());
    final List<XPathStep> steps = XPathProcessor.parse(xpathRelative).getSteps();

    for (int i = 0; i < steps.size(); i++) {
      final String stepText = steps.get(i).getStepText();
      if (stepText == null || !stepText.contains(":")) {
        // An attribute or an unnamed step: nothing below it can be inspected.
        return;
      }
      xpath.append('/').append(stepText);
      if (i < steps.size() - 1) {
        rememberInspected(inspected, stepText, xpath.toString(), parentNode);
      }
    }
  }

  /*
   * Keep one place per element name, the one with the shortest absolute XPath. Several nodes can share
   * an element (efext:EformsExtension is the element of the notice-root extension and of the extension
   * under every value estimate, cac:RequestedTenderTotal included); since coverage is judged per
   * element, the reader should be shown the primary placement rather than whichever came first.
   */
  private void rememberInspected(final Map<String, InspectedElement> inspected,
      final String elementName, final String xpathAbsolute, final XmlStructureNode subject) {
    if (elementName == null || xpathAbsolute == null || subject == null) {
      return;
    }
    inspected.merge(elementName, new InspectedElement(xpathAbsolute, subject),
        (kept, fresh) -> fresh.xpathAbsolute.length() < kept.xpathAbsolute.length() ? fresh : kept);
  }

  /**
   * The element a node stands for: the last step of its relative XPath, or the document-root sentinel
   * for the root node, whose relative XPath is "/*" and names no element.
   */
  private String elementNameOfNode(final XmlStructureNode node) {
    final String xpathRelative = StringUtils.trim(node.getXpathRelative());
    return DOCUMENT_ROOT.equals(xpathRelative) ? DOCUMENT_ROOT
        : prefixedLastElementName(xpathRelative);
  }

  /**
   * The prefixed name of the last element of an XPath (predicates removed), or null when the XPath has
   * no such element.
   */
  private String prefixedLastElementName(final String xpath) {
    if (StringUtils.isBlank(xpath)) {
      return null;
    }
    final List<XPathStep> steps = XPathProcessor.parse(xpath).getSteps();
    if (steps.isEmpty()) {
      return null;
    }
    final String stepText = steps.get(steps.size() - 1).getStepText();
    return stepText != null && stepText.contains(":") ? stepText : null;
  }

  /** Where an inspected element sits, and the node a finding about it is reported against. */
  private static final class InspectedElement {
    private final String xpathAbsolute;
    private final XmlStructureNode subject;

    InspectedElement(final String xpathAbsolute, final XmlStructureNode subject) {
      this.xpathAbsolute = xpathAbsolute;
      this.subject = subject;
    }
  }

  /**
   * The element references directly inside the type of the given element, or an empty list when the
   * type holds no child element (a simple type, or simple content such as a code or a text).
   */
  private List<XmlSchemaElement> getChildElementRefs(final XmlSchemaElement element) {
    if (!(element.getSchemaType() instanceof XmlSchemaComplexType)) {
      return Collections.emptyList();
    }
    final XmlSchemaComplexType type = (XmlSchemaComplexType) element.getSchemaType();
    if (type.getContentModel() instanceof XmlSchemaSimpleContent) {
      return Collections.emptyList();
    }

    final XmlSchemaParticle particle = type.getParticle();
    if (particle instanceof XmlSchemaSequence) {
      return ((XmlSchemaSequence) particle).getItems().stream()
          .filter(XmlSchemaElement.class::isInstance).map(XmlSchemaElement.class::cast)
          .collect(Collectors.toList());
    }
    if (particle instanceof XmlSchemaChoice) {
      return ((XmlSchemaChoice) particle).getItems().stream()
          .filter(XmlSchemaElement.class::isInstance).map(XmlSchemaElement.class::cast)
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
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

    XmlSchemaElement fieldElement = schemaCollection.getElementByQName(fieldQName);
    if (fieldElement == null) {
      results.add(new ValidationResult(new FieldFact(field),
          "XML element corresponding to the field is not present in the schema", ValidationStatusEnum.ERROR));
      // We can't check repeatability
      return;
    }

    if (field.getParentNodeId().equals(ROOT_NODE_ID)) {
      // We don't know in which document root(s) the field can appear, so we look at all roots,
      // and if the element corresponding to the field can appear, it must be repeatable
      documentTypes.stream().forEach(dt -> {
        final QName root = new QName(dt.getNamespace(), dt.getRootElement());

        XmlSchemaElement rootElement = schemaCollection.getElementByQName(root);
        if (rootElement == null) {
          results.add(new ValidationResult(new FieldFact(field),
              "XML element corresponding to the root is not present in the schema", ValidationStatusEnum.ERROR));
          return;
        }
    
        if (isDefinedUnder(fieldQName, root) && !canElementBeRepeatedUnder(fieldElement, rootElement)) {
          results.add(new ValidationResult(new FieldFact(field),
              "Field is repeatable but this is not allowed by the schema", ValidationStatusEnum.ERROR));
        }
      });
    } else {
      if (field.getParentNode() == null) {
        // Parent node is missing (reported by the "Field's parent node is missing" rule);
        // repeatability cannot be checked without it.
        return;
      }
      final QName parentQName = buildQName(getLastElementName(field.getParentNode().getXpathRelative()));

      XmlSchemaElement parentElement = schemaCollection.getElementByQName(parentQName);
      if (parentElement == null) {
        results.add(new ValidationResult(new FieldFact(field),
            "XML element corresponding to the parent is not present in the schema", ValidationStatusEnum.ERROR));
        return;
      }

      if (!canElementBeRepeatedUnder(fieldElement, parentElement)) {
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

    XmlSchemaElement nodeElement = schemaCollection.getElementByQName(nodeQName);
    if (nodeElement == null) {
      results.add(new ValidationResult(new NodeFact(node),
          "XML element corresponding to the node is not present in the schema", ValidationStatusEnum.ERROR));
      // We can't check repeatability
      return;
    }

    XmlStructureNode parentNode = node.getParent();
    if (parentNode == null) {
      // Parent node is missing (reported by the "Node's parent node is missing" rule);
      // repeatability cannot be checked without it.
      return;
    }

    if (parentNode.getId().equals(ROOT_NODE_ID)) {
      documentTypes.stream().forEach(dt -> {
        final QName root = new QName(dt.getNamespace(), dt.getRootElement());

        XmlSchemaElement rootElement = schemaCollection.getElementByQName(root);
        if (rootElement == null) {
          results.add(new ValidationResult(new NodeFact(node),
              "XML element corresponding to the root is not present in the schema", ValidationStatusEnum.ERROR));
          return;
        }

        if (isDefinedUnder(nodeQName, root) && !canElementBeRepeatedUnder(nodeElement, rootElement)) {
          results.add(new ValidationResult(new NodeFact(node),
              "Node is repeatable but this is not allowed by the schema", ValidationStatusEnum.ERROR));
        }
      });
    } else {
      final QName parentQName = buildQName(getLastElementName(parentNode.getXpathRelative()));
      
      XmlSchemaElement parentElement = schemaCollection.getElementByQName(parentQName);
      if (parentElement == null) {
        results.add(new ValidationResult(new NodeFact(node),
            "XML element corresponding to the parent is not present in the schema", ValidationStatusEnum.ERROR));
        return;
      }

      if (!canElementBeRepeatedUnder(nodeElement, parentElement)) {
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
    return XPathProcessor.parse(xpath).getSteps().get(0).getStepText();
  }

  /**
   * Return the name of the element that is the last step in the given XPath,
   * removing any predicate.
   */
  private String getLastElementName(String xpath) {
    List<XPathStep> steps = XPathProcessor.parse(xpath).getSteps();
    return steps.get(steps.size() - 1).getStepText();
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

  /**
   * Returns true if "element" is referenced in the schema somewhere under the type of "parent",
   * and this reference has maxOccurs > 1.
   * As an element can be at multiple locations in the descendants of a given element, this
   * will just look at the closest occurence of "element".
   * 
   * @param element The element to search for.
   * @param parent  The element under which to search
   * @return        True if the schema allows "element" to be repeated under "parent"
   */
  private boolean canElementBeRepeatedUnder(final XmlSchemaElement element, final XmlSchemaElement parent) {
    visited = new HashSet<>();

    long maxOccurs = findElementMaxOccursUnder(element, parent);

    if (maxOccurs < 0) {
      throw new RuntimeException("Element not found: " + element.getName());
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
