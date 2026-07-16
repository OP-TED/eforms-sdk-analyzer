package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.util.Set;
import jakarta.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

import eu.europa.ted.eforms.sdk.analysis.domain.EFormsTrackableEntity;
import eu.europa.ted.eforms.sdk.analysis.domain.SvrlReport;
import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.CodelistsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.field.FieldsAndNodes;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.domain.label.TranslationsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeTypesForIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplatesIndex;

/**
 * Abstraction over a source of eForms SDK content that the analyzer can consume.
 * The default implementation is {@link SdkLoader}, which reads from the file
 * system. Alternative implementations (e.g. backed by a metadata database) let
 * callers run the analyzer's checks against content that does not yet exist as
 * an exported SDK on disk.
 */
public interface SdkContentSource {

  /**
   * The kind of source backing this implementation. Drives the rule
   * applicability framework: rules tagged for one source kind only (e.g.
   * file-only checks of schema locations or filenames) are skipped when the
   * source kind does not match.
   *
   * <p>Implementations declare their nature; callers do not. This makes
   * mismatch structurally impossible.
   */
  SourceKind getSourceKind();

  EFormsTrackableEntity getFieldsAndNodesMetadata() throws IOException;

  FieldsAndNodes getFieldsAndNodes() throws IOException;

  Set<NoticeType> getNoticeTypes() throws IOException;

  NoticeTypesForIndex getNoticeTypesForIndex() throws IOException;

  TranslationsIndex getTranslationsIndex() throws IOException;

  Set<Label> getLabels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException;

  TedefoViewTemplatesIndex getViewTemplatesIndex() throws IOException;

  Set<Codelist> getCodelists()
      throws IOException, JAXBException, SAXException, ParserConfigurationException;

  CodelistsIndex getCodelistsIndex() throws IOException;

  Set<XmlNotice> getXmlNotices()
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException;

  Set<SvrlReport> getSvrlReports()
      throws IOException, SAXException, ParserConfigurationException, XPathExpressionException;

  Set<SchematronFile> getSchematronFiles();
}
