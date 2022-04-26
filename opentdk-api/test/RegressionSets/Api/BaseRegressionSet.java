package RegressionSets.Api;

import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public abstract class BaseRegressionSet {

	protected static final void runRegressionTest(Class<?>[] testClasses) {
		StringBuilder sb = new StringBuilder();
		for (Class<?> cls : testClasses) {
			sb.append(cls.getSimpleName()).append(" ");
		}
		System.out.println();
		System.out.println("=================== < Run test classes: " + sb.toString() + ">");
		System.out.println();

		String successful = "true";
		Result result = JUnitCore.runClasses(testClasses);
		for (Failure failure : result.getFailures()) {
			System.out.println("Failure detected: " + failure.toString());
			successful = "false";
		}
		if (!result.wasSuccessful()) {
			successful = "false";
		}
		System.out.println();
		System.out.println("=================== >> Successful: " + successful);
		System.out.println();
	}

	public static void testResult(String actual, String fieldName, String expected) {
		if (actual != null && expected != null) {
			if (actual.contentEquals(expected)) {
				System.out.println("Success: " + fieldName + " == " + actual);
			} else {
				System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
				Assert.fail();
			}
		} else {
			if (expected == null) {
				System.out.println("Success: " + fieldName + " == " + actual);
			} else {
				System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
				Assert.fail();
			}
		}
	}

	public static void testResult(Number actual, String fieldName, Number expected) {
		if (actual == expected) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			Assert.fail();
		}

	}
}
