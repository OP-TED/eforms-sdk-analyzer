package eu.europa.ted.eforms.sdk.analysis.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Verifies that {@link TextValidator} names the offending label identifier(s) in the error
 * message, rather than only reporting that some identifier is present.
 */
class TextValidatorTest {

  @Test
  void errorMessageNamesTheOffendingLabelIdentifiers() throws Exception {
    final Path sdkRoot =
        Path.of(getClass().getResource("/eforms-sdk-tests/tedefo-3301/invalid").toURI());

    final TextValidator validator = new TextValidator(sdkRoot);
    validator.validate();

    final Set<ValidationResult> errors = validator.getErrors();
    assertEquals(2, errors.size());

    final Set<String> messages =
        errors.stream().map(ValidationResult::getMessage).collect(Collectors.toSet());

    assertTrue(messages.stream().anyMatch(m -> m.contains("expression|name|123")),
        () -> "Expected an error naming expression|name|123, got: " + messages);
    assertTrue(messages.stream().anyMatch(m -> m.contains("field|name|BT-132-Lot")),
        () -> "Expected an error naming field|name|BT-132-Lot, got: " + messages);
  }
}
