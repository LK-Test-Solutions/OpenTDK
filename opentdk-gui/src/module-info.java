module opentdk.gui {
	
	exports org.opentdk.gui.application;
	exports org.opentdk.gui.controls;
	exports org.opentdk.gui.chart;
	exports com.kostikiadis.charts;

	requires transitive javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;	
	requires transitive javafx.swing;
	
	requires transitive java.logging;	
	requires transitive opentdk.api;

}