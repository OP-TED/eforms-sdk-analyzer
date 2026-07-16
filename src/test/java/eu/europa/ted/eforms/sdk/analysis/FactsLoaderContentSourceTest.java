package eu.europa.ted.eforms.sdk.analysis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.testutil.EmptySdkContentSource;

/**
 * Verifies that {@link FactsLoader} can be driven by an arbitrary
 * {@link SdkContentSource}, without any file system access.
 */
class FactsLoaderContentSourceTest {

  @Test
  void newConstructorLoadsAllFactsFromContentSourceWithoutFileSystem() throws Exception {
    final FactsLoader loader = new FactsLoader(new EmptySdkContentSource());

    assertNotNull(loader.loadFieldsAndNodesMetadata());
    assertNotNull(loader.loadFields());
    assertNotNull(loader.loadBusinessEntities());
    assertNotNull(loader.loadNodes());
    assertNotNull(loader.loadNoticeTypes());
    assertNotNull(loader.loadNoticeTypesIndex());
    assertNotNull(loader.loadTranslationsIndex());
    assertNotNull(loader.loadLabels());
    assertNotNull(loader.loadViewTemplates());
    assertNotNull(loader.loadViewTemplatesIndex());
    assertNotNull(loader.loadDocumentTypes());
    assertNotNull(loader.loadCodelists());
    assertNotNull(loader.loadCodelistsIndex());
    assertNotNull(loader.loadXmlNotices());
    assertNotNull(loader.loadSvrlReports());
    assertNotNull(loader.loadSchematrons());
  }
}
