package RegressionTest.Container;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

import RegressionTest.BaseRegression;

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
		dc.setHeaders(new String[] {"A", "B", "C"});
		
		dc.addRow(new String[] {"1", "2", "3"});
		dc.addRow(new String[] {"4", "5", "6"});
		dc.addRow(new String[] {"7", "8", "9"});
		
		dc.deleteRow(1);
		
		Filter filter = new Filter();
		filter.addFilterRule("B", "2", EOperator.EQUALS);
		dc.deleteRows(filter);
		
		testResult(dc.getRowCount(), "Container row count", 1);
		testResult(dc.getRow(0)[2], "Container remaining data", "9");
	}

}
