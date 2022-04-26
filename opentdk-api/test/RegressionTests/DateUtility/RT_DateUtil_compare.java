package RegressionTests.DateUtility;

import org.junit.Assert;
import org.junit.Test;
import org.opentdk.api.util.DateUtil;

public class RT_DateUtil_compare {

	@Test
	public void test() {
		
		testOutput(DateUtil.compare("20.03.2019", "2019-03-20"), "COMPARE_DATE", 0);		
		testOutput(DateUtil.compare("20.03.2019", "2019-03-19"), "COMPARE_DATE", 1);
		testOutput(DateUtil.compare("20.03.2019", "2019-04-20"), "COMPARE_DATE", -1);
		testOutput(DateUtil.compare("20.03.2018", "2019-03-20"), "COMPARE_DATE", -1);
		testOutput(DateUtil.compare("2019-03-19", "2019-03-20"), "COMPARE_DATE", -1);
		testOutput(DateUtil.compare("2019-03-20", "2019-03-20"), "COMPARE_DATE", 0);
		testOutput(DateUtil.compare("Irgendetwas", "2019-03-20"), "COMPARE_DATE", -999);
		
	}
	
	private void testOutput(int actual, String fieldName, int expected) {
		if (actual == expected) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			Assert.fail();
		}
	}

}
