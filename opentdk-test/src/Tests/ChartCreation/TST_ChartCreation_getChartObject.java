package Tests.ChartCreation;

import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartProperties;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.stage.Stage;

public class TST_ChartCreation_getChartObject extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) {
		ChartProperties cp = new ChartProperties();
		cp.setTitle("Test Title");
			
		ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("LINE", "./output/Dummy.png", cp);
		Chart chart = chartPlugin.createChart();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
		
		Scene scene = new Scene(chart, cp.getWidth(), cp.getHeight());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
		// Stop JavaFX Thread
		stage.setOnCloseRequest(event -> {
			Platform.exit();
		});
	}
}
