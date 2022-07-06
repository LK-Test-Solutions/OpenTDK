package Tests.ChartCreation;

import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartProperties;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TST_ChartCreation_createMaxChart extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) {
		ChartProperties cp = new ChartProperties();
		
		String title = "";
		for(int i=0; i<10000; i++) {
			title+="-";
		}
		
		cp.setTitle(title);
		cp.setWidth(10000);
		cp.setHeight(10000);
		
		
		
		ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("BAR", "./output/MaxBarChart.png", cp);
		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
		// Stop JavaFX Thread
		Platform.exit();
	}

}
