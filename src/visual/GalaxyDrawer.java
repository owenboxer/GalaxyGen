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
	private float angle = 0;

	public static String message = "";// DEBUG/UTILITY ONLY


	public GalaxyDrawer(double[][] density) {
		image = new BufferedImage(density.length * rectWidth,density[0].length * rectHeight,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage fogImage = ImageHandler.loadImage("/Fog1.png");
		BufferedImage background = new BufferedImage(image.getWidth(),image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		drawBackground(background);
		drawGalaxy(image, density, fogImage);
		//drawGalaxyOLD(image, density);
		Graphics2D g = (Graphics2D)background.getGraphics();
		g.drawImage(image, 0, 0,null);
		if (window == null) // When restarting, you don't want to make a new window!
			window = new Window(background);
		else
			window.setImage(background);
	}


	private void drawGalaxy(BufferedImage image, double[][] density, BufferedImage fog) {
		Graphics2D g = (Graphics2D)image.getGraphics();
		drawFogLayer(image, fog, g);
		cutColorGalaxy(image, density, g);
	}

	private void cutColorGalaxy(BufferedImage image, double[][] density, Graphics2D g) {
		// Color stuff
		Color col1 = Color.getHSBColor((float)Math.random(), 1, 1);//Color.YELLOW;//new Color(250,255,189);// yellowish
		Color col2 = Color.getHSBColor((float)Math.random(), 1, 1);//Color.BLUE;//new Color(166,220,255);// blueish
		float distance,
			  maxDist = (float)image.getWidth() / 2;// TEMPORARY. Just a non-official way to define the radius.

		// TEMPORARY FIX
		int offsetx = 0, 
			offsety = 0;

		for(int xx = 0; xx < density.length; xx++)
			for(int yy = 0; yy < density[xx].length; yy++) {
				xx -= offsetx;// Translation
				yy -= offsety;// Translation
				distance = (float) Math.sqrt(Math.pow((xx * rectWidth) - maxDist * rectWidth, 2)
											+ Math.pow((yy * rectHeight) - maxDist* rectHeight, 2));
				xx += offsetx;// UNTranslation
				yy += offsety;// UNTranslation
				if (distance > maxDist) distance = maxDist;
				
				int[] rgbArray = colorify(new Color(image.getRGB(xx,yy)), (float)Math.pow(distance,1.2) / maxDist,
						col1, col2);
				// Just the RGB array, without alpha.

				int alpha =  (int) (255 * ((1.0f - (distance / maxDist)) * 
						((float)density[xx][yy] - 1.0f) / 10.0f));
				int rgb = (alpha << 24) | (rgbArray[0] << 16) | (rgbArray[1] << 8) | rgbArray[2];
				image.setRGB(xx,yy,rgb);
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
	
	private void drawBackground(BufferedImage img) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.RED);
		g.drawRect(0, 0, img.getWidth(), img.getHeight());
		g.dispose();
		 
	}
	
	private int[] colorify(Color original,float colPercent,
			Color col1, Color col2) {
		// colPercent defines the separation btwn the colors.
		float originalPercent = 0.6f;// %  Combining merged col1+col2 and the image color
		
		if (colPercent > 1.0f) colPercent = 1.0f;
		Color actualCol = VisualFunction.combineColorsHue(original, 
							VisualFunction.combineColorsRGB(col1, col2, colPercent),
							originalPercent);
		return new int[] {actualCol.getRed(), actualCol.getGreen(),actualCol.getBlue()};
		
		
	}

	
}
