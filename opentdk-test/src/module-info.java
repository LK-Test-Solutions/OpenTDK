module opentdk.test {
	exports RegressionTest.Application;
	exports Tests.BaseApplication;
	
	requires java.desktop;
	requires java.logging;
	requires java.sql;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.swing;
	requires opentdk.api;
	requires opentdk.gui;
	requires org.json;
	requires snakeyaml;
	requires org.apache.commons.io;
}