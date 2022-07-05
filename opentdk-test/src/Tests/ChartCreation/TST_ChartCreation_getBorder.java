package Tests.ChartCreation;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class TST_ChartCreation_getBorder {

	public static void main(String[] args) {
		BorderStroke s = new BorderStroke(Color.BLACK, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY);
		
		Border border = new Border(s);
		BorderStroke stroke = border.getStrokes().get(0);
		System.out.println("Top stroke: " + stroke.getTopStroke().toString());
		System.out.println("Right stroke: " + stroke.getRightStroke().toString());
		System.out.println("Bottom stroke: " + stroke.getBottomStroke().toString());
		System.out.println("Left stroke: " + stroke.getLeftStroke().toString());
		
		System.out.println("Top style: " + stroke.getTopStyle().toString());
		System.out.println("Right style: " + stroke.getRightStyle().toString());
		System.out.println("Bottom style: " + stroke.getBottomStyle().toString());
		System.out.println("Left style: " + stroke.getLeftStyle().toString());
		
		System.out.println("CornerRadii: " + stroke.getRadii().toString());
		
		System.out.println("Top width: " + stroke.getWidths().getTop());
		System.out.println("Top width: " + stroke.getWidths().getRight());
		System.out.println("Top width: " + stroke.getWidths().getBottom());
		System.out.println("Top width: " + stroke.getWidths().getLeft());
		
		System.out.println("Insets: " + stroke.getInsets().toString());
	}

}
