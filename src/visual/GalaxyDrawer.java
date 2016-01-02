package visual;

import java.awt.*;
//import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GalaxyDrawer {
	public BufferedImage image;
	final int rectWidth = 2;
	final int rectHeight = 2;
	
	
	public GalaxyDrawer(int[][] density) {
		image = new BufferedImage(density.length * rectWidth,density[0].length * rectHeight,
				BufferedImage.TYPE_INT_RGB);
		drawGalaxy(image,density);
		Window window = new Window(image);
	}
	
	private void drawGalaxy(BufferedImage image, int[][] density) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		for(int xx = 0; xx < density.length; xx++) {
			for(int yy = 0; yy < density[xx].length; yy++) {
				g.setColor(new Color(255 * (int)density[xx][yy]/10,
						             255 * (int)density[xx][yy]/10,
						             255 * (int)density[xx][yy]/10));
				g.fillRect(xx * rectWidth, yy * rectHeight, rectWidth, rectHeight);
				//g.setColor(Color.BLUE);
				//g.drawString(Integer.toString(density[xx][yy]), xx * rectWidth, (yy + 1) * rectHeight);
				
			}
		}
	}
}
