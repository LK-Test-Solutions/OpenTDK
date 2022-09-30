package Tests.ChartCreation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.gui.chart.ChartAxis;
import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartFont;
import org.opentdk.gui.chart.ChartMarker;
import org.opentdk.gui.chart.ChartProperties;
import org.opentdk.gui.chart.ChartSeries;
import org.opentdk.gui.chart.ChartMarker.DataPoint;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.mapping.EOperator;
import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;
import org.opentdk.api.util.ListUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TST_ChartCreation_createHistogram extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) {
		createHistogram();

		// Stop JavaFX Thread
		Platform.exit();
	}
	
	private void createHistogram() {
		MLogger.getInstance().setTraceLevel(Level.ALL);
		ChartProperties cp = new ChartProperties();

		int xRange = 100;
		int xStep = 5;
		List<String> xCategories = new ArrayList<>();
		xCategories.add("0");
		for (int i = xStep; i <= xRange; i += xStep) {
			xCategories.add(String.valueOf(i));
		}
		xCategories.add(">= " + xCategories.get(xCategories.size() - 1));

		// Series values
		double sum = 0;
		List<ChartSeries> seriesValues = new ArrayList<>();
		List<String> values = new ArrayList<>();
		values.add("0");
		String[] moreValues = { "0", "0", "0", "0", "68", "439", "259", "814", "782", "458", "263", "162", "134", "99", "65", "46", "29", "34", "16", "14", "83" };
		values.addAll(ListUtil.asList(moreValues));
		List<Integer> labels = new ArrayList<>();
		List<Double> numberValues = new ArrayList<>();

		List<ChartMarker> chartMarkers = new ArrayList<>();

		for (int i = 0; i < values.size(); i++) {
			String value = values.get(i);
			double dVal = Double.parseDouble(value.trim());
			numberValues.add(dVal);
			int iVal = Integer.parseInt(value.trim());
			sum += iVal;
		}
		for (int i = 0; i < values.size(); i++) {
			String value = values.get(i);
			int iVal = Integer.parseInt(value.trim());
			labels.add(iVal);
			double fVal = (iVal / sum) * 100.0f;
			values.set(i, String.valueOf(fVal));

			Text text = new Text(String.valueOf(iVal));
			text.setRotate(270);
			text.setFill(Color.BLACK);
			text.setFont(Font.font(16));

			chartMarkers.add(ChartMarker.marker(new StackPane(), new Shape[] { text }, DataPoint.point(xCategories.get(i), fVal + 5)));
		}
		ChartSeries series = new ChartSeries();
		series.setSeriesName("Prozentsatz");
		series.setSeriesValues(values);
		series.setSeriesColor(Color.STEELBLUE);
		series.setSeriesLabelVisible(false);
		series.setSeriesFontSize(16);
		seriesValues.add(series);

		String sCurDate = DateUtil.get(EFormat.DATE_4.getDateFormat());
		Filter filter = new Filter();
		filter.addFilterRule("Datum", sCurDate, EOperator.CONTAINS_DATE);

		String mean = "45933";
		double dMean = Double.parseDouble(mean) / 1000f;
		String quantile = "53402";
		double dQuantile = Double.parseDouble(quantile) / 1000f;

		String info = "Anzahl:\t\t\t\t " + (int) sum + "\nMittelwert Antwortzeit:\t " + dMean + " Sek\n" + "Quantile 80%:\t\t\t " + dQuantile + " Sek";			
		StackPane pane = new StackPane();
		Text text = new Text(info);
//		text.setRotate(270);
		text.setFill(Color.BLACK);
		text.setFont(Font.font(20));
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(300);
		rectangle.setHeight(200);
		rectangle.setFill(Color.WHITE);
		Shape[] children = { text, rectangle };
		chartMarkers.add(ChartMarker.marker(pane, children, DataPoint.point("90", 90), -1.0));
		
		cp.setTitle("Histogram " + DateUtil.get(EFormat.DATE_4.getDateFormat()));
		cp.setTitleFont(ChartFont.font(26, FontWeight.BOLD));
		cp.setHeight(756);
		cp.setWidth(1461);
		
		ChartAxis xAxis = new ChartAxis();
		xAxis.setLabel("Sekunden (sek)");
		xAxis.setLabelFont(ChartFont.font(18));
		xAxis.setCategories(xCategories);
		cp.setxAxis(xAxis);
		
		ChartAxis y1Axis = new ChartAxis();
		y1Axis.setLabel("Prozentsatz (%)");
		y1Axis.setLabelFont(ChartFont.font(18));
		y1Axis.setValueRange(100);
		y1Axis.setValueStep(10);
		y1Axis.setTickLabelFont(Font.font(16));
		y1Axis.setEndMargin(20);
		cp.setY1Axis(y1Axis);
		
		cp.setChartMarkers(chartMarkers);
		cp.setCategoryGap(30);
		
		cp.setLegendVisible(false);
		cp.setHorizontalGridLinesVisible(false);
		cp.setVerticalGridLinesVisible(false);
		
		List<ChartSeries> allSeries = new ArrayList<>();
		allSeries.add(series);
		cp.setSeriesValues(allSeries);
		
		ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("BAR", "./output/Histogram.png", cp, 1);
		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
		// Stop JavaFX Thread
		Platform.exit();
	}
}
