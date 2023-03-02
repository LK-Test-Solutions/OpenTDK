package RegressionTest.CryptoUtility;

import java.io.File;

import org.opentdk.api.util.CryptoUtil;

import RegressionTest.BaseRegression;

public class RT_CryptoUtil_encrypt extends BaseRegression {
	
	private static final String privateKey = location + "output/private.key";
	private static final String publicKey = location + "output/public.key";

	public static void main(String[] args) {
		new RT_CryptoUtil_encrypt();
	}
	
	@Override
	public void runTest() {
		String encrypt = "test";
		CryptoUtil.generateKeyPair(2048, privateKey, publicKey);
		String secret = CryptoUtil.encrypt(encrypt, new File(publicKey));
		String decrypt = CryptoUtil.decrypt(secret, new File(privateKey));
		BaseRegression.testResult(decrypt, "Decrypted string", "test");
	}

}
