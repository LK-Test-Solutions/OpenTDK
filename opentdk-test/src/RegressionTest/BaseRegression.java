package RegressionTest;

/**
 * Super class of all test classes with predefined assertion methods.
 * 
 * @author LK Test Solutions
 *
 */
public abstract class BaseRegression {
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
			runTest();
		} catch(Exception e) {
			e.printStackTrace();
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
				throw new AssertionError(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			}
		} else {
			if (expected == null) {
				System.out.println("Success: " + fieldName + " == " + actual);
			} else {
				throw new AssertionError(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			}
		}
	}

	public static void testResult(int actual, String fieldName, int expected) {
		if (actual == expected) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			throw new AssertionError(fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
		}

	}
}
