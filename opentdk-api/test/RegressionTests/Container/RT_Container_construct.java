package RegressionTests.Container;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.Filter;
import org.opentdk.api.datastorage.BaseContainer.EHeader;
import org.opentdk.api.io.FileUtil;

import RegressionSets.Api.BaseRegressionSet;

public class RT_Container_construct {

	@Test
	public void test() throws IOException {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test ###");

		String testfile = "testdata/testfile.txt";
		FileUtil.createFile(testfile);
		
		DataContainer dc0 = new DataContainer();
		BaseRegressionSet.testResult(dc0.getClass().getSimpleName(), "Name of container class", "DataContainer");
		BaseRegressionSet.testResult(dc0.getColumnDelimiter(), "Column Delimiter", "");
		BaseRegressionSet.testResult(dc0.getFileName(), "File Name", "");
		BaseRegressionSet.testResult(dc0.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
		BaseRegressionSet.testResult(String.valueOf(dc0.getHeaderRowIndex()), "Header Row Index", "0");
		BaseRegressionSet.testResult(String.valueOf(dc0.getHeaders().isEmpty()), "Headers empty", "true");
		System.out.println();
		
		DataContainer dc1 = new DataContainer("	");
		BaseRegressionSet.testResult(dc1.getClass().getSimpleName(), "Name of container class", "DataContainer");
		BaseRegressionSet.testResult(dc1.getColumnDelimiter(), "Column Delimiter", "	");
		BaseRegressionSet.testResult(dc1.getFileName(), "File Name", "");
		BaseRegressionSet.testResult(dc1.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
		BaseRegressionSet.testResult(String.valueOf(dc1.getHeaderRowIndex()), "Header Row Index", "0");
		BaseRegressionSet.testResult(String.valueOf(dc1.getHeaders().isEmpty()), "Headers empty", "true");
		System.out.println();

		Vector<DataContainer> dcs = new Vector<>();
		dcs.add(new DataContainer(new String[5]));
		for (DataContainer dc : dcs) {
			BaseRegressionSet.testResult(dc.getClass().getSimpleName(), "Name of container class", "DataContainer");
			BaseRegressionSet.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
			BaseRegressionSet.testResult(dc.getFileName(), "File Name", "");
			BaseRegressionSet.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
			BaseRegressionSet.testResult(String.valueOf(dc.getHeaderRowIndex()), "Header Row Index", "-1");
			BaseRegressionSet.testResult(String.valueOf(dc.getHeaders().isEmpty()), "Headers empty", "false");
		}

		System.out.println();
		dcs.clear();
		dcs.add(new DataContainer(testfile));
		dcs.add(new DataContainer(testfile, new Filter()));
		dcs.add(new DataContainer(testfile, ";", new Filter()));
		dcs.add(new DataContainer(testfile, ";"));
		dcs.add(new DataContainer(testfile, ";", 3, new Filter()));
		dcs.add(new DataContainer(testfile, ";", -1));
		for (DataContainer dc : dcs) {
			BaseRegressionSet.testResult(dc.getClass().getSimpleName(), "Name of container class", "DataContainer");
			BaseRegressionSet.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
			BaseRegressionSet.testResult(dc.getFileName(), "File Name", "testdata/testfile.txt");
			BaseRegressionSet.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
			BaseRegressionSet.testResult(String.valueOf(dc.getHeaders().isEmpty()), "Headers empty", "true");
		}

		System.out.println();
		dcs.clear();
		dcs.add(new DataContainer(testfile, "+", new String[] { "header1", "header2", "header3" }));
		dcs.add(new DataContainer(testfile, "+"));
		dcs.add(new DataContainer(testfile, "+", -1));
		dcs.add(new DataContainer(testfile, "+", -1, new String[] { "header1", "header2" }, null));
		for (DataContainer dc : dcs) {
			BaseRegressionSet.testResult(dc.getClass().getSimpleName(), "Name of container class", "DataContainer");
			BaseRegressionSet.testResult(dc.getColumnDelimiter(), "Column Delimiter", "+");
			BaseRegressionSet.testResult(dc.getFileName(), "File Name", "testdata/testfile.txt");
			BaseRegressionSet.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
		}

		System.out.println();
		dcs.clear();
		ResultSet dummy = null;
		dcs.add(new DataContainer(dummy));
		dcs.add(new DataContainer(dummy, new Filter()));
		dcs.add(new DataContainer(dummy, new Filter(), ";"));

		for (DataContainer dc : dcs) {
			BaseRegressionSet.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
			BaseRegressionSet.testResult(dc.getFileName(), "File Name", null);
			BaseRegressionSet.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
			BaseRegressionSet.testResult(String.valueOf(dc.getHeaderRowIndex()), "Header Row Index", "0");
			BaseRegressionSet.testResult(String.valueOf(dc.getHeaders().isEmpty()), "Headers empty", "true");
		}

		FileUtil.deleteFile(testfile);

		System.out.println("### " + this.getClass().getSimpleName() + " test ###");
		System.out.println();
	}
}
