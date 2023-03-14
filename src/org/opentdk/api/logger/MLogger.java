/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.api.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.util.*;

/**
 * Custom logging class which uses the Java Logging API for writing messages to a log file. All methods of this class can be called in a static way, using the <code>getInstance</code> method. When
 * calling the <code>getInstance</code> method the fist time at application runtime, a new instance of the <code>MLogger</code> class will be created by itself. This instance is accessible during
 * runtime of the application by any other class. To initialize a new instance of <code>MLogger</code>, just call the following methods, before writing the first log message:
 * 
 * <pre>
 * MLogger.getInstance().setLogFile("[Name of the Log file]");
 * MLogger.getInstance().setTraceLevel([0, 1, 2 or 3]);
 * </pre>
 * 
 * and then write a log message, like shown in the following sample:<br>
 * <br>
 * 
 * <pre>
 * MLogger.getInstance().setLogFile("logs/Application.log");
 * MLogger.getInstance().setTra
 * ceLevel(3); // Log all messages (Error, Warning and Information)
 * MLogger.getInstance().log(Level.SEVERE, "The mesh trace string has the wrong format! ", FileUtil.class.getSimpleName(), "createResultsFileName");
 * </pre>
 * 
 * This will produce the output:<br>
 * <br>
 * 
 * <code>Sep. 21, 2018 4:36:39 NACHM. FileUtil createResultsFileName<br> 
 *		SCHWERWIEGEND: The mesh trace string has the wrong format!</code><br>
 * <br>
 *
 * When no <code>logFile</code> and <code>traceLevel</code> is set, the instance will be initialized with the default values:<br>
 * 
 * <pre>
 * logFile = ./logs/Application.log 
 * traceLevel = Level.SEVERE
 * </pre>
 */
public final class MLogger {

	/**
	 * The one and only instance of the <code>MLogger</code> class.
	 */
	private static MLogger instance;

	/**
	 * The java.util.Logger to perform the logging itself.
	 */
	private final Logger logger = Logger.getAnonymousLogger();

	/**
	 * The name of the log file. Default is "./logs/Application.log".
	 */
	private String logFile = "./logs/Application.log";

	/**
	 * The file size limit in KB for the log file. Default is 10 MB.
	 */
	private long logSizeLimit = 10000;

	/**
	 * The age in days for keeping the log file. Default is 30.
	 */
	private int logKeepAge = 30;

	/**
	 * Enable or disable printing full stack traces of exceptions.
	 */
	private boolean printExceptions = true;

	/**
	 * Formatter object for the log output. Gets used by the FileHandler object and is declared as field to initialize it only once.
	 */
	private final SimpleFormatter formatter = new SimpleFormatter();

	/**
	 * Turn on or off printing exceptions.
	 * 
	 * @param print true to print exceptions, false to ignore the trace.
	 */
	public void printExceptions(boolean print) {
		printExceptions = print;
	}

	/**
	 * Invisible constructor that gets called when using {@link #getInstance()} for the first time in an application.
	 */
	private MLogger() {

	}

	/**
	 * When calling this method the fist time, a new instance of the MLogger class will be created and returned to the caller. For every further call, the already created instance will be returned.
	 * This construct allows access to all methods and properties of an instance of the MLogger class from any other class during runtime of an application. The usage of the methods is like it is in a
	 * static way, but with an instantiated class.<br>
	 * <br>
	 * 
	 * e.g.:<br>
	 * <code>MLogger.getInstance().log(Level.SEVERE, "Message", "mainClassName", "thisClassName", "thisMethod");</code>
	 * 
	 * @return The instance of the MLogger class
	 */
	public static MLogger getInstance() {
		if (instance == null) {
			instance = new MLogger();
		}
		return instance;
	}

	/**
	 * This log method writes a message into the log file with Level.INFO.
	 * 
	 * @param message The message that will be written to the log file
	 * @return A flag to indicate whether the logging was successful or not
	 * 
	 * @see java.util.logging.Level
	 */
	public boolean log(String message) {
		return log(Level.INFO, message);
	}

	/**
	 * This log method writes a message into the log file with configurable level.
	 * 
	 * @param level   The logging level of type java.util.logging.Level
	 * @param message The message that will be written to the log file
	 * @return A flag to indicate whether the logging was successful or not
	 */
	public boolean log(Level level, String message) {
		return log(level, message, "", "");
	}

	/**
	 * This log method writes an exception message into the log file and the complete stack trace of the exception if {@link #printExceptions} is set to true.
	 * 
	 * @param level The logging level of type java.util.logging.Level
	 * @param e     The object of the exception that will be logged like IOException, FileNotFoundException etc.
	 * @return A flag to indicate whether the logging was successful or not
	 * 
	 * @see java.util.logging.Level
	 * @see java.lang.Exception
	 */
	public boolean log(Level level, Exception e) {
		return log(level, e, "");
	}

