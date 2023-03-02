package RegressionTest.Logging;

import java.util.logging.Level;

import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

import RegressionTest.BaseRegression;

public class RT_Logging_log extends BaseRegression {

	private static final String defaultLogFile = location + "logs/Application.log";
	private static final String logFile = location + "logs/RT_Logging_log.log";

	public static void main(String[] args) {
		new RT_Logging_log();
	}

	@Override
	public void runTest() {
		boolean success = false;

		MLogger.getInstance().printExceptions(false);
		MLogger.getInstance().setTraceLevel(Level.ALL);

		System.out.println("Expected log format:");
		System.out.println("Mai 05, 2020 9:18:42 NACHM. ");
		System.out.println("INFO: Message");
		success = MLogger.getInstance().log("Message");
		BaseRegression.testResult(String.valueOf(success), "Log message", "true");

		success = FileUtil.checkDir(defaultLogFile);
		BaseRegression.testResult(String.valueOf(success), "Default log file exists", "true");

		MLogger.getInstance().setLogFile(logFile);

		System.out.println("");
		System.out.println("Expected log format:");
		System.out.println("Juli 05, 2022 7:10:09 PM MainClass Class Method");
		System.out.println("WARNING: Message");
		success = MLogger.getInstance().log(Level.WARNING, "Message", "MainClass", "Class", "Method");
		BaseRegression.testResult(String.valueOf(success), "Log warning", "true");

		System.out.println("");
		System.out.println("Expected log format:");
		System.out.println("Mai 05, 2020 9:18:42 NACHM. java.lang.Exception");
		System.out.println("SEVERE: java.lang.Exception");
		success = MLogger.getInstance().log(Level.SEVERE, new Exception());
		BaseRegression.testResult(String.valueOf(success), "Log error", "true");

		success = FileUtil.checkDir(logFile);
		BaseRegression.testResult(String.valueOf(success), "Log file exists", "true");

		MLogger.getInstance().printExceptions(true);
		MLogger.getInstance().setTraceLevel(Level.OFF);
		System.out.println("");
		System.out.println("Expected log format:");
		System.out.println("");
		success = MLogger.getInstance().log(Level.FINE, new Exception());
		BaseRegression.testResult(String.valueOf(success), "Log nothing", "true");

		System.out.println("");
		System.out.println("Committ invalid parameters:");
		MLogger.getInstance().setLogKeepAge(Integer.MAX_VALUE);
		MLogger.getInstance().setLogSizeLimit(10000000);
		MLogger.getInstance().setTraceLevel(5);
		int keepAge = MLogger.getInstance().getLogKeepAge();
		long logSize = MLogger.getInstance().getLogSizeLimit();
		Level level = MLogger.getInstance().getTraceLevel();
		BaseRegression.testResult(keepAge, "Log keep age unchanged", 30);
		BaseRegression.testResult((int) logSize, "Log size unchanged", 10000);
		BaseRegression.testResult(level.getName(), "Log level unchanged", Level.SEVERE.getName());

		System.out.println("");
		MLogger.getInstance().setTraceLevel(Level.SEVERE);
		System.out.println("Committ valid parameters:");
		MLogger.getInstance().setLogKeepAge(10);
		MLogger.getInstance().setLogSizeLimit(1000);
		MLogger.getInstance().setTraceLevel(2);
		keepAge = MLogger.getInstance().getLogKeepAge();
		logSize = MLogger.getInstance().getLogSizeLimit();
		level = MLogger.getInstance().getTraceLevel();
		BaseRegression.testResult(keepAge, "Log keep age", 10);
		BaseRegression.testResult((int) logSize, "Log size", 1000);
		BaseRegression.testResult(level.getName(), "Log level", Level.WARNING.getName());

		System.out.println("");
		System.out.println("Committ invalid log parameters:");
		MLogger.getInstance().log(Level.WARNING, null, "", "", "");
		MLogger.getInstance().log(Level.WARNING, "", "", "", "");
		MLogger.getInstance().setLogFile("");
		BaseRegression.testResult(MLogger.getInstance().getLogFile(), "Log file", logFile);

		MLogger.getInstance().setTraceLevel(0);
		MLogger.getInstance().setTraceLevel(1);
		MLogger.getInstance().setTraceLevel(2);
		MLogger.getInstance().setTraceLevel(3);
		
		// Reset
		MLogger.getInstance().setLogSizeLimit(10000);
		MLogger.getInstance().setLogKeepAge(30);
		MLogger.getInstance().setLogFile("");
	}

}
