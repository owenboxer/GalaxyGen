package universal;

import visual.GalaxyDrawer;

public class Universe {
	public double universeAge, unitMeasure;
	private boolean coord[][];
	public int maxRadius = 0;
	public galactic.Main mainGalaxy;
	public galactic.Satellite satelliteGalaxy[];
	public String id;

	public Universe(){
		universeAge = calcUniverseAge();
	}
	public Universe(double age){
		universeAge = age;
	}

	public void initiateUniverse(){
	}
	public void displayUniverse(){
		double display[][] = new double[360][maxRadius];
		for (int t = 0; t < 360; t++)
			for (int r = 0; r < maxRadius; r++)
				if (coord[t][r]) display[t][r] = 10;
		display = Function.arrayToCartesian(display, 750, 360, maxRadius);
		
		new GalaxyDrawer(display);
	}

	public void createGalaxies(){
		mainGalaxy = new galactic.Main();
		mainGalaxy.initiateGalaxy();
		mainGalaxy.createSectors();
		/*
		unitmeasure = calcUnitMeasure();
		
		satellitegalaxy = new galactic.Satellite[maingalaxy.numbersatellites];
		for (int i = 0; i < maingalaxy.numbersatellites; i++){
			satellitegalaxy[i] = new galactic.Satellite(i + 1);
			satellitegalaxy[i].initiateGalaxy();
			System.out.println();
			satellitegalaxy[i].display();
		}
		
		//counting clusters
		int clusters = 0, galaxies;
		
		for (int i = 0; i < satellitegalaxy.length; i++)
			if (satellitegalaxy[i].globularcluster) clusters++;
		
		galaxies = satellitegalaxy.length - clusters;
		
		//placing clusters
		int haloboundary;
		boolean roundup = false;
		if (clusters % 50 > 0) roundup = true;
		haloboundary = clusters / 50;
		if (roundup) haloboundary++;
		*/
		//maingalaxy.displayDensities();
	}

	public double calcUniverseAge(){
		double age = Main.getRandomDouble(1, 10); 
		age = Function.exponentialFunction(0.9, age);
		age = (age*1.1) + 2;
		return age;
	}
	public double calcUnitMeasure(){
		return Math.pow(mainGalaxy.actualRadius, 10) * 0.2;
	}
}
