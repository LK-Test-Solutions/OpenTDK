package RegressionTest.Application;

import java.io.IOException;

import org.opentdk.api.application.BaseApplication;

public class Application extends BaseApplication {

	public static void main(String[] args) {
		new Application(args);
	}
	
	public Application(String[] args)  {
		parseArgs(ERuntimeProperties.class, args);
		try {
			initRuntimeProperties(ERuntimeProperties.class, EAppSettings.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

