package Tests.ChartCreation;

import com.kostikiadis.charts.MultiAxisChart;
import com.kostikiadis.charts.MultiAxisLineChart;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

public class TST_ChartCreation_getCssOptions extends Application {

	public static void main(String[] args) {
		launch();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void start(Stage arg0) {
		Axis dummy = new NumberAxis();
		Axis dummy1 = new NumberAxis();
		Axis dummy2 = new NumberAxis();
		@SuppressWarnings("unchecked")
		MultiAxisChart<?, ?> chart = new MultiAxisLineChart<>(dummy, dummy1, dummy2);
		System.out.println(chart.lookup(".chart").getStyle());
		Platform.exit();
	}

}
