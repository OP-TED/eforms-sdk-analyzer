package eu.europa.ted.eforms.sdk.analysis.validator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.europa.ted.eforms.sdk.ComponentFactory;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.SdkVersion;
import eu.europa.ted.eforms.sdk.analysis.SdkAnalyzerSymbolResolver;
import eu.europa.ted.eforms.sdk.analysis.SdkLoader;
import eu.europa.ted.eforms.sdk.analysis.domain.field.Field;
import eu.europa.ted.eforms.sdk.analysis.domain.field.StringConstraint;
import eu.europa.ted.eforms.sdk.analysis.domain.field.StringProperty;
import eu.europa.ted.eforms.sdk.analysis.domain.view.index.TedefoViewTemplateIndex;
import eu.europa.ted.eforms.sdk.analysis.efx.mock.MarkupGeneratorMock;
import eu.europa.ted.eforms.sdk.analysis.enums.ValidationStatusEnum;
import eu.europa.ted.eforms.sdk.analysis.fact.FieldFact;
import eu.europa.ted.eforms.sdk.analysis.fact.SdkComponentFact;
import eu.europa.ted.eforms.sdk.analysis.fact.ViewTemplateFact;
import eu.europa.ted.eforms.sdk.analysis.util.SdkMetadataParser;
import eu.europa.ted.eforms.sdk.analysis.vo.Finding;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import eu.europa.ted.eforms.sdk.analysis.vo.ValidationResult;
import eu.europa.ted.efx.EfxTranslator;
import eu.europa.ted.efx.exceptions.InvalidArgumentException;
import eu.europa.ted.efx.exceptions.InvalidIdentifierException;
import eu.europa.ted.efx.exceptions.InvalidIndentationException;
import eu.europa.ted.efx.exceptions.InvalidUsageException;
import eu.europa.ted.efx.exceptions.SymbolResolutionException;
import eu.europa.ted.efx.exceptions.ThrowingErrorListener;
import eu.europa.ted.efx.exceptions.TypeMismatchException;
import eu.europa.ted.efx.interfaces.MarkupGenerator;
import eu.europa.ted.efx.interfaces.ScriptGenerator;
import eu.europa.ted.efx.interfaces.SymbolResolver;
import eu.europa.ted.efx.interfaces.TranslatorDependencyFactory;
import eu.europa.ted.efx.interfaces.TranslatorOptions;
import eu.europa.ted.efx.interfaces.ValidatorGenerator;

/**
 * Validates EFX expressions and templates 
 */
public class EfxValidator implements Validator {
  private static final Logger logger = LoggerFactory.getLogger(EfxValidator.class);

  private final Path sdkRoot;
  private final String sdkVersion;

  private final SdkLoader sdkLoader;
  private final TranslatorDependencyFactory dependencyFactory;

  private final Set<ValidationResult> results;

  // Findings carry the failure's category (derived from the exception type) as their problem, so the
  // report groups EFX errors by category rather than by their unique per-instance messages.
  private final Queue<Finding> findings = new ConcurrentLinkedQueue<>();

  public EfxValidator(final Path sdkRoot) throws IOException {
    this.sdkRoot = Validate.notNull(sdkRoot, "Undefined SDK root path");

    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    final SdkMetadata sdkMetadata = SdkMetadataParser.loadSdkMetadata(sdkRoot);
    this.sdkVersion = new SdkVersion(sdkMetadata.getVersion()).toStringWithoutPatch();

    this.dependencyFactory = new DependencyFactory(sdkRoot);

    this.sdkLoader = new SdkLoader(Path.of(sdkRoot.toString()));

    // Thread-safe: the EFX passes may run their translations across worker threads.
    this.results = ConcurrentHashMap.newKeySet();
    logger.debug("Initialized for SDK {}", sdkVersion);
  }

  @Override
  public Validator validate() throws Exception {
    // Each EFX translation is independent and the dominant cost, so the two passes run across worker
    // threads — but only once the shared symbol resolver is fully pre-warmed and read-only.
    final boolean parallel = resolverIsReadOnly();
    runTemplates(parallel);
    runExpressions(parallel);
    return this;
  }

  // Kept for the cucumber steps that drive each pass directly; always single-threaded.
  public EfxValidator validateTemplates() throws IOException {
    runTemplates(false);
    return this;
  }

  public EfxValidator validateExpressions() throws IOException {
    runExpressions(false);
    return this;
  }

