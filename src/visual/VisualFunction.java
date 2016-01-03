package visual;

import java.awt.Color;

public class VisualFunction {
	public static Color combineColorsRGB(Color col1, Color col2, float percent) {
		int r = (int) (col1.getRed() * (1.0f - percent) + col2.getRed() * percent);
		int g = (int) (col1.getGreen() * (1.0f - percent) + col2.getGreen() * percent);
		int b = (int) (col1.getBlue() * (1.0f - percent) + col2.getBlue() * percent);
		return new Color(r,g,b);
	}
	public static Color combineColorsHue(Color col1, Color col2, float percent) {
		float[] hsbValues1 = new float[3];
		float[] hsbValues2 = new float[3];
		
		Color.RGBtoHSB(col1.getRed(),col1.getGreen(),col1.getBlue(),hsbValues1);
		Color.RGBtoHSB(col2.getRed(),col2.getGreen(),col2.getBlue(),hsbValues2);
		float hue = hsbValues1[0]*(1.0f - percent) + hsbValues2[0] * percent;
		
		return Color.getHSBColor(hue, hsbValues2[1], hsbValues1[2]);
	}
	
}
