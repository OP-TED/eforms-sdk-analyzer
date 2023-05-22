package eu.europa.ted.eforms.sdk.domain.noticetype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class NoticeTypeContentTest {
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
    NoticeTypeContent a = new NoticeTypeContent();
    a.setId("a");

    NoticeTypeContent ab = new NoticeTypeContent();
    ab.setId("ab");
    ab.setParent(a);
    ab.setRepeatable(true);

    NoticeTypeContent abc = new NoticeTypeContent();
    abc.setId("abc");
    abc.setParent(ab);
    abc.setRepeatable(true);

    NoticeTypeContent leaf = new NoticeTypeContent();
    leaf.setId("leaf");
    leaf.setParent(abc);

    assertEquals(abc, leaf.getFirstRepeatableAncestor());
  }
}
