package RegressionTests.CSVContainer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

public class RT_CSVContainer_getRows {

	DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");

	private void testResult(String actual, String fieldName, String expected) {
		if (actual.contentEquals(expected)) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			Assert.fail();
		}
	}

	@Test
	public void test1() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstraße;1;0123/4567890;max@muster.mu");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;;lwi@lk-test.de");
		System.out.println();
		System.out.println("######################################");
		System.out.println("#  1 -  Method getRow(index)         #");
		System.out.println("#       get all rows by row index    #");
		System.out.println("######################################");
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i)), "getRow(" + i + ")", expResults1.get(i));
		}
	}

	@Test
	public void test2() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;81675;München");
		expResults1.add("Muster AG;12345;Musterhausen");
		expResults1.add("LK Test Solutions GmbH;86450;München");
		expResults1.add("LK Test Solutions GmbH;81675;München");
		System.out.println();
		System.out.println("####################################################################");
		System.out.println("#  2 -  Method getRow(index, headers)                              #");
		System.out.println("#       get columns Firma, PLZ and Ort of all rows by row index    #");
		System.out.println("####################################################################");
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i, "Firma;PLZ;Ort")), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	@Test
	public void test3() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;81675;München");
		expResults1.add("Muster AG;12345;Musterhausen");
		expResults1.add("LK Test Solutions GmbH;86450;München");
		expResults1.add("LK Test Solutions GmbH;81675;München");
		System.out.println();
		System.out.println("####################################################################");
		System.out.println("#  3 -  Method getRow(index, headers)                              #");
		System.out.println("#       get columns Firma, PLZ and Ort of all rows by row index    #");
		System.out.println("####################################################################");
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i, new String[] { "Firma", "PLZ", "Ort" })), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	@Test
	public void test4() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;81675;München");
		expResults1.add("");
		expResults1.add("");
		expResults1.add("LK Test Solutions GmbH;81675;München");
		System.out.println();
		System.out.println("####################################################################");
		System.out.println("#  4 -  Method getRow(index, headers, Filter)                      #");
		System.out.println("#       get columns Firma, PLZ and Ort of all rows by row index    #");
		System.out.println("#       the filter criteria is PLZ = 81675                         #");
		System.out.println("####################################################################");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("PLZ", "81675", EOperator.CONTAINS);
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i, "Firma;PLZ;Ort", fltr1)), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	@Test
	public void test5() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstraße;1;0123/4567890;max@muster.mu");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;;lwi@lk-test.de");
		System.out.println();
		System.out.println("##################################");
		System.out.println("#  5 - Method getRowsList()      #");
		System.out.println("#      get all rows as List      #");
		System.out.println("##################################");
		List<String[]> resLst = dc.getRowsList();
		for (int i = 0; i < resLst.size(); i++) {
			testResult(String.join(";", resLst.get(i)), "getRowsList() " + i, expResults1.get(i));
		}
	}

	@Test
	public void test6() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;;lwi@lk-test.de");
		System.out.println();
		System.out.println("#########################################################################");
		System.out.println("#  6 - Method getRowsList(Filter)                                       #");
		System.out.println("#      get all rows where value in column Firma starts with LK Test     #");
		System.out.println("#########################################################################");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			List<String[]> resLst = dc.getRowsList(fltr1);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(Filter) " + i, expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void test7() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("München;Walter");
		expResults1.add("München;Meisinger");
		expResults1.add("München;Winkler");
		System.out.println();
		System.out.println("##################################################################################");
		System.out.println("#  7 - Method getRowsList(headers, filter)                                       #");
		System.out.println("#      get all Ort and Nachname where value in column Firma ends with GmbH       #");
		System.out.println("##################################################################################");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Firma", "GmbH", EOperator.ENDS_WITH);
		try {
			List<String[]> resLst = dc.getRowsList("Ort;Nachname", fltr2);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(headers, filter) " + i, expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void test8() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Meisinger");
		expResults1.add("LK Test Solutions GmbH;Winkler");
		System.out.println();
		System.out.println("##################################################################################");
		System.out.println("#  8 - Method getRowsList(rowIndexes[], columnHeaders[], filter)                 #");
		System.out.println("#      get Firma and Nachname of the rows defined by rowIndexes, in case that    #");
		System.out.println("#      the value in column Email ends with lk-test.de                            #");
		System.out.println("##################################################################################");
		Filter fltr3 = new Filter();
		fltr3.addFilterRule("Email", "lk-test.de", EOperator.ENDS_WITH);
		try {
			List<String[]> resLst = dc.getRowsList(new int[] { 0, 1, 2, 3 }, new String[] { "Firma", "Nachname" }, fltr3);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(rowIndexes[], columnHeaders[], filter)", expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@Test
	public void test9() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("0");
		expResults1.add("2");
		expResults1.add("3");
		System.out.println();
		System.out.println("##################################################################################");
		System.out.println("#  9 - Method getRowsIndexed(Filter)                                             #");
		System.out.println("#      get Firma and Nachname of the rows defined by rowIndexes, in case that    #");
		System.out.println("#      the value in column Strasse is not Musterstraße                           #");
		System.out.println("##################################################################################");
		Filter fltr3 = new Filter();
		fltr3.addFilterRule("Straße", "Musterstraße", EOperator.NOT_EQUALS);
		try {
			int[] resLst = dc.getRowsIndexes(fltr3);
			for (int i = 0; i < resLst.length; i++) {
				testResult(String.valueOf(resLst[i]), "getRowsIndexed(filter) - number", expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
