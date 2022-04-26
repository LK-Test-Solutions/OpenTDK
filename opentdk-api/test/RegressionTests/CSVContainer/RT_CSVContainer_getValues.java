package RegressionTests.CSVContainer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CSVContainer_getValues {

	private final DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");

	@Test
	public void test1() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("089/45709053");
		BaseRegressionSet.testResult(dc.getValue("Telefon"), "getValue() - Telefon in row 1", expResults1.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");
		System.out.println();
	}

	@Test
	public void test2() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test2 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Musterhausen");
		BaseRegressionSet.testResult(dc.getValue("Ort", 1), "getValue(header,rowindex) - Second occurance in column 'Ort'", expResults1.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test2 ###");
		System.out.println();
	}

	@Test
	public void test3() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test3 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("München");
		BaseRegressionSet.testResult(dc.getValue(3, 2), "getValue(headerindex,rowindex) - Column 4, Row 3", expResults1.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test3 ###");
		System.out.println();
	}

	@Test
	public void test4() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test4 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("0123/4567890");
		Filter fltr = new Filter();
		fltr.addFilterRule("Firma", "Muster AG", EOperator.EQUALS);
		BaseRegressionSet.testResult(dc.getValue("Telefon", fltr), "getValue(header,filter) - Second occurrence in column 'Telefon'", expResults1.get(0));

		List<String> expResults2 = new ArrayList<String>();
		expResults2.add("86450");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Nachname", "Meisinger", EOperator.EQUALS_IGNORE_CASE);
		BaseRegressionSet.testResult(dc.getValue("PLZ", fltr2), "getValue(header,filter) - Third occurance in column 'PLZ'", expResults2.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test4 ###");
		System.out.println();
	}

	@Test
	public void test5() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test5 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");

		List<String> outList = dc.getValuesAsList("Nachname");
		String[] outRow = outList.toArray(new String[outList.size()]);
		BaseRegressionSet.testResult(String.join(";", outRow), "getValuesAsList(header) - All values of column 'Nachname'", expResults1.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test5 ###");
		System.out.println();
	}

	@Test
	public void test6() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test6 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		Filter fltr = new Filter();
		fltr.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);

		List<String> outList = dc.getValuesAsList("Email", fltr);
		String[] outRow = outList.toArray(new String[outList.size()]);
		BaseRegressionSet.testResult(String.join(";", outRow), "getValuesAsList(header,filter) - All values in column 'Email' where 'Firma' matches 'LK Test'", expResults1.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test6 ###");
		System.out.println();
	}

	@Test
	public void test7() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test7 ###");

		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("hwa@lk-test.com;lwi@lk-test.de");
		Filter fltr = new Filter();
		fltr.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);

		List<String> outList = dc.getValuesAsList("Email", new int[] { 0, 3 }, fltr);
		String[] outRow = outList.toArray(new String[outList.size()]);
		BaseRegressionSet.testResult(String.join(";", outRow), "getValuesAsList(header,rowIndexes,filter) - All values in column 'Email' where 'Firma' matches 'LK Test'", expResults1.get(0));

		System.out.println("### " + this.getClass().getSimpleName() + " test7 ###");
		System.out.println();
	}

	@Test
	public void test8() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test8 ###");
		
		String allValues = dc.getValuesAsString();
		StringBuilder sb = new StringBuilder();
		sb.append("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com").append("\n");
		sb.append("Muster AG;Mustermann;12345;Musterhausen;Musterstraße;1;0123/4567890;max@muster.mu").append("\n");
		sb.append("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;;fme@lk-test.de").append("\n");
		sb.append("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;;lwi@lk-test.de").append("\n");
		
		boolean valuesEqual = allValues.equals(sb.toString());
		BaseRegressionSet.testResult(String.valueOf(valuesEqual), "getValuesAsString() - values equal", "true");
		
		List<String> uniqueValues = dc.getValuesAsDistinctedList("Straße");
		BaseRegressionSet.testResult(String.valueOf(uniqueValues.size()), "getValuesAsDistinctedList(header) - size", "2");

		System.out.println("### " + this.getClass().getSimpleName() + " test8 ###");
		System.out.println();
	}
	
	@Test
	public void test9() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test9 ###");
				
		List<Integer> intValues = dc.getValuesAsIntList("PLZ");
		BaseRegressionSet.testResult(String.valueOf(intValues.get(2)), "getValuesAsIntList(header) - values", "86450");
		
		List<Double> doubleValues = dc.getValuesAsDoubleList("PLZ");
		BaseRegressionSet.testResult(String.valueOf(doubleValues.get(2)), "getValuesAsDoubleList(header) - values", "86450.0");
		
		int biggestValue = dc.getMaxLen("Firma");
		BaseRegressionSet.testResult(String.valueOf(biggestValue), "getMaxLen(header) - values", "22");

		System.out.println("### " + this.getClass().getSimpleName() + " test9 ###");
		System.out.println();
	}

}
