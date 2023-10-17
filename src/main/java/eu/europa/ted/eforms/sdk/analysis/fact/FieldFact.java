package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

import eu.europa.ted.eforms.sdk.analysis.domain.field.AbstractConstraint;
import eu.europa.ted.eforms.sdk.analysis.domain.field.AbstractFieldProperty;
import eu.europa.ted.eforms.sdk.analysis.domain.field.BooleanConstraint;
import eu.europa.ted.eforms.sdk.analysis.domain.field.Field;
import eu.europa.ted.eforms.sdk.analysis.domain.field.FieldPrivacy;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlElementPosition;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;
import eu.europa.ted.eforms.xpath.XPathInfo;
import eu.europa.ted.eforms.xpath.XPathProcessor;

public class FieldFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -8325643682910825716L;

  private Field field;

  private int stepCount = 0;

  public FieldFact(Field field) {
    this.field = field;
  }

  public XmlStructureNode getParent() {
    return field.getParentNode();
  }

  public String getParentId() {
    return field.getParentNodeId();
  }

  public List<XmlElementPosition> getXsdSequenceOrder() {
    return field.getXsdSequenceOrder();
  }

  public int getXsdSequenceOrderCount() {
    return getXsdSequenceOrder() == null ? 0 : getXsdSequenceOrder().size();
  }

  public boolean hasAncestor(String ancestorNodeId) {
    if (StringUtils.isBlank(ancestorNodeId)) {
      return false;
    }

    XmlStructureNode currentAncestor = field.getParentNode();

    while (currentAncestor != null) {
      if (currentAncestor.getId().equals(ancestorNodeId)) {
        return true;
      }

      currentAncestor = currentAncestor.getParent();
    }

    return false;
  }

  public BooleanConstraint getForbiddenConstraintWithoutCondition() {
    if (field.getForbidden() != null) {
      List<BooleanConstraint> constraints = field.getForbidden().getConstraints();

      if (constraints != null) {
        return constraints.stream()
            .filter(
                (BooleanConstraint constraint) -> StringUtils.isBlank(constraint.getCondition()))
            .findFirst()
            .orElse(null);
      }
    }

    return null;
  }

  /*
   * Return the constraint representing when the field is always mandatory.
   * This is the "mandatory" constraint without a condition, but also without any notice
   * subtype for which the field can be forbidden.
   */
  public BooleanConstraint getAlwaysMandatoryConstraint() {
    if (field.getMandatory() == null) {
      return null;
    }

    List<BooleanConstraint> constraints = field.getMandatory().getConstraints();

    if (constraints == null) {
      return null;
    }

    // the one mandatory constraint with no condition
    BooleanConstraint mandatory = constraints.stream()
          .filter(
              (BooleanConstraint constraint) -> StringUtils.isBlank(constraint.getCondition()))
          .findFirst()
          .orElse(null);
    
    if (mandatory != null && field.getForbidden() != null) {
      // If the field can be forbidden for a notice subtype, then it's not always mandatory,
      // So we remove those notice subtypes from the constraint.
      mandatory.getNoticeTypes().removeAll(field.getForbidden().getAllNoticeTypeIds());
    }

    return mandatory;
  }

  public String getXpathAbsolute() {
    return field.getXpathAbsolute();
  }

  public String getXpathRelative() {
    return field.getXpathRelative();
  }

  public int getXpathRelativeElementCount() {
    if (stepCount == 0 && getXpathRelative() != null) {
      XPathInfo xpathInfo = XPathProcessor.parse(getXpathRelative());
      stepCount = xpathInfo.getSteps().size();
      if (xpathInfo.isAttribute()) {
        // we don't want to count attributes
        stepCount--;
      }
    }
    return stepCount;
  }

  public String getType() {
    return field.getType();
  }

  /*
   * Return a stream of the dynamic properties of the field
   */
  private Stream<AbstractFieldProperty<? extends AbstractConstraint<?>, ?>> getDynamicProperties() {
    return Stream.of(field.getRepeatable(), field.getForbidden(), field.getMandatory(), 
            field.getCodeList(), field.getPattern(), field.getRangeNumeric(), field.getAssertion(),
            field.getInChangeNotice(), field.getInContinueProcedure());
  }

  /**
   * Return the notices types referenced in all properties of the field.
   */
  public Set<String> getAllNoticeTypes() {
    Set<String> noticeTypes = new HashSet<>();
    
    // Go over all dynamic properties and collect referenced notice types
    getDynamicProperties()
        .forEach(property -> {
          if (property != null) {
            noticeTypes.addAll(property.getAllNoticeTypeIds());
          }
        });
    
    return noticeTypes;
  }

  public String getPrivacyCode() {
    if (field.getPrivacy() != null) {
      return field.getPrivacy().getCode().getLiteral();
    } else {
      return null;
    }
  }

  public String getUnpublishedFieldId() {
    if (field.getPrivacy() != null) {
      return field.getPrivacy().getUnpublishedFieldId();
    } else {
      return null;
    }
  }

  /*
   * Return the various field identifiers indicated in the "privacy" property.
   */
  public Set<String> getReferencedPrivacyFieldIds() {
    Set<String> fieldReferences = new HashSet<>();
    
    FieldPrivacy privacy = field.getPrivacy();

    if (privacy != null) {
      fieldReferences = Stream.of(privacy.getUnpublishedFieldId(), privacy.getReasonCodeFieldId(),
              privacy.getReasonDescriptionFieldId(), privacy.getPublicationDateFieldId())
          .collect(Collectors.toSet());
    }

    return fieldReferences;
  }

    /**
   * Returns the list of ancestors, starting from the field: parent node, then grand-parent, etc.
   * If the structure is incorrect and a node is in its ancestor, this will put the node in the
   * list and return it, to avoid going into an infinite loop.
   */
  public List<XmlStructureNode> getAncestors() {
    List<XmlStructureNode> result = new ArrayList<>();
    XmlStructureNode currentNode = getParent();

    while (currentNode != null) {
      if (result.contains(currentNode)) {
        // A node is its own ancestor. Add it to the list of ancestors,
        // but break to avoid going into an infinite loop.
        result.add(currentNode);
        break;
      }
      result.add(currentNode);

      currentNode = currentNode.getParent();
    }

    return result;
  }

  public List<XmlStructureNode> getAllRepeatableAncestors() {
    List<XmlStructureNode> result;

    result = this.getAncestors().stream().filter(n -> n.isRepeatable()).collect(Collectors.toList());

    return result;
  }

  public String getCodelistId() {
    if (field.getCodeList() != null) {
      return field.getCodeList().getValue().getId();
    }
    return null;
  }

  /*
   * Return all label identifiers referenced by a field
   */
  public Set<String> getAllLabelIds() {
    Set<String> labelIds = new HashSet<>();

    // Go through every property that can contain a reference to a label
    // Dynamic properties can reference a label, but it is not currently not used.
    getDynamicProperties().forEach(property -> {
      if (property != null) {
        labelIds.add(property.getMessage());
        // Constraints can also reference a label
        labelIds.addAll(property.getAllConstraintsLabelIds());
      }
    });

    return labelIds;
  }

  @Override
  public String getId() {
    return field.getId();
  }

  @Override
  public String getTypeName() {
    return "field";
  }
}
