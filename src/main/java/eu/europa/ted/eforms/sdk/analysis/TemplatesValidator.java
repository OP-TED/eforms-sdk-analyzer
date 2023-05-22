package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.BaseErrorListener;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.ComponentFactory;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.SdkVersion;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplateFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;
import eu.europa.ted.eforms.sdk.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.resource.SdkResourceLoader;
import eu.europa.ted.efx.EfxTranslator;
import eu.europa.ted.efx.exceptions.ThrowingErrorListener;
import eu.europa.ted.efx.interfaces.MarkupGenerator;
import eu.europa.ted.efx.interfaces.ScriptGenerator;
import eu.europa.ted.efx.interfaces.SymbolResolver;
import eu.europa.ted.efx.interfaces.TranslatorDependencyFactory;
import eu.europa.ted.efx.interfaces.TranslatorOptions;
import eu.europa.ted.efx.mock.MarkupGeneratorMock;

public class TemplatesValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(TemplatesValidator.class);

  final Path sdkRoot;
  final String sdkVersion;

  final Set<TedefoViewTemplateIndex> viewTemplates;
  final TranslatorDependencyFactory dependencyFactory;

  // Variable to store validations results
  final Set<ValidationResult> results;

  public TemplatesValidator(final Path sdkRoot, final String sdkVersion) throws IOException {
    this.sdkRoot = Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    Validate.notBlank(sdkVersion, "Undefined SDK version");
    this.sdkVersion = new SdkVersion(sdkVersion).toStringWithoutPatch();
    this.viewTemplates =
        new SdkLoader(Path.of(sdkRoot.toString(), this.sdkVersion)).getViewTemplates();

    this.dependencyFactory = new DependencyFactory(sdkRoot);

    results = new HashSet<>();
  }

  public TemplatesValidator validate() {
    viewTemplates.forEach((TedefoViewTemplateIndex template) -> {
      final Path templatePath = SdkResourceLoader.getResourceAsPath(sdkVersion,
          SdkResource.VIEW_TEMPLATES, template.getFilename(), sdkRoot);

      logger.debug("Compiling template [{}] using file [{}]", template.getId(), templatePath);

      try {
        EfxTranslator.translateTemplate(dependencyFactory, sdkVersion, templatePath);
      } catch (Exception e) {
        results.add(new ValidationResult(new ViewTemplateFact(template), e.getMessage(),
            ValidationStatusEnum.ERROR));
      }
    });

    return this;
  }

  @Override
  public Set<ValidationResult> getResults() {
    return results;
  }

  private static final class DependencyFactory implements TranslatorDependencyFactory {
    private final Path sdkRoot;

    private DependencyFactory(final Path sdkRoot) {
      this.sdkRoot = sdkRoot;
    }

    @Override
    public SymbolResolver createSymbolResolver(final String sdkVersion) {
      try {
        return ComponentFactory.getSymbolResolver(sdkVersion, sdkRoot);
      } catch (InstantiationException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    @Override
    public ScriptGenerator createScriptGenerator(final String sdkVersion,
        final TranslatorOptions options) {
      try {
        return ComponentFactory.getScriptGenerator(sdkVersion, options);
      } catch (InstantiationException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    @Override
    public MarkupGenerator createMarkupGenerator(final String sdkVersion,
        final TranslatorOptions options) {
      return new MarkupGeneratorMock();
    }

    @Override
    public BaseErrorListener createErrorListener() {
      return ThrowingErrorListener.INSTANCE;
    }
  }
}
