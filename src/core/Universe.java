package core;

import util.FileHandler;

public class Universe {
	public boolean fromSave = false;

	//TODO: Add satellite galaxies
	public double universeAge;

	public galactic.Parent parentGalaxy;
	public galactic.Satellite satelliteGalaxy[];

	public int resolution = 750;
	public double[][] density = new double[resolution][resolution];
	public structural.SuperParticle superParticle[][] = new structural.SuperParticle[resolution][resolution];

	public static chemistry.Element[] element = new chemistry.Element[94];

	public String id;
	public String packedData = "", dirPath;

	public Universe(){
		id = getNewID();
		dirPath = "res/saves/" + id;

		universeAge = calcUniverseAge();
	}
	public Universe(String id){
		this.id = id;
		fromSave = true;

		dirPath = "res/saves/" + id; 
		String temp[] = new String[1];
		temp = FileHandler.readFile(dirPath + "/universe.txt");
		packedData = temp[0];

		unpackData();
	}

	public void saveUniverse(){
		if (!packedData.equals(universeAge)){
			packData();
			String[] temp = new String[1];
			temp[0] = packedData;
			FileHandler.makeDirectory(dirPath);
			FileHandler.writeToFile(temp, dirPath + "/universe.txt");
		}

		parentGalaxy.saveGalaxy();
	}
	private String getNewID(){
		int i = 0;
		String id;
		do{
			id = "U" + i;
			i++;
		} while (FileHandler.checkExists("res/saves/" + id));

		return id;
	}
	private void packData(){
		packedData = new StringBuilder().append(universeAge).toString();
	}
	private void unpackData(){
		universeAge = Double.valueOf(packedData).doubleValue();
	}

	public void initiateUniverse(){
		runSimulation();
		convertToDensityArray();
		//display();
	}

	public void createGalaxies(){
		parentGalaxy = new galactic.Parent();
		parentGalaxy.initiateGalaxy();

		if (!fromSave)
			parentGalaxy.createSectors();

		//parentGalaxy.displayDensities();
	}

	public void makeElements(){
		for (int e = 0; e < 94; e++)
			element[e] = new chemistry.Element(e + 1);

		for (int e = 0; e < 94; e++)
			element[e].createIsotopes();
	}

	public double calcUniverseAge(){
		double age = Main.getRandomDouble(1, 10); 
		age = Function.exponentialFunction(0.9, age);
		age = (age*1.1) + 2;
		return age;
	}

	private void runSimulation(){
		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++){
				superParticle[xx][yy] = new structural.SuperParticle(xx, yy);
			}
	}
	private void convertToDensityArray(){
		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				density[xx][yy] = 0;

		for (int l = 0; l < resolution; l++)
			for (int w = 0; w < resolution; w++)
				density[(int) superParticle[l][w].xx][(int) superParticle[l][w].yy]++;

		double max = 0;
		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				if (density[xx][yy] > max) max = density[xx][yy];

		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				density[xx][yy] /= max;
	}
	private void display(){
		new visual.UniverseDrawer(density);
	}
}
