package eu.europa.ted.eforms.sdk.analysis;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "", mixinStandardHelpOptions = true, description = "eForms SDK analyzer",
    versionProvider = CliCommand.ManifestVersionProvider.class)
class CliCommand implements Callable<Integer> {
  @Spec
  CommandSpec spec; // injected by picocli

  String filePrefix;

  @Parameters(index = "0", description = "SDK resources root folder.")
  private Path sdkRoot;

  @Option(names = {"-v", "--verbose"},
      description = "List every individual finding and show framework logging (INFO) on the console."
          + " A full run log is always written to analyzer.log regardless of this flag.")
  private boolean verbose;

  @Override
  public Integer call() throws Exception {
    new LoggingConfigurator().configure(this.verbose);
    return SdkAnalyzer.analyze(this.sdkRoot, this.verbose);
  }

  @Command(name = "benchmark", mixinStandardHelpOptions = true,
      description = "Run benchmark of Schematron rules")
  public int runBenchmark() throws Exception {
    new LoggingConfigurator().configure(this.verbose);
    return SchematronBenchmark.run(this.sdkRoot);
  }

  /**
   * {@link IVersionProvider} implementation that returns version information from the
   * built JAR file's {@code /META-INF/MANIFEST.MF} file.
   */
  static class ManifestVersionProvider implements IVersionProvider {
    public String[] getVersion() throws Exception {
      Enumeration<URL> resources =
          CommandLine.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
      while (resources.hasMoreElements()) {
        URL url = resources.nextElement();
        try {
          Manifest manifest = new Manifest(url.openStream());
          if (isApplicableManifest(manifest)) {
            Attributes attr = manifest.getMainAttributes();
            return new String[] {get(attr, "Implementation-Title") + " version \""
                + get(attr, "Implementation-Version") + "\""};
          }
        } catch (IOException ex) {
          return new String[] {"Unable to read from " + url + ": " + ex};
        }
      }
      return new String[0];
    }

    private boolean isApplicableManifest(Manifest manifest) {
      Attributes attributes = manifest.getMainAttributes();
      return "eforms-sdk-analyzer".equals(get(attributes, "Implementation-Title"));
    }

    private static Object get(Attributes attributes, String key) {
      return attributes.get(new Attributes.Name(key));
    }
  }
}
