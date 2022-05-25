package RegressionTest.CSVContainer;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.util.ListUtil;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_addColumn extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_addColumn();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer(new String[] { "header1", "header2", "header3" });
		dc.addRow(new String[] { "r", "e", "d" });
		dc.addRow(new String[] { "g", "r", "e" });
		dc.addRow(new String[] { "b", "l", "u" });
	
		dc.addColumn("header3");
		dc.addColumn("header3");
		
		dc.setColumn("header3_2", new String[] { "", "e", "e"});
		dc.setColumn("header3_3", new String[] { "", "n", ""});
		
		BaseRegression.testResult(String.join("", dc.getRow(1)), "Second row joined", "green");
		
		dc.setColumn("header3_3", ListUtil.asList(new String[] { "", "t", "" }));
		BaseRegression.testResult(String.join("", dc.getRow(1)), "Second row joined", "greet");
	}
}
