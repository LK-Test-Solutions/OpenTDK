package org.opentdk.api.logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;


/**
 * A factory class to provide custom log handlers such as {@link ConsoleHandler} and {@link FileHandler}.
 * The handlers created by this factory are pre-configured with a custom log format using {@link LogFormatter}.
 * This class is designed to standardize the creation of logging handlers across the application.
 * <p>
 * This is a utility class with only static methods and cannot be instantiated.
 */
public final class LogHandlerFactory {

	/**
	 * Creates and configures a {@link ConsoleHandler} with a custom log formatter {@link LogFormatter}.
	 * The returned handler is ready to be added to a {@link java.util.logging.Logger} instance and ensures
	 * consistent log output formatting for console logging.
	 *
	 * @return A {@link ConsoleHandler} instance configured with {@link LogFormatter}.
	 */
	public static ConsoleHandler buildConsoleHandler() {
		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(new LogFormatter());
		return ch;
	}

	/**
	 * Creates and configures a {@link FileHandler} with a custom log formatter {@link LogFormatter}.
	 * The returned handler is used for logging messages to a file specified by the logger name.
	 * The handler is configured to append log messages to the file and ensures consistent log
	 * formatting across the application.
	 *
	 * @param loggerName The name of the logger, which is also used as the name of the log file.
	 *                   This defines the destination to which log messages will be written.
	 * @return A {@link FileHandler} instance configured with {@link LogFormatter}.
	 * @throws SecurityException If a security manager exists and denies access to the file.
	 * @throws IOException If an I/O error occurs while creating or opening the log file.
	 */
	public static FileHandler buildFileHandler(String loggerName) throws SecurityException, IOException {
		FileHandler fh = new FileHandler(loggerName, true);
		fh.setFormatter(new LogFormatter());
		return fh;
	}

}
