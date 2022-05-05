module opentdk.api {
	
	exports org.opentdk.api.datastorage;
	exports org.opentdk.api.io;
	exports org.opentdk.api.logger;
	exports org.opentdk.api.meter;
	exports org.opentdk.api.util;				
	exports org.opentdk.api.dispatcher;				
	
	exports RegressionSets.Api to junit, javafx.graphics;
	exports RegressionTests.CommonUtility to junit;
	exports RegressionTests.Container to junit;
	exports RegressionTests.CryptoUtility to junit;
	exports RegressionTests.CSVContainer to junit;
	exports RegressionTests.DateUtility to junit;
	exports RegressionTests.IO to junit;
	exports RegressionTests.ListUtility to junit;
	exports RegressionTests.Logging to junit;
	exports RegressionTests.Meter to junit;
	exports RegressionTests.Dispatcher to junit;
	exports RegressionTests.XMLContainer to junit;
	exports Tests.FileUtil to junit;
	exports Tests.StringUtility to junit;
	exports Tests.OverrideBehavior to junit;
	exports Tests.Logging to junit;
	exports Tests.Utility to junit;

	requires java.scripting;
	requires java.sql;
	requires java.desktop; 
	requires java.xml;
	requires java.logging;
	
	requires org.apache.commons.io;
	requires commons.math3;
	
	requires junit;
}