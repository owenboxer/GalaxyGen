package galactic;

import visual.GalaxyDrawer;

public abstract class Galaxy{
	public int maxRadius, maxTheta;
	public double galaxyMass, numberStars, galaxyAge, radius1, radius2, meanRadius, meanDensity,
		actualRadius, tGalaxy, rGalaxy;
	public String id;
	public double hiResDensity[][], density[][] = new double[180][100];

	structural.Sector sector[][] = null;

	public Galaxy(){
	}

	public abstract void initiateGalaxy();
	public abstract void createSectors();

	public abstract void display();

	public void displayDensities(){
		double ionizedGas[][] = new double[sector.length][sector[0].length];

		for (int t = 0; t < sector.length; t++)
			for (int r = 0; r < sector[0].length; r++){
				ionizedGas[t][r] = sector[t][r].ionizedMatter;
				if (ionizedGas[t][r] > 1) ionizedGas[t][r] = 1;
			}
		//new GalaxyDrawer(universal.Function.arrayToCartesian(hiResDensity, 750, maxTheta, maxRadius),
				//universal.Function.arrayToCartesian(ionizedGas, 750, 180, 100));
		new GalaxyDrawer(universal.Function.arrayToCartesian(density, 750, 180, 100),
				universal.Function.arrayToCartesian(ionizedGas, 750, 180, 100));
	}

	public abstract double calcMass();
	public double calcNumberStars(){
		return galaxyMass - 1;
	}
	public abstract double calcGalaxyAge();
	public double calcMeanRadius(){
		double volume = Math.pow(10, galaxyMass) / Math.pow(10, meanDensity);
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
		double mass = (galaxyMass - 4) / .8;
		double density = 10 - universal.Function.exponentialFunction(.9, mass);
		density = (density / 2) - 4;
		return density;
	}
	public double calcActualRadius(){
		return galaxyMass * 0.5;
	}

	public abstract String getID(int id);
}
