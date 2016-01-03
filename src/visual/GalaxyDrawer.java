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
	private float angle = 0;
	
	
	public GalaxyDrawer(double[][] density) {
		image = new BufferedImage(density.length * rectWidth,density[0].length * rectHeight,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage fogImage = ImageHandler.loadImage("/Fog1.png");
		//drawGalaxy(image, density, fogImage);
		drawGalaxyOLD(image, density);
		
		
		if (window == null) // When restarting, you don't want to make a new window!
			window = new Window(image);
		else
			window.setImage(image);
	}


	private void drawGalaxy(BufferedImage image, double[][] density, BufferedImage fog) {
		Graphics2D g = (Graphics2D)image.getGraphics();
		drawFogLayer(image, fog, g);
		cutGalaxy(image, density, g);
	}
	
	private void cutGalaxy(BufferedImage image, double[][] density, Graphics2D g) {
		
		float distance,
			  maxDist = (float)density.length / 2;// TEMPORARY. Just a non-official way to define the radius.
		for(int xx = 0; xx < density.length; xx++)
			for(int yy = 0; yy < density[xx].length; yy++) {
				distance = (float) Math.sqrt(Math.pow((xx - (float)density.length/2), 2)
											+ Math.pow((yy - (float)density[xx].length/2), 2));
				g.setComposite(AlphaComposite.getInstance (AlphaComposite.SRC_OVER));
				if (density[xx][yy] <= 1.0) {
					g.setColor(new Color(0,0,0,1.0f));
				} else {
					
					g.setColor(new Color(0,0,0,1.0f - ((1.0f - (distance / maxDist)) * 
								((float)density[xx][yy] - 1.0f) / 10.0f) ));
				}
				g.fillRect(xx * rectWidth, yy * rectHeight, rectWidth, rectHeight);
			}
	}
	
	private void drawGalaxyOLD(BufferedImage image, double[][] density) {
		/// FOR DEBUGING AND EARLY DEVELOPMENT.

		Graphics2D g = (Graphics2D) image.getGraphics();
		float hue = (new Random()).nextFloat();// Random hue, just for fun.
		for(int xx = 0; xx < density.length; xx++) {
			for(int yy = 0; yy < density[xx].length; yy++) {
				g.setColor(Color.getHSBColor(hue, 1, (float)density[xx][yy]/10));
				g.fillRect(xx * rectWidth, yy * rectHeight, rectWidth, rectHeight);
				//g.setColor(Color.BLUE);
				//g.drawString(Integer.toString(density[xx][yy]), xx * rectWidth, (yy + 1) * rectHeight);
				
			}
		}
	}
	
	private void drawFogLayer(BufferedImage img, BufferedImage fog, Graphics2D g) {

		int w = img.getWidth() / fog.getWidth();
		int h = img.getHeight() / fog.getHeight();
		int width = fog.getWidth();
		int height = fog.getHeight();
		for(int xx = 0; xx <= w; xx++)
			for(int yy = 0; yy <= h; yy++) {
				g.drawImage(fog, xx * width, yy * height, width, height, null);
			}
	}
	
	
}
