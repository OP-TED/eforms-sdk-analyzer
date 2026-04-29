package eu.europa.ted.eforms.sdk.analysis.drools;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kie.api.runtime.rule.Match;
import eu.europa.ted.eforms.sdk.analysis.SourceKind;
import eu.europa.ted.eforms.sdk.analysis.ValidationProfile;

/**
 * Decides whether a {@link Match} should fire under a given
 * {@link ValidationProfile}, by inspecting the rule's DRL metadata
 * annotations.
 *
 * <p>Applicability rule (per the framework spec): a rule applies to a profile
 * iff, for {@code every} annotation present on the rule, the profile's value
 * on that axis is {@code in} the annotation's listed set. Rules without
 * annotations apply to every profile. Annotations on multiple axes are
 * AND'd.
 *
 * <p>Supported annotations in DRL:
 * <pre>{@code
 *   rule "..."
 *     @sdkMajor(2)            // single SDK major
 *     @sdkMajor(1, 2)         // any of these
 *     @source(FILE)           // single source kind
 *     @source(FILE, DATABASE) // any of these
 *   when ...
 * }</pre>
 *
 * <p>Drools' DRL parser surfaces single-arg {@code @key(value)} annotations
 * with the value's natural type (e.g. {@code Integer} for {@code @sdkMajor(2)})
 * and multi-arg {@code @key(v1, v2)} annotations as the raw inner text as a
 * {@code String}. We always read via {@code String.valueOf(...)} and split
 * on commas, so both shapes parse uniformly.
 */
final class ApplicabilityFilter {

  static final String META_SDK_MAJOR = "sdkMajor";
  static final String META_SOURCE = "source";

  private ApplicabilityFilter() {}

  static boolean appliesTo(final Match match, final ValidationProfile profile) {
    return appliesTo(match.getRule().getMetaData(), profile);
  }

  static boolean appliesTo(final Map<String, Object> metadata, final ValidationProfile profile) {
    final Object sdkMajorRaw = metadata.get(META_SDK_MAJOR);
    if (sdkMajorRaw != null
        && !parseInts(sdkMajorRaw).contains(profile.getSdkMajor())) {
      return false;
    }

    final Object sourceRaw = metadata.get(META_SOURCE);
    if (sourceRaw != null
        && !parseSourceKinds(sourceRaw).contains(profile.getSourceKind())) {
      return false;
    }

    return true;
  }

  private static List<Integer> parseInts(final Object raw) {
    return Arrays.stream(String.valueOf(raw).split(","))
        .map(String::trim)
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }

  private static List<SourceKind> parseSourceKinds(final Object raw) {
    return Arrays.stream(String.valueOf(raw).split(","))
        .map(String::trim)
        .map(SourceKind::valueOf)
        .collect(Collectors.toList());
  }
}
