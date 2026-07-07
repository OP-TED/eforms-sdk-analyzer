package eu.europa.ted.eforms.sdk.analysis.fact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.domain.SvrlReport;
import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * File-backed facts derive their sdkPath from a filename that only exists when the content was
 * read from files. Under a DATABASE source the filename is legitimately null — the path must then
 * be null (not a "codelists/null" string), so that {@link ValidationResult#getSubject()} falls back
 * to the asset id.
 */
class FileBackedFactSdkPathTest {

  @Test
  void codelistSdkPathIsNullWithoutFilename() {
    final Codelist codelist = new Codelist();
    codelist.setId("nuts-lv13");

    final CodelistFact fact = new CodelistFact(codelist);

    assertNull(fact.getSdkPath());
  }

  @Test
  void codelistSubjectFallsBackToIdWithoutFilename() {
    final Codelist codelist = new Codelist();
    codelist.setId("nuts-lv13");

    final ValidationResult result = new ValidationResult(
        new CodelistFact(codelist), "message", ValidationStatusEnum.ERROR);

    assertEquals(new AssetRef("codelist", "nuts-lv13"), result.getSubject());
  }

  @Test
  void codelistSdkPathUsesFilenameWhenPresent() {
    final Codelist codelist = new Codelist();
    codelist.setId("nuts-lv13");
    codelist.setFilename("nuts_nuts-lv13.gc");

    final ValidationResult result = new ValidationResult(
        new CodelistFact(codelist), "message", ValidationStatusEnum.ERROR);

    assertEquals("codelists/nuts_nuts-lv13.gc", result.getSubject().getId());
  }

  @Test
  void viewTemplateSdkPathIsNullWithoutFilename() {
    final TedefoViewTemplateIndex viewTemplate = new TedefoViewTemplateIndex();
    viewTemplate.setId("summary");

    final ViewTemplateFact fact = new ViewTemplateFact(viewTemplate);

    assertNull(fact.getSdkPath());
    assertEquals(new AssetRef("viewTemplate", "summary"),
        new ValidationResult(fact, "message", ValidationStatusEnum.ERROR).getSubject());
  }

  @Test
  void svrlReportSdkPathIsNullWithoutFilename() {
    // Only getSdkPath is exercised: the id of an SVRL report is itself derived from the
    // filename, so a source that emits these facts must always provide one (the DATABASE
    // source emits none at all).
    assertNull(new SvrlReportFact(new SvrlReport(null, 0)).getSdkPath());
  }

  @Test
  void xmlNoticeSdkPathIsNullWithoutFilename() {
    assertNull(new XmlNoticeFact(new XmlNotice(null, null)).getSdkPath());
  }
}
