package RegressionTest.Container;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

/**
 * Tests if the fields of the {@link org.opentdk.api.datastorage.DataContainer} class have the correct value
 * after initialization.
 * 
 * @author LK Test Solutions
 *
 */
public class RT_Container_construct extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_construct();
	}

	@Override
	protected void runTest() {
		// EMPTY
		testEmptyContainer(new DataContainer(), "", null, null);
		
		// STRING
		testEmptyContainer(new DataContainer("Test"), "", null, null);
//		testEmptyContainer(new DataContainer(new DataContainer(), new int[0]), "", null, null);
		
		// FILE
		File testfile = new File("testdata/testfile.txt");
		try {
			FileUtil.createFile(testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		testEmptyContainer(new DataContainer(testfile), testfile.getPath(), null, null);	

		try {
			FileUtil.deleteFile(testfile.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// STREAM
		String rootTag = "";
		InputStream stream = new ByteArrayInputStream(rootTag.getBytes(StandardCharsets.UTF_8));
		testEmptyContainer(new DataContainer(stream), "", stream, null);
		
		// RS
		ResultSet rs = null;
		testEmptyContainer(new DataContainer(rs), "", null, rs);
	}

	private void testEmptyContainer(DataContainer dc, String filePath, InputStream stream, ResultSet rs) {
		System.out.println();
		BaseRegression.testResult(dc.getInputFile().getPath(), "File Path", filePath);
		BaseRegression.testResult(dc.getTabContainer().getColumnDelimiter(), "Column Delimiter", ";");
		BaseRegression.testResult(dc.getTabContainer().getHeaderRowIndex(), "Header Index", 0);
		BaseRegression.testResult(dc.getContainerFormat().name(), "Container Format", "CSV");
		BaseRegression.testResult(dc.getContainerFormat().getOrientation().name(), "Container Header Orientation", "COLUMN");
		BaseRegression.testResult(dc.getRootNode(), "Root Node", "");
		BaseRegression.testResult(dc.getInputStream(), "Input Stream", stream);
		BaseRegression.testResult(dc.getResultSet(), "Result Set", rs);
		BaseRegression.testResult(dc.getTabContainer().getHeaders().isEmpty(), "Headers Empty", true);
		BaseRegression.testResult(dc.getImplicitHeaders().isEmpty(), "Implicit Headers Empty", true);
		BaseRegression.testResult(dc.getTabContainer().getValues().isEmpty(), "Values Empty", true);
		BaseRegression.testResult(dc.getTabContainer().getMetaData().isEmpty(), "Values Empty", true);
	}
}
