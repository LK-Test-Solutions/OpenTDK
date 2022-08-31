package RegressionTest.DateUtility;

import java.time.temporal.ChronoField;

import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

import RegressionTest.BaseRegression;

public class RT_DateUtil_getFirstOf extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_getFirstOf();
	}
	
	@Override
	public void runTest() {		
		
		BaseRegression.testResult(DateUtil.getFirstOf("04.04.2021", ChronoField.DAY_OF_WEEK, EFormat.DATE_2.getDateFormat()), "First of week", "2021-03-29");
		BaseRegression.testResult(DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, EFormat.DATE_5.getDateFormat()), "First of month", "2021.04.01");
		BaseRegression.testResult(DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, EFormat.DATE_4.getDateFormat()), "First of year", "01.01.2021");
		BaseRegression.testResult(DateUtil.getFirstOf(ChronoField.NANO_OF_DAY, EFormat.TIME_1.getDateFormat()), "First time of day", "00.00.00");
		BaseRegression.testResult(DateUtil.getFirstOf("2021-04-04", ChronoField.NANO_OF_DAY, EFormat.TIMESTAMP_1.getDateFormat()), "First time of day", "2021-04-04-00.00.00.000000");

	}

}
