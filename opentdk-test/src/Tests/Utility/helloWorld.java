package Tests.Utility;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.logger.MLogger;

public class helloWorld {

	public static void main(String[] args) {
		ESettings.setDataContainer(EBaseSettings.class, "logs/helloWorld.csv");
		
		MLogger.getInstance().setLogFile(ESettings.APP_LOGFILE.getValue());
		MLogger.getInstance().log(ESettings.HUNDNAME.getValue());
		
		ESettings.HUNDNAME.setValue("Emma");
		
		MLogger.getInstance().log(ESettings.HUNDNAME.getValue());
		
		

	}

}
