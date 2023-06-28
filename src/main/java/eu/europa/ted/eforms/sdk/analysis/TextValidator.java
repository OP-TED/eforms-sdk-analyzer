package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import eu.europa.ted.eforms.sdk.analysis.domain.Label;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

public class TextValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(TextValidator.class);

  private final SdkLoader sdkLoader;

  private final Set<Label> translations;

  private final Set<ValidationResult> results;

  public TextValidator(Path sdkRoot) throws IOException, JAXBException, SAXException,
      ParserConfigurationException {
    Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    this.sdkLoader = new SdkLoader(Path.of(sdkRoot.toString()));
    
    logger.debug("Loading translations");
    this.translations = sdkLoader.getLabels();

    this.results = new HashSet<>();
  }

  public TextValidator validateTranslationTexts() {
    translations.forEach(l -> {
      validateLabel(l);
    });

    return this;
  }

  private void validateLabel(Label l) {
    l.getTranslations().forEach((lang, text) -> {
      text.codePoints()
          .filter(c -> isInvalidCharacter(c))
          .mapToObj(c -> Character.toString(c))
          .forEach(c -> {
            String msg = String.format("Label in %s contains invalid character [%s]", lang, c);
            results.add(new ValidationResult(new LabelFact(l), msg, ValidationStatusEnum.ERROR));
          });
    });
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
