package RegressionTests.XMLContainer;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

import java.util.List;

public class RT_XMLContainer_getValues {

	@Test
	public void test() {
		int i=0;

		DataContainer dc = new DataContainer("./testdata/RegressionTestData/TST_XMLContainer_Data.xml");
		
		System.out.println();
		System.out.println("##############################");
		System.out.println("#  1.1 - Show DC Headers     #");
		System.out.println("##############################");
		System.out.println();
		
		for(i =0; i<dc.getHeaders().size();i++) {
			System.out.print(dc.getHeadersIndexed().get(i) + " | ");
		}		
		System.out.println();	

		System.out.println();
		System.out.println("################################################################");
		System.out.println("#  2.1 - getValue(headername);                                 #");
		System.out.println("#        retrieve first occurance of tag defined by headername #");
		System.out.println("################################################################");
		System.out.println();		
		System.out.println(dc.getValue("lbpattern"));
		System.out.println();

		System.out.println();
		System.out.println("###############################################################");
		System.out.println("#  2.2 - getValue(headername, index);                         #");
		System.out.println("#        retrieve n'th occurance of tag defined by headername #");
		System.out.println("###############################################################");
		System.out.println();		
		System.out.println(dc.getValue("lbpattern", 1));
		System.out.println();
		
		System.out.println();
		System.out.println("###############################################################");
		System.out.println("#  2.3 - getValue(index, index);                              #");
		System.out.println("#        retrieve n'th occurance of tag defined by tag index  #");
		System.out.println("###############################################################");
		System.out.println();		
		System.out.println(dc.getValue(6, 1));
		System.out.println();

		System.out.println();
		System.out.println("#######################################################");
		System.out.println("#  3.1 - getValuesAsList(headername);                 #");
		System.out.println("#        Get values of all tags defined by headername #");
		System.out.println("#######################################################");
		System.out.println();		
		try {
			List<String> items = dc.getValuesAsList("startswithpattern");
			i=0;
			for(String s: items) {
				System.out.println(i++ + ": " + s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println();
		System.out.println("###########################################################################################");
		System.out.println("#  3.2 - getValuesAsList(headername, filter);                                             #");
		System.out.println("#        Get values of all tags defined by headername that match to implicit header XPath #");
		System.out.println("###########################################################################################");
		System.out.println();		
		Filter f1 = new Filter();
		f1.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']/element[@name='PARAMETER']/attributelist", EOperator.EQUALS);
		try {
			List<String> items = dc.getValuesAsList("item", f1);
			for(String s: items) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();	

		System.out.println();
		System.out.println("############################################################################################");
		System.out.println("#  3.3 - getValuesAsList(headername, filter);                                              #");
		System.out.println("#        Get values of all tags defined by headername that match to standard header filter #");
		System.out.println("############################################################################################");
		System.out.println();		
		Filter f2 = new Filter();
		f2.addFilterRule("item", "STATEMENT", EOperator.ENDS_WITH);
		try {
			List<String> items = dc.getValuesAsList("item", f2);
			for(String s: items) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();

		System.out.println();
		System.out.println("################################################################################");
		System.out.println("#  3.4 - getValuesAsList(headername, filter);                                  #");
		System.out.println("#        Get values of all tags defined by headername that match to combined   #");
		System.out.println("#        standard with implicit header filter                                  #");
		System.out.println("################################################################################");
		System.out.println();		
		Filter f3 = new Filter();
		f3.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']/element[@name='PARAMETER']/attributelist", EOperator.EQUALS);
		f3.addFilterRule("item", "STATEMENT", EOperator.ENDS_WITH);
		try {
			List<String> items = dc.getValuesAsList("item", f3);
			for(String s: items) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println();
		System.out.println("###################################################################################");
		System.out.println("#  3.5 - getValuesAsList(headername, filter);                                     #");
		System.out.println("#        Get values of all tags defined by headername that match to Header filter #");
		System.out.println("###################################################################################");
		System.out.println();
		Filter f4 = new Filter();
		// EOperator.NOT_EQUALS is set to <> by default ==> no matches
		f4.addFilterRule("lbpattern", "", EOperator.NOT_EQUALS);
		try {
			List<String> items = dc.getValuesAsList("lbpattern", f4);
			i = 0;
			for(String s: items) {
				System.out.println(i++ + ": " + s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();	
		
		System.out.println();
		System.out.println("##################################");
		System.out.println("#  4.1 - getRowCount();          #");
		System.out.println("#        Get the number of rows  #");
		System.out.println("##################################");
		System.out.println();		
		System.out.println(dc.getRowCount());
		System.out.println();	

		System.out.println();
		System.out.println("####################################################");
		System.out.println("#  4.2 - getRow(Index);                            #");
		System.out.println("#        Get all values of a row defined by index  #");
		System.out.println("####################################################");
		System.out.println();		
		System.out.println(String.join("; ", dc.getRow(3)));
		System.out.println();	

	}
}
