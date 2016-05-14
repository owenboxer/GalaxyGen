package core;

import java.util.ArrayList;

import util.FileHandler;

public class Universe {
	public boolean fromSave = false;

	//TODO: Add satellite galaxies
	public double universeAge;

	public galactic.Parent parentGalaxy;
	public galactic.Satellite satelliteGalaxy[];

	public int resolution = util.Input.getN(), timeInterval = 10000000;
	public double[][] density = new double[resolution][resolution];
	public ArrayList<structural.SuperParticle> superParticle = new ArrayList<structural.SuperParticle>(0);

	public static chemistry.Element[] element = new chemistry.Element[94];

	public String id;
	public String packedData = "", dirPath;

	public Universe(){
		System.out.println(resolution);
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
		System.out.println(age);
		return 0.001;//age;
	}

	private void runSimulation(){
		for (int i = 0; i < Math.pow(resolution, 2); i++)
			superParticle.add(new structural.SuperParticle(i / resolution, i % resolution));
			
		/*superParticle[0] = new structural.SuperParticle(30, 30);
		superParticle[1] = new structural.SuperParticle(36, 31);*/

		for (long time = 0; time < 1000; time++){
			for (int i = 0; i < superParticle.size(); i++)
				superParticle.get(i).calcVector();
			for (int i = 0; i < superParticle.size(); i++){
				superParticle.get(i).moveParticle();
				if (!superParticle.get(i).checkForScope())
					superParticle.remove(i);
			}
			for (int i = 0; i < superParticle.size(); i++)
				superParticle.get(i).checkForProximity();
			convertToDensityArray();
			display();
		}
	}
	private void convertToDensityArray(){
		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				density[xx][yy] = 0;

		for (int i = 0; i < superParticle.size(); i++){
			if ((int) superParticle.get(i).xx >= resolution || (int) superParticle.get(i).yy >= resolution || (int) superParticle.get(i).xx < 0 || (int) superParticle.get(i).yy < 0) continue;
			density[(int) superParticle.get(i).xx][(int) superParticle.get(i).yy]++;
		}

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
