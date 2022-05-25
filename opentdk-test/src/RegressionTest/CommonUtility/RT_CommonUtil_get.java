package RegressionTest.CommonUtility;

import org.opentdk.api.util.CommonUtil;

import RegressionTest.BaseRegression;

public class RT_CommonUtil_get extends BaseRegression {

	public static void main(String[] args) {
		new RT_CommonUtil_get();
	}

	@Override
	protected void runTest() {
		boolean success = false;

		boolean osDetected = false;
		if (CommonUtil.isWindows()) {
			osDetected = true;
		} else if (CommonUtil.isMac()) {
			osDetected = true;
		} else if (CommonUtil.isUnix()) {
			osDetected = true;
		}
		BaseRegression.testResult(String.valueOf(osDetected), "Operating System detected", "true");
		System.out.println("Operating system: " + CommonUtil.getOSName());

		String computerName = CommonUtil.getComputername();
		String ip = CommonUtil.getIPAdress();
		String mac = CommonUtil.getMacAddress();

		if (computerName != null && !computerName.isBlank() && ip != null && !ip.isBlank() && mac != null && !mac.isBlank()) {
			System.out.println("Computer Name: " + computerName);
			System.out.println("Computer IP: " + ip);
			System.out.println("Computer MAC adress: " + mac);
			success = true;
		}
		BaseRegression.testResult(String.valueOf(success), "Computer name, IP and mac adress detected", "true");
	}

}
