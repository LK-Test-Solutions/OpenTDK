package Tests.Utility;

import java.io.IOException;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.logger.MLogger;

public class helloWorld {

	public static void main(String[] args) throws IOException {
		ESettings.setDataContainer(EBaseSettings.class, "logs/helloWorld.xml", "AppSettings");
		
		MLogger.getInstance().setLogFile(ESettings.LOGFILE.getValue());
		MLogger.getInstance().log(ESettings.HUNDNAME.getValue());
		
		ESettings.HUNDNAME.setValue("Emma");
		
		MLogger.getInstance().log(ESettings.HUNDNAME.getValue());
		
		

	}

}
