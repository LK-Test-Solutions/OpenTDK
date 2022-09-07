module opentdk.test {
	exports RegressionTest.Container;
	exports RegressionTest.ListUtility;
	exports RegressionTest.Meter;
	exports RegressionTest.XMLContainer;
	exports Tests.ChartCreation;
	exports RegressionTest;
	exports RegressionTest.CommonUtility;
	exports RegressionTest.DateUtility;
	exports RegressionTest.Dispatcher;
	exports Tests.Utility;
	exports Tests.StringUtility;
	exports Tests.FileUtil;
	exports RegressionTest.IO;
	exports Tests.Logging;
	exports Tests.OverrideBehavior;
	exports RegressionTest.ChartCreation;
	exports RegressionTest.Logging;
	exports RegressionTest.CryptoUtility;
	exports RegressionTest.CSVContainer;
	exports Template.Application;
	exports Tests.BaseApplication;
	

	requires java.desktop;
	requires java.logging;
	requires java.sql;
	requires javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.swing;
	requires transitive opentdk.api;
	requires transitive opentdk.gui;
	requires org.json;
}