package RegressionTest.Logging;

import java.io.IOException;
import java.util.logging.Level;

import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

import RegressionTest.BaseRegression;

public class RT_Logging_log extends BaseRegression {
	
	private static final String defaultLogFile = "logs/Application.log";
	private static final String logFile = "logs/RT_Logging_log.log";

	public static void main(String[] args) {
		new RT_Logging_log();
	}
	
	@Override
	public void runTest() {		
		boolean success = false;
		
		try {
			FileUtil.deleteFileOrFolder(defaultLogFile);
			FileUtil.deleteFileOrFolder(logFile);
		} catch (IOException e) {		
			throw new RuntimeException(e);
		}
		
		MLogger.getInstance().printExceptions(false);
		MLogger.getInstance().setTraceLevel(Level.ALL);

		System.out.println("Expected log format:");
		System.out.println("Mai 05, 2020 9:18:42 NACHM. ");
		System.out.println("INFO: Message");
		success = MLogger.getInstance().log("Message");
		BaseRegression.testResult(String.valueOf(success), "Log message", "true");
		
		try {
			success = FileUtil.checkDir(defaultLogFile);
			BaseRegression.testResult(String.valueOf(success), "Default log file exists", "true");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		MLogger.getInstance().setLogFile(logFile);
		
		System.out.println("");
		MLogger.getInstance().setTraceLevel(Level.WARNING);
		System.out.println("Expected log format:");
		System.out.println("Juli 04, 2022 2:11:28 PM  java.lang.NullPointerException runTest");
		System.out.println("SEVERE: java.lang.NullPointerException: Null reference");
		success = MLogger.getInstance().log(Level.WARNING, "Message", "MainClass", "Class", "Method");
		BaseRegression.testResult(String.valueOf(success), "Log warning", "true");

		System.out.println("");
		System.out.println("Expected log format:");
		System.out.println("Mai 05, 2020 9:18:42 NACHM. java.lang.Exception");
		System.out.println("SEVERE: java.lang.Exception");
		success = MLogger.getInstance().log(Level.SEVERE, new Exception());
		BaseRegression.testResult(String.valueOf(success), "Log error", "true");
		
		try {
			success = FileUtil.checkDir(logFile);
			BaseRegression.testResult(String.valueOf(success), "Log file exists", "true");
		} catch (IOException e) {
			throw new RuntimeException(e); 
		}
		
		try {
			FileUtil.deleteFileOrFolder(logFile);
		} catch (IOException e) {		
			throw new RuntimeException(e);
		}
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
		
		MLogger.getInstance().setTraceLevel(Level.SEVERE);
		int keepAge = MLogger.getInstance().getLogKeepAge();
		long logSize = MLogger.getInstance().getLogSizeLimit();
		Level level = MLogger.getInstance().getTraceLevel();
		BaseRegression.testResult(keepAge, "Log keep age unchanged", 30);
		BaseRegression.testResult((int)logSize, "Log size unchanged", 10000);
		BaseRegression.testResult(level.getName(), "Log level unchanged", Level.SEVERE.getName());

	}

}
