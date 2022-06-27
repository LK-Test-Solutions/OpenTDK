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

import java.util.ArrayList;
import java.util.List;

import com.kostikiadis.charts.Legend;
import com.kostikiadis.charts.MultiAxisLineChart;

import javafx.scene.chart.Axis;

public class CustomLineChart<X, Y> extends MultiAxisLineChart<X, Y> {

	private final List<ChartMarker> chartMarkers = new ArrayList<>();

	public CustomLineChart(Axis<X> xAxis, Axis<Y> y1Axis, Axis<Y> y2Axis) {
		super(xAxis, y1Axis, y2Axis);
	}

	public void addMarker(ChartMarker marker) {
		if (marker.getContainer() == null) {
			super.getPlotChildren().add(marker.getMarker());
		} else {
			super.getPlotChildren().add(marker.getContainer());
		}
		this.chartMarkers.add(marker);
	}

	public void setLegend(Legend legend) {
		super.setLegend(legend);
	}

	@Override
	protected void layoutPlotChildren() {
		super.layoutPlotChildren();
		CustomChartHelper<X,Y> helper = new CustomChartHelper<>();
		helper.setMarkers(chartMarkers, getXAxis(), getY1Axis(), getY2Axis());
	}
}
