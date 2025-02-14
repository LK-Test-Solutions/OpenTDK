package org.opentdk.api.logger;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


/**
 * Custom log formatter that extends {@link SimpleFormatter} to provide a consistent and
 * structured format for log messages. The formatter outputs log entries in the following structure:
 * [timestamp] [log level] logger name: message
 * <p>
 * Example formatted log entry:
 * [2025-01-01 12:00:00] [SEVERE] logs/application.log: Invalid program parameter
 * <p>
 * The format includes:
 * - Timestamp of the log entry in ISO 8601 date and time format (yyyy-MM-dd HH:mm:ss).
 * - Log level (e.g., SEVERE, INFO, WARNING).
 * - Name of the logger that generated the log entry.
 * - The actual log message.
 * <p>
 * This implementation overrides the default formatting behavior of {@link SimpleFormatter}.
 */
public class LogFormatter extends SimpleFormatter {
	@Override
	public String format(LogRecord logRecord) {	
		// %x refers to the number of the following parameter
		// E.g [2025-01-01 12:00:00] [SEVERE] logs/application.log: Invalid program parameter 
		return String.format("[%1$tF %1$tT] [%2$s] %3$s: %4$s%n", logRecord.getMillis(), logRecord.getLevel().getName(), logRecord.getLoggerName(), logRecord.getMessage());
	}
}
