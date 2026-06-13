package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.SdkSymbolResolver;
import eu.europa.ted.eforms.sdk.component.SdkComponent;
import eu.europa.ted.eforms.sdk.component.SdkComponentType;
import eu.europa.ted.eforms.sdk.entity.SdkCodelist;
import eu.europa.ted.eforms.sdk.entity.SdkNode;
import eu.europa.ted.eforms.sdk.repository.SdkCodelistRepository;
import eu.europa.ted.eforms.sdk.repository.SdkDataTypeRepository;
import eu.europa.ted.eforms.sdk.repository.SdkFieldRepository;
import eu.europa.ted.eforms.sdk.repository.SdkNodeRepository;
import eu.europa.ted.eforms.sdk.repository.SdkNoticeTypeRepository;

@SdkComponent(versions = { "1", "2" }, componentType = SdkComponentType.SYMBOL_RESOLVER,
    qualifier = SdkAnalyzerSymbolResolver.QUALIFIER)
public class SdkAnalyzerSymbolResolver extends SdkSymbolResolver {
  public static final String QUALIFIER = "analyzer";

  private static final Logger logger = LoggerFactory.getLogger(SdkAnalyzerSymbolResolver.class);

  /**
   * True once every lazily-loaded structure has been resolved up front, leaving this resolver fully
   * read-only. The EFX validator parallelises its translations only when this holds; otherwise it
   * stays single-threaded — the only safe option while a structure could still be populated on demand.
   *
   * <p>No {@code = false} initializer on purpose: {@code loadMapData()} (which sets this {@code true})
   * runs from the super constructor, <em>before</em> this subclass's field initializers — an explicit
   * initializer here would run afterwards and silently reset it, forcing serial mode.
   */
  private boolean readOnly;

  public SdkAnalyzerSymbolResolver(String sdkVersion, Path sdkRootPath)
      throws InstantiationException {
    super(sdkVersion, sdkRootPath);
  }

  public boolean isReadOnly() {
    return this.readOnly;
  }

  @Override
  protected void loadMapData(final String sdkVersion, final Path sdkRootPath)
      throws InstantiationException {
    Path jsonPath = Path.of(sdkRootPath.toString(), SdkResource.FIELDS_JSON.getPath().toString());
    Path codelistsPath =
        Path.of(sdkRootPath.toString(), SdkResource.CODELISTS.getPath().toString());
    Path noticeTypesPath =
        Path.of(sdkRootPath.toString(), SdkResource.NOTICE_TYPES_JSON.getPath().toString());

    // Load nodes first (fields depend on nodes for parent wiring)
    this.nodeById = new SdkNodeRepository(sdkVersion, jsonPath);
    this.nodeByAlias = indexNodesByAlias();

    // Load fields with parent node wiring
    this.fieldById = new SdkFieldRepository(sdkVersion, jsonPath, this.nodeById);
    this.fieldByAlias = indexFieldsByAlias();

    this.noticeTypesById = new SdkNoticeTypeRepository(sdkVersion, noticeTypesPath);
    this.dataTypeById = SdkDataTypeRepository.createDefault();

    // Nodes, fields and notice types are fully populated above (eager). The structures that load on
    // demand — node ancestry (cached per node), the cached root node, and codelist contents — are
    // resolved here, so the resolver becomes read-only and safe to share across the EFX worker threads.
    this.codelistById = prewarm(sdkRootPath, codelistsPath, sdkVersion);
  }

  /**
   * Resolves ancestry for every node and returns a fully-loaded, frozen codelist map. The codelist
   * repository loads file contents on demand into a non-thread-safe map; rather than share that, we
   * load every codelist listed in {@code codelists.json} (1:1 with the {@code .gc} files) and return a
   * plain {@link HashMap} snapshot, whose {@code get()} is a pure read. If the index cannot be read we
   * return the lazy repository and leave {@link #readOnly} {@code false}, keeping the validator serial.
   */
  private Map<String, SdkCodelist> prewarm(final Path sdkRootPath, final Path codelistsPath,
      final String sdkVersion) throws InstantiationException {
    // Node ancestry is cached lazily on each shared node; compute it now so later reads never race.
    this.nodeById.values().forEach(node -> {
      try {
        node.getAncestry();
      } catch (final RuntimeException e) {
        logger.debug("Could not pre-resolve ancestry for node [{}]: {}", node.getId(),
            e.getMessage());
      }
    });

    // The base class caches the root node lazily on the first getRootNode() call (reached from the
    // absolute-path/repeatability lookups during translation). Resolve it now, on this single thread,
    // so the worker threads never race to populate that one remaining lazy cache.
    try {
      this.getRootNodeId();
    } catch (final RuntimeException e) {
      logger.debug("Could not pre-resolve the root node: {}", e.getMessage());
    }

    final SdkCodelistRepository codelists = new SdkCodelistRepository(sdkVersion, codelistsPath);
    final Set<String> codelistIds = readCodelistIds(sdkRootPath);
    if (codelistIds.isEmpty()) {
      logger.warn("Could not enumerate codelist ids; EFX validation will run single-threaded");
      return codelists; // lazy fallback; readOnly stays false
    }

    codelistIds.forEach(id -> {
      try {
        codelists.get(id);
      } catch (final RuntimeException e) {
        logger.debug("Could not pre-load codelist [{}]: {}", id, e.getMessage());
      }
    });
    this.readOnly = true;
    // A plain snapshot: get() on it never loads on demand, so concurrent reads are safe.
    return new HashMap<>(codelists);
  }

  /** The ids of every codelist, read from {@code codelists.json}; empty if it cannot be read. */
  private static Set<String> readCodelistIds(final Path sdkRootPath) {
    final Path indexPath =
        Path.of(sdkRootPath.toString(), SdkResource.CODELISTS_JSON.getPath().toString());
    if (!Files.isReadable(indexPath)) {
      return Set.of();
    }
    try {
      final JsonNode root = new ObjectMapper().readTree(indexPath.toFile());
      final Set<String> ids = new HashSet<>();
      root.path("codelists").forEach(entry -> {
        final String id = entry.path("id").asText(null);
        if (id != null && !id.isBlank()) {
          ids.add(id);
        }
      });
      return ids;
    } catch (final IOException e) {
      logger.warn("Failed to read codelist ids from [{}]: {}", indexPath, e.getMessage());
      return Set.of();
    }
  }
}
