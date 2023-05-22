package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.BaseErrorListener;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.ComponentFactory;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.SdkVersion;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplateFact;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;
import eu.europa.ted.eforms.sdk.domain.field.Field;
import eu.europa.ted.eforms.sdk.domain.field.StringConstraint;
import eu.europa.ted.eforms.sdk.domain.field.StringProperty;
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

public class EfxValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(EfxValidator.class);

  private final Path sdkRoot;
  private final String sdkVersion;

  private final SdkLoader sdkLoader;
  private final TranslatorDependencyFactory dependencyFactory;

  // Variable to store validations results
  private final Set<ValidationResult> results;

  public EfxValidator(final Path sdkRoot, final String sdkVersion) throws FileNotFoundException {
    Validate.notBlank(sdkVersion, "Undefined SDK version");
    this.sdkVersion = new SdkVersion(sdkVersion).toStringWithoutPatch();

    this.sdkRoot = Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    this.dependencyFactory = new DependencyFactory(sdkRoot);

    this.sdkLoader = new SdkLoader(Path.of(sdkRoot.toString(), this.sdkVersion));

    this.results = new HashSet<>();
  }

  public EfxValidator validateTemplates() throws IOException {
    final Set<TedefoViewTemplateIndex> viewTemplates = sdkLoader.getViewTemplates();

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

  public EfxValidator validateExpressions() throws IOException {
    final List<Field> fields = sdkLoader.getFieldsAndNodes().getFields();

    fields.stream()
        .forEach((Field field) -> getExpressions(field)
            .forEach((String expression) -> {
              logger.debug("Translating expression [{}] of assertion constraint of field [{}]",
                  expression, field.getId());

              try {
                EfxTranslator.translateExpression(dependencyFactory, sdkVersion, expression);
              } catch (Exception e) {
                results.add(new ValidationResult(new FieldFact(field), e.getMessage(),
                    ValidationStatusEnum.ERROR));
              }
            }));

    return this;
  }

  private Set<String> getExpressions(final Field field) {
    return Optional.ofNullable(field)
        .map(Field::getAssertion)
        .map(StringProperty::getConstraints)
        .map((List<StringConstraint> constraints) -> constraints.stream()
            .map(StringConstraint::getValue)
            .collect(Collectors.toSet()))
        .orElse(Collections.emptySet());
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
