package eu.europa.ted.eforms.sdk.analysis.fact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.vo.AssetRef;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Characterises the translation-text checks that {@link LabelFact} exposes (and which the drools
 * label-text rules and {@link eu.europa.ted.eforms.sdk.analysis.validator.TextValidator} delegate
 * to): label-identifier leaks and invalid characters.
 */
class LabelFactTest {

  private LabelFact labelWith(final String text) {
    final Label label = new Label("code|name|test");
    label.addTranslation(Language.EN, text);
    return new LabelFact(label);
  }

  @Test
  void flagsLeakedLabelIdentifier() {
    final LabelFact fact = labelWith("is mandatory when: expression|name|906");

    assertTrue(fact.hasLabelIdentifier());
    final List<ValidationResult> results = fact.labelIdentifierResults();
    assertEquals(1, results.size());

    final ValidationResult result = results.get(0);
    assertEquals(List.of(AssetRef.label("expression|name|906")), result.getReferences());
    assertTrue(result.getMessage().contains("expression|name|906"),
        () -> "message should name the identifier: " + result.getMessage());
  }

  @Test
  void flagsInvalidControlCharacter() {
    final String textWithControlChar = "bad" + ((char) 0x07) + "text"; // U+0007 (BEL)
    final LabelFact fact = labelWith(textWithControlChar);

    assertTrue(fact.hasInvalidCharacter());
    final List<ValidationResult> results = fact.invalidCharacterResults();
    assertEquals(1, results.size());
    // The code point is rendered, never the raw (non-printable) character.
    assertTrue(results.get(0).getMessage().contains("[U+0007]"),
        () -> "unexpected message: " + results.get(0).getMessage());
  }

  @Test
  void acceptsCleanTextAndUppercasePipeTokens() {
    final LabelFact clean = labelWith("Description of the procedure");
    assertFalse(clean.hasLabelIdentifier());
    assertFalse(clean.hasInvalidCharacter());

    // Uppercase pipe tokens (e.g. RESULT|((RES|TEN))) are not label identifiers.
    final LabelFact uppercase = labelWith("identifier (RESULT|((RES|TEN|TPA|ORG-XXXX))");
    assertFalse(uppercase.hasLabelIdentifier());
  }

  @Test
  void handlesNullTranslationWithoutCrashing() {
    final LabelFact fact = labelWith(null);
    assertFalse(fact.hasLabelIdentifier());
    assertFalse(fact.hasInvalidCharacter());
  }

  @Test
  void deduplicatesAnIdentifierRepeatedInOneTranslation() {
    final LabelFact fact = labelWith("when expression|name|906 unless expression|name|906");

    final List<ValidationResult> results = fact.labelIdentifierResults();
    assertEquals(1, results.size());
    assertEquals(List.of(AssetRef.label("expression|name|906")), results.get(0).getReferences());

    final String message = results.get(0).getMessage();
    assertEquals(message.indexOf("expression|name|906"), message.lastIndexOf("expression|name|906"),
        () -> "identifier should appear once in the message: " + message);
  }
}
