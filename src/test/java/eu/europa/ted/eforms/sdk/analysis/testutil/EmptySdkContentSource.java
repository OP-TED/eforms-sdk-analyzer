package eu.europa.ted.eforms.sdk.analysis.testutil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import eu.europa.ted.eforms.sdk.analysis.SdkContentSource;
import eu.europa.ted.eforms.sdk.analysis.SourceKind;
import eu.europa.ted.eforms.sdk.analysis.domain.EFormsTrackableEntity;
import eu.europa.ted.eforms.sdk.analysis.domain.SvrlReport;
import eu.europa.ted.eforms.sdk.analysis.domain.XmlNotice;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.CodelistsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.field.BusinessEntity;
import eu.europa.ted.eforms.sdk.analysis.domain.field.FieldsAndNodes;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.domain.label.TranslationsIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeTypesForIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.schematron.SchematronFile;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplatesIndex;

/**
 * Stub {@link SdkContentSource} for tests. Every accessor returns an empty
 * collection or a default domain object, with subclass overrides where the
 * real domain type leaves a collection field uninitialized (so the FactsLoader
 * can iterate without an NPE).
 */
public class EmptySdkContentSource implements SdkContentSource {

  @Override
  public SourceKind getSourceKind() {
    return SourceKind.FILE;
  }

  @Override
  public EFormsTrackableEntity getFieldsAndNodesMetadata() {
    return new EFormsTrackableEntity();
  }

  @Override
  public FieldsAndNodes getFieldsAndNodes() {
    return new FieldsAndNodes() {
      private static final long serialVersionUID = 1L;

      @Override
      public List<BusinessEntity> getBusinessEntities() {
        return Collections.emptyList();
      }
    };
  }

  @Override
  public Set<NoticeType> getNoticeTypes() {
    return Collections.emptySet();
  }

  @Override
  public NoticeTypesForIndex getNoticeTypesForIndex() {
    return new NoticeTypesForIndex();
  }

  @Override
  public TranslationsIndex getTranslationsIndex() {
    return new TranslationsIndex();
  }

  @Override
  public Set<Label> getLabels() {
    return Collections.emptySet();
  }

  @Override
  public TedefoViewTemplatesIndex getViewTemplatesIndex() {
    return new TedefoViewTemplatesIndex() {
      private static final long serialVersionUID = 1L;

      @Override
      public List<TedefoViewTemplateIndex> getViewTemplates() {
        return Collections.emptyList();
      }
    };
  }

  @Override
  public Set<Codelist> getCodelists() {
    return Collections.emptySet();
  }

  @Override
  public CodelistsIndex getCodelistsIndex() {
    return new CodelistsIndex();
  }

  @Override
  public Set<XmlNotice> getXmlNotices() {
    return Collections.emptySet();
  }

  @Override
  public Set<SvrlReport> getSvrlReports() {
    return Collections.emptySet();
  }

  @Override
  public Set<SchematronFile> getSchematronFiles() {
    return Collections.emptySet();
  }
}
