package RegressionTests.CSVContainer;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.util.ListUtil;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CSVContainer_addColumn {

	@Test
	public void test() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test ###");

		DataContainer dc = new DataContainer(new String[] { "header1", "header2", "header3" });
		dc.addRow(new String[] { "r", "e", "d" });
		dc.addRow(new String[] { "g", "r", "e" });
		dc.addRow(new String[] { "b", "l", "u" });
	
		dc.addColumn("header3");
		dc.addColumn("header3");
		
		dc.setColumn("header3_2", new String[] { "", "e", "e" });
		dc.setColumn("header3_3", new String[] { "", "n", "" });
		
		BaseRegressionSet.testResult(String.join("", dc.getRow(1)), "Second row joined", "green");
		
		dc.setColumn("header3_3", ListUtil.asList(new String[] { "", "t", "" }));
		BaseRegressionSet.testResult(String.join("", dc.getRow(1)), "Second row joined", "greet");


		System.out.println("### " + this.getClass().getSimpleName() + " test ###");
	}
}
