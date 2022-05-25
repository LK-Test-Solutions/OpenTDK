package RegressionTest.DateUtility;

import org.opentdk.api.util.DateUtil;

import RegressionTest.BaseRegression;

public class RT_DateUtil_getMillisecondsLength extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_getMillisecondsLength();
	}
	
	@Override
	public void runTest() {
		testResult(13, "Millis length", DateUtil.getLengthMilliseconds());
	}

}
