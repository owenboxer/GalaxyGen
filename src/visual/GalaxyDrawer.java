package visual;

import java.awt.*;
//import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GalaxyDrawer {
	public static BufferedImage image;
	final int rectWidth = 1;
	final int rectHeight = 1;
	public static Window window = null;

	public static String message = "";// DEBUG/UTILITY ONLY


	public GalaxyDrawer(double[][] density, double[][] ionizedGas) {
		image = new BufferedImage(density.length * rectWidth,density[0].length * rectHeight,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage background = new BufferedImage(image.getWidth(),image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		BufferedImage fogImage1 = ImageHandler.loadImage("/images/Fog1.png");
		BufferedImage fogImage2 = ImageHandler.loadImage("/images/GalaxyCluster1.png");
		
		drawBackground(background, fogImage2);
		//drawGalaxy(image, density, fogImage1);
		drawGalaxyOLD(image, density, ionizedGas);
		Graphics2D g = (Graphics2D)background.getGraphics();
		g.drawImage(image, 0, 0,null);
		if (window == null) // When restarting, you don't want to make a new window!
			window = new Window(background);
		else
			window.setImage(background);
	}


	private void drawGalaxy(BufferedImage image, double[][] density, BufferedImage fog) {
		Graphics2D g = (Graphics2D)image.getGraphics();
		drawFogLayer(image, fog,1f, g);
		cutColorGalaxy(image, density, g);
	}

	private void cutColorGalaxy(BufferedImage image, double[][] density, Graphics2D g) {
		// Color stuff
		
		// These variables exist to prevent sharp edges when going from red to blue
		float hue1 = (float)Math.random();
		float hue2;
		if (hue1 > 0.5) {
			hue2 = 0.5f + 0.5f * (float)Math.random();
		}else {
			hue2 = 0.5f * (float)Math.random();
		}
		
		Color col1 = Color.getHSBColor(hue1, 1, 1);//Color.YELLOW;//new Color(250,255,189);// yellowish
		Color col2 = Color.getHSBColor(hue2, 1, 1);//Color.BLUE;//new Color(166,220,255);// blueish
		System.out.println("HUE1: " + hue1 + ", HUE2: " + hue2);
		float distance,
			  maxDist = (float)image.getWidth() / 2;// TEMPORARY. Just a non-official way to define the radius.


		for(int xx = 0; xx < density.length; xx++)
			for(int yy = 0; yy < density[xx].length; yy++) {
				distance = (float) Math.sqrt(Math.pow((xx * rectWidth) - maxDist * rectWidth, 2)
											+ Math.pow((yy * rectHeight) - maxDist* rectHeight, 2));
				if (distance > maxDist) distance = maxDist;
				
				int[] rgbArray = colorify(new Color(image.getRGB(xx,yy)), (float)Math.pow(distance,1.2) / maxDist,
						col1, col2);
				// Just the RGB array, without alpha.

				int alpha =  (int) (255 * ((1.0f - (distance / maxDist)) * 
						((float)density[xx][yy] - 1.0f) / 10.0f));
				
				// THE FUNCTION BELOW makes the alpha distribution better
				alpha = (int) (255f * (1f - Math.pow((float)alpha/255f - 1, 6)));
				
				int rgb = (alpha << 24) | (rgbArray[0] << 16) | (rgbArray[1] << 8) | rgbArray[2];
				image.setRGB(xx,yy,rgb);
			}
	}

	private void drawGalaxyOLD(BufferedImage image, double[][] density, double[][] ionizedGas) {
		/// FOR DEBUGING AND EARLY DEVELOPMENT.

		Graphics2D g = (Graphics2D) image.getGraphics();
		for(int xx = 0; xx < density.length; xx++) {
			for(int yy = 0; yy < density[xx].length; yy++) {
				g.setColor(Color.getHSBColor((float)0.93, (float)ionizedGas[xx][yy], (float)density[xx][yy] / 10));
				g.fillRect(xx * rectWidth, yy * rectHeight, rectWidth, rectHeight);
				//g.setColor(Color.BLUE);
				//g.drawString(Integer.toString(density[xx][yy]), xx * rectWidth, (yy + 1) * rectHeight);
				
			}
		}
	}
	
	private void drawFogLayer(BufferedImage img, BufferedImage fog,float alpha, Graphics2D g) {

		int w = img.getWidth() / fog.getWidth() + 1;
		int h = img.getHeight() / fog.getHeight() + 1;
		int width = fog.getWidth();
		int height = fog.getHeight();
		
		// Makes fog placement random, and as a result the Galaxies more random.
		int xOffset = (int)(width * Math.random());
		int yOffset = (int)(height * Math.random());
		
		g.setComposite(AlphaComposite.SrcOver.derive(alpha));
		for(int xx = 0; xx <= w; xx++)
			for(int yy = 0; yy <= h; yy++) {
				g.drawImage(fog, xx * width - xOffset, yy * height - yOffset, width, height, null);
			}
	}
	
	private void drawBackground(BufferedImage img, BufferedImage fog) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		float alpha = 0.0f;
		drawFogLayer(img, fog,1f - alpha, g);
		Color col = Color.BLACK;
		int rgb = (0 << 150) | (col.getRed() << 16) | (col.getGreen() << 8) | col.getBlue();
		col = new Color(rgb);
		g.setColor(col);
		
        g.setComposite(AlphaComposite.SrcOver.derive(alpha));
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		//g.dispose();
		 
	}
	
	private int[] colorify(Color original,float colPercent,
			Color col1, Color col2) {
		//Used to merge two colors onto an image color.
		
		// colPercent defines the separation btwn the colors.
		float originalPercent = 0.6f;// %  Combining merged col1+col2 and the image color
		
		if (colPercent > 1.0f) colPercent = 1.0f;
		Color actualCol = VisualFunction.combineColorsHue(original, 
							VisualFunction.combineColorsRGB(col1, col2, colPercent),
							originalPercent);
		return new int[] {actualCol.getRed(), actualCol.getGreen(),actualCol.getBlue()};
		
		
	}

	
}
