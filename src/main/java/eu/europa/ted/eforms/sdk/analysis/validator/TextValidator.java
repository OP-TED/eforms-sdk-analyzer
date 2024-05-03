package eu.europa.ted.eforms.sdk.analysis.validator;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ted.eforms.sdk.analysis.SdkLoader;
import eu.europa.ted.eforms.sdk.analysis.domain.enums.Language;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates human-readable texts (translations).
 */
public class TextValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(TextValidator.class);

  // Match label identifiers, so | with characters before and after.
  private static Pattern labelIdPattern = Pattern.compile(".*\\w\\|\\w.*");

  private final SdkLoader sdkLoader;

  private final Set<ValidationResult> results;

  public TextValidator(Path sdkRoot) throws FileNotFoundException {
    Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    this.sdkLoader = new SdkLoader(Path.of(sdkRoot.toString()));

    this.results = new HashSet<>();
  }

  public TextValidator validate() throws Exception {
    logger.debug("Loading translations");
    final Set<Label> translations = sdkLoader.getLabels();

    translations.forEach(l -> {
      validateLabel(l);
    });

    return this;
  }

  private void validateLabel(Label label) {
    label.getTranslations().forEach((lang, text) -> {
      checkCharacters(label, lang, text);
      checkIdReference(label, lang, text);
    });
  }

  private void checkCharacters(Label l, Language lang, String text) {
    text.codePoints()
        .filter(c -> isInvalidCharacter(c))
        .mapToObj(c -> Character.toString(c))
        .forEach(c -> {
          String msg = String.format("Label in %s contains invalid character [%s]", lang, c);
          results.add(new ValidationResult(new LabelFact(l), msg, ValidationStatusEnum.ERROR));
        });
  }

  private void checkIdReference(Label l, Language lang, String text) {
    if (labelIdPattern.matcher(text).matches()) {
      String msg = String.format("Label in %s contains label identifier", lang);
      results.add(new ValidationResult(new LabelFact(l), msg, ValidationStatusEnum.ERROR));
    }
  }

  private final boolean isInvalidCharacter(int codePoint) {
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

  @Override
  public Set<ValidationResult> getResults() {
    return results;
  }
    
}
