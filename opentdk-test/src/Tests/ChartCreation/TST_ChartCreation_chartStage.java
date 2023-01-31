package Tests.ChartCreation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.MathUtil;
import org.opentdk.gui.chart.ChartAxis;
import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartFont;
import org.opentdk.gui.chart.ChartProperties;
import org.opentdk.gui.chart.ChartSeries;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TST_ChartCreation_chartStage extends Application {
	
	
	private final String[] xValues = {
			"22.03.2021", "23.03.2021", "24.03.2021", "25.03.2021", "26.03.2021", "29.03.2021", "30.03.2021", "31.03.2021", "01.04.2021", "02.04.2021", 
			"05.04.2021", "06.04.2021", "08.04.2021", "09.04.2021", "12.04.2021", "13.04.2021", "14.04.2021", "15.04.2021", "16.04.2021", "19.04.2021", 
			"21.04.2021", "22.04.2021", "23.04.2021", "26.04.2021", "27.04.2021", "28.04.2021", "29.04.2021", "30.04.2021", "03.05.2021", "05.05.2021",
			"06.05.2021", "07.05.2021", "10.05.2021", "11.05.2021", "12.05.2021", "13.05.2021", "14.05.2021", "17.05.2021", "18.05.2021", "19.05.2021", 
			"20.05.2021", "21.05.2021", "24.05.2021", "25.05.2021", "27.05.2021", "28.05.2021", "31.05.2021", "01.06.2021", "02.06.2021", "03.06.2021", 
			"04.06.2021", "07.06.2021", "08.06.2021", "09.06.2021", "10.06.2021", "11.06.2021", "14.06.2021", "15.06.2021", "16.06.2021", "17.06.2021", 
			"18.06.2021", "21.06.2021", "22.06.2021", "23.06.2021", "24.06.2021", "25.06.2021", "28.06.2021", "29.06.2021", "30.06.2021", "01.07.2021", 
			"02.07.2021", "05.07.2021", "07.07.2021", "08.07.2021", "09.07.2021", "12.07.2021", "13.07.2021", "14.07.2021", "15.07.2021", "16.07.2021", 
			"19.07.2021", "21.07.2021", "22.07.2021", "23.07.2021", "26.07.2021", "27.07.2021", "28.07.2021", "29.07.2021", "30.07.2021", "02.08.2021", 
			"04.08.2021", "05.08.2021", "06.08.2021", "09.08.2021", "10.08.2021", "11.08.2021", "12.08.2021", "13.08.2021", "16.08.2021", "18.08.2021", 
			"19.08.2021", "20.08.2021", "23.08.2021", "24.08.2021", "25.08.2021", "26.08.2021", "27.08.2021", "30.08.2021", "31.08.2021", "01.09.2021", 
			"02.09.2021", "03.09.2021", "06.09.2021", "07.09.2021", "08.09.2021", "09.09.2021", "10.09.2021", "13.09.2021", "14.09.2021", "15.09.2021", 
			"16.09.2021", "17.09.2021", "20.09.2021", "21.09.2021", "22.09.2021", "23.09.2021", "24.09.2021", "27.09.2021", "28.09.2021", "29.09.2021", 
			"30.09.2021", "01.10.2021", "04.10.2021", "06.10.2021", "07.10.2021", "08.10.2021", "11.10.2021", "12.10.2021", "13.10.2021", "14.10.2021", 
			"15.10.2021", "18.10.2021", "20.10.2021", "21.10.2021", "22.10.2021", "25.10.2021", "26.10.2021", "27.10.2021", "28.10.2021", "29.10.2021", 
			"01.11.2021", "02.11.2021", "03.11.2021", "04.11.2021", "05.11.2021", "08.11.2021", "10.11.2021", "11.11.2021", "12.11.2021", "15.11.2021", 
			"16.11.2021", "17.11.2021", "18.11.2021", "19.11.2021", "22.11.2021", "23.11.2021", "24.11.2021", "25.11.2021", "26.11.2021", "29.11.2021", 
			"30.11.2021", "01.12.2021", "02.12.2021", "03.12.2021", "06.12.2021", "07.12.2021", "08.12.2021", "09.12.2021", "10.12.2021", "13.12.2021", 
			"14.12.2021", "16.12.2021", "17.12.2021", "20.12.2021", "21.12.2021", "22.12.2021", "23.12.2021", "24.12.2021", "27.12.2021", "28.12.2021", 
			"29.12.2021", "30.12.2021", "31.12.2021", "03.01.2022", "04.01.2022", "05.01.2022", "06.01.2022", "07.01.2022", "10.01.2022", "12.01.2022", 
			"13.01.2022", "14.01.2022", "17.01.2022", "18.01.2022", "19.01.2022", "20.01.2022", "21.01.2022", "24.01.2022", "26.01.2022", "27.01.2022", 
			"28.01.2022", "31.01.2022", "01.02.2022", "02.02.2022", "03.02.2022", "04.02.2022", "07.02.2022", "09.02.2022", "10.02.2022", "11.02.2022", 
			"14.02.2022", "15.02.2022", "16.02.2022", "17.02.2022", "18.02.2022", "21.02.2022", "23.02.2022", "24.02.2022", "25.02.2022", "28.02.2022", 
			"01.03.2022", "02.03.2022", "03.03.2022", "04.03.2022", "07.03.2022", "08.03.2022", "09.03.2022", "10.03.2022", "11.03.2022", "14.03.2022", 
			"15.03.2022", "16.03.2022", "17.03.2022", "18.03.2022"};

	private final double[] yValues = {
			1.190, 1.363, 1.440, 1.209, 1.231, 1.083, 1.320, 1.384, 1.217, 0.714, 
			0.874, 1.307, 1.430, 1.302, 1.301, 1.482, 1.406, 1.496, 1.345, 1.439, 
			1.465, 1.524, 1.416, 1.511, 1.201, 1.047, 1.485, 1.415, 1.240, 1.077, 
			1.060, 1.285, 1.351, 1.277, 1.314, 0.656, 1.193, 1.334, 1.305, 1.466, 
			1.441, 1.321, 0.874, 1.423, 1.217, 1.321, 1.446, 1.308, 1.324, 0.980, 
			1.150, 1.213, 1.549, 1.306, 1.522, 1.387, 1.391, 1.496, 1.207, 1.503, 
			1.149, 1.056, 1.079, 1.057, 1.074, 0.981, 1.013, 1.053, 1.077, 1.084, 
			0.986, 1.056, 1.096, 1.172, 1.005, 1.091, 1.113, 1.104, 1.066, 1.034, 
			1.095, 1.111, 1.092, 1.021, 1.106, 1.034, 1.051, 1.046, 0.977, 1.083, 
			1.042, 1.068, 0.974, 1.109, 1.125, 1.044, 1.051, 0.953, 1.048, 1.059, 
			1.040, 0.956, 1.040, 1.070, 0.999, 1.025, 0.927, 1.061, 1.045, 0.929, 
			1.084, 0.992, 0.933, 1.082, 1.063, 1.084, 1.007, 1.017, 1.115, 1.098, 
			1.123, 0.997, 1.039, 1.075, 1.082, 1.100, 0.967, 1.052, 1.068, 1.044, 
			1.062, 1.019, 1.069, 1.054, 0.935, 0.945, 1.075, 1.098, 1.044, 0.986, 
			0.837, 0.899, 0.965, 1.043, 1.007, 0.954, 1.003, 0.921, 0.884, 0.949, 
			0.924, 1.131, 1.102, 1.065, 1.046, 0.927, 1.021, 1.157, 1.085, 0.925, 
			0.876, 0.955, 1.002, 0.963, 0.922, 0.762, 0.724, 1.097, 0.990, 1.118, 
			0.917, 0.807, 0.733, 0.752, 0.799, 0.810, 0.819, 0.802, 0.797, 0.747, 
			0.744, 0.699, 0.711, 0.749, 0.697, 0.735, 0.700, 0.706, 0.936, 0.939, 
			0.727, 0.975, 0.837, 0.818, 0.766, 0.847, 0.882, 0.806, 0.873, 0.927, 
			0.763, 0.736, 0.751, 0.742, 0.783, 0.749, 0.739, 0.810, 0.970, 0.953, 
			0.843, 0.720, 0.793, 0.693, 0.693, 0.668, 0.713, 0.696, 0.706, 0.685, 
			0.720, 0.716, 0.729, 0.741, 0.709, 0.839, 0.952, 0.926, 0.888, 0.945, 
			0.946, 0.941, 0.937, 0.875, 0.931};
	
	private double maxBackendValue = 0;
	private List<String> xCategories = new ArrayList<>();
	private List<Double> backendNumbers = new ArrayList<>();
	private List<String> backendValues = new ArrayList<>();
	private final String meshtrace = "Vertrag Oeffnen";
	private DecimalFormat numberFormat;
	
	public static void main(String[] args) {
		launch();		
	}
	
	@Override
	public void start(Stage stage) {
		DecimalFormatSymbols format = DecimalFormatSymbols.getInstance();
		format.setDecimalSeparator('.');
		numberFormat = new DecimalFormat("0.00", format);
		
		MLogger.getInstance().setTraceLevel(3);
		for(String value : this.xValues) {
			this.xCategories.add(value);
		}
		for(double value : this.yValues) {
			this.backendValues.add(String.valueOf(value));
			this.backendNumbers.add(value);
		}
		this.maxBackendValue = MathUtil.getMaximum(this.backendNumbers);
		
		Chart chart = this.createBackendChart();
		Scene scene = new Scene(chart);
		stage.setScene(scene);
		stage.setWidth(1000);
		stage.setHeight(600);
		stage.setOnCloseRequest(event -> {
			Platform.exit();
		});
		stage.show();
		
	}

	private final Chart createBackendChart() {
		ChartProperties cp = new ChartProperties();
		cp.setTitle("Backend-Zeiten ABS 09:00 - 17:00 Uhr");
		cp.setTitleFont(ChartFont.font(15, FontWeight.BOLD));
		cp.setPlotAreaColor(Color.WHITE);
		cp.setLegendFont(ChartFont.font(14, FontWeight.NORMAL));
		cp.setLegendSide(Side.BOTTOM);
		cp.setVerticalGridLinesVisible(false);
		cp.setHorizontalGridLinesVisible(true);
		cp.setHorizontalGridLinesColor(Color.BLACK);
		cp.setNumberAxisFormat(this.numberFormat);
//		int chartPadding = 10;
//		cp.setChartPadding(new Insets(chartPadding, chartPadding, chartPadding, chartPadding));

		ChartAxis xAxis = new ChartAxis();
		xAxis.setCategories(this.xCategories);
		xAxis.setTickLabelRotation(270);
		xAxis.setTickLabelFont(Font.font(12));
		xAxis.setEndMargin(10);
		xAxis.setTickLength(3);
		cp.setxAxis(xAxis);

		ChartAxis y1Axis = new ChartAxis();
		y1Axis.setLabel("Antwortzeit [sec]");
		y1Axis.setLabelFont(ChartFont.font(14, FontWeight.BOLD));

		/*
		 * Y1 axis value range: Get the closest rounded number in comparison to the biggest value of the data set. E.g. 189 gets 200, 3462 gets 4000 etc. by using the logarithm and the exponent.
		 */
		int digits = (int) (Math.log10(this.maxBackendValue) + 1);
		double range = Math.pow(10, digits - 1);
		double step = range; // Save the starting offset value to use it for the value step

		for (int i = 0; i < range * 10; i++) {
			if (range > this.maxBackendValue) {
				break;
			} else {
				range += step;
			}
		}
		
		y1Axis.setValueRange(range);		
		y1Axis.setValueStep(step * 0.4);
		
		y1Axis.setMinorTickMarksVisible(false);
		y1Axis.setForceZeroInRange(false);
		y1Axis.setTickLength(5);
		y1Axis.setMinorTickMarkColor(Color.BLACK);
//		y1Axis.setMinorTickCount(0);
//		y1Axis.setMinorTickLength(0);
		cp.setY1Axis(y1Axis);

		List<ChartSeries> series = new ArrayList<>();
		ChartSeries serie = new ChartSeries();
		serie.setBelongingAxis(0);
		serie.setSeriesColor(Color.STEELBLUE);
		serie.setSeriesName(this.meshtrace);
		serie.setSeriesSymbolSize(2);
		serie.setSeriesLegendNode(new Circle(3));
		serie.setSeriesValues(this.backendValues);
		series.add(serie);
		cp.setSeriesValues(series);

		ChartCreatorPlugin creator = new ChartCreatorPlugin("LINE", cp);
		return creator.createChart();
	}


}
