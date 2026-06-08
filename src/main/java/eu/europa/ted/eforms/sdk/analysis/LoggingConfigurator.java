package eu.europa.ted.eforms.sdk.analysis;

import java.io.File;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;

/**
 * Configures logging for the analyzer CLI. Framework logging is kept off the console by default
 * (only warnings and errors show) and raised to INFO with {@code --verbose}, while a full INFO run
 * log is written to {@value #LOG_FILE} in the working directory (when it is writable) for later
 * inspection. The validation report itself is written separately to stdout by
 * {@link eu.europa.ted.eforms.sdk.analysis.report.ConsoleReportRenderer}.
 *
 * <p>Applied programmatically by the CLI only, so applications that use the analyzer as a library
 * keep their own logging configuration; the library jar ships no {@code logback.xml}.
 */
public class LoggingConfigurator {
  private static final String ROOT_LOGGER = "ROOT"; // the SLF4J/logback root logger name
  private static final String LOG_FILE = "analyzer.log";
  private static final String PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

  public void configure(final boolean verbose) {
    final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    context.reset();

    final ConsoleAppender<ILoggingEvent> console = new ConsoleAppender<>();
    console.setContext(context);
    console.setEncoder(encoder(context));
    console.addFilter(thresholdFilter(context, verbose ? Level.INFO : Level.WARN));
    console.start();

    final Logger root = context.getLogger(ROOT_LOGGER);
    root.setLevel(Level.INFO);
    root.addAppender(console);

    // Only attach the run-log file when the working directory is writable, so a read-only
    // directory does not produce logback start-up errors. The report still goes to stdout.
    final FileAppender<ILoggingEvent> file = fileAppender(context);
    if (file != null) {
      root.addAppender(file);
    }

    // Keep the noisiest frameworks quiet on both the console and the file.
    context.getLogger("org.reflections").setLevel(Level.ERROR);
    context.getLogger("org.drools").setLevel(Level.ERROR);
    context.getLogger("net.sf.saxon").setLevel(Level.WARN);
  }

  private FileAppender<ILoggingEvent> fileAppender(final LoggerContext context) {
    if (!canWriteLogFile()) {
      return null;
    }
    final FileAppender<ILoggingEvent> file = new FileAppender<>();
    file.setContext(context);
    file.setFile(LOG_FILE);
    file.setAppend(false);
    file.setEncoder(encoder(context));
    file.start();
    return file.isStarted() ? file : null;
  }

  private boolean canWriteLogFile() {
    final File logFile = new File(LOG_FILE).getAbsoluteFile();
    if (logFile.exists()) {
      return logFile.canWrite();
    }
    final File directory = logFile.getParentFile();
    return directory != null && directory.canWrite();
  }

  private PatternLayoutEncoder encoder(final LoggerContext context) {
    final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext(context);
    encoder.setPattern(PATTERN);
    encoder.start();
    return encoder;
  }

  private ThresholdFilter thresholdFilter(final LoggerContext context, final Level level) {
    final ThresholdFilter filter = new ThresholdFilter();
    filter.setContext(context);
    filter.setLevel(level.levelStr);
    filter.start();
    return filter;
  }
}
