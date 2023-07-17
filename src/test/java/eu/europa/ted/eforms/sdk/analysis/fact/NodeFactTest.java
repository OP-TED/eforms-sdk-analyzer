package eu.europa.ted.eforms.sdk.analysis.fact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import eu.europa.ted.eforms.sdk.analysis.domain.field.XmlStructureNode;

class NodeFactTest {
  @Test
  void testGetFirstRepeatableAncestor() {
    /*Create the following structure:
     *  a (root)
     *  |
     *  ab (repeatable)
     *  |
     *  abc (repeatable)
     *  |
     *  leaf
     */
    XmlStructureNode a = new XmlStructureNode();
    a.setId("a");

    XmlStructureNode ab = new XmlStructureNode();
    ab.setId("ab");
    ab.setParent(a);
    ab.setRepeatable(true);

    XmlStructureNode abc = new XmlStructureNode();
    abc.setId("abc");
    abc.setParent(ab);
    abc.setRepeatable(true);

    XmlStructureNode leaf = new XmlStructureNode();
    leaf.setId("leaf");
    leaf.setParent(abc);

    NodeFact nodeFact = new NodeFact(leaf);
    assertEquals(abc, nodeFact.getFirstRepeatableAncestor());
  }
}
