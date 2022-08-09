package Template.Application;

import org.opentdk.api.application.BaseApplication;

public class Application extends BaseApplication {

	public static void main(String[] args) throws Exception {
		new Application(args);
	}
	
	public Application(String[] args) throws Exception {
		parseArgs(ERuntimeProperties.class, args);
		initRuntimeProperties(ERuntimeProperties.class, EAppSettings.class);
		
		System.out.println("HOMEDIR = " + ERuntimeProperties.HOMEDIR.getValue());
		System.out.println("BASEURL = " + ERuntimeProperties.BASEURL.getValue()); 
		
		System.out.println("HOMEDIR = " + EAppSettings.HOMEDIR.getValue());
		System.out.println("BASEURL = " + EAppSettings.BASEURL.getValue()); 

	}
}

