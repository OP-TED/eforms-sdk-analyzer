package eu.europa.ted.eforms.sdk.analysis;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

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

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;

@State(Scope.Benchmark)
public class SchematronBenchmark {
  private static final Logger logger = LoggerFactory.getLogger(SchematronBenchmark.class);

  // Path to the root of the eForms SDK, overriden with the path given on the command line
  @Param("../eforms-sdk")
  public String sdkRootPath;

  // Filenames of XML notices included as resources in this project
  @Param({"16.xml", "29.xml"})
  public String noticeFilename;

  private ISchematronResource phSchematron;

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
                .build();
             
    new Runner(opt).run();

    return 0;
  }

  @Setup
  public void setup() {
    SdkLoader sdkLoader = new SdkLoader(Path.of(sdkRootPath));

    // Use the first set of Schematron rules (dynamic/static)
    // They are mostly identical, so it does not make a difference for the benchmark
    Path file = sdkLoader.getSchematronFilesPaths().get(0);
    IReadableResource schematron = new FileSystemResource(file);
    phSchematron = new SchematronResourcePure(schematron);
  }

  @Benchmark
  @Fork(1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 3)
  @BenchmarkMode(Mode.AverageTime)
  public int executeValidation() throws Exception {

    String resourceName = MessageFormat.format("benchmark/notices/{0}", noticeFilename);
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
    Validate.notNull(stream, "Notice file not found in resources: " + resourceName);

    Source source = new StreamSource(stream);
    phSchematron.applySchematronValidation(source);
    
    return 0;
  }
}
