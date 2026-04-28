package eu.europa.ted.eforms.sdk.analysis.drools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.SourceKind;
import eu.europa.ted.eforms.sdk.analysis.ValidationProfile;

/**
 * Verifies the rule applicability rule: a rule applies to a profile iff, for
 * every annotation present, the profile's value on that axis is in the
 * annotation's listed set. Annotations on multiple axes are AND'd. Rules
 * without annotations apply to every profile.
 *
 * <p>The test exercises {@link ApplicabilityFilter#appliesTo(Map, ValidationProfile)}
 * by constructing the metadata map directly, to avoid having to compile DRL
 * at runtime (which would require an additional Drools test dependency).
 */
class ApplicabilityFilterTest {

  private static final ValidationProfile FILE_1 = new ValidationProfile(1, SourceKind.FILE);
  private static final ValidationProfile FILE_2 = new ValidationProfile(2, SourceKind.FILE);
  private static final ValidationProfile DB_1 = new ValidationProfile(1, SourceKind.DATABASE);
  private static final ValidationProfile DB_2 = new ValidationProfile(2, SourceKind.DATABASE);

  @Test
  void noAnnotationsAppliesToEveryProfile() {
    final Map<String, Object> none = Collections.emptyMap();
    assertTrue(ApplicabilityFilter.appliesTo(none, FILE_1));
    assertTrue(ApplicabilityFilter.appliesTo(none, FILE_2));
    assertTrue(ApplicabilityFilter.appliesTo(none, DB_1));
    assertTrue(ApplicabilityFilter.appliesTo(none, DB_2));
  }

  @Test
  void singleSdkMajorMatchesOnlyThatVersion() {
    // Drools surfaces single-int @sdkMajor(2) as Integer.
    final Map<String, Object> meta = Map.of("sdkMajor", Integer.valueOf(2));
    assertFalse(ApplicabilityFilter.appliesTo(meta, FILE_1));
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_2));
  }

  @Test
  void multiSdkMajorMatchesAnyListedVersion() {
    // Drools surfaces @sdkMajor(1, 2) as the raw String "1, 2".
    final Map<String, Object> meta = Map.of("sdkMajor", "1, 2");
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_1));
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_2));
    final Map<String, Object> only2 = Map.of("sdkMajor", "2");
    assertFalse(ApplicabilityFilter.appliesTo(only2, FILE_1));
  }

  @Test
  void singleSourceMatchesOnlyThatKind() {
    // Drools surfaces @source(FILE) as the String "FILE".
    final Map<String, Object> meta = Map.of("source", "FILE");
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_1));
    assertFalse(ApplicabilityFilter.appliesTo(meta, DB_1));
  }

  @Test
  void multiSourceMatchesAnyListedKind() {
    // Drools surfaces @source(FILE, DATABASE) as the raw String "FILE, DATABASE".
    final Map<String, Object> meta = Map.of("source", "FILE, DATABASE");
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_1));
    assertTrue(ApplicabilityFilter.appliesTo(meta, DB_1));
  }

  @Test
  void multipleAxesAreAndedTogether() {
    // @sdkMajor(2) @source(FILE) — both must match.
    final Map<String, Object> meta = Map.of("sdkMajor", Integer.valueOf(2), "source", "FILE");
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_2));
    assertFalse(ApplicabilityFilter.appliesTo(meta, FILE_1)); // wrong sdkMajor
    assertFalse(ApplicabilityFilter.appliesTo(meta, DB_2));   // wrong source
    assertFalse(ApplicabilityFilter.appliesTo(meta, DB_1));   // both wrong
  }

  @Test
  void unrelatedMetadataKeysAreIgnored() {
    final Map<String, Object> meta = Map.of("author", "alice", "since", "2024-01");
    assertTrue(ApplicabilityFilter.appliesTo(meta, FILE_1));
    assertTrue(ApplicabilityFilter.appliesTo(meta, DB_2));
  }
}
