package RegressionTest.DateUtility;

import org.opentdk.api.util.DateUtil;

import RegressionTest.BaseRegression;

public class RT_DateUtil_compare extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_compare();
	}
	
	@Override
	public void runTest() {
		
		BaseRegression.testResult(DateUtil.compare("20.03.2019", "2019-03-20"), "COMPARE_DATE", 0);		
		BaseRegression.testResult(DateUtil.compare("20.03.2019", "2019-03-19"), "COMPARE_DATE", 1);
		BaseRegression.testResult(DateUtil.compare("20.03.2019", "2019-04-20"), "COMPARE_DATE", -1);
		BaseRegression.testResult(DateUtil.compare("20.03.2018", "2019-03-20"), "COMPARE_DATE", -1);
		BaseRegression.testResult(DateUtil.compare("2019-03-19", "2019-03-20"), "COMPARE_DATE", -1);
		BaseRegression.testResult(DateUtil.compare("2019-03-20", "2019-03-20"), "COMPARE_DATE", 0);
		BaseRegression.testResult(DateUtil.compare("Irgendetwas", "2019-03-20"), "COMPARE_DATE", -999);
		
		BaseRegression.testResult(DateUtil.compare("20210329", "2021-03-29"), "COMPARE_DATE", 0);
		BaseRegression.testResult(DateUtil.compare("23:45:31", "23:45:30"), "COMPARE_TIME", 1);
		BaseRegression.testResult(DateUtil.compare("2021.03.29 12:00:56", "2021.03.29 12:00:56.001002003"), "COMPARE_TIMESTAMP", -1);
		BaseRegression.testResult(DateUtil.compare("20210329 15:14:22", "20210330"), "COMPARE_TIMESTAMP_DATE", -1);
		BaseRegression.testResult(DateUtil.compare("2021-03-30", "15:14:22.123"), "COMPARE_DATE_TIME", -1);
		BaseRegression.testResult(DateUtil.compare("15:14:22.123456", "2021-03-10.15:14:22"), "COMPARE_TIME_TIMESTAMP", 1);
		BaseRegression.testResult(DateUtil.compare("2021-03-10.15:14:22", "2021-03-10.15:14:22.175"), "COMPARE_TIMESTAMP_TIMESTAMP", -1);
	
		
	}

}