	/**
	 * This log method writes an exception message into the log file and the complete stack trace of the exception if {@link #printExceptions} is set to true.
	 * 
	 * @param level      The logging level of type java.util.logging.Level
	 * @param ex         The object of the exception that will be logged like IOException, FileNotFoundException etc.
	 * @param methodName The name of the method that issued the logging request
	 * @return A flag to indicate whether the logging was successful or not
	 * 
	 * @see java.util.logging.Level
	 * @see java.lang.Exception
	 */
	public boolean log(Level level, Exception ex, String methodName) {
		boolean loggedSuccessful = false;
		loggedSuccessful = log(level, ex.toString(), ex.getClass().getName(), methodName);
		if (printExceptions) {
			try {
				File f = new File(logFile);
				FileUtil.checkDir(f.getParent(), true);
				ex.printStackTrace(new PrintStream((new FileOutputStream(f, true))));
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		return loggedSuccessful;
	}

	/**
	 * This log method writes a message into the log file with configurable level, origin class, and method.
	 * 
	 * @param level      The logging level of type java.util.logging.Level
	 * @param message    The message that will be written to the log file
	 * @param className  The name of the class that issued the logging request
	 * @param methodName The name of the method that issued the logging request
	 * 
	 * @see java.util.logging.Level
	 * @see java.lang.Exception
	 * 
	 * @return A flag to indicate whether the logging was successful or not
	 */
	public boolean log(Level level, String message, String className, String methodName) {
		return log(level, message, "", className, methodName);
	}

	/**
	 * This method writes a message, specifying the main class, the source class and the calling method into the log file.
	 *
	 * @param level      The logging level of type java.util.logging.Level
	 * @param message    The message that will be written to the log file
	 * @param mainName   The main class of the application
	 * @param className  The name of the class that issued the logging request
	 * @param methodName The name of the method that issued the logging request
	 * @return A flag to indicate whether the logging was successful or not.
	 * 
	 * @see java.util.logging.Level
	 */
	public boolean log(Level level, String message, String mainName, String className, String methodName) {
		boolean ret = true;

		if (level == null || message == null || mainName == null || className == null || methodName == null) {
			logger.log(Level.SEVERE, "Null arguments committed");
			ret = false;
		} else if (StringUtils.isBlank(message) && StringUtils.isBlank(mainName) && StringUtils.isBlank(className) && StringUtils.isBlank(methodName)) {
			logger.log(Level.SEVERE, "Only blank arguments committed");
			ret = false;
		}
		if (ret) {
			File file = new File(getLogFile());
			if (file.getParent() != null) {
				FileUtil.checkDir(file.getParent(), true);
			}
			archiveLogFile();
			setFileHandler();
			mainName += " " + className;

			logger.logp(level, mainName, methodName, message);

			for (Handler handler : logger.getHandlers()) {
				handler.flush();
				if (handler instanceof FileHandler) {
					handler.close(); // Releases the log file
				}
			}
			ret = true;
		}
		return ret;
	}

	/**
	 * A string that is valid to be written into the log file is not null, not empty or a sequence of empty strings.
	 * 
	 * @param toCheck The string to check
	 * @return False if the string is not valid, true otherwise
	 */
	private boolean isValidLogString(String toCheck) {
		if (StringUtils.isBlank(toCheck) || toCheck.length() > Short.MAX_VALUE) {
			return false;
		}
		return true;
	}

	/**
	 * Get the name of the log file, to which MLogger will write messages.
	 * 
	 * @return A string with the name of the log file
	 */
	public String getLogFile() {
		return logFile;
	}

	/**
	 * Sets the name of the log file, to which MLogger will write messages.
	 * 
	 * @param lf A string with the name of the log file
	 */
	public void setLogFile(String lf) {
		if (isValidLogString(lf)) {
			logFile = lf;
		} else {
			logger.log(Level.SEVERE, "Log file string is invalid. Log file stays the same or default 'logs/Application.log' gets used in case of a first call.");
		}
	}

	/**
	 * Removes all file handler instances from {@link #logger} and add a new one with the committed file.
	 */
	private void setFileHandler() {
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler(logFile, true);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
			fileHandler = null;
		}
		if (fileHandler != null) {
			fileHandler.setFormatter(formatter);

			for (Handler handler : logger.getHandlers()) {
				if (handler instanceof FileHandler) {
					logger.removeHandler(handler);
				}
			}
			logger.addHandler(fileHandler);
		} else {
			logger.log(Level.SEVERE, "FileHandler initialization failed in 'setLogFile' ==> Logging to file is disabled");
		}
	}

	/**
	 * Get the maximum size in KB (kilobyte). If the {@link #logSizeLimit} is exceeded, MLogger will rename the log file by adding the current date to the name and create a new log file. The archived
	 * log files will automatically be deleted, when the keeping age has been exceeded.
	 * 
	 * @return an long value with the file size in KB
	 */
	public long getLogSizeLimit() {
		return logSizeLimit;
	}

	/**
	 * Sets the maximum size in KB (kilobyte). If the {@link #logSizeLimit} is exceeded, MLogger will rename the log file by adding the current date to the name and create a new log file. The archived
	 * log files will automatically be deleted, when the keeping age has been exceeded.
	 * 
	 * @param lsLimit An long value with the file size in KB, Maximum is 1 GB
	 * 
	 * @see #getLogSizeLimit()
	 * @see #getLogKeepAge()
	 * @see #setLogKeepAge(int)
	 */
	public void setLogSizeLimit(long lsLimit) {
		if (lsLimit > 0 && lsLimit < 1000000) {
			logSizeLimit = lsLimit;
		} else {
			logger.log(Level.SEVERE, "Log size limit out of bounds. Default 10 MB gets used.");
		}
	}

	/**
	 * Get the number of days to store archived log files. I an archived log file has exceeded the defined age, it will be deleted. This logic does only work for log files that have the standard date
	 * and time prefix in their name and for log files, that are stored in the defined log path.
	 * 
	 * @return An integer value with the number of days
	 */
	public int getLogKeepAge() {
		return logKeepAge;
	}

	/**
	 * Sets the number of days to store archived log files. I an archived log file has exceeded the defined age, it will be deleted. This logic does only work for log files that have the standard date
	 * and time prefix in their name and for log files, that are stored in the defined logPath.
	 * 
	 * @param lka An integer value with the number of days
	 */
	public void setLogKeepAge(int lka) {
		if (lka > 0 && lka < Short.MAX_VALUE) {
			logKeepAge = lka;
		} else {
			logger.log(Level.SEVERE, "Log keep age out of bounds. Default 30 days gets used.");
		}
	}

	/**
	 * Get the value of the logger trace level property.
	 * 
	 * @return the value of trace level property of type java.util.logging.Level
	 * 
	 * @see java.util.logging.Level
	 */
	public Level getTraceLevel() {
		return logger.getLevel();
	}

	/**
	 * Sets the trace level property with a value of type java.util.logging.Level.
	 * 
	 * @param lv A value of type Level e.g. Level.SEVERE
	 * 
	 * @see java.util.logging.Level
	 */
	public void setTraceLevel(Level lv) {
		logger.setLevel(lv);
	}

	/**
	 * Sets the traceLevel property with a value of type java.util.logging.Level. that corresponds to the specified input parameter of type integer. The following integer values are valid:
	 * 
	 * <pre>
	 * 0 = Level.OFF
	 * 1 = Level.SEVERE
	 * 2 = Level. WARNING
	 * 3 = Level.INFO
	 * </pre>
	 * 
	 * @param lv An integer value between 0, 1, 2 and 3.
	 * 
	 * @see java.util.logging.Level
	 * @see #setTraceLevel(Level)
	 */
	public void setTraceLevel(int lv) {
		switch (lv) {
		case 0:
			logger.setLevel(Level.OFF);
			break;
		case 1:
			logger.setLevel(Level.SEVERE);
			break;
		case 2:
			logger.setLevel(Level.WARNING);
			break;
		case 3:
			logger.setLevel(Level.ALL);
			break;
		default:
			logger.log(Level.SEVERE, "Invalid value for traveLevel in 'setTraceLevel' ==> Using Level.SEVERE instead.");
			logger.setLevel(Level.SEVERE);
			break;
		}
	}

	/**
	 * Creates a copy of the log file, specified by <code>logPath</code> and <code>logFile</code> properties, with a date prefix in the new name.<br>
	 * <br>
	 * e.g. <code>c:\temp\logs\20220101_App.log</code><br>
	 * <br>
	 * This method will be automatically executed when the size of the current log file has exceeded the size in KB, as defined by property <code>logSizeLimit</code>. The original log file will be
	 * deleted and the logging starts with a new, empty file for further messages.
	 */
	private void archiveLogFile() {
		File f = new File(logFile);
		if (f.exists()) {
			if (f.length() / 1024 > logSizeLimit) {
				boolean success = f.renameTo(new File(f.getParent() + DateUtil.get(EFormat.DATE_1.getDateFormat()) + "_" + f.getName()));
				if (!success) {
					logger.log(Level.SEVERE, "Log file could not be archived");
				}
				f.delete();
			}
		}
	}

}