package Tests.ChartCreation;

import javafx.scene.paint.Color;

public class TST_ChartCreation_getColors {

	public static void main(String[] args) {
		getColorFromString("gainsboro");
		getColorFromString("lightgrey");
		getColorFromString("#D3D3D3");
		getColorFromString("grey");
		getColorFromString("rgb(222,222,222)");
		getColorFromString("0x808080ff");

	}

	private static void getColorFromString(String input) {
		Color color = Color.valueOf(input);
		System.out.println("Input: " + input);
		System.out.println("Hex: " + color);
		System.out.println("RGB: rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")");
		System.out.println("toString(): " + color.toString());
		System.out.println();
	}

}
