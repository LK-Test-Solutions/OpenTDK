package RegressionTest.Container;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;

import org.opentdk.api.datastorage.BaseContainer.EHeader;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

/**
 * Tests if the fields of the {@link org.opentdk.api.datastorage.BaseContainer} class have the correct value
 * after initialization of the {@link org.opentdk.api.datastorage.DataContainer}.
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class RT_Container_construct extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_construct();
	}

	@Override
	protected void runTest() {
		testEmptyContainer(new DataContainer(), "", null, null);
		testEmptyContainer(new DataContainer(EHeader.UNKNOWN), "", null, null);
		testEmptyContainer(new DataContainer(new DataContainer(), new int[0]), "", null, null);
		
		String testfile = "testdata/testfile.txt";
		try {
			FileUtil.createFile(testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		testEmptyContainer(new DataContainer(testfile), testfile, null, null);	

		try {
			FileUtil.deleteFile(testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String rootTag = "";
		InputStream stream = new ByteArrayInputStream(rootTag.getBytes(StandardCharsets.UTF_8));
		testEmptyContainer(new DataContainer(stream), "", stream, null);
		
		ResultSet rs = null;
		testEmptyContainer(new DataContainer(rs), "", null, rs);
	}

	private void testEmptyContainer(DataContainer dc, String fileName, InputStream stream, ResultSet rs) {
		System.out.println();
		BaseRegression.testResult(dc.getFileName(), "File Name", fileName);
		BaseRegression.testResult(dc.getColumnDelimiter(), "Column Delimiter", ";");
		BaseRegression.testResult(dc.getHeaderRowIndex(), "Header Index", 0);
		BaseRegression.testResult(dc.getContainerFormat().name(), "Container Format", "CSV");
		BaseRegression.testResult(dc.getContainerFormat().getHeaderType().name(), "Container Header Orientation", "COLUMN");
		BaseRegression.testResult(dc.getRootNode(), "Root Node", "");
		BaseRegression.testResult(dc.getInputStream(), "Input Stream", stream);
		BaseRegression.testResult(dc.getResultSet(), "Result Set", rs);
		BaseRegression.testResult(dc.getHeaders().isEmpty(), "Headers Empty", true);
		BaseRegression.testResult(dc.getImplicitHeaders().isEmpty(), "Implicit Headers Empty", true);
		BaseRegression.testResult(dc.getValues().isEmpty(), "Values Empty", true);
		BaseRegression.testResult(dc.getMetaData().isEmpty(), "Values Empty", true);
	}
}
