package Tests.ChartCreation;

import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartProperties;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TST_ChartCreation_createMinimumChart extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) {
		createLineChartMinimumProperties();

		// Stop JavaFX Thread
		Platform.exit();
	}

	public void createLineChartMinimumProperties() {
		var properties = new ChartProperties();
		var chartPlugin = new ChartCreatorPlugin("LINE", "./output/MinimumChart.png", properties);
		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
	}
}
