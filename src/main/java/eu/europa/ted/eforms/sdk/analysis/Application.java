package eu.europa.ted.eforms.sdk.analysis;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private Application() {}

  /**
   * Entry point.
   */
  public static void main(final String... args) {
    int exitCode = new CommandLine(new CliCommand())
        .setExecutionExceptionHandler(new IExecutionExceptionHandler() {
          @Override
          public int handleExecutionException(Exception e, CommandLine commandLine,
              ParseResult parseResult) throws Exception {
            logger.error("Error executing the application with arguments [{}]: {}",
                Arrays.asList(args).stream()
                    .map((String arg) -> arg.startsWith("--password")
                        ? arg.replaceAll("=.*", "=****")
                        : arg)
                    .collect(Collectors.toList()),
                e.getMessage());
            logger.debug("Exception thrown:", e);

            return 1;
          }
        })
        .execute(args);

    System.exit(exitCode);
  }
}
