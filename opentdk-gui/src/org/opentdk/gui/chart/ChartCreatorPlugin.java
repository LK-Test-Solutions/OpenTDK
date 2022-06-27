/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.gui.chart;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.opentdk.api.logger.MLogger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;

/**
 * This is the main class (entry point) of the chart creation plug in.<br>
 * <br>
 * Usage to trigger the chart creation for one chart:
 * 
 * <pre>
 * ChartProperties properties = new ChartProperties();
 * properties.setTitle("Test");
 * ...
 * ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("LINE", "./output/LineChartMinimum.png", properties);
 * chartPlugin.run();
 * if (chartPlugin.isSuccess() == false) {
 * 	System.out.println("Chart creation finished with error");
 * }
 * </pre>
 * 
 * This will create and export a line chart to the given output location. All properties (like the
 * title) are optional. If nothing gets used an empty charts gets created. <br>
 * <br>
 * Note: To use this class the JavaFX application thread has to be started in the calling class and
 * has to inherit from <code>javafx.application.Application</code> and call
 * <code>Application.launch()</code>.
 * 
 * <pre>
 * public class CallingApplication extends Application {
 * 	public static void main(String[] args) {
 * 		// Start the JavaFX application thread
 * 		launch(args);
 * 	}
 * 
 * 	&#64;Override
 * 	public void start(Stage arg0) throws Exception {
 * 		// Chart creation like above
 * 
 * 		// Stop JavaFX Thread
 * 		Platform.exit();
 * 	}
 *
 * }
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 */
public final class ChartCreatorPlugin {

	private final ChartCreator creator;
	private final String chartType;
	private final String outputLocation;
	private final int sharp;

	private boolean success = false;

	public ChartCreatorPlugin(String chartT, ChartProperties cp) {
		if (chartT == null || chartT.isBlank() || chartT.length() > Short.MAX_VALUE) {
			throw new IllegalArgumentException("Chart type parameter is null, blank or too large");
		}
		chartType = chartT;
		creator = new ChartCreator(cp);
		outputLocation = null;
		sharp = 1;
	}

	public ChartCreatorPlugin(String chartT, String outputLoc, ChartProperties cp) {
		this(chartT, outputLoc, cp, 1);
	}

	public ChartCreatorPlugin(String chartT, String outputLoc, ChartProperties cp, int sharpness) {
		if (chartT == null || chartT.isBlank() || chartT.length() > Short.MAX_VALUE) {
			throw new IllegalArgumentException("Chart type parameter is null, blank or too large");
		}
		if (outputLoc == null || outputLoc.isBlank() || outputLoc.length() > Short.MAX_VALUE) {
			throw new IllegalArgumentException("Output location parameter is null, blank or too large");
		}
		if (sharpness < 1 || sharpness > 100) {
			throw new IllegalArgumentException("Sharpness parameter is out of range. 1 to 100 is allowed.");
		}

		chartType = chartT;
		outputLocation = outputLoc;
		creator = new ChartCreator(cp);
		sharp = sharpness;
	}

	public void run() {
		ChartType cType = ChartType.getChartType(chartType);
		if (cType != ChartType.NONE) {

			creator.createChart(cType);
			new Scene(creator.getChart(), creator.getWidth(), creator.getHeight());

			File out = new File(outputLocation);
			if (!out.getParentFile().exists()) {
				out.getParentFile().mkdir();
			}
			success = exportChart(creator.getChart(), outputLocation, sharp);
			if (success = true) {
				MLogger.getInstance().log(Level.INFO, cType.name() + " chart created and exported to ==> " + out.getPath(), getClass().getSimpleName(), "run");
			} else {
				MLogger.getInstance().log(Level.SEVERE, cType.name() + " chart export failed to ==> " + out.getPath(), getClass().getSimpleName(), "run");
			}
		} else {
			MLogger.getInstance().log(Level.SEVERE, "No chart type could be identified. No chart will be created!", getClass().getSimpleName(), "run");
		}
	}

	public Chart createChart() {
		if (creator == null) {
			MLogger.getInstance().log(Level.SEVERE, "ChartCreatorPlugin has to be initialized first!", getClass().getSimpleName(), "createChart");
			return null;
		}
		ChartType cType = ChartType.getChartType(chartType);
		if (cType != ChartType.NONE) {
			creator.createChart(cType);
			success = true;
			MLogger.getInstance().log(Level.INFO, cType.name() + " chart created successfully", getClass().getSimpleName(), "createChart");
			return creator.getChart();
		} else {
			MLogger.getInstance().log(Level.SEVERE, "No chart type could be identified. No chart will be created!", getClass().getSimpleName(), "createChart");
			return null;
		}
	}

	private boolean exportChart(Chart chart, String exportFile, double scale) {
		boolean ret = false;

		SnapshotParameters snapshotParameter = new SnapshotParameters();
		snapshotParameter.setTransform(new Scale(scale, scale));
		WritableImage image = chart.snapshot(snapshotParameter, null);

		ImageView view = new ImageView(image);
		view.setFitHeight(image.getHeight() / scale);
		view.setFitWidth(image.getWidth() / scale);
		view.setPreserveRatio(true);

		try {
			File exportLoc = new File(exportFile);
			ImageIO.write(SwingFXUtils.fromFXImage(view.getImage(), null), "png", exportLoc);
			ret = true;
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
		return ret;
	}

	public boolean isSuccess() {
		return success;
	}
}
