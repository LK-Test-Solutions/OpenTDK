package RegressionTest.CSVContainer;

import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

import RegressionTest.BaseRegression;


public class RT_CSVContainer_getValues extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_getValues();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");
		test1(dc);
		test2(dc);
		test3(dc);
		test5(dc);
		test5(dc);
		test6(dc);
		test7(dc);
		test8(dc);
		test9(dc);
	}
	
	public void test1(DataContainer dc) {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("089/45709053");
		testResult(dc.getValue("Telefon"), "getValue() - Telefon in row 1", expResults1.get(0));
	}

	public void test2(DataContainer dc) {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Musterhausen");
		testResult(dc.getValue("Ort", 1), "getValue(header,rowindex) - Second occurance in column 'Ort'", expResults1.get(0));
	}

	public void test3(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("M�nchen");
		testResult(dc.getValue(3, 2), "getValue(headerindex,rowindex) - Column 4, Row 3", expResults1.get(0));
	}

	public void test4(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("0123/4567890");
		Filter fltr = new Filter();
		fltr.addFilterRule("Firma", "Muster AG", EOperator.EQUALS);
		testResult(dc.getValue("Telefon", fltr), "getValue(header,filter) - Second occurrence in column 'Telefon'", expResults1.get(0));

		List<String> expResults2 = new ArrayList<>();
		expResults2.add("86450");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Nachname", "Meisinger", EOperator.EQUALS_IGNORE_CASE);
		testResult(dc.getValue("PLZ", fltr2), "getValue(header,filter) - Third occurance in column 'PLZ'", expResults2.get(0));
	}

	public void test5(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");

		List<String> outList = dc.getValuesAsList("Nachname");
		String[] outRow = outList.toArray(new String[outList.size()]);
		testResult(String.join(";", outRow), "getValuesAsList(header) - All values of column 'Nachname'", expResults1.get(0));
	}

	public void test6(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		Filter fltr = new Filter();
		fltr.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);

		List<String> outList = dc.getValuesAsList("Email", fltr);
		String[] outRow = outList.toArray(new String[outList.size()]);
		testResult(String.join(";", outRow), "getValuesAsList(header,filter) - All values in column 'Email' where 'Firma' matches 'LK Test'", expResults1.get(0));
	}

	public void test7(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("hwa@lk-test.com;lwi@lk-test.de");
		Filter fltr = new Filter();
		fltr.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);

		List<String> outList = dc.getValuesAsList("Email", new int[] { 0, 3 }, fltr);
		String[] outRow = outList.toArray(new String[outList.size()]);
		testResult(String.join(";", outRow), "getValuesAsList(header,rowIndexes,filter) - All values in column 'Email' where 'Firma' matches 'LK Test'", expResults1.get(0));
	}

	public void test8(DataContainer dc) {	
		String allValues = dc.getValuesAsString();
		StringBuilder sb = new StringBuilder();
		sb.append("LK Test Solutions GmbH;Walter;81675;M�nchen;Schneckenburgerstra�e;32;089/45709053;hwa@lk-test.com").append("\n");
		sb.append("Muster AG;Mustermann;12345;Musterhausen;Musterstra�e;1;0123/4567890;max@muster.mu").append("\n");
		sb.append("LK Test Solutions GmbH;Meisinger;86450;M�nchen;Schneckenburgerstra�e;32;null;fme@lk-test.de").append("\n");
		sb.append("LK Test Solutions GmbH;Winkler;81675;M�nchen;Schneckenburgerstra�e;32;null;lwi@lk-test.de").append("\n");
		
		boolean valuesEqual = allValues.equals(sb.toString());
		testResult(String.valueOf(valuesEqual), "getValuesAsString() - values equal", "true");
		
		List<String> uniqueValues = dc.getValuesAsDistinctedList("Stra�e");
		testResult(String.valueOf(uniqueValues.size()), "getValuesAsDistinctedList(header) - size", "2");
	}
	
	public void test9(DataContainer dc) {			
		List<Integer> intValues = dc.getValuesAsIntList("PLZ");
		testResult(String.valueOf(intValues.get(2)), "getValuesAsIntList(header) - values", "86450");
		
		List<Double> doubleValues = dc.getValuesAsDoubleList("PLZ");
		testResult(String.valueOf(doubleValues.get(2)), "getValuesAsDoubleList(header) - values", "86450.0");
		
		int biggestValue = dc.getMaxLen("Firma");
		testResult(String.valueOf(biggestValue), "getMaxLen(header) - values", "22");
	}

}
