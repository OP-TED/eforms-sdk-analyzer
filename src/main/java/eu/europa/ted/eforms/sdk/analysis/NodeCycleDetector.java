package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europa.ted.eforms.sdk.SdkConstants;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;

/**
 * Detects cycles in the SDK's node hierarchy — a node reachable from itself by following
 * {@code parentId}. The EFX translator walks node ancestry without a cycle guard, so a cyclic SDK
 * would make it loop forever; the analyzer uses this to avoid even starting the EFX validator on such
 * an SDK (the cycles themselves are reported by the node-hierarchy rules).
 */
final class NodeCycleDetector {
  private static final Logger logger = LoggerFactory.getLogger(NodeCycleDetector.class);

  private NodeCycleDetector() {}

  /**
   * Whether any node in {@code fields.json} lies on a {@code parentId} cycle. Returns {@code false}
   * (treated as acyclic) when the file cannot be read — that failure is reported by the loader.
   */
  static boolean hasNodeCycle(final Path sdkRoot) {
    final Map<String, String> parentById = readParents(sdkRoot);
    for (final String start : parentById.keySet()) {
      final Set<String> seen = new HashSet<>();
      String current = start;
      while (current != null && seen.add(current)) {
        current = parentById.get(current);
      }
      if (current != null) {
        // The walk re-encountered a node already on the path: a cycle.
        logger.debug("Node hierarchy cycle reached from node [{}]", start);
        return true;
      }
    }
    return false;
  }

  /** Maps each node id to its {@code parentId} (or {@code null} for the root / a missing parent). */
  private static Map<String, String> readParents(final Path sdkRoot) {
    final Path fieldsJson =
        Path.of(sdkRoot.toString(), SdkResource.FIELDS_JSON.getPath().toString());
    final Map<String, String> parentById = new HashMap<>();
    try {
      final JsonNode root = new ObjectMapper().readTree(fieldsJson.toFile());
      root.path(SdkConstants.FIELDS_JSON_XML_STRUCTURE_KEY).forEach(node -> {
        final String id = node.path("id").asText(null);
        if (id != null && !id.isBlank()) {
          parentById.put(id, node.path("parentId").asText(null));
        }
      });
    } catch (final IOException e) {
      logger.debug("Could not read node hierarchy from [{}]: {}", fieldsJson, e.getMessage());
    }
    return parentById;
  }
}
