package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.Validate;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.xml.namespace.MapBasedNamespaceContext;

@State(Scope.Benchmark)
public class SchematronBenchmark {
  private static final Logger logger = LoggerFactory.getLogger(SchematronBenchmark.class);

  private static final String XPATH_NOTICE_SUBTYPE = "/*/ext:UBLExtensions/ext:UBLExtension"
      + "/ext:ExtensionContent/efext:EformsExtension/efac:NoticeSubType/cbc:SubTypeCode";

  // Only the namespace prefixes used in the XPath expression
  private static final MapBasedNamespaceContext NAMESPACES = new MapBasedNamespaceContext()
      .addMapping("ext", "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2")
      .addMapping("efext", "http://data.europa.eu/p27/eforms-ubl-extensions/1")
      .addMapping("efac", "http://data.europa.eu/p27/eforms-ubl-extension-aggregate-components/1")
      .addMapping("cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");

  // The Schematron rules of each notice subtype are grouped in a phase named with this prefix
  private static final String PHASE_PREFIX = "eforms-";

  // Path to the root of the eForms SDK, overriden with the path given on the command line
  @Param("../eforms-sdk")
  public String sdkRootPath;

  // Filenames of XML notices included as resources in this project
  @Param({"16.xml", "29.xml"})
  public String noticeFilename;

  private SchematronResourcePure phSchematron;

  public static int run(final Path sdkRoot) throws Exception {
    logger.warn("Benchmarking Schematron rules from SDK under folder [{}]", sdkRoot);

    Validate.notNull(sdkRoot, "Undefined SDK root path");
    if (!Files.isDirectory(sdkRoot)) {
      throw new FileNotFoundException(sdkRoot.toString());
    }

    Options opt = new OptionsBuilder()
                .include(SchematronBenchmark.class.getSimpleName())
                .param("sdkRootPath", sdkRoot.toString())
                .resultFormat(ResultFormatType.JSON)
                // Stop with an error when a benchmark fails, instead of leaving it out of the results
                .shouldFailOnError(true)
                .build();
             
    new Runner(opt).run();

    return 0;
  }

  @Setup
  public void setup() throws IOException, XPathExpressionException {
    SdkLoader sdkLoader = new SdkLoader(Path.of(sdkRootPath));

    // Use the first set of Schematron rules (dynamic/static)
    // They are mostly identical, so it does not make a difference for the benchmark
    Path file = sdkLoader.getSchematronFilesPaths().get(0);
    IReadableResource schematron = new FileSystemResource(file);
    phSchematron = new SchematronResourcePure(schematron);

    // Validate the notice only with the rules of its own subtype, as a real validation does.
    // Without a phase, the rules of all notice subtypes would be applied.
    String phase = PHASE_PREFIX + readNoticeSubType();
    logger.info("Validating notice [{}] with phase [{}]", noticeFilename, phase);
    phSchematron.setPhase(phase);

    // ph-schematron only warns about an unknown phase and then applies all the rules
    if (phSchematron.getOrCreateBoundSchema().getOriginalSchema().getPhaseOfID(phase) == null) {
      throw new IllegalStateException(
          MessageFormat.format("No phase [{0}] in the Schematron file [{1}]", phase, file));
    }
  }

  @Benchmark
  @Fork(1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 3)
  @BenchmarkMode(Mode.AverageTime)
  public int executeValidation() throws Exception {
    Source source = new StreamSource(openNotice());
    phSchematron.applySchematronValidation(source);
    
    return 0;
  }

  private InputStream openNotice() {
    String resourceName = MessageFormat.format("benchmark/notices/{0}", noticeFilename);
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
    Validate.notNull(stream, "Notice file not found in resources: " + resourceName);

    return stream;
  }

  private String readNoticeSubType() throws IOException, XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(NAMESPACES);

    try (InputStream stream = openNotice()) {
      String subType = xpath.evaluate(XPATH_NOTICE_SUBTYPE, new InputSource(stream)).trim();
      return Validate.notBlank(subType, "Missing or empty notice subtype in notice %s",
          noticeFilename);
    }
  }
}
