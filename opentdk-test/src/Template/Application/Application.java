package Template.Application;

import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.application.BaseApplication;
import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;
import org.opentdk.api.logger.MLogger;

import RegressionTest.BaseRegression;

import java.io.IOException;
import java.lang.reflect.Field;

public class Application extends BaseApplication {

	public static void main(String[] args) throws Exception {
		new Application(args);
	}
	
	public Application(String[] args) throws Exception {
		parseArgs(ERuntimeProperties.class, args);	
	}
}

