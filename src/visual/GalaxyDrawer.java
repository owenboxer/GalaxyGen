package visual;

import java.awt.*;
//import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GalaxyDrawer {
	public BufferedImage image;
	final int rectWidth = 1;
	final int rectHeight = 1;
	public static Window window = null;
	
	
	public GalaxyDrawer(int[][] density) {
		image = new BufferedImage(density.length * rectWidth,density[0].length * rectHeight,
				BufferedImage.TYPE_INT_RGB);
		drawGalaxy(image,density);
		
		if (window == null)
			window = new Window(image);
		else
			window.setImage(image);
	}

	private void drawGalaxy(BufferedImage image, int[][] density) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		float hue = (new Random()).nextFloat();
		for(int xx = 0; xx < density.length; xx++) {
			for(int yy = 0; yy < density[xx].length; yy++) {
				g.setColor(Color.getHSBColor(hue, 1, (float)density[xx][yy]/10));
						/*new Color(255 * (int)density[xx][yy]/10,
						             255 * (int)density[xx][yy]/10,
						             255 * (int)density[xx][yy]/10));*/
				g.fillRect(xx * rectWidth, yy * rectHeight, rectWidth, rectHeight);
				//g.setColor(Color.BLUE);
				//g.drawString(Integer.toString(density[xx][yy]), xx * rectWidth, (yy + 1) * rectHeight);
				
			}
		}
	}
}
