package RegressionTest.Container;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.mapping.EOperator;

import RegressionTest.BaseRegression;

/**
 * Tests if removing rows from a tabular container works correctly if they where added to an empty
 * container at first.
 * 
 * @author LK Test Solutions
 *
 */
public class RT_Container_delete extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_delete();
	}

	@Override
	protected void runTest() {
		deleteRow();
	}

	private void deleteRow() {
		DataContainer dc = new DataContainer();
		dc.tabInstance().setHeaders(new String[] { "A", "B", "C" });

		dc.tabInstance().addRow(new String[] { "1", "2", "3" });
		dc.tabInstance().addRow(new String[] { "4", "5", "6" });
		dc.tabInstance().addRow(new String[] { "7", "8", "9" });

		dc.tabInstance().deleteRow(1);

		Filter filter = new Filter();
		filter.addFilterRule("B", "2", EOperator.EQUALS);
		dc.tabInstance().deleteRows(filter);

		testResult(dc.tabInstance().getRowCount(), "Container row count", 1);
		testResult(dc.tabInstance().getRow(0)[2], "Container remaining data", "9");
	}

}
