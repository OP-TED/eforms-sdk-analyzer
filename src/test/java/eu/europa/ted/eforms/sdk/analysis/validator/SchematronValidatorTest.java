package eu.europa.ted.eforms.sdk.analysis.validator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Verifies that {@link SchematronValidator} captures the precise engine error detail in the
 * {@link ValidationResult} message, rather than leaving it only in the helger/Saxon console logs.
 */
class SchematronValidatorTest {

  @Test
  void executionErrorCarriesThePreciseXpathDetail() throws Exception {
    final Path sdkRoot =
        Path.of(getClass().getResource("/eforms-sdk-tests/tedefo-2767/invalid").toURI());

    final SchematronValidator validator = new SchematronValidator(sdkRoot);
    validator.validate();

    final Set<String> messages = validator.getErrors().stream()
        .map(ValidationResult::getMessage).collect(Collectors.toSet());

    assertTrue(messages.stream().anyMatch(m -> m.contains("BAD(")),
        () -> "Expected the captured XPath detail (the offending 'BAD(' expression) in the message, "
            + "got: " + messages);
  }
}
