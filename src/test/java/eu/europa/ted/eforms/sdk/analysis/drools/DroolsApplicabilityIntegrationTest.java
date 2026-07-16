package eu.europa.ted.eforms.sdk.analysis.drools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;

import eu.europa.ted.eforms.sdk.analysis.SourceKind;
import eu.europa.ted.eforms.sdk.analysis.ValidationProfile;

/**
 * Locks in two assumptions on Drools 8.44.2:
 *
 * <ol>
 *   <li>The DRL parser surfaces single-arg {@code @key(value)} annotations
 *       in {@code Match.getRule().getMetaData()} with the value's natural
 *       Java type (e.g. {@code Integer} for {@code @sdkMajor(2)}, raw
 *       {@code String} for an identifier-arg or string-arg).</li>
 *   <li>Multi-arg {@code @key(v1, v2)} annotations are surfaced as the raw
 *       inner text as a single {@code String} (e.g. {@code "1, 2"}). The
 *       parser does not split the args into a list.</li>
 * </ol>
 *
 * <p>Those assumptions feed {@link ApplicabilityFilter}'s metadata parsing.
 * If a future Drools upgrade changes the shape, this test fails loudly and
 * we know to revisit the filter's parsing logic.
 *
 * <p>The test compiles a small in-line DRL through the real Drools engine
 * (so the {@code drools-mvel} test dependency is required), reads the
 * metadata that came out, then asserts {@link ApplicabilityFilter#appliesTo}
 * returns the expected boolean across the four-corner profile matrix
 * {@code ({1, FILE}, {2, FILE}, {1, DATABASE}, {2, DATABASE})}.
 *
 * <p><b>API choice note.</b> Production code uses Drools' RuleUnits API,
 * but this test uses the legacy {@link KieServices} / {@code KieBase} API
 * because we only need to compile rules and inspect their metadata, not
 * fire them. RuleUnits would require a concrete unit class and a fact
 * fixture for every rule, which is unnecessary overhead for a metadata
 * shape check.
 */
class DroolsApplicabilityIntegrationTest {

  private static final String DRL = String.join("\n",
      "package eu.europa.ted.eforms.sdk.analysis.drools.appfilter.fixture;",
      "",
      "rule \"unannotated\"",
      "when",
      "then",
      "end",
      "",
      "rule \"single_sdk_major\"",
      "  @sdkMajor(2)",
      "when",
      "then",
      "end",
      "",
      "rule \"multi_sdk_major\"",
      "  @sdkMajor(1, 2)",
      "when",
      "then",
      "end",
      "",
      "rule \"single_source\"",
      "  @source(FILE)",
      "when",
      "then",
      "end",
      "",
      "rule \"multi_source\"",
      "  @source(FILE, DATABASE)",
      "when",
      "then",
      "end",
      "",
      "rule \"both_axes\"",
      "  @sdkMajor(2)",
      "  @source(FILE)",
      "when",
      "then",
      "end",
      ""
  );

  private static Map<String, Map<String, Object>> rulesByName;

  @BeforeAll
  static void compileAndCollect() {
    final KieServices ks = KieServices.Factory.get();
    final KieFileSystem kfs = ks.newKieFileSystem();
    kfs.write("src/main/resources/applicability-fixture.drl", DRL);

    final KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
    if (kb.getResults().hasMessages(Message.Level.ERROR)) {
      fail("Could not compile applicability fixture DRL: "
          + kb.getResults().getMessages(Message.Level.ERROR));
    }

    final KieContainer kc = ks.newKieContainer(kb.getKieModule().getReleaseId());
    rulesByName = new HashMap<>();
    final Collection<KiePackage> packages = kc.getKieBase().getKiePackages();
    for (final KiePackage pkg : packages) {
      for (final Rule rule : pkg.getRules()) {
        rulesByName.put(rule.getName(), rule.getMetaData());
      }
    }
  }

  // --- (1) Metadata-shape assumptions ---

  @Test
  void unannotatedRuleHasEmptyMetadata() {
    assertNotNull(rulesByName.get("unannotated"));
    assertFalse(rulesByName.get("unannotated").containsKey(ApplicabilityFilter.META_SDK_MAJOR));
    assertFalse(rulesByName.get("unannotated").containsKey(ApplicabilityFilter.META_SOURCE));
  }

