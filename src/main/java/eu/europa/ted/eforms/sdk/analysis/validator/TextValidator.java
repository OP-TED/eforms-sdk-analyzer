package eu.europa.ted.eforms.sdk.analysis.validator;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ted.eforms.sdk.analysis.SdkLoader;
import eu.europa.ted.eforms.sdk.analysis.domain.label.Label;
import eu.europa.ted.eforms.sdk.analysis.fact.LabelFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;

/**
 * Validates human-readable texts (translations).
 *
 * <p>The checks themselves live on {@link LabelFact} so that they can also run as drools rules; this
 * validator is a thin file-backed driver that delegates to them.
 */
public class TextValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(TextValidator.class);

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
    final Set<Label> translations = this.sdkLoader.getLabels();

    translations.forEach(this::validateLabel);

    return this;
  }

  private void validateLabel(final Label label) {
    final LabelFact fact = new LabelFact(label);
    this.results.addAll(fact.invalidCharacterResults());
    this.results.addAll(fact.labelIdentifierResults());
  }

  @Override
  public Set<ValidationResult> getResults() {
    return this.results;
  }
}
