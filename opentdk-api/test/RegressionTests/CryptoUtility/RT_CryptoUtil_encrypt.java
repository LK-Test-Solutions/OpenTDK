package RegressionTests.CryptoUtility;

import org.junit.Test;
import org.opentdk.api.util.CryptoUtil;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CryptoUtil_encrypt {

	@Test
	public void test() {
		String toEncrypt = "MyPassword";
		String encrypted = CryptoUtil.encrypt(toEncrypt);
//		String encrypted = CryptoUtil.encrypt(toEncrypt, new File("conf/howto.key"));
		System.out.println("Encrypted passwort: " + encrypted);				
		BaseRegressionSet.testResult(encrypted.length(), "Password length", 32);
		
		// TODO private key necessary ?
//		String decrypted = CryptoUtil.decrypt(encrypted, new File("conf/howto.key"));
//		System.out.println("Encrypted passwort: " + decrypted);
//		BaseRegressionSet.testResult(decrypted, "Decrypted password", toEncrypt);
	}

}