  @Test
  void singleIntSdkMajorIsExposedAsInteger() {
    final Object raw = rulesByName.get("single_sdk_major").get(ApplicabilityFilter.META_SDK_MAJOR);
    assertEquals(Integer.class, raw.getClass());
    assertEquals(Integer.valueOf(2), raw);
  }

  @Test
  void multiIntSdkMajorIsExposedAsRawString() {
    final Object raw = rulesByName.get("multi_sdk_major").get(ApplicabilityFilter.META_SDK_MAJOR);
    assertEquals(String.class, raw.getClass());
    assertEquals("1, 2", raw);
  }

  @Test
  void singleIdentifierSourceIsExposedAsString() {
    final Object raw = rulesByName.get("single_source").get(ApplicabilityFilter.META_SOURCE);
    assertEquals(String.class, raw.getClass());
    assertEquals("FILE", raw);
  }

  @Test
  void multiIdentifierSourceIsExposedAsRawString() {
    final Object raw = rulesByName.get("multi_source").get(ApplicabilityFilter.META_SOURCE);
    assertEquals(String.class, raw.getClass());
    assertEquals("FILE, DATABASE", raw);
  }

  // --- (2) ApplicabilityFilter behaviour against compiled-DRL metadata ---

  @Test
  void unannotatedRuleAppliesToEveryProfile() {
    final Map<String, Object> meta = rulesByName.get("unannotated");
    for (final ValidationProfile p : allProfiles()) {
      assertTrue(ApplicabilityFilter.appliesTo(meta, p),
          "Unannotated rule should apply to " + p);
    }
  }

  @Test
  void singleSdkMajorAppliesOnlyToMatchingMajor() {
    final Map<String, Object> meta = rulesByName.get("single_sdk_major"); // @sdkMajor(2)
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(1, SourceKind.FILE)));
    assertTrue(ApplicabilityFilter.appliesTo(meta, profile(2, SourceKind.FILE)));
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(1, SourceKind.DATABASE)));
    assertTrue(ApplicabilityFilter.appliesTo(meta, profile(2, SourceKind.DATABASE)));
  }

  @Test
  void multiSdkMajorAppliesToAnyListedMajor() {
    final Map<String, Object> meta = rulesByName.get("multi_sdk_major"); // @sdkMajor(1, 2)
    for (final ValidationProfile p : allProfiles()) {
      assertTrue(ApplicabilityFilter.appliesTo(meta, p),
          "Rule @sdkMajor(1, 2) should apply to " + p);
    }
  }

  @Test
  void singleSourceAppliesOnlyToMatchingKind() {
    final Map<String, Object> meta = rulesByName.get("single_source"); // @source(FILE)
    assertTrue(ApplicabilityFilter.appliesTo(meta, profile(1, SourceKind.FILE)));
    assertTrue(ApplicabilityFilter.appliesTo(meta, profile(2, SourceKind.FILE)));
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(1, SourceKind.DATABASE)));
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(2, SourceKind.DATABASE)));
  }

  @Test
  void multiSourceAppliesToAnyListedKind() {
    final Map<String, Object> meta = rulesByName.get("multi_source"); // @source(FILE, DATABASE)
    for (final ValidationProfile p : allProfiles()) {
      assertTrue(ApplicabilityFilter.appliesTo(meta, p),
          "Rule @source(FILE, DATABASE) should apply to " + p);
    }
  }

  @Test
  void bothAxesAreAndedTogether() {
    final Map<String, Object> meta = rulesByName.get("both_axes"); // @sdkMajor(2) @source(FILE)
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(1, SourceKind.FILE)));
    assertTrue(ApplicabilityFilter.appliesTo(meta, profile(2, SourceKind.FILE)));
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(1, SourceKind.DATABASE)));
    assertFalse(ApplicabilityFilter.appliesTo(meta, profile(2, SourceKind.DATABASE)));
  }

  private static ValidationProfile profile(final int major, final SourceKind kind) {
    return new ValidationProfile(major, kind);
  }

  private static ValidationProfile[] allProfiles() {
    return new ValidationProfile[] {
        profile(1, SourceKind.FILE),
        profile(2, SourceKind.FILE),
        profile(1, SourceKind.DATABASE),
        profile(2, SourceKind.DATABASE)
    };
  }
}
