package RegressionTest.CryptoUtility;

import org.opentdk.api.util.CryptoUtil;

import RegressionTest.BaseRegression;

public class RT_CryptoUtil_encrypt extends BaseRegression {

	public static void main(String[] args) {
		new RT_CryptoUtil_encrypt();
	}
	
	@Override
	public void runTest() {
		String toEncrypt = "MyPassword";
		String encrypted = CryptoUtil.encrypt(toEncrypt);
//		String encrypted = CryptoUtil.encrypt(toEncrypt, new File("conf/howto.key"));
		System.out.println("Encrypted passwort: " + encrypted);				
		BaseRegression.testResult(encrypted.length(), "Password length", 32);
		
		// TODO private key necessary ?
//		String decrypted = CryptoUtil.decrypt(encrypted, new File("conf/howto.key"));
//		System.out.println("Encrypted passwort: " + decrypted);
//		BaseRegressionSet.testResult(decrypted, "Decrypted password", toEncrypt);
	}

}
