package eu.europa.ted.eforms.sdk.analysis;

import java.nio.file.Path;
import eu.europa.ted.eforms.sdk.SdkSymbolResolver;
import eu.europa.ted.eforms.sdk.component.SdkComponent;
import eu.europa.ted.eforms.sdk.component.SdkComponentType;
import eu.europa.ted.eforms.sdk.SdkConstants.SdkResource;
import eu.europa.ted.eforms.sdk.repository.SdkCodelistRepository;
import eu.europa.ted.eforms.sdk.repository.SdkFieldRepository;
import eu.europa.ted.eforms.sdk.repository.SdkNodeRepository;

@SdkComponent(versions = { "1", "2" }, componentType = SdkComponentType.SYMBOL_RESOLVER,
    qualifier = SdkAnalyzerSymbolResolver.QUALIFIER)
public class SdkAnalyzerSymbolResolver extends SdkSymbolResolver {
  public static final String QUALIFIER = "analyzer";

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
