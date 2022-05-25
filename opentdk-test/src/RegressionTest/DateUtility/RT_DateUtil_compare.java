package RegressionTest.DateUtility;

import org.opentdk.api.util.DateUtil;

import RegressionTest.BaseRegression;

public class RT_DateUtil_compare extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_compare();
	}
	
	@Override
	public void runTest() {
		
		testResult(DateUtil.compare("20.03.2019", "2019-03-20"), "COMPARE_DATE", 0);		
		testResult(DateUtil.compare("20.03.2019", "2019-03-19"), "COMPARE_DATE", 1);
		testResult(DateUtil.compare("20.03.2019", "2019-04-20"), "COMPARE_DATE", -1);
		testResult(DateUtil.compare("20.03.2018", "2019-03-20"), "COMPARE_DATE", -1);
		testResult(DateUtil.compare("2019-03-19", "2019-03-20"), "COMPARE_DATE", -1);
		testResult(DateUtil.compare("2019-03-20", "2019-03-20"), "COMPARE_DATE", 0);
		testResult(DateUtil.compare("Irgendetwas", "2019-03-20"), "COMPARE_DATE", -999);
		
	}

}
