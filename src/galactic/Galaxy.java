package galactic;

import visual.GalaxyDrawer;

public abstract class Galaxy{
	public boolean satellite;
	public int maxradius, maxtheta, tgalaxy, rgalaxy;
	public double galaxymass, numberstars, galaxyage, radius1, radius2, meanradius, meandensity,
		actualradius;
	public String id;

	structural.Sector sector[][] = null;

	public Galaxy(boolean satellite){
		this.satellite = satellite;
	}

	public abstract void initiateGalaxy();
	public abstract void createSectors();

	public abstract void display();

	public void displayDensities(){
		double density[][] = new double[maxtheta][maxradius];
		for (int i = 0; i < maxtheta; i++)
			for (int j = 0; j < maxradius; j++)
				density[i][j] = sector[i][j].rawdensity;
		density = convertToCartesian(density, 750);
		
		/*for (int i = 0; i < density.length; i++){
			for (int j = 0; j < density.length; j++){
				if (density[j][i] == 0) universal.Main.log("  ");
				else universal.Main.log((int) density[j][i] + " ");
			}
			universal.Main.log("\n");
		}*/
		
		new GalaxyDrawer(density);
	}

	public double[][] convertToCartesian(double[][] polar, int resolution){
		int radius = resolution / 2;
		double cartesian[][] = new double[resolution][resolution];
		int x, y;
		double coord[] = new double[2], theta, radial;
		for (int i = 0; i < resolution; i++)
			for (int j = 0; j < resolution; j++){
				// Moves galaxy to center
				x = (i - radius);
				y = (j - radius);
				coord = universal.Function.cartesianToPolar(x, y);
				theta = coord[0];
				radial = coord[1];
				theta -= theta % (360.0 / (maxtheta - 1));
				theta = theta / (360.0 / (maxtheta - 1));
				if (radial >= radius) continue;
				radial = maxradius * (radial / radius);
				cartesian[j][i] = polar[(int) theta][(int) radial];
			}
		
		return cartesian;
	}
	public double[][] convertToPolar(double[][] cartesian){
		double polar[][] = new double[maxtheta][maxradius];
		double coord[] = new double[2], x, y;
		for (int i = 0; i < maxradius; i++)
			for (double j = 0; j < maxtheta; j++){
				x = maxtheta;
				coord = universal.Function.polarToCartesian(j / (x / 360.0), i);
				x = coord[0];
				y = coord[1];
				x = (0.5 * cartesian.length) * (x / maxradius);
				y = (0.5 * cartesian.length) * (y / maxradius);
				x += (0.5 * cartesian.length);
				y += (0.5 * cartesian.length);
				polar[(int) j][i] = cartesian[(int) x][(int) y];
			}
		return polar;
	}
	
	public abstract double calcMass();
	public double calcNumberStars(){
		return galaxymass - 1;
	}
	public abstract double calcGalaxyAge();
	public double calcMeanRadius(){
		double volume = Math.pow(10, galaxymass) / Math.pow(10, meandensity);
		double radius = Math.sqrt(volume) / Math.PI;
		radius = Math.log10(radius);
		return radius;
	}
	public abstract double calcRadius1();
	public abstract double calcRadius2();
	public int calcMaxRadius(){
		return 500;
	}
	public int calcMaxTheta(){
		return 1440;
	}
	public double calcMeanDensity(){
		double mass = (galaxymass - 4) / .8;
		double density = 10 - universal.Function.exponentialFunction(.9, mass);
		density = (density / 2) - 4;
		return density;
	}
	public double calcActualRadius(){
		return galaxymass * 0.5;
	}

	public abstract String getID(int id);

	public static double lyToGalacticUnits(int ly){
		return universal.Universe.lyToUniversalUnits(ly);
	}
}
