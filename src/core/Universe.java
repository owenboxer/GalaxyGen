package core;

import java.util.ArrayList;

import util.FileHandler;

public class Universe {
	public boolean fromSave = false;
	public boolean saveForAnimation = true;
	int frame = 0, maxDensity = 0;

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
	public void deleteAll(){
		util.FileHandler.deleteFile(dirPath);
	}

	public void initiateUniverse(){
		maxDensity = 0;

		if (saveForAnimation) util.FileHandler.makeDirectory(dirPath + "/animation"); 

		runSimulation();
		displayFinal();

		if (saveForAnimation)
			displayAnimation();
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
		age = (age * 1.1) + 2;
		return age;
	}

	/**@author owencarter @ This simulation functions in three stages.
	 * Stage I: "Super particles" (points that are representative of large clouds of gas) begin evenly 
	 * distributed across the screen a square shape. There particles are assigned random vectors with no bias
	 * to direction but a heavy bias on magnitude towards 0, using a Gaussian distribution. This random
	 * element, though not emulating quantum fluctuations in reality, has the same results in that certain
	 * regions of the universe will have higher densities than others, and the outcome of the program varies
	 * between iterations. If these random vectors were consistent, then the simulation would most likely
	 * produce the exact same results every single time. In addition to vectors and positions being assigned 
	 * to each particle, a mass of 1 is also given to each particle.
	 * Stage II: After their initial setting, each particle then is acted upon by the gravitational force
	 * between it and all other particles. This is a very time consuming operation, and therefore some 
	 * shortcuts must be taken to speed up the simulation time. The amount of time between each calculation is
	 * significant, apx. 10000000 years. This means that particles that approach closely may be flung out of 
	 * the scope of the simulation before being pulled back by the particle they were close to. 
	 */
	private void runSimulation(){
		for (int i = 0; i < Math.pow(resolution, 2); i++)
			superParticle.add(new structural.SuperParticle(i / resolution, i % resolution));

		/*superParticle[0] = new structural.SuperParticle(30, 30);
		superParticle[1] = new structural.SuperParticle(36, 31);*/

		for (long time = 0; time < universeAge * 1000000000; time += timeInterval){
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

			if (saveForAnimation){
				saveDensityArray(frame);
				System.out.println((int) (time / (universeAge * 10000000)) + "% Complete");
				frame++;
			}
			else display();
		}
	}

	private void convertToDensityArray(){
		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				density[xx][yy] = 0;

		for (int i = 0; i < superParticle.size(); i++){
			if ((int) superParticle.get(i).xx >= resolution || (int) superParticle.get(i).yy >= resolution ||
					(int) superParticle.get(i).xx < 0 || (int) superParticle.get(i).yy < 0) 
				continue;
			density[(int) superParticle.get(i).xx][(int) superParticle.get(i).yy] += superParticle.get(i).mass;
		}

		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++){
				if (density[xx][yy] > maxDensity) maxDensity = (int) density[xx][yy];
			}
	}
	private void saveDensityArray(int frame){
		String[] packedDensity = new String[density.length * density[0].length];

		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				packedDensity[xx * resolution + yy] = density[xx][yy] + "";

		util.FileHandler.writeToFile(packedDensity, dirPath + "/animation/frame" + frame);
	}
	private void readDensityArray(int frame){
		String[] packedDensity = util.FileHandler.readFile(dirPath + "/animation/frame" + frame);

		for (int xx = 0; xx < resolution; xx++)
			for (int yy = 0; yy < resolution; yy++)
				density[xx][yy] = Double.valueOf(packedDensity[xx * resolution + yy]).doubleValue() / 
									(double) maxDensity;
	}
	private void display(){
		new visual.UniverseDrawer(density, resolution);
	}
	private void displayFinal(){
		System.out.println("Years run: " + universeAge * 1000000000);
		System.out.println("Number of galaxies: " + superParticle.size());
	}
	private void displayAnimation(){
		for (int i = 0; i < frame; i++){
			readDensityArray(i);
			display();
			util.FileHandler.deleteFile(dirPath + "/animation/frame" + i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		util.FileHandler.deleteFile(dirPath + "/animation");
	}
}
