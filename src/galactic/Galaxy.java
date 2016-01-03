package galactic;

import visual.GalaxyDrawer;

public abstract class Galaxy{
	public boolean satellite;
	public int maxradius, maxtheta;
	public double galaxymass, numberstars, galaxyage, radius1, radius2, meanradius, meandensity;
	public String id;

	structural.Sector sector[][] = null;

	public Galaxy(boolean satellite){
		this.satellite = satellite;
	}

	public abstract void initiateGalaxy();
	public abstract void createSectors();

	public abstract void display();

	public void displayDensities(){
		int diameter = 750, radius = diameter / 2;
		int density[][] = new int[(int) diameter][(int) diameter];
		for (int i = 0; i < diameter; i++){
			for (int j = 0; j < diameter; j++){
				// Moves galaxy to center
				int x = (i - radius), y = (j - radius);
				double coord[] = new double[2];
				coord = universal.Function.cartesianToPolar(x, y);
				double polar = coord[0], radial = coord[1];
				polar -= polar % (360.0 / (maxtheta - 1));
				polar = polar / (360.0 / (maxtheta - 1));
				if (radial >= radius) continue;
				radial = maxradius * (radial / radius);
				radial = (int) radial;
				density[i][j] = (int) sector[(int) polar][(int) radial].rawdensity;
			}
		}
		
		/*for (int i = 0; i < diameter; i++){
			for (int j = 0; j < diameter; j++){
				if (density[j][i] == 0) universal.Main.log("  ");
				else universal.Main.log(density[j][i] + " ");
			}
			universal.Main.log("\n");
		}*/
		
		new GalaxyDrawer(density);
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

	public abstract String getID(int id);

	public static double lyToGalacticUnits(int ly){
		return universal.Universe.lyToUniversalUnits(ly);
	}
}
