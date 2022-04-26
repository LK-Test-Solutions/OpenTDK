package RegressionTests.CommonUtility;

import org.junit.Assert;
import org.junit.Test;
import org.opentdk.api.util.CommonUtil;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CommonUtil_get {

	@Test
	public void test() {
		boolean osDetected = false;
		if (CommonUtil.isWindows()) {
			System.out.println(CommonUtil.getOSName());
			osDetected = true;
		} else if (CommonUtil.isMac()) {
			System.out.println(CommonUtil.getOSName());
			osDetected = true;
		} else if (CommonUtil.isUnix()) {
			System.out.println(CommonUtil.getOSName());
			osDetected = true;
		} else {
			Assert.fail("No OS detected.");
		}
		BaseRegressionSet.testResult(String.valueOf(osDetected), "Operating System detected", "true");

		boolean success = false;
		String computerName = CommonUtil.getComputername();
		String ip = CommonUtil.getIPAdress();
		String mac = CommonUtil.getMacAddress();

		if (computerName != null && !computerName.isBlank() && ip != null && !ip.isBlank() && mac != null && !mac.isBlank()) {
			System.out.println("Computer Name: " + computerName);
			System.out.println("Computer IP: " + ip);
			System.out.println("Computer MAC adress: " + mac);
			success = true;
		}
		BaseRegressionSet.testResult(String.valueOf(success), "Computer name, IP and mac adress detected", "true");
	}

}