  /**
   * Builds the shared symbol resolver up front (single-threaded) and reports whether it is read-only,
   * the precondition for driving the translations concurrently. On any failure to build it we fall
   * back to serial; the per-translation {@code try/catch} then surfaces the real error as a finding.
   */
  private boolean resolverIsReadOnly() {
    try {
      final SymbolResolver resolver =
          dependencyFactory.createSymbolResolver(sdkVersion, SdkAnalyzerSymbolResolver.QUALIFIER);
      return resolver instanceof SdkAnalyzerSymbolResolver
          && ((SdkAnalyzerSymbolResolver) resolver).isReadOnly();
    } catch (final RuntimeException e) {
      logger.warn("Symbol resolver could not be pre-built; EFX validation runs single-threaded: {}",
          e.getMessage());
      return false;
    }
  }

  private void runTemplates(final boolean parallel) throws IOException {
    forEach(parallel, "templates", sdkLoader.getViewTemplates(), this::validateTemplate);
  }

  private void runExpressions(final boolean parallel) throws IOException {
    forEach(parallel, "expressions", sdkLoader.getFieldsAndNodes().getFields(),
        this::validateFieldExpressions);
  }

  private void validateTemplate(final TedefoViewTemplateIndex template) {
    final Path templatePath = Path.of(sdkRoot.toString(),
        SdkResource.VIEW_TEMPLATES.getPath().toString(), template.getFilename());
    logger.debug("Compiling template [{}] using file [{}]", template.getId(), templatePath);
    try {
      EfxTranslator.translateTemplate(dependencyFactory, sdkVersion, templatePath);
    } catch (final Exception e) {
      record(new ViewTemplateFact(template), e);
    }
  }

  private void validateFieldExpressions(final Field field) {
    getExpressions(field).forEach((String expression) -> {
      logger.debug("Translating expression [{}] of assertion constraint of field [{}]", expression,
          field.getId());
      try {
        EfxTranslator.translateExpression(dependencyFactory, sdkVersion, expression);
      } catch (final Exception e) {
        record(new FieldFact(field), e);
      }
    });
  }

  /**
   * Records an EFX failure: the full message goes on the {@link ValidationResult} (shown verbatim only
   * in {@code --verbose}), while the {@link Finding}'s problem is the failure's <em>category</em>,
   * derived from the exception type — so the summary and actionable items group EFX errors by kind
   * rather than by their unique per-instance text.
   */
  private void record(final SdkComponentFact<?> fact, final Exception error) {
    final ValidationResult result =
        new ValidationResult(fact, error.getMessage(), ValidationStatusEnum.ERROR);
    this.results.add(result);
    this.findings.add(new Finding(getClass().getSimpleName(), efxProblem(error), result));
  }

  /**
   * The problem category for a failure raised while translating EFX. A failure within the EFX/ANTLR
   * taxonomy reads uniformly as {@code "EFX compilation error: <type>"} (the {@code <type>} is the
   * short, honest name of the failure — see {@link #efxErrorType}). Anything outside that taxonomy is
   * not an EFX-author mistake but an unexpected analyzer/toolkit failure (e.g. a NullPointerException or
   * a concurrency bug from the parallel passes); it is labelled {@code "Unexpected analyzer error:
   * <type>"} so it stands out loudly instead of blending into the EFX errors against innocent fields.
   */
  private static String efxProblem(final Throwable error) {
    return efxErrorType(error)
        .map(type -> "EFX compilation error: " + type)
        .orElse("Unexpected analyzer error: " + error.getClass().getSimpleName());
  }

  /**
   * The failure category derived from the exception's <em>type</em> (and {@code ErrorCode}) rather than
   * its message text — the message differs between EFX1/EFX2, the exception taxonomy does not. The
   * concrete subtypes are checked before bare {@link ParseCancellationException} (they all extend it);
   * anything outside the known taxonomy yields {@link Optional#empty()} (the bare prefix).
   */
  private static Optional<String> efxErrorType(final Throwable error) {
    if (error instanceof SymbolResolutionException) {
      return Optional.of(symbolErrorType((SymbolResolutionException) error));
    }
    if (error instanceof TypeMismatchException) {
      return Optional.of("type mismatch");
    }
    if (error instanceof InvalidIndentationException) {
      return Optional.of("invalid indentation");
    }
    if (error instanceof InvalidArgumentException) {
      return Optional.of("invalid argument");
    }
    if (error instanceof InvalidIdentifierException) {
      return Optional.of("invalid identifier");
    }
    if (error instanceof InvalidUsageException) {
      return Optional.of("invalid usage");
    }
    if (error instanceof ParseCancellationException) {
      return Optional.of("syntax error");
    }
    return Optional.empty();
  }

