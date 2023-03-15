package eu.europa.ted.eforms.sdk.analysis.fact;

import eu.europa.ted.eforms.sdk.domain.field.XmlStructureNode;
import lombok.Data;

@Data
public class NodeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -6237630016231337698L;

  private XmlStructureNode node;

  public NodeFact(XmlStructureNode node) {
    this.node = node;
  }

  public boolean isRepeatable() {
    return node.isRepeatable();
  }

  public XmlStructureNode getFirstRepeatableParent() {
    XmlStructureNode result = new XmlStructureNode();
    XmlStructureNode currentNode = node.getParent();

    while (currentNode != null) {
      if (currentNode.isRepeatable()) {
        result = currentNode;
      }

      currentNode = currentNode.getParent();
    }

    return result;
  }

  @Override
  public String getId() {
    return node.getId();
  }

  @Override
  public String getTypeName() {
    return "node";
  }
}
