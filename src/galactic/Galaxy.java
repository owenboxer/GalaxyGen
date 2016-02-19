package galactic;

import visual.GalaxyDrawer;

public abstract class Galaxy{
	public int maxradius, maxtheta;
	public double galaxymass, numberstars, galaxyage, radius1, radius2, meanradius, meandensity,
		actualradius, tgalaxy, rgalaxy;
	public String id;
	public double hiresdensity[][], density[][] = new double[180][100];

	structural.Sector sector[][] = null;

	public Galaxy(){
	}

	public abstract void initiateGalaxy();
	public abstract void createSectors();

	public abstract void display();

	public void displayDensities(){
		//new GalaxyDrawer(universal.Function.arrayToCartesian(hiresdensity, 750, maxtheta, maxradius));
		new GalaxyDrawer(universal.Function.arrayToCartesian(density, 750, 180, 100));
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
}
