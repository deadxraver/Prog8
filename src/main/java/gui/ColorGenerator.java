package gui;

import elements.User;

import java.awt.*;

public class ColorGenerator {
	public static Color generateColor(String username) {
		int hash = Math.abs(username.hashCode());
		int r = hash % 256;
		int g = (r + hash) % 256;
		int b = (r + g + hash) % 256;
		return new Color(r, g, b);
	}
	public static Color getContrastColor(Color color) {
		double y = (double) (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
}
