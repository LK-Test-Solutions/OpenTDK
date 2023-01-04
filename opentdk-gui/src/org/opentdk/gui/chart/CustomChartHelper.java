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

import java.util.List;
import java.util.logging.Level;

import com.kostikiadis.charts.MultiAxisChart.Data;
import org.opentdk.api.logger.MLogger;

import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class CustomChartHelper<X, Y> {

	@SuppressWarnings("unchecked")
	void setMarkers(List<ChartMarker> chartMarkers, Axis<X> xAxis, Axis<Y> y1Axis, Axis<Y> y2Axis) {
			
		for (ChartMarker marker : chartMarkers) {
			String xsValue = marker.getDataPoint().getXsValue();
			Number xnValue = marker.getDataPoint().getXnValue();
			String ysValue = marker.getDataPoint().getYsValue();
			Number ynValue = marker.getDataPoint().getYnValue();
			Data<X, Y> point = new Data<>();

			if (xAxis instanceof CategoryAxis) {
				if (xsValue == null) {
					MLogger.getInstance().log(Level.SEVERE, "xAxis is of type category but marker xValue is not set. Marker gets not added to the chart.", getClass().getSimpleName(), "addChartMarkers");
					continue;
				} else {
					point.setXValue((X) xsValue);
				}
			} else if (xAxis instanceof NumberAxis) {
				if (xnValue.intValue() == -1) {
					MLogger.getInstance().log(Level.SEVERE, "xAxis is of type number but marker xValue is not set. Marker gets not added to the chart.", getClass().getSimpleName(), "addChartMarkers");
					continue;
				} else {
					point.setXValue((X) xnValue);
				}
			}

			if (y1Axis instanceof CategoryAxis) {
				if (ysValue == null) {
					MLogger.getInstance().log(Level.SEVERE, "y1Axis is of type category but marker yValue is not set. Marker gets not added to the chart.", getClass().getSimpleName(), "addChartMarkers");
					continue;
				} else {
					point.setYValue((Y) ysValue);
				}
			} else if (y1Axis instanceof NumberAxis) {
				if (ynValue.intValue() == -1) {
					MLogger.getInstance().log(Level.SEVERE, "y1Axis is of type number but marker yValue is not set. Marker gets not added to the chart.", getClass().getSimpleName(), "addChartMarkers");
					continue;
				} else {
					point.setYValue((Y) ynValue);
				}
			}
			point.setExtraValue(marker.getBelongingAxis());
			
			double xOrientation = xAxis.getDisplayPosition(point.getXValue());
			double yOrientation = Double.NaN;
			if (marker.getBelongingAxis() == 1) {
				yOrientation = y2Axis.getDisplayPosition(point.getYValue());
			} else {
				yOrientation = y1Axis.getDisplayPosition(point.getYValue());
			}

			for (Shape child : marker.getChildren()) {
//				Arc arc = null;
//				Circle circle = null;
//				CubicCurve cubicCurve = null;
//				Ellipse ellipse = null;
//				Line line = null; 
//				Path path = null; 
//				Polygon polygon = null;
//				Polyline polyline = null;
//				QuadCurve curve = null; 
//				Rectangle rectangle = null;
//				SVGPath svgPath = null; 
				if (child instanceof Arc) {

				} else if (child instanceof Circle) {

				} else if (child instanceof Line) {

				} else if (child instanceof Rectangle) {

				} else if (child instanceof Text) {
					Text text = (Text) child;
					if (text.getText().contentEquals("0")) {
						text.setText("");
					}
					text.setX(xOrientation);
					text.setY(yOrientation);
					text.toFront();
				}
				child.setViewOrder(marker.getViewOrder());
			}
			if (marker.getContainer() == null) {
				marker.getMarker().setLayoutX(xOrientation);
				marker.getMarker().setLayoutY(yOrientation);
				marker.getMarker().setViewOrder(marker.getViewOrder());

				if (marker.getMarker() instanceof Rectangle) {
					Rectangle rec = (Rectangle) marker.getMarker();
					rec.setX(rec.getX() - (rec.getWidth() / 4));
					if (rec.getHeight() == 0.0) {
						rec.setHeight(yOrientation);
					}
				} 
			} else {
				marker.getContainer().setLayoutX(xOrientation);
				marker.getContainer().setLayoutY(yOrientation);
				marker.getContainer().setViewOrder(marker.getViewOrder());
			}
		}
	}
}
