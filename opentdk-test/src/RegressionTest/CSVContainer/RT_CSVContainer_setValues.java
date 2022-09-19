package RegressionTest.CSVContainer;

import java.io.IOException;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_setValues extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_setValues();
	}
	
	@Override
	public void runTest() {
		try {
			FileUtil.copyFile("./testdata/RegressionTestData/CSVContainer_Contacts.csv", "./testdata/RegressionTestData/CSVContainer_Contacts_temp.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts_temp.csv");

		StringBuilder sb = new StringBuilder();
		sb.append("LK Test Solutions AG;Walter;81675;Freiburg;Schneckenburgerstrasse;32;089/45709053;hwa@lk-test.com").append("\n");
		sb.append("Muster AG;Mustermann;12345;Musterhausen;Teststrasse;1;0123/4567890;fme@lk-test.de").append("\n");
		sb.append("Neotys;Meisinger;86450;Muenchen;Schneckenburgerstrasse;32;null;fme@lk-test.de").append("\n");
		sb.append("LK Test Solutions AG;Winkler;81675;Muenchen;Schneckenburgerstrasse;32;null;lwi@lk-test.de").append("\n");

		dc.setValue("Ort", "Amberg");
		testResult(dc.getValue("Ort"), "setValue(header,value) - value", "Amberg");

		dc.setValue("Firma", 2, "Neotys");
		testResult(dc.getValue("Firma", 2), "setValue(header,index,value) - value", "Neotys");

		Filter fltr = new Filter();
		dc.setValue("Ort", "Freiburg", fltr);
		testResult(dc.getValue("Ort"), "setValue(header,value,filter) - value", "Freiburg");

		dc.setValue("Strasse", 1, "Teststrasse", new Filter());
		testResult(dc.getValue("Strasse", 1), "setValue(header,index,value,filter) - value", "Teststrasse");

		fltr.clear();
		fltr.addFilterRule("Firma", "GmbH", EOperator.CONTAINS);
		dc.setValues("Firma", "LK Test Solutions AG", fltr);
		testResult(dc.getValuesAsDistinctedList("Firma").get(2), "setValues(header,value,filter) - values equal", "LK Test Solutions AG");

		dc.setValues("Email", new int[] { 1 }, "fme@lk-test.de", new Filter());
		testResult(dc.getValue("Email", 1), "setValues(header,indices,value,filter) - value", "fme@lk-test.de");
		
		boolean valuesEqual = dc.asString().equals(sb.toString());
		testResult(String.valueOf(valuesEqual), "getValuesAsString() - values equal", "true");

	}
}
