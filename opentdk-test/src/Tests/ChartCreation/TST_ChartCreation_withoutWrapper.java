package Tests.ChartCreation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.opentdk.gui.chart.ChartMarker;
import org.opentdk.gui.chart.CustomLineChart;
import org.opentdk.gui.chart.ChartMarker.DataPoint;

import com.kostikiadis.charts.Legend;
import com.kostikiadis.charts.Legend.LegendItem;
import com.kostikiadis.charts.MultiAxisChart.Data;
import com.kostikiadis.charts.MultiAxisChart.Series;
import org.opentdk.api.logger.MLogger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class TST_ChartCreation_withoutWrapper extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage arg0) {

		NumberAxis xAxis = new NumberAxis(1, 31, 1);		
		xAxis.setLabel("Januar 2021");
//		xAxis.setTickLabelRotation(270);
		xAxis.setTickLabelFill(Color.BLACK);
		xAxis.setTickLabelFont(Font.font(14));
//		xAxis.setTickLabelGap(properties.getTickLabelGap());
		xAxis.setTickLabelsVisible(true);
//		xAxis.setTickLength(properties.getTickLength());
		xAxis.setTickMarkVisible(false);
//		xAxis.setBorder(border.getBorder());
		xAxis.setAnimated(false);
//		xAxis.setForceZeroInRange(true);
		
		NumberAxis y1Axis = new NumberAxis(5, 70, 5);
		y1Axis.setLabel("CPU-Auslastung in %");
//		xAxis.setTickLabelRotation(270);
		y1Axis.setTickLabelFill(Color.BLACK);
		y1Axis.setTickLabelFont(Font.font(14));
//		xAxis.setTickLabelGap(properties.getTickLabelGap());
		y1Axis.setTickLabelsVisible(true);
//		xAxis.setTickLength(properties.getTickLength());
		y1Axis.setTickMarkVisible(false);
//		xAxis.setBorder(border.getBorder());
		y1Axis.setAnimated(false);
		
		NumberAxis y2Axis = new NumberAxis(1000, 7000, 1000);
		y2Axis.setLabel("Anzahl User");
//		xAxis.setTickLabelRotation(270);
		y2Axis.setTickLabelFill(Color.BLACK);
		y2Axis.setTickLabelFont(Font.font(14));
//		xAxis.setTickLabelGap(properties.getTickLabelGap());
		y2Axis.setTickLabelsVisible(true);
//		xAxis.setTickLength(properties.getTickLength());
		y2Axis.setTickMarkVisible(false);
//		xAxis.setBorder(border.getBorder());
		y2Axis.setAnimated(false);

		CustomLineChart<Number, Number> chart = new CustomLineChart<>(xAxis, y1Axis, y2Axis);		
		chart.setTitle("CPU-Auslastung und Anzahl User f\u00FCr alle ABS-Blades in der Top Stunde 11 - 12 Uhr");
		chart.setVerticalGridLinesVisible(false);
		chart.setVerticalZeroLineVisible(false);
		chart.setHorizontalGridLinesVisible(true);
		chart.setHorizontalZeroLineVisible(true);
		chart.setTitleSide(Side.TOP);
		chart.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		chart.setLegendSide(Side.TOP);
		chart.setLegendVisible(true);
		
		Legend legend = new Legend();
		ObservableList<LegendItem> items = FXCollections.observableArrayList();
		items.add(new LegendItem("CPU-Auslastung in %", new Rectangle(10, 10, 10, 10)));
		items.add(new LegendItem("Anzahl User", new Rectangle(10, 10, 10, 10)));
		legend.setItems(items);
		chart.setLegend(legend);
		
//		List<Integer> series1Values = new ArrayList<>(Collections.nCopies(31, null));
//		series1Values.set(4, 50);
//		series1Values.set(5, 55)
		Series<Number, Number> userCount = new Series<>();
		userCount.setName("Anzahl User");
		Data<Number, Number> userCount1 = new Data<>(4, 50, CustomLineChart.Y1_AXIS);
		Data<Number, Number> userCount2 = new Data<>(5, 55, CustomLineChart.Y1_AXIS);
//		userCount1.setNode(new Circle(5, Color.STEELBLUE));
//		userCount2.setNode(new Circle(5, Color.STEELBLUE));
		userCount.getData().add(userCount1);
		userCount.getData().add(userCount2);
//		userCount.setNode(new Line());
		
		Series<Number, Number> cpuLoad = new Series<>();
		userCount.setName("CPU Auslastung in %");
		Data<Number, Number> cpuLoad1 = new Data<>(4, 3800, CustomLineChart.Y1_AXIS);
		Data<Number, Number> cpuLoad2 = new Data<>(5, 4000, CustomLineChart.Y1_AXIS);
		userCount.getData().add(cpuLoad1);
		userCount.getData().add(cpuLoad2);

		chart.getData().add(userCount);
		chart.getData().add(cpuLoad);
				
		StackPane pane = new StackPane();
		Text text = new Text("Neujahr");
		text.setRotate(270);
		text.setFill(Color.GREY);
		text.setFont(Font.font(24));
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(50);
		rectangle.setHeight(200);
		rectangle.setFill(Color.GAINSBORO);
		Shape[] children = { text, rectangle };
		chart.addMarker(ChartMarker.marker(pane, children, DataPoint.point(1, 35)));
				
		Scene scene = new Scene(chart, 2230, 831);
		List<String> cssFiles = new ArrayList<>();
		cssFiles.add("conf/chart.css");
		for (String css : cssFiles) {
			File f = new File(css);
			scene.getStylesheets().clear();
			scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		}
		
		int scale = 1;
		SnapshotParameters snapshotParameter = new SnapshotParameters();
		snapshotParameter.setTransform(new Scale(scale, scale));
		WritableImage image = chart.snapshot(snapshotParameter, null);

		ImageView view = new ImageView(image);
		view.setFitHeight(image.getHeight() / scale);
		view.setFitWidth(image.getWidth() / scale);
		view.setPreserveRatio(true);

		try {
			File exportLoc = new File("output/ChartWithoutWrapper.png");
			ImageIO.write(SwingFXUtils.fromFXImage(view.getImage(), null), "png", exportLoc);
		} catch (IOException e1) {
			MLogger.getInstance().log(Level.SEVERE, e1);
		}
		
		// Stop JavaFX Thread
		Platform.exit();
	}

}
