package RegressionTest.Container;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

/**
 * Tests if the export of the content of a tabular {@link org.opentdk.api.datastorage.DataContainer}
 * equals the original one.
 * 
 * @author LK Test Solutions
 *
 */
public class RT_Container_exportContainer extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_exportContainer();
	}

	@Override
	public void runTest() {
		DataContainer dc = new DataContainer(new File(location + "testdata/RegressionTestData/CSVContainer_Contacts.csv"));
		String exportFile = location + "testdata/out.csv";

		try {
			dc.tabInstance().exportContainer(location + "output/out.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean equal = true;

		DataContainer exportedContent = new DataContainer(new File(exportFile));
		Set<String> exportedHeaders = exportedContent.tabInstance().getHeaders().keySet();
		Set<String> originalHeaders = dc.tabInstance().getHeaders().keySet();
		for (String header : exportedHeaders) {
			if (!originalHeaders.contains(header)) {
				equal = false;
			}
		}

		List<String[]> exportedValues = exportedContent.tabInstance().getValues();
		List<String[]> originalValues = dc.tabInstance().getValues();
		for (int i = 0; i < exportedValues.size(); i++) {
			String[] exportedRow = exportedValues.get(i);
			String[] originalRow = originalValues.get(i);
			for (int k = 0; k < exportedRow.length; k++) {
				String exportedValue = exportedRow[k];
				String originalValue = originalRow[k];
				if (exportedValue != null && originalValue != null) {
					if (!exportedValue.contentEquals(originalValue)) {
						equal = false;
					}
				}
			}
		}

		BaseRegression.testResult(String.valueOf(equal), "exportContainer", "true");
	}

}
