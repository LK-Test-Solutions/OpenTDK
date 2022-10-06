package Tests.Utility;

import java.io.File;

import org.opentdk.api.util.CommonUtil;

import RegressionTest.BaseRegression;

public class TST_CommonUtil extends BaseRegression {

	public static void main(String[] args) {
		new TST_CommonUtil();
	}
	
	@Override
	protected void runTest() {
		String command = "cmd /c cd testdata\\RegressionTestData && \"C:\\Program Files\\7-Zip\\7z.exe\" a \"..\\RegressionTestData.7z\" *.csv -t7z -m0=BCJ2 -m1=LZMA:d25:fb255 -m2=LZMA:d19 -m3=LZMA:d19 -mb0:1 -mb0s1:2 -mb0s2:3 -mx";
		int success = CommonUtil.exeucteCommand(command);
		BaseRegression.testResult(success, "State", 0);
		BaseRegression.testResult(String.valueOf(new File("testdata/RegressionTestData.7z").exists()), "Compressed exists", "true");
		
		command = "cmd /c cd testdata && \"C:\\Program Files\\7-Zip\\7z.exe\" e \"RegressionTestData.7z\" -o\"RegressionTestData_Extracted\" -t7z -y -x!*.cmd -x!config.txt -x!*.html";
		success = CommonUtil.exeucteCommand(command);
		BaseRegression.testResult(success, "State", 0);
		BaseRegression.testResult(String.valueOf(new File("testdata/RegressionTestData_Extracted").exists()), "Extracted exists", "true");
	}

}