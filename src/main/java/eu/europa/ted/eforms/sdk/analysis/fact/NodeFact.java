package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlElementPosition;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;
import eu.europa.ted.eforms.sdk.analysis.util.XPathSplitter;

public class NodeFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -6237630016231337698L;

  private XmlStructureNode node;

  private int stepCount = 0;

  public NodeFact(XmlStructureNode node) {
    this.node = node;
  }

  public String getParentId() {
    return node.getParentId();
  }
  
  public XmlStructureNode getParent() {
    return node.getParent();
  }
  
  public List<XmlElementPosition> getXsdSequenceOrder() {
    return node.getXsdSequenceOrder();
  }

  public int getXsdSequenceOrderCount() {
    return getXsdSequenceOrder() == null ? 0 : getXsdSequenceOrder().size();
  }

  public boolean isRepeatable() {
    return node.isRepeatable();
  }

  /**
   * Returns the list of ancestors, starting from the node: the node parent, then grand-parent, etc.
   * If the structure is incorrect and a node is in its ancestor, this will put the node in the
   * list and return it, to avoid going into an infinite loop.
   */
  public List<XmlStructureNode> getAncestors() {
    List<XmlStructureNode> result = new ArrayList<>();
    XmlStructureNode currentNode = node.getParent();

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

  public XmlStructureNode getFirstRepeatableAncestor() {
    XmlStructureNode result = new XmlStructureNode();

    result = this.getAncestors().stream().filter(n -> n.isRepeatable()).findFirst().orElse(result);

    return result;
  }

  public String getXpathAbsolute() {
    return node.getXpathAbsolute();
  }

  public String getXpathRelative() {
    return node.getXpathRelative();
  }

  public int getXpathRelativeStepCount() {
    if (stepCount == 0 && getXpathRelative() != null) {
      stepCount = XPathSplitter.getStepElementNames(getXpathRelative()).size();
    }
    return stepCount;
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
