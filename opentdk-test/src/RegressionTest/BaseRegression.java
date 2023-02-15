package RegressionTest;

import org.opentdk.api.datastorage.DataContainer;

/**
 * Super class of all test classes with predefined assertion methods.
 * 
 * @author LK Test Solutions
 *
 */
public abstract class BaseRegression {
	/**
	 * Storage object to display the result overview at the end of the test(s).
	 */
	static final DataContainer resultContainer = new DataContainer();
	static {
		resultContainer.getTabContainer().setColumnDelimiter(" | ");
		resultContainer.getTabContainer().setHeaders(new String[] { "TEST CLASS", "SUCCESS" });
	}
	/**
	 * Set to false if any error occurred during one test case execution. Set to true before running a
	 * test.
	 */
	private static boolean success;

	/**
	 * Usage in the sub classes:
	 * 
	 * <pre>
	 * public static void main(String[] args) {
	 * 	new Subclass();
	 * }
	 * 
	 * protected void runTest() {
	 * 	String expected = ...
	 * 	BaseRegression.testResult(expected, "Test value", "true");
	 * }
	 * </pre>
	 */
	protected BaseRegression() {
		System.out.println();
		System.out.println("=================== < Run test class: " + getClass().getSimpleName() + " >");
		System.out.println();
		try {
			success = true;
			runTest(); // Sets success to false in case of an error
		} catch (Exception e) {
			success = false;
			e.printStackTrace();			
		} finally {
			resultContainer.getTabContainer().addRow(new String[] { getClass().getSimpleName(), String.valueOf(success) });
		}
		System.out.println();
		System.out.println("=================== >> Finished ");
		System.out.println();
	}

	/**
	 * Every test class to implement a method where the test gets executed.
	 */
	protected abstract void runTest();

	public static void testResult(String actual, String fieldName, String expected) {
		if (actual != null && expected != null) {
			if (actual.contentEquals(expected)) {
				System.out.println("Success: " + fieldName + " == " + actual);
			} else {
				System.err.println(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
				success = false;
			}
		} else {
			if (expected == null) {
				System.out.println("Success: " + fieldName + " == " + actual);
			} else {
				System.err.println(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
				success = false;
			}
		}
	}

	public static void testResult(int actual, String fieldName, int expected) {
		if (actual == expected) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.err.println(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			success = false;
		}

	}
	
	public static void testResult(Object actual, String fieldName, Object expected) {
		if (actual == expected) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.err.println(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			success = false;
		}

	}
}
