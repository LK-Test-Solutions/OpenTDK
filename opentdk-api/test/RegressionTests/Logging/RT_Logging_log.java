package RegressionTests.Logging;

import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;

import RegressionSets.Api.BaseRegressionSet;

import org.junit.BeforeClass;
import org.junit.Test;

public class RT_Logging_log {

	@BeforeClass
	public static void init() {
		MLogger.getInstance().printExceptions(false);
		MLogger.getInstance().setTraceLevel(Level.ALL);
//		MLogger.getInstance().setLogSizeLimit(1);
	}

	@Test
	public void test() {
		boolean success = false;

		System.out.println("");
		System.out.println("Expected log format:");
		System.out.println("Mai 05, 2020 9:18:42 NACHM. ");
		System.out.println("INFO: Message");
		success = MLogger.getInstance().log("Message");
		
		MLogger.getInstance().setLogFile("logs/RT_Logging_log.log");

		System.out.println("");
		System.out.println("Expected log format:");
		System.out.println("Mai 05, 2020 9:18:42 NACHM. java.lang.Exception");
		System.out.println("SEVERE: java.lang.Exception");
		success = MLogger.getInstance().log(Level.SEVERE, new Exception());

		System.out.println("");
		System.out.println("Perform heavy logging");	
		for (int i = 0; i < 1000; i++) {
			success = MLogger.getInstance().log(Level.SEVERE, "Message", "Main Class", "Class", "Method");
		}
		System.out.println("");
		System.out.println("Check overflow");
		MLogger.getInstance().setLogKeepAge(Integer.MAX_VALUE);
		MLogger.getInstance().setLogSizeLimit(10000000);
		MLogger.getInstance().setTraceLevel(5);
		
		System.out.println("");
		BaseRegressionSet.testResult(String.valueOf(success), "Logging", "true");

	}

}
