package eu.europa.ted.eforms.sdk.analysis.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.SdkContentSource;
import eu.europa.ted.eforms.sdk.analysis.SourceKind;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.DocumentType;
import eu.europa.ted.eforms.sdk.analysis.domain.noticetype.NoticeTypesForIndex;
import eu.europa.ted.eforms.sdk.analysis.testutil.EmptySdkContentSource;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;

/**
 * End-to-end "the wiring works" check: drives {@link SdkValidator#validate()}
 * with a non-trivial fact (a single codelist with a mismatched filename) and
 * confirms that the {@code @source(FILE)}-tagged rule "Codelist filenames are
 * as expected" actually fires under a FILE source and is skipped under a
 * DATABASE source. Exercises the full path:
 *
 * <pre>
 *   SdkMetadata
 *     -> SdkValidator builds a ValidationProfile from metadata.major + source.getSourceKind()
 *     -> RulesRunner.execute(unit, profile, ...)
 *     -> ApplicabilityFilter skips/keeps rules per their DRL annotations
 * </pre>
 */
class SdkValidatorProfileFilteringTest {

  private static final String RULE_NAME = "Codelist filenames are as expected";

  @Test
  void fileOnlyRuleFiresWhenSourceIsFile() throws Exception {
    final SdkValidator validator =
        new SdkValidator(new SingleMismatchedCodelistSource(SourceKind.FILE),
            new SdkMetadata("1.15.0"));
    validator.validate();

    assertTrue(validator.getFiredRulesNames().contains(RULE_NAME),
        "Rule '" + RULE_NAME + "' is tagged @source(FILE) and the source declares FILE, "
            + "so the rule must fire on a mismatched codelist filename. "
            + "Fired rules were: " + validator.getFiredRulesNames());
  }

  @Test
  void fileOnlyRuleIsSkippedWhenSourceIsDatabase() throws Exception {
    final SdkValidator validator =
        new SdkValidator(new SingleMismatchedCodelistSource(SourceKind.DATABASE),
            new SdkMetadata("1.15.0"));
    validator.validate();

    assertFalse(validator.getFiredRulesNames().contains(RULE_NAME),
        "Rule '" + RULE_NAME + "' is tagged @source(FILE) and the source declares DATABASE, "
            + "so the rule must be skipped by the applicability filter. "
            + "Fired rules were: " + validator.getFiredRulesNames());
  }

  /**
   * Regression: a custom FILE-source providing a document type but no path
   * (e.g. an in-memory or archive-backed reader) must not NPE inside
   * {@code DocumentTypeFact.schemaLocationExists}. The
   * {@code @source(FILE)}-tagged "Document types use existing schemaLocation"
   * rule still applies — but the underlying check has to handle a null
   * {@code sdkRoot} gracefully.
   */
  @Test
  void fileSourceWithDocumentTypeAndNoSdkRootDoesNotThrow() throws Exception {
    final SdkValidator validator =
        new SdkValidator(new SingleDocumentTypeFileSource(), new SdkMetadata("1.15.0"));
    // The actual assertion is that validate() returns without throwing NPE.
    validator.validate();
  }

  /**
   * A stub {@link SdkContentSource} that overrides only the source kind and
   * the codelists set. Returns one codelist whose {@code filename} does not
   * match its computed {@code expectedFilename}, which is the precondition for
   * "Codelist filenames are as expected" to match.
   */
  private static final class SingleMismatchedCodelistSource extends EmptySdkContentSource {
    private final SourceKind kind;

    SingleMismatchedCodelistSource(final SourceKind kind) {
      this.kind = kind;
    }

    @Override
    public SourceKind getSourceKind() {
      return this.kind;
    }

    @Override
    public Set<Codelist> getCodelists() {
      final Codelist cl = new Codelist();
      cl.setId("foo"); // expectedFilename → "foo.gc"
      cl.setFilename("wrong.gc"); // mismatch triggers the rule
      cl.setCodes(Collections.emptyList());
      cl.setColumnDefinitions(Collections.emptyList());
      cl.setRows(Collections.emptyList());
      return Collections.singleton(cl);
    }
  }

  /**
   * A FILE-source stub that supplies one {@link DocumentType} but exposes no
   * SDK root (constructed via the source-based {@code SdkValidator} ctor, so
   * {@code SdkUnit.sdkRoot} stays null). Reproduces the scenario where the
   * {@code Document types use existing schemaLocation} rule fires under the
   * applicability filter and reaches {@code schemaLocationExists(null)}.
   */
  private static final class SingleDocumentTypeFileSource extends EmptySdkContentSource {
    @Override
    public SourceKind getSourceKind() {
      return SourceKind.FILE;
    }

    @Override
    public NoticeTypesForIndex getNoticeTypesForIndex() {
      return new NoticeTypesForIndex() {
        private static final long serialVersionUID = 1L;

        @Override
        public java.util.List<DocumentType> getDocumentTypes() {
          final DocumentType dt = new DocumentType() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getId() {
              return "BRIN";
            }

            @Override
            public String getSchemaLocation() {
              return "schemas/maindoc/BRIN.xsd";
            }
          };
          return Collections.singletonList(dt);
        }
      };
    }
  }
}
