package RegressionTest.CryptoUtility;

import java.io.File;

import org.opentdk.api.util.CryptoUtil;

import RegressionTest.BaseRegression;

public class RT_CryptoUtil_encrypt extends BaseRegression {
	
	private static final String privateKey = "output/private.key";
	private static final String publicKey = "output/public.key";

	public static void main(String[] args) {
		new RT_CryptoUtil_encrypt();
	}
	
	@Override
	public void runTest() {
		CryptoUtil.generateKeyPair(2048, privateKey, publicKey);
		CryptoUtil.encrypt("test", new File(publicKey));
		
	}

}
