package eu.europa.ted.eforms.sdk.analysis.fact;

import java.io.Serializable;
import java.lang.reflect.Type;
import eu.europa.ted.eforms.sdk.analysis.Identifiable;

public interface SdkComponentFact<ID extends Serializable> extends Identifiable<ID>, Type {

  /**
   * The SDK-relative path of the file backing this asset — the file a reader opens to act on a finding
   * whose subject is this fact — or {@code null} when the asset is not a standalone file (its id is
   * shown instead). File-backed facts override this with their real on-disk name, so a tailored or
   * misnamed file is reported by the path that actually exists, not by a guessed one.
   */
  default String getSdkPath() {
    return null;
  }
}
