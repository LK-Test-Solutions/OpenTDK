package RegressionTests.CSVContainer;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CSVContainer_setValues {

	private final DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");

	@Test
	public void test1() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");

		StringBuilder sb = new StringBuilder();
		sb.append("LK Test Solutions AG;Walter;81675;Freiburg;Schneckenburgerstraﬂe;32;089/45709053;hwa@lk-test.com").append("\n");
		sb.append("Muster AG;Mustermann;12345;Musterhausen;Teststrasse;1;0123/4567890;fme@lk-test.de").append("\n");
		sb.append("Neotys;Meisinger;86450;M¸nchen;Schneckenburgerstraﬂe;32;;fme@lk-test.de").append("\n");
		sb.append("LK Test Solutions AG;Winkler;81675;M¸nchen;Schneckenburgerstraﬂe;32;;lwi@lk-test.de").append("\n");

		dc.setValue("Ort", "Amberg");
		BaseRegressionSet.testResult(dc.getValue("Ort"), "setValue(header,value) - value", "Amberg");

		dc.setValue("Firma", 2, "Neotys");
		BaseRegressionSet.testResult(dc.getValue("Firma", 2), "setValue(header,index,value) - value", "Neotys");

		Filter fltr = new Filter();
//		fltr.addFilterRule("PLZ", "12345", EOperator.EQUALS);
		dc.setValue("Ort", "Freiburg", fltr);
		BaseRegressionSet.testResult(dc.getValue("Ort"), "setValue(header,value,filter) - value", "Freiburg");

		dc.setValue("Straﬂe", 1, "Teststrasse", new Filter());
		BaseRegressionSet.testResult(dc.getValue("Straﬂe", 1), "setValue(header,index,value,filter) - value", "Teststrasse");

		fltr.clear();
		fltr.addFilterRule("Firma", "GmbH", EOperator.CONTAINS);
		dc.setValues("Firma", "LK Test Solutions AG", fltr);
		BaseRegressionSet.testResult(dc.getValuesAsDistinctedList("Firma").get(2), "setValues(header,value,filter) - values equal", "LK Test Solutions AG");

		dc.setValues("Email", new int[] { 1 }, "fme@lk-test.de", new Filter());
		BaseRegressionSet.testResult(dc.getValue("Email", 1), "setValues(header,indices,value,filter) - value", "fme@lk-test.de");
		
		boolean valuesEqual = dc.getValuesAsString().equals(sb.toString());
		BaseRegressionSet.testResult(String.valueOf(valuesEqual), "getValuesAsString() - values equal", "true");

		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");
		System.out.println();
	}
}
