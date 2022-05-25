package RegressionTest.Container;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.Filter;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

public class RT_Container_construct extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_construct();
	}
	
	@Override
	protected void runTest() {
		String testfile = "testdata/testfile.txt";
		try {
			FileUtil.createFile(testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DataContainer dc0 = new DataContainer();
		BaseRegression.testResult(dc0.getClass().getSimpleName(), "Name of container class", "DataContainer");
		BaseRegression.testResult(dc0.getColumnDelimiter(), "Column Delimiter", "");
		BaseRegression.testResult(dc0.getFileName(), "File Name", "");
		BaseRegression.testResult(dc0.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
		BaseRegression.testResult(String.valueOf(dc0.getHeaderRowIndex()), "Header Row Index", "0");
		BaseRegression.testResult(String.valueOf(dc0.getHeaders().isEmpty()), "Headers empty", "true");
		System.out.println();
		
		DataContainer dc1 = new DataContainer("");
		BaseRegression.testResult(dc1.getClass().getSimpleName(), "Name of container class", "DataContainer");
		BaseRegression.testResult(dc1.getColumnDelimiter(), "Column Delimiter", ";");
		BaseRegression.testResult(dc1.getFileName(), "File Name", "");
		BaseRegression.testResult(dc1.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
		BaseRegression.testResult(String.valueOf(dc1.getHeaderRowIndex()), "Header Row Index", "0");
		BaseRegression.testResult(String.valueOf(dc1.getHeaders().isEmpty()), "Headers empty", "true");
		System.out.println();

		Vector<DataContainer> dcs = new Vector<>();
		dcs.add(new DataContainer(new String[5]));
		for (DataContainer dc : dcs) {
			BaseRegression.testResult(dc.getClass().getSimpleName(), "Name of container class", "DataContainer");
			BaseRegression.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
			BaseRegression.testResult(dc.getFileName(), "File Name", "");
			BaseRegression.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
			BaseRegression.testResult(String.valueOf(dc.getHeaderRowIndex()), "Header Row Index", "-1");
			BaseRegression.testResult(String.valueOf(dc.getHeaders().isEmpty()), "Headers empty", "false");
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
			BaseRegression.testResult(dc.getClass().getSimpleName(), "Name of container class", "DataContainer");
			BaseRegression.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
			BaseRegression.testResult(dc.getFileName(), "File Name", "testdata/testfile.txt");
			BaseRegression.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
			BaseRegression.testResult(String.valueOf(dc.getHeaders().isEmpty()), "Headers empty", "true");
		}

		System.out.println();
		dcs.clear();
		dcs.add(new DataContainer(testfile, "+", new String[] { "header1", "header2", "header3" }));
		dcs.add(new DataContainer(testfile, "+"));
		dcs.add(new DataContainer(testfile, "+", -1));
		dcs.add(new DataContainer(testfile, "+", -1, new String[] { "header1", "header2" }, null));
		for (DataContainer dc : dcs) {
			BaseRegression.testResult(dc.getClass().getSimpleName(), "Name of container class", "DataContainer");
			BaseRegression.testResult(dc.getColumnDelimiter(), "Column Delimiter", "+");
			BaseRegression.testResult(dc.getFileName(), "File Name", "testdata/testfile.txt");
			BaseRegression.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
		}

		System.out.println();
		dcs.clear();
		ResultSet dummy = null;
		dcs.add(new DataContainer(dummy));
		dcs.add(new DataContainer(dummy, new Filter()));
		dcs.add(new DataContainer(dummy, new Filter(), ";"));

		for (DataContainer dc : dcs) {
			BaseRegression.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
			BaseRegression.testResult(dc.getFileName(), "File Name", null);
			BaseRegression.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Type", "COLUMN");
			BaseRegression.testResult(String.valueOf(dc.getHeaderRowIndex()), "Header Row Index", "0");
			BaseRegression.testResult(String.valueOf(dc.getHeaders().isEmpty()), "Headers empty", "true");
		}

		try {
			FileUtil.deleteFile(testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