  /** The codelist / symbol / node distinction a {@link SymbolResolutionException} carries as its code. */
  private static String symbolErrorType(final SymbolResolutionException error) {
    switch (error.getErrorCode()) {
      case UNKNOWN_CODELIST:       return "unknown codelist";
      case UNKNOWN_SYMBOL:         return "unknown field or node";
      case NO_CODELIST_FOR_FIELD:  return "field without codelist";
      case ROOT_NODE_NOT_FOUND:    return "unresolved root node";
      case UNKNOWN_NOTICE_SUBTYPE: return "unknown notice subtype";
      default:                     return "unresolved symbol";
    }
  }

  /**
   * Applies {@code action} to every item — across {@link #threadCount()} worker threads when
   * {@code parallel}, otherwise serially — logging progress at INFO so a run can be watched live (via
   * {@code analyzer.log} or {@code --verbose}). Each action handles its own errors (recorded as
   * findings), so no exception escapes the workers; only an unexpected framework failure surfaces here.
   * The progress counter is the key debugging aid: a climbing count means it is working; a frozen count
   * pinpoints (to within one report step) where it is stuck.
   */
  private static <T> void forEach(final boolean parallel, final String label,
      final Collection<T> items, final Consumer<T> action) {
    final int total = items.size();
    final int step = Math.max(1, total / 20);
    final AtomicInteger done = new AtomicInteger();
    logger.info("EFX {}: starting {} items ({})", label, total,
        parallel && total > 1 ? threadCount() + " threads" : "serial");
    final Consumer<T> tracked = item -> {
      action.accept(item);
      final int n = done.incrementAndGet();
      if (n % step == 0 || n == total) {
        logger.info("EFX {}: {}/{} ({}%)", label, n, total, n * 100 / total);
      }
    };

    if (!parallel || total < 2) {
      items.forEach(tracked);
    } else {
      final ForkJoinPool pool = new ForkJoinPool(threadCount());
      try {
        pool.submit(() -> items.parallelStream().forEach(tracked)).get();
      } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("EFX validation was interrupted", e);
      } catch (final ExecutionException e) {
        throw new IllegalStateException("EFX validation failed", e.getCause());
      } finally {
        pool.shutdown();
      }
    }
    logger.info("EFX {}: done {} items", label, total);
  }

  /** Worker thread count: {@code -Defx.validator.threads} if set, else the available processors. */
  private static int threadCount() {
    final String override = System.getProperty("efx.validator.threads");
    if (override != null && !override.isBlank()) {
      try {
        return Math.max(1, Integer.parseInt(override.trim()));
      } catch (final NumberFormatException e) {
        logger.warn("Ignoring invalid efx.validator.threads=[{}]", override);
      }
    }
    return Math.max(1, Runtime.getRuntime().availableProcessors());
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

  /**
   * Overrides the default (message-as-problem) behaviour: each EFX finding's problem is the failure
   * category set in {@link #record}, so the report groups EFX errors by category, not by raw message.
   */
  @Override
  public List<Finding> getFindings() {
    return new ArrayList<>(this.findings);
  }

  private static final class DependencyFactory implements TranslatorDependencyFactory {
    private final Path sdkRoot;

    private DependencyFactory(final Path sdkRoot) {
      this.sdkRoot = sdkRoot;
    }

    @Override
    public SymbolResolver createSymbolResolver(final String sdkVersion, String qualifier) {
      try {
        // We want SdkAnalyzerSymbolResolver, so always indicate its qualifier
        return ComponentFactory.getSymbolResolver(sdkVersion, SdkAnalyzerSymbolResolver.QUALIFIER,
            sdkRoot);
      } catch (InstantiationException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    @Override
    public ScriptGenerator createScriptGenerator(final String sdkVersion, String qualifier,
        final TranslatorOptions options) {
      try {
        return ComponentFactory.getScriptGenerator(sdkVersion, qualifier, options);
      } catch (InstantiationException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    @Override
    public MarkupGenerator createMarkupGenerator(final String sdkVersion, String qualifier,
        final TranslatorOptions options) {
      return new MarkupGeneratorMock();
    }

    @Override
    public BaseErrorListener createErrorListener() {
      return ThrowingErrorListener.INSTANCE;
    }

    @Override
    public ValidatorGenerator createValidatorGenerator(String sdkVersion, String qualifier, TranslatorOptions options) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'createValidatorGenerator'");
    }
  }
}
