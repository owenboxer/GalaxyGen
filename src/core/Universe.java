package core;

import util.FileHandler;

public class Universe {
	public boolean fromSave = false;

	//TODO: Add satellite galaxies
	public double universeAge;

	public galactic.Parent parentGalaxy;
	public galactic.Satellite satelliteGalaxy[];

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

	public void createGalaxies(){
		parentGalaxy = new galactic.Parent();
		parentGalaxy.initiateGalaxy();
		parentGalaxy.createSectors();
		parentGalaxy.displayDensities();
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
}
