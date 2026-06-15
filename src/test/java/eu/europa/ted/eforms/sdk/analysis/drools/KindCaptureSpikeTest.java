package eu.europa.ted.eforms.sdk.analysis.drools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.SourceKind;
import eu.europa.ted.eforms.sdk.analysis.domain.codelist.Codelist;
import eu.europa.ted.eforms.sdk.analysis.testutil.EmptySdkContentSource;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;

/**
 * Step-0 spike (gate for the structured-findings work): proves that the engine's before-match hook
 * plus the {@code SdkUnit} result sink pair every {@code ValidationResult} with the rule that
 * produced it. A single mismatched codelist trips several {@code @source(FILE)} rules; we assert that
 * each finding's kind (rule name) matches the rule whose message it carries — which only holds if the
 * hook sets the current rule immediately before each consequence, in fire order.
 */
class KindCaptureSpikeTest {

  @Test
  void eachFindingIsStampedWithItsProducingRuleName() throws Exception {
    final SdkValidator validator =
        new SdkValidator(new MismatchedCodelistSource(), new SdkMetadata("1.15.0"));
    validator.validate();

    final List<Finding> findings = validator.getFindings();
    assertFalse(findings.isEmpty(), "expected findings from the mismatched codelist");

    // No null kind: proves the before-hook ran and set the current rule before each consequence.
    findings.forEach(finding -> assertNotNull(finding.getKind(),
        () -> "finding has no kind: " + finding.getResult()));

    // Correct pairing: each result's kind is the rule that logically emits that message.
    assertEquals("Codelist filenames are as expected",
        findingWithMessage(findings, "does not have the expected filename").getKind());
    assertEquals("Every codelist has a column for the code",
        findingWithMessage(findings, "does not have a 'code' column").getKind());

    // The rule's @problem metadata is read and carried on the finding.
    assertEquals("Codelist filename does not match its id",
        findingWithMessage(findings, "does not have the expected filename").getProblem());
  }

  private static Finding findingWithMessage(final List<Finding> findings, final String needle) {
    return findings.stream()
        .filter(finding -> finding.getResult().getMessage().contains(needle))
        .findFirst()
        .orElseThrow(() -> new AssertionError("no finding with message containing: " + needle));
  }

  /**
   * One codelist whose filename does not match its expected filename and which has no columns, so it
   * trips both "Codelist filenames are as expected" and "Every codelist has a column for the code".
   */
  private static final class MismatchedCodelistSource extends EmptySdkContentSource {
    @Override
    public SourceKind getSourceKind() {
      return SourceKind.FILE;
    }

    @Override
    public Set<Codelist> getCodelists() {
      final Codelist codelist = new Codelist();
      codelist.setId("foo"); // expectedFilename -> "foo.gc"
      codelist.setFilename("wrong.gc"); // mismatch
      codelist.setCodes(Collections.emptyList());
      codelist.setColumnDefinitions(Collections.emptyList()); // no 'code' column
      codelist.setRows(Collections.emptyList());
      return Collections.singleton(codelist);
    }
  }
}
