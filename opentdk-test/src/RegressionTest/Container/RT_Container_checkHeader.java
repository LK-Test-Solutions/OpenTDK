package RegressionTest.Container;

import java.util.HashMap;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

/**
 * Tests if adding headers to the {@link org.opentdk.api.datastorage.TabularContainer} works
 * correctly by calling its checkHeader method.
 * 
 * @author LK Test Solutions
 *
 */
public class RT_Container_checkHeader extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_checkHeader();
	}

	@Override
	protected void runTest() {
		DataContainer dc1 = new DataContainer(); // Default CSV container
		dc1.tabInstance().setHeaders(new String[] { "header1", "header2", "header3" });
		HashMap<String, Integer> compareHeaders1 = new HashMap<>();
		compareHeaders1.put("header1", 0);
		compareHeaders1.put("header2", 1);
		compareHeaders1.put("header3", 2);
		BaseRegression.testResult(dc1.tabInstance().checkHeader(compareHeaders1), "Header Compare", 0);

		DataContainer dc2 = new DataContainer();
		dc2.tabInstance().setHeaders(new String[] { "header1", "header3", "header2" });
		String[] compareHeaders2 = new String[] { "header1", "header2", "header3" };
		BaseRegression.testResult(String.valueOf(dc2.tabInstance().checkHeader(compareHeaders2)), "Header Compare", "1");

		DataContainer dc3 = new DataContainer();
		String[] referenceHeaders3 = new String[] { "header1", "header2", "header4" };
		String[] compareHeaders3 = new String[] { "header1", "header2", "header4" };
		BaseRegression.testResult(String.valueOf(dc3.tabInstance().checkHeader(referenceHeaders3, compareHeaders3)), "Header Compare", "0");

		DataContainer dc4 = new DataContainer();
		dc4.tabInstance().setHeaders(new String[] { "header1", "header3", "header2" });
		String[] compareHeaders4 = new String[] { "header1", "header2", "header4" };
		BaseRegression.testResult(String.valueOf(dc4.tabInstance().checkHeader(dc4.tabInstance().getHeaders(), compareHeaders4)), "Header Compare", "-1");
	}
}
