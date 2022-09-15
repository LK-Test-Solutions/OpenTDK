package Tests.ChartCreation;

import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.logger.MLogger;
import org.opentdk.gui.chart.ChartCreatorPlugin;
import org.opentdk.gui.chart.ChartFont;
import org.opentdk.gui.chart.ChartMarker;
import org.opentdk.gui.chart.ChartProperties;
import org.opentdk.gui.chart.ChartSeries;
import org.opentdk.gui.chart.ChartMarker.DataPoint;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TST_ChartCreation_createBarChart extends Application {

	private String chartType = "BAR";
	private String outputFile;
	private ChartProperties cp;
	private String prefix = "HB_G01_Firmen_neu";
	private String meshtraceInTitle = "Vertrag rechnen_HB";
	private String fromRelTo = " - Rel. 20.5 bis 23.0";
	private String testcase = "11240";
	private String branch = "Firmen";
	private double y1ValRange = 3000.0;
	private double step = 1000.0;

	private String[] clientSerie = new String[] { "1446", "1457", "1613", "1531", "674", "661", "656", "598", "607", "1823", "592", "634", "598", "808", "881" };
	private String[] totalSerie = new String[] { "1450", "1470", "1640", "1550", "690", "691", "686", "620", "630", "1850", "620", "650", "623", "830", "901" };
	private String[] backcallSerie = new String[] { "24", "20", "20", "20", "8", "8", "8", "8", "8", "12", "8", "8", "8", "8", "8" };
	private String[] backendSerie = new String[] { "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30", "30" };
	private String[] saIterations = new String[] { "20.5 RC7", "20.6 RC5_20H2", "21.0 RC6_neuVDI", "21.1 RC5", "21.1 U3_neuVDI", "22.0 RC6", "22.0 U3_AGL", "22.6 RC5", "22.6 U3_21H2", "23.0 IT6", "23.0 RC1", "23.0 RC2", "23.0 RC3", "23.0 RC4", "23.0 RC5" };

	private List<String> meanClientSerie;
	private List<String> meanTotalSerie;
	private List<String> meanBackendSerie;
	private List<String> meanBackcallSerie;
	private List<String> iterations;

	int categorySize = clientSerie.length;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) throws Exception {
//		do {
			meanClientSerie = new ArrayList<>();
			meanTotalSerie = new ArrayList<>();
			meanBackendSerie = new ArrayList<>();
			meanBackcallSerie = new ArrayList<>();
			iterations = new ArrayList<>();
			for (int i = 0; i < categorySize; i++) {
				meanClientSerie.add(clientSerie[i]);
				meanTotalSerie.add(totalSerie[i]);
				meanBackendSerie.add(backendSerie[i]);
				meanBackcallSerie.add(backcallSerie[i]);
				iterations.add(saIterations[i]);
			}
			cp = new ChartProperties();
			createChart();
//			categorySize++;
//		} while(categorySize <= clientSerie.length);
		
		Platform.exit();
	}

	public void createChart() {
		for (String iteration : iterations) {
			cp.getxAxis().getCategories().add(iteration);
		}

		ChartSeries series0 = new ChartSeries();
		series0.setSeriesName("Antwortzeit Client");
		series0.setSeriesValues(meanClientSerie);
		series0.setSeriesFontSize(14);
		series0.setSeriesColor(Color.valueOf("steelblue"));
		cp.getSeriesValues().add(series0);

		ChartSeries series1 = new ChartSeries();
		series1.setSeriesName("Antwortzeit Gesamt");
		series1.setSeriesValues(meanTotalSerie);
		series1.setSeriesFontSize(14);
		series1.setSeriesColor(Color.valueOf("lightblue"));
		cp.getSeriesValues().add(series1);
		
		ChartSeries series2 = new ChartSeries();
		series2.setSeriesName("Antwortzeit Backend");
		series2.setSeriesValues(meanBackendSerie);
		series2.setSeriesFontSize(14);
		series2.setSeriesColor(Color.valueOf("orange"));
		cp.getSeriesValues().add(series2);
		
		ChartSeries series3 = new ChartSeries();
		series3.setSeriesName("Anzahl Backendcalls");
		series3.setSeriesValues(meanBackcallSerie);
		series3.setSeriesFontSize(14);
		series3.setSeriesColor(Color.valueOf("green"));
		cp.getSeriesValues().add(series3);

		cp.setHeight(666);
		cp.setWidth(1180);
		cp.setTitle(prefix + "\"" + meshtraceInTitle + "\"" + fromRelTo);
		cp.setTitleFont(ChartFont.font(20, FontWeight.findByName("bold")));
		cp.setLegendSide(Side.valueOf("right".toUpperCase()));
		cp.setLegendFont(ChartFont.font(14, FontWeight.findByName("normal")));
		cp.setCategoryGap(this.getCategoryGap());
		cp.setPlotAreaColor(Color.valueOf("rgb(255, 255, 255)"));
		cp.setVerticalGridLinesVisible(false);
		cp.setHorizontalGridLinesColor(Color.valueOf("grey"));
		cp.setChartContentPadding(0);

		// Abscissa
		cp.getxAxis().setLabel("Releases (" + testcase + "/" + branch + ")");
		cp.getxAxis().setLabelFont(ChartFont.font(14));
		cp.getxAxis().setTickLabelFont(Font.font(16));
		cp.getxAxis().setTickLabelRotation(270);
		cp.getxAxis().setStartMargin(this.getAbscissaMargin());
		cp.getxAxis().setEndMargin(this.getAbscissaMargin());

		// Ordinate
		cp.getY1Axis().setLabel("Antwortzeit in ms / Anzahl Backend Calls");
		cp.getY1Axis().setValueRange(y1ValRange);
		cp.getY1Axis().setValueStep(step);
		cp.getY1Axis().setLabelFont(ChartFont.font(14));
		cp.getY1Axis().setMinorTickMarksVisible(true);
		cp.getY1Axis().setMinorTickMarkColor(Color.valueOf("black"));
		cp.getY1Axis().setMinorTickCount(Integer.parseInt("5"));
		cp.getY1Axis().setMinorTickLength(Integer.parseInt("5"));

		int seriesCounter = 0;
		for (ChartSeries series : cp.getSeriesValues()) {
			for (int i = 0; i < series.getSeriesValues().size(); i++) {

				String value = series.getSeriesValues().get(i);
				int responseTime = Integer.parseInt(value);

				Text text = new Text(String.valueOf(responseTime));
				text.setRotate(270);
				text.setFill(Color.valueOf("black"));
				text.setFont(Font.font(14));

				StackPane container = new StackPane();
				// Bottom padding is always set to display the bar label above the bars
				int bottomPadding = 50;
				Insets padding = new Insets(0, 0, bottomPadding, 0);

				int seriesNumber = cp.getSeriesValues().size();
				// Two bars per iteration (x value)
				if (seriesNumber == 2) {
					if (seriesCounter == 0) {
						padding = new Insets(0, 22, bottomPadding, 0);
					} else if (seriesCounter == 1) {
						padding = new Insets(0, 0, bottomPadding, 22);
					}
				}
				// Three bars per iteration (x value)
				else if (seriesNumber == 3) {
					if (seriesCounter == 0) {
						padding = new Insets(0, 22 + 10, bottomPadding, 0);
					} else if (seriesCounter == 2) {
						padding = new Insets(0, 0, bottomPadding, 22 + 10);
					}
				}
				// Four bars per iteration (x value)
				else if (seriesNumber == 4) {
					if (seriesCounter == 0) {
						padding = new Insets(0, 22 + 15, bottomPadding, 0);
					} else if (seriesCounter == 1) {
						padding = new Insets(0, 22 - 7, bottomPadding, 0);
					} else if (seriesCounter == 2) {
						padding = new Insets(0, 0, bottomPadding, 22 - 7);
					} else if (seriesCounter == 3) {
						padding = new Insets(0, 0, bottomPadding, 22 + 15);
					}
				}
				container.setPadding(padding);

				int barOffset = 0;
				cp.getChartMarkers().add(ChartMarker.marker(container, new Shape[] { text }, DataPoint.point(cp.getxAxis().getCategories().get(i), responseTime + barOffset)));
			}
			seriesCounter++;
		}

//		this.outputFile = "output/SingleChart" + "_" + this.categorySize + ".png";
		this.outputFile = "output/BarChart.png";
		final ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin(chartType, outputFile, cp);
		chartPlugin.run();
		if (chartPlugin.isSuccess()) {
			MLogger.getInstance().log("Chart creation finished successful ==> " + outputFile);
		} else {
			MLogger.getInstance().log("Chart creation NOT finished properly ==> " + this.outputFile);
		}
	}

	// Steers the bar size
	private int getCategoryGap() {
		if (iterations.size() == 1) {
			return 906;
		} else if (iterations.size() == 2) {
			return 433;
		} else if (iterations.size() == 3) {
			return 275;
		} else if (iterations.size() == 4) {
			return 196;
		} else if (iterations.size() == 5) {
			return 149;
		} else if (iterations.size() == 6) {
			return 118;
		} else if (iterations.size() == 7) {
			return 95;
		} else if (iterations.size() == 8) {
			return 78;
		} else if (iterations.size() == 9) {
			return 65;
		} else if (iterations.size() == 10) {
			return 55;
		} else if (iterations.size() == 11) {
			return 46;
		} else if (iterations.size() == 12) {
			return 39;
		} else if (iterations.size() == 13) {
			return 33;
		} else if (iterations.size() == 14) {
			return 28;
		}
		return 23;
	}

	// Steers the category positions
	private int getAbscissaMargin() {
		if (iterations.size() == 1) {
			return 480;
		} else if (iterations.size() == 2) {
			return 300;
		} else if (iterations.size() == 3) {
			return 200;
		} else if (iterations.size() == 4) {
			return 150;
		} else if (iterations.size() == 5) {
			return 125;
		} else if (iterations.size() == 6) {
			return 110;
		} else if (iterations.size() == 7) {
			return 100;
		} else if (iterations.size() == 8) {
			return 90;
		} else if (iterations.size() == 9) {
			return 80;
		} else if (iterations.size() == 10) {
			return 70;
		} else if (iterations.size() == 11) {
			return 60;
		} else if (iterations.size() == 12) {
			return 50;
		}  else if (iterations.size() == 13) {
			return 40;
		}  else if (iterations.size() == 14) {
			return 30;
		} 
		return 30;
	}

}
