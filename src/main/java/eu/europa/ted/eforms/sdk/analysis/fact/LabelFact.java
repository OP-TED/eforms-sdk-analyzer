package eu.europa.ted.eforms.sdk.analysis.fact;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class LabelFact implements SdkComponentFact<String> {
  private static final long serialVersionUID = -8325643682910825716L;

  // Match a label identifier token, e.g. "expression|name|906" or "business-entity|name|UBO".
  private static final Pattern LABEL_ID_PATTERN =
      Pattern.compile("[a-z][a-z-]*\\|[a-z]+(?:\\|\\S+)?");

  private final Label label;

  private transient List<ValidationResult> labelIdentifierResults;
  private transient List<ValidationResult> invalidCharacterResults;

  public LabelFact(Label label) {
    this.label = label;
  }

  @Override
  public String getId() {
    return this.label.getId();
  }

  public String getNormalizedId() {
    String id = this.label.getId();
    return id.strip().toLowerCase(Locale.ROOT).replaceAll("[-_\\.]", "");
  }

  @Override
  public String getTypeName() {
    return "label";
  }

  /**
   * Translations whose text contains a label identifier — the exporter leaves the identifier in
   * place when it cannot resolve a label, so its presence means the referenced label is missing.
   * One result per language, carrying the offending identifier(s) as label references.
   */
  public List<ValidationResult> labelIdentifierResults() {
    if (this.labelIdentifierResults == null) {
      this.labelIdentifierResults = computeLabelIdentifierResults();
    }
    return this.labelIdentifierResults;
  }

  public boolean hasLabelIdentifier() {
    return !labelIdentifierResults().isEmpty();
  }

  /**
   * Translations whose text contains characters that should never appear in a human-readable label
   * (control, format, private-use, surrogate or unassigned). One result per offending character.
   */
  public List<ValidationResult> invalidCharacterResults() {
    if (this.invalidCharacterResults == null) {
      this.invalidCharacterResults = computeInvalidCharacterResults();
    }
    return this.invalidCharacterResults;
  }

  public boolean hasInvalidCharacter() {
    return !invalidCharacterResults().isEmpty();
  }

  private List<ValidationResult> computeLabelIdentifierResults() {
    final List<ValidationResult> found = new ArrayList<>();
    this.label.getTranslations().forEach((lang, text) -> {
      if (text == null) {
        return;
      }
      final Matcher matcher = LABEL_ID_PATTERN.matcher(text);
      // A set so an identifier repeated in one translation is listed once.
      final Set<String> ids = new LinkedHashSet<>();
      while (matcher.find()) {
        ids.add(matcher.group());
      }
      if (!ids.isEmpty()) {
        final String message = String.format("Label in %s contains label identifier(s): %s", lang,
            String.join(", ", ids));
        found.add(new ValidationResult(this, message, ValidationStatusEnum.ERROR,
            ids.stream().map(AssetRef::label).collect(Collectors.toList())));
      }
    });
    return List.copyOf(found);
  }

  private List<ValidationResult> computeInvalidCharacterResults() {
    final List<ValidationResult> found = new ArrayList<>();
    // Render the code point (U+XXXX), never the raw character: these are control/format/etc.
    // characters that would otherwise inject non-printable bytes into the report.
    this.label.getTranslations().forEach((lang, text) -> {
      if (text == null) {
        return;
      }
      text.codePoints()
          .filter(this::isInvalidCharacter)
          .forEach(codePoint -> found.add(new ValidationResult(this,
              String.format("Label in %s contains invalid character [U+%04X]", lang, codePoint),
              ValidationStatusEnum.ERROR)));
    });
    return List.copyOf(found);
  }

  private boolean isInvalidCharacter(final int codePoint) {
    switch (Character.getType(codePoint)) {
      case Character.CONTROL:
      case Character.FORMAT:
      case Character.PRIVATE_USE:
      case Character.SURROGATE:
      case Character.UNASSIGNED:
        return true;
      default:
        return false;
    }
  }
}
