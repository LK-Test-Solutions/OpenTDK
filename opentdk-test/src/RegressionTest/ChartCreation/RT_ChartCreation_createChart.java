package RegressionTest.ChartCreation;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.gui.chart.ChartAxis;
import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartFont;
import org.opentdk.gui.chart.ChartMarker;
import org.opentdk.gui.chart.ChartProperties;
import org.opentdk.gui.chart.ChartSeries;
import org.opentdk.gui.chart.ChartMarker.DataPoint;

import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;
import org.opentdk.api.util.ListUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RT_ChartCreation_createChart extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) {
		createLineChartMinimumProperties();
		createLineChartFull1();
		createLineChartFull2();
		createBarChart();

		// Stop JavaFX Thread
		Platform.exit();
	}

	public void createLineChartMinimumProperties() {
		var properties = new ChartProperties();
		var chartPlugin = new ChartCreatorPlugin("LINE", "./output/LineChartMinimum.png", properties);
		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
	}

	// COMP.: VDIMON 
	public void createLineChartFull1() {
		MLogger.getInstance().setTraceLevel(Level.ALL);

		ChartProperties cp = new ChartProperties();
		cp.setTitle("CPU-Auslastung und Anzahl User f\u00FCr alle ABS-Blades in der Top Stunde 11 - 12 Uhr");
		cp.setTitleFont(ChartFont.font(24, FontWeight.BOLD));
		cp.setHeight(831);
		cp.setWidth(2230);
		cp.setLegendFont(ChartFont.font(18, FontWeight.NORMAL));
		cp.setLegendSide(Side.TOP);
		cp.setVerticalGridLinesVisible(false);
		cp.setHorizontalGridLinesVisible(true);
		cp.setHorizontalGridLinesColor(Color.GREY);

		ChartAxis xAxis = new ChartAxis();
		xAxis.setLabel("Januar 2021");
		xAxis.setValueRange(31);
		xAxis.setValueStep(1);
		xAxis.setLabelFont(ChartFont.font(18, FontWeight.BOLD));
		xAxis.setTickLabelFont(Font.font(14));
		xAxis.setLeaveOneStepSpace(true);
		cp.setxAxis(xAxis);

		ChartAxis y1Axis = new ChartAxis();
		y1Axis.setLabel("CPU-Auslastung in %");
		y1Axis.setValueRange(100);
		y1Axis.setValueStep(5);
		y1Axis.setLabelFont(ChartFont.font(18));
		y1Axis.setTickLabelFont(Font.font(14));
		y1Axis.setMinorTickMarksVisible(false);
		y1Axis.setForceZeroInRange(true);
		cp.setY1Axis(y1Axis);

		ChartAxis y2Axis = new ChartAxis();
		y2Axis.setLabel("Anzahl User");
		y2Axis.setValueRange(7000);
		y2Axis.setValueStep(1000);
		y2Axis.setLabelFont(ChartFont.font(18));
		y2Axis.setTickLabelFont(Font.font(14));
		y2Axis.setMinorTickMarksVisible(false);
		y2Axis.setForceZeroInRange(true);
		cp.setY2Axis(y2Axis);

		ChartSeries series1 = new ChartSeries();
		series1.setSeriesName("CPU Auslastung in %");
		List<String> series1Values = new ArrayList<>(Collections.nCopies(31, (String) null));
		series1Values.set(4, "50");
		series1Values.set(5, "55");
		series1.setSeriesValues(series1Values);
		series1.setSeriesColor(Color.STEELBLUE);
		series1.setBelongingAxis(0);

		ChartSeries series2 = new ChartSeries();
		series2.setSeriesName("Anzahl User");
		List<String> series2Values = new ArrayList<>(Collections.nCopies(31, (String) null));
		series2Values.set(4, "3800");
		series2Values.set(5, "4000");
		series2.setSeriesValues(series2Values);
		series2.setSeriesColor(Color.DARKRED);
		series2.setBelongingAxis(1);

		List<ChartSeries> series = new ArrayList<>();
		series.add(series1);
		series.add(series2);
		cp.setSeriesValues(series);

		List<ChartMarker> chartMarkers = new ArrayList<>();

		StackPane pane = new StackPane();
		Text text = new Text("Neujahr");
		text.setRotate(270);
		text.setFill(Color.GREY);
		text.setFont(Font.font(24));
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(50);
		rectangle.setHeight(200);
		rectangle.setFill(Color.WHITE);
		Shape[] children = { text, rectangle };
		chartMarkers.add(ChartMarker.marker(pane, children, DataPoint.point(1, 35), -1.0));

		Color color = Color.rgb((int) (Color.GAINSBORO.getRed() * 255), (int) (Color.GAINSBORO.getGreen() * 255), (int) (Color.GAINSBORO.getBlue() * 255), 0.5);
		
		Rectangle rangeRectangle1 = new Rectangle();
		rangeRectangle1.setHeight(650);
		rangeRectangle1.setWidth(95);
		rangeRectangle1.setFill(color);
		chartMarkers.add(ChartMarker.marker(rangeRectangle1, DataPoint.point(0.75, 100), 1.0));
		
		Rectangle rangeRectangle2 = new Rectangle();
		rangeRectangle2.setHeight(650);
		rangeRectangle2.setWidth(62.5 * 5);
		rangeRectangle2.setFill(color);
		chartMarkers.add(ChartMarker.marker(rangeRectangle2, DataPoint.point(6.0, 100), 1.0));

		cp.setChartMarkers(chartMarkers);

		ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("LINE", "./output/LineChartFull_1.png", cp, 2);

		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
	}

	// COMP.: AVERAGE CHART PRODMON
	public void createLineChartFull2() {		
		ChartProperties cp = new ChartProperties();
		cp.setTitle("Alle Standorte \"ADAG-Schaden@KV_KalkBelegPrf_cmdComplete_execute\"\n\tVDI Antwortzeit ohne Erststarts, Anzahl Vorgänge mit Erststarts");
		cp.setPlotAreaColor(Color.valueOf("rgb(188,222,255)"));
		cp.setLegendColor(Color.valueOf("rgb(222,222,222)"));
		cp.setTitleFont(ChartFont.font(18, FontWeight.BOLD));
		cp.setHeight(741);
		cp.setWidth(1286);
		cp.setLegendSide(Side.BOTTOM);
		cp.setVerticalGridLinesVisible(false);
//		cp.setHorizontalGridLinesToFront(false);
		cp.setHorizontalGridLinesColor(Color.GREY);
		cp.setLegendFont(ChartFont.font(16, FontWeight.NORMAL));
		cp.setChartPadding(new Insets(10, 10, 10, 10));

		ChartAxis xAxis = new ChartAxis();
		List<String> xCategories = new ArrayList<>();
		String sCurDate = DateUtil.get("27.12.2021", EFormat.DATE_4.getDateFormat());
		int daysToThePast = 94;
		for (int i = daysToThePast; i >= 0; i--) {
			xCategories.add(DateUtil.get(sCurDate, -i, EFormat.DATE_4.getDateFormat(), ChronoUnit.DAYS));
		}
		xAxis.setCategories(xCategories);
		xAxis.setTickLabelRotation(270);
		xAxis.setTickLabelFont(Font.font(13));
		xAxis.setEndMargin(30);
		xAxis.setTickLength(8);
		cp.setxAxis(xAxis);
		
		ChartAxis y1Axis = new ChartAxis();
		y1Axis.setLabel("Antwortzeit in Sek");
		y1Axis.setLabelFont(ChartFont.font(16));
		y1Axis.setValueRange(15);
		y1Axis.setValueStep(1);
		y1Axis.setMinorTickMarksVisible(true);
		y1Axis.setForceZeroInRange(true);
		y1Axis.setMinorTickLength(5);
		y1Axis.setMinorTickMarkColor(Color.BLACK);
		y1Axis.setMinorTickCount(5);
		cp.setY1Axis(y1Axis);
		
		ChartAxis y2Axis = new ChartAxis();
		y2Axis.setLabel("Anzahl Vorgänge");
		y2Axis.setLabelFont(ChartFont.font(16));
		y2Axis.setValueRange(7000);
		y2Axis.setValueStep(1000);
		y2Axis.setForceZeroInRange(true);
		y2Axis.setMinorTickCount(5);
		y2Axis.setMinorTickLength(5);
		y2Axis.setMinorTickMarkColor(Color.BLACK);
		y2Axis.setMinorTickMarksVisible(true);
		cp.setY2Axis(y2Axis);
		
		List<ChartSeries> series = new ArrayList<>();
		
		ChartSeries counts = new ChartSeries();
		counts.setSeriesName("Anzahl Vorgänge");
		counts.setSeriesLegendNode(new Circle(5));
		counts.setSeriesColor(Color.WHITE);
		series.add(counts);
		
		List<ChartMarker> chartMarkers = new ArrayList<>();
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(4);
		rectangle.setFill(Color.WHITE);
		chartMarkers.add(ChartMarker.marker(rectangle, DataPoint.point("26.12.2021", 3250), -0.5, 1));
				
		StackPane pane = new StackPane();
		Rectangle line = new Rectangle();
		line.setStroke(Color.GREEN);
		line.setWidth(cp.getWidth() * 2);
		line.getStrokeDashArray().addAll(10d, 10d);
		line.setStrokeWidth(3);
		Shape[] children = { line };
		chartMarkers.add(ChartMarker.marker(pane, children, DataPoint.point(xCategories.get(0), 7.5), 0));
		
		StackPane releasePane = new StackPane();
		Text text = new Text("Upd. 50/21");
		text.setRotate(270);
		text.setFill(Color.GREY);
		text.setFont(Font.font(16));
		Shape[] relChildren = { text };
		chartMarkers.add(ChartMarker.marker(releasePane, relChildren, DataPoint.point("26.12.2021", 2), -1.0));
		
		cp.setChartMarkers(chartMarkers);
		
		ChartSeries responseTimes = new ChartSeries();
		responseTimes.setBelongingAxis(0);
		responseTimes.setSeriesColor(Color.STEELBLUE);
		responseTimes.setSeriesName("Alle Standorte VDI");
		responseTimes.setSeriesSymbolSize(3);
		responseTimes.setSeriesLegendNode(new Circle(5));
		List<String> averages = new ArrayList<>(Collections.nCopies(94, (String) null));
		averages.set(93, "5.1");
		responseTimes.setSeriesValues(averages);
		series.add(responseTimes);
		
		ChartSeries quantileValues = new ChartSeries();
		quantileValues.setBelongingAxis(0);
		quantileValues.setSeriesColor(Color.RED);
		quantileValues.setSeriesName("Alle Standorte Quantile 80 %");
		quantileValues.setSeriesSymbolSize(3);
		quantileValues.setSeriesLegendNode(new Circle(5));
		List<String> quantiles = new ArrayList<>(Collections.nCopies(94, (String) null));
		quantiles.set(93, "7.0");
		quantileValues.setSeriesValues(quantiles);
		series.add(quantileValues);
		
		ChartSeries references = new ChartSeries();
		references.setSeriesName("Referenz Mittelwert");
		references.setSeriesColor(Color.GREEN);
		series.add(references);
				
		cp.setSeriesValues(series);

		ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("LINE", "./output/LineChartFull_2.png", cp);
		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
		
	}
	
	// COMP.: HISTOGRAM PRODMON
	private void createBarChart() {
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
		
		ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("BAR", "./output/BarChart.png", cp, 1);
		chartPlugin.run();
		if (chartPlugin.isSuccess() == false) {
			System.err.println("Chart creation finished with error ==> " + getClass().getSimpleName());
		}
		// Stop JavaFX Thread
		Platform.exit();
	}
}
