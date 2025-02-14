package org.opentdk.api.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class to create and configure {@link Logger} instances with specific logging levels and
 * output handlers such as console and file handlers. This class is designed to simplify and standardize
 * the initialization of loggers across the application, ensuring consistent configurations.
 * <p>
 * The {@link Logger} instances created by this class are configured with a custom formatting
 * implementation provided by the {@link LogFormatter}, and the logging level can be configured at runtime.
 * The loggers will not propagate log messages to parent handlers by default.
 * <p>
 * The created logger can optionally log messages to a specified file with file handling capabilities
 * provided by {@link LogHandlerFactory}, ensuring that directories and files are created as needed. If
 * logging to a file is enabled and an error occurs during file creation, an error message is logged.
 * <p>
 * This class is final and cannot be extended.
 */
public final class LogFactory {

	/**
	 * Creates and configures a {@link Logger} instance with a specified logging level and output handlers.
	 * The logger is configured with a custom formatter, default logging level,
	 * and can write logs to a specified file if enabled. Removes any parent handlers
	 * and existing handlers to ensure a fresh setup.
	 *
	 * @param logFile the path of the log file for file handler configuration. This defines the name of the logger.
	 * @param traceLevel the logging level to be set for the logger. Defaults to INFO if the provided value is invalid.
	 * @param writeToFile a flag indicating whether logs should be written to the specified file.
	 * @return A fully configured {@link Logger} instance with the specified settings.
	 */
	public static Logger buildLogger(Path logFile, String traceLevel, boolean writeToFile) {
		Logger logger = Logger.getLogger(logFile.toString());
		Level level;
		try {
			level = Level.parse(traceLevel);
		} catch (IllegalArgumentException e) {
			level = Level.INFO;
		}
		logger.setLevel(level);
			
		logger.setUseParentHandlers(false);
		for (Handler handler : logger.getHandlers()) {
			handler.close();
			logger.removeHandler(handler);
		}
		// Use factory class to create a console handler that uses the custom formatter as well
		logger.addHandler(LogHandlerFactory.buildConsoleHandler());

		if(writeToFile) {
			try {
				Files.createDirectories(logFile.getParent());
				if(Files.notExists(logFile)) {
					Files.createFile(logFile);
				}
				logger.addHandler(LogHandlerFactory.buildFileHandler(logger.getName()));
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		return logger;
	}
}
