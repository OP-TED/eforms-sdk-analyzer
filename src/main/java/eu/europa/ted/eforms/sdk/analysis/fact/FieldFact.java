package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import eu.europa.ted.eforms.sdk.domain.field.BooleanConstraint;
import eu.europa.ted.eforms.sdk.domain.field.Field;
import eu.europa.ted.eforms.sdk.domain.field.XmlStructureNode;
import lombok.Data;

@Data
public class FieldFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -8325643682910825716L;

  private Field field;

  public FieldFact(Field field) {
    this.field = field;
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

  @Override
  public String getId() {
    return field.getId();
  }

  @Override
  public String getTypeName() {
    return "field";
  }
}
