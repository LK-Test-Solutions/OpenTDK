package RegressionTests.Container;

import java.util.HashMap;


import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;

import RegressionSets.Api.BaseRegressionSet;

public class RT_Container_checkHeader {
	
	@Test
	public void test1() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");

		DataContainer dc = new DataContainer(new String[] {"header1", "header2", "header3"});
		
		HashMap<String,Integer> compareHeaders = new HashMap<>();
		compareHeaders.put("header1", 0);
		compareHeaders.put("header2", 1);
		compareHeaders.put("header3", 2);
		BaseRegressionSet.testResult(String.valueOf(dc.checkHeader(compareHeaders)), "Header Compare", "0");
		
		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");
	}
	
	@Test
	public void test2() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test2 ###");

		DataContainer dc = new DataContainer(new String[] {"header1", "header3", "header2"});
		
		String[] compareHeaders = new String[] {"header1", "header2", "header3"};
		BaseRegressionSet.testResult(String.valueOf(dc.checkHeader(compareHeaders)), "Header Compare", "1");
		
		System.out.println("### " + this.getClass().getSimpleName() + " test2 ###");
	}
	
	@Test
	public void test3() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test3 ###");

		DataContainer dc = new DataContainer();
		
		String[] referenceHeaders = new String[] {"header1", "header2", "header4"};
		String[] compareHeaders = new String[] {"header1", "header2", "header4"};
		BaseRegressionSet.testResult(String.valueOf(dc.checkHeader(referenceHeaders, compareHeaders)), "Header Compare", "0");
		
		System.out.println("### " + this.getClass().getSimpleName() + " test3 ###");
	}
	
	@Test
	public void test4() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test4 ###");

		DataContainer dc = new DataContainer(new String[] {"header1", "header3", "header2"});
		
		String[] compareHeaders = new String[] {"header1", "header2", "header4"};
		BaseRegressionSet.testResult(String.valueOf(dc.checkHeader(dc.getHeaders(), compareHeaders)), "Header Compare", "-1");
		
		System.out.println("### " + this.getClass().getSimpleName() + " test4 ###");
	}
}
