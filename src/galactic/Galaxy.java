package galactic;

import structural.Sector;
import util.FileHandler;
import visual.GalaxyDrawer;

public abstract class Galaxy {
	public int maxRadius, maxTheta;
	public double // TODO: Document the variables that don't make sense
	galaxyMass, galaxyAge;

	double tGalaxy, rGalaxy;
	
	/* hiResDensity stores more data in order to display a higher quality data that is
	 * geometrically identical. density stores data with a relative (logorithmic) value
	 * representative of the number of stars in a giving region. This value contains
	 * 18,000 values no matter the galaxy, which is representative of the number of sectors
	 * in the galaxy.
	 */
	public double hiResDensity[][], density[][] = new double[180][100];

	Sector sector[][] = null;

	//the following variables are used for saving and loading saves
	public String id;
	String packedData[], dirPath;

	public Galaxy(int id) {
		this.id = getID(id);
		dirPath = "res/saves/" + core.Main.universe.id + "/" + this.id;

		if (core.Main.universe.fromSave)
			packedData = FileHandler.readFile(dirPath + "/galaxy.txt");
	}

	protected String getID(int id) {
		return "G" + id;
	}
	public void saveGalaxy(){
		packData();
		FileHandler.makeDirectory(dirPath);
		FileHandler.writeToFile(packedData, dirPath + "/galaxy.txt");
	}
	protected abstract void packData();
	protected abstract void unpackData();

	public abstract void initiateGalaxy();

	public abstract void createSectors();
	
	public abstract void setHiResDensities();

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
		// TODO: 750 is the size of the display
		new GalaxyDrawer(core.Function.arrayToCartesian(density, 750, 180, 100),
				core.Function.arrayToCartesian(ionizedGas, 750, 180, 100));
	}

	public abstract double calcMass();
	public abstract double calcGalaxyAge();
	// TODO: Remove Magic numbers
	public int calcMaxRadius() {
		return 500;
	}
	public int calcMaxTheta() {
		return 1440;
	}
}
