package RegressionTest.Container;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.mapping.EOperator;

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
		// Use tabular container directly
		DataContainer dc = new DataContainer();
		dc.getTabContainer().setHeaders(new String[] {"A", "B", "C"});
		
		dc.getTabContainer().addRow(new String[] {"1", "2", "3"});
		dc.getTabContainer().addRow(new String[] {"4", "5", "6"});
		dc.getTabContainer().addRow(new String[] {"7", "8", "9"});
		
		dc.getTabContainer().deleteRow(1);
		
		Filter filter = new Filter();
		filter.addFilterRule("B", "2", EOperator.EQUALS);
		dc.getTabContainer().deleteRows(filter);
		
		testResult(dc.getTabContainer().getRowCount(), "Container row count", 1);
		testResult(dc.getTabContainer().getRow(0)[2], "Container remaining data", "9");
		
		
		// Use general methods
		dc = new DataContainer();
		dc.set(new String[] {"A", "B", "C"});
		
		dc.add(new String[] {"1", "2", "3"});
		dc.add(new String[] {"4", "5", "6"});
		dc.add(new String[] {"7", "8", "9"});
		
		dc.delete(1);
		
		Filter filter = new Filter();
		filter.addFilterRule("B", "2", EOperator.EQUALS);
		dc.delete(filter);
		
		testResult(dc.get(), "Container row count", 1);
		testResult(dc.get(0)[2], "Container remaining data", "9");
	}

}
