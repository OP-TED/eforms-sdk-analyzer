package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import eu.europa.ted.eforms.sdk.domain.field.BooleanConstraint;
import eu.europa.ted.eforms.sdk.domain.field.Field;
import eu.europa.ted.eforms.sdk.domain.field.XmlStructureNode;

public class FieldFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -8325643682910825716L;

  private Field field;

  public FieldFact(Field field) {
    this.field = field;
  }

  public XmlStructureNode getParent() {
    return field.getParentNode();
  }

  public String getParentId() {
    return field.getParentNodeId();
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

  public String getXpathAbsolute() {
    return field.getXpathAbsolute();
  }

  public String getXpathRelative() {
    return field.getXpathRelative();
  }

  /**
   * Return the notices types referenced in all properties of the field.
   */
  public Set<String> getAllNoticeTypes() {
    Set<String> noticeTypes = new HashSet<>();
    
    // Go over all dynamic properties and collect referenced notice types
    Stream.of(field.getRepeatable(), field.getForbidden(), field.getMandatory(), 
            field.getCodeList(), field.getPattern(), field.getRangeNumeric(), field.getAssertion(),
            field.getInChangeNotice(), field.getInContinueProcedure())
        .forEach(c -> {
          if (c != null) {
            noticeTypes.addAll(c.getAllNoticeTypeIds());
          }
        });
    
    return noticeTypes;
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
