package galactic;

import structural.Sector;
import visual.GalaxyDrawer;

public abstract class Galaxy {
	public int maxRadius, maxTheta;
	public double // TODO: Document the variables that don't make sense
	galaxyMass, galaxyAge;

	double tGalaxy, rGalaxy;
	
	/*hiResDensity stores more data in order to display a higher quality data that is
	 *geometrically identical. density stores data with a relative (logorithmic) value
	 *representative of the number of stars in a giving region. This value contains
	 *18,000 values no matter the galaxy, which is representative of the number of sectors
	 *in the galaxy.
	 */
	public double hiResDensity[][], density[][] = new double[180][100];

	Sector sector[][] = null;

	public String id;
	String[] packedDensities = new String[18000], packedData;

	public Galaxy() {
	}

	public abstract void initiateGalaxy();

	public abstract void createSectors();

	public abstract void display();

	public void displayDensities() {
		double ionizedGas[][] = new double[sector.length][sector[0].length];

		for (int t = 0; t < sector.length; t++)
			for (int r = 0; r < sector[0].length; r++) {
				ionizedGas[t][r] = sector[t][r].ionizedMatter;
				if (ionizedGas[t][r] > 1)
					ionizedGas[t][r] = 1;
			}
		// new GalaxyDrawer(universal.Function.arrayToCartesian(hiResDensity,
		// 750, maxTheta, maxRadius),
		// universal.Function.arrayToCartesian(ionizedGas, 750, 180, 100));
		// TODO: What does 750 mean
		new GalaxyDrawer(universal.Function.arrayToCartesian(density, 750, 180, 100),
				universal.Function.arrayToCartesian(ionizedGas, 750, 180, 100));
	}

	public abstract double calcMass();

	public double calcNumberStars() {
		return galaxyMass - 1;
	}

	public abstract double calcGalaxyAge();

	// TODO: Remove Magic numbers
	public int calcMaxRadius() {
		return 500;
	}

	public int calcMaxTheta() {
		return 1440;
	}

	public double calcActualRadius() {
		return galaxyMass * 0.5;
	}

	public abstract String getID(int id);
	

	public void packData(){
		//densities:
		for (int t = 0; t < 180; t++)
			for (int r = 0; r < 100; r++){
				packedDensities[t * r + r] = density[t][r] + "";
				if (sector[t][r].loadedMass) 
					packedDensities[t * r + r] = new StringBuilder().append(packedDensities[t * r + r]).append("L").toString();
			}
	}
}
