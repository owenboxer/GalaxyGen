package visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UniverseDrawer {
	public static BufferedImage image;
	int rectWidth, rectHeight;
	public static Window window = null;

	public static String message = "";// DEBUG/UTILITY ONLY


	public UniverseDrawer(double[][] density, int resolution) {
		rectWidth = 750 / resolution;
		rectHeight = 750 / resolution;

		image = new BufferedImage(density.length * rectWidth,density[0].length * rectHeight,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage background = new BufferedImage(image.getWidth(),image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		drawUniverse(image, density);
		Graphics2D g = (Graphics2D)background.getGraphics();
		g.drawImage(image, 0, 0,null);
		if (window == null) // When restarting, you don't want to make a new window!
			window = new Window(background);
		else
			window.setImage(background);
	}

	private void drawUniverse(BufferedImage image, double[][] density) {

		Graphics2D g = (Graphics2D) image.getGraphics();
		for(int xx = 0; xx < density.length; xx++) {
			for(int yy = 0; yy < density[xx].length; yy++) {
				g.setColor(Color.getHSBColor((float)density[xx][yy], 1, (float)density[xx][yy]));
				g.fillRect(xx * rectWidth, yy * rectHeight, rectWidth, rectHeight);
			}
		}
	}
}
