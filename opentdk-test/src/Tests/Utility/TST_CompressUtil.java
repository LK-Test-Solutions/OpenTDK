package Tests.Utility;

import java.io.File;

import org.opentdk.api.util.CompressUtil;
import org.opentdk.api.util.CompressUtil.CompressCommand;

import RegressionTest.BaseRegression;

public class TST_CompressUtil extends BaseRegression {

	public static void main(String[] args) {
		new TST_CompressUtil();
	}
	
	@Override
	protected void runTest() {
		int success = CompressUtil.compress("testdata\\RegressionTestData", "C:\\Program Files\\7-Zip\\7z.exe", "RegressionTestData", CompressCommand.Add, true);
		BaseRegression.testResult(success, "State", 0);
		BaseRegression.testResult(String.valueOf(new File("testdata/RegressionTestData.7z").exists()), "Exists", "true");
	}

}
