package RegressionTest.Container;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.Filter;

import RegressionTest.BaseRegression;

public class RT_Container_unsupportedMethods extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_unsupportedMethods();
	}

	@Override
	protected void runTest() {
		// CSVDataContainer
		DataContainer csvDc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");

		expectWarning();
		csvDc.addField("Firma", "Test", new Filter());
		System.out.println();

		expectWarning();
		csvDc.addField("Firma", null, null, new Filter());
		System.out.println();

		expectWarning();
		csvDc.addField("Firma", null, null, null, new Filter());
		System.out.println();

		expectWarning();
		csvDc.getAttributes("Firma", "name");
		System.out.println();

		BaseRegression.testResult(csvDc.getRootNode(), "Root node with CSV container", "");
		BaseRegression.testResult(String.valueOf(csvDc.getResultSet()), "ResultSet with CSV container", null);
		System.out.println();

		// JSONDataContainer
		
		// PropertiesDataContainer
		
		// XMLDataContainer


	}

	private void expectWarning() {
		System.out.println("Expect warning message:");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
