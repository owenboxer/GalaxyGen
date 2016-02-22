package utility.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import visual.Window;
/**Grapher.java
 * Debugging/logging utility where you input data and it graphs it visually.
 *
 * Usage:
 * 	Values are self explanatory except autoAxis
 * 	If autoAxis is enabled, the graph automatically sets the domain and range
 * 	of itself based on the coordinates provided.
 * 	THUS:
 * 	If autoAxis is true, you don't need to worry about setting
 * 	the startX, startY, endX or endY variables ever.
 */
public class Grapher {
	public boolean autoAxis = true;// Automatically sets domain + range
	private double startX;
	private double startY;
	private double endX;
	private double endY;

	// Refference Lines
	private double lineIncrementX;
	private double lineIncrementY;

	private LinkedList<double[]> list = new LinkedList<double[]>();

	public Grapher(double startX, double startY, double endX, double endY, boolean autoAxis) {
		// IF AUTOAXIS IS TRUE, STARTX,Y AND ENDX,Y ARE ARBITRARY
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.autoAxis = autoAxis;
		// Too lazy to add these into the constructor
		lineIncrementX = 10;
		lineIncrementX = 10;
	}

	public void setRefferenceLineIncrement(double incrementX, double incrementY) {
		lineIncrementX = incrementX;
		lineIncrementY = incrementY;
	}

	public void addData(double x, double y) {
		list.add(new double[] {x,y});
		if (autoAxis) setAxisAuto();
	}

	public void clearData() {
		list.clear();
	}

	private void setAxisAuto() {
		// Sets startX,Y and endX,Y based on data such that the graph perfectly
		// fits.
		// To be used only if autoAxis is true.
		if (list.size() != 0) {
			double minimumX = list.get(0)[0];
			double minimumY = list.get(0)[0];
			double maximumX = list.get(0)[1];
			double maximumY = list.get(0)[1];
			for(int i = 0; i < list.size(); i++) {
				double tempX = list.get(0)[0];
				double tempY = list.get(0)[1];
				if (tempX < minimumX) minimumX = tempX;
				if (tempX > maximumX) maximumX = tempX;
				if (tempY < minimumY) minimumY = tempY;
				if (tempY > maximumY) maximumY = tempY;
			}
			startX = minimumX;
			startY = minimumY;
			endX = maximumX;
			endY = maximumY;
		} else {
			System.err.println("Data should have at least one element before "
					+ "setting axis automatically!");
		}
	}

	// Draws onto image and makes a window based on the data.
	public void makeDataWindow(int width, int height) {
		int offset = 64;//Offset from graph to make room for numbers
		double conversionFactorWidth =  (double)(width - 2*offset) / (endX - startX);
		double conversionFactorHeight = (double)(height - 2*offset) / (endY - startY);
		BufferedImage img = new BufferedImage(width, //(int)(endX - startX) + 2*offset
				height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();

		// Fill bg to white
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());

		g.setColor(Color.BLACK);
		// X Axis
		g.drawLine(offset, img.getHeight() - offset, 
				img.getWidth() - offset, img.getHeight() - offset);
		// Y Axis
		g.drawLine(offset, offset, 
				offset, img.getHeight() - offset);

		//X Axis lines
		int tempStartY = img.getHeight() - offset - 8;
		int tempEndY = tempStartY + 16;
		for(double i = startX; i < endX; i+= lineIncrementX) {
			int tempX = offset + (int)((i - startX) * conversionFactorWidth);
			g.drawLine(tempX, 
					tempStartY,
					tempX, 
					tempEndY);
			g.drawString(Double.toString(i), tempX, tempEndY + 8);
		}
		//Y Axis lines
		int tempStartX = offset - 8;
		int tempEndX = tempStartX + 16;
		for(double i = startY; i < endY; i+= lineIncrementY) {
			int tempY = offset + (int)((endY - i) * conversionFactorHeight);
			g.drawLine(tempStartX,
					tempY,
					tempEndX,
					tempY);
			g.drawString(Double.toString(i), tempStartX - 8, tempY);
		}
		
		//The points themselves
		int radius = 4;
		for(int i = 0; i < list.size(); i++) {
			double[] point = list.get(i);
			int drawX = offset + (int)(point[0] * conversionFactorWidth) - radius;
			int drawY = offset + (int) (point[1] * conversionFactorHeight) - radius;
			g.drawOval(drawX,
					drawY,
					radius*2, radius*2);
			g.drawString("(" + Double.toString(point[0]) 
				+ "," + Double.toString(point[1]) + "", drawX + radius + 1, drawY);
		}

		// And finally the window!
		new Window(img);
	}
}
