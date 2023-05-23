package eu.europa.ted.eforms.sdk;

import java.nio.file.Path;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.repository.SdkCodelistRepository;
import eu.europa.ted.eforms.sdk.repository.SdkFieldRepository;
import eu.europa.ted.eforms.sdk.repository.SdkNodeRepository;

public class SdkAnalyzerSymbolResolver extends SdkSymbolResolver {
  public SdkAnalyzerSymbolResolver(String sdkVersion, Path sdkRootPath)
      throws InstantiationException {
    super(sdkVersion, sdkRootPath);
  }

  @Override
  protected void loadMapData(final String sdkVersion, final Path sdkRootPath)
      throws InstantiationException {
    Path jsonPath = Path.of(sdkRootPath.toString(), SdkResource.FIELDS_JSON.getPath().toString());
    Path codelistsPath =
        Path.of(sdkRootPath.toString(), SdkResource.CODELISTS.getPath().toString());

    this.fieldById = new SdkFieldRepository(sdkVersion, jsonPath);
    this.nodeById = new SdkNodeRepository(sdkVersion, jsonPath);
    this.codelistById = new SdkCodelistRepository(sdkVersion, codelistsPath);
  }
}
