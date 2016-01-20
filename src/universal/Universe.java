package universal;

public class Universe {
	public double universeage;
	public galactic.Main maingalaxy;
	public galactic.Satellite satellitegalaxy[];
	public String id;
	
	public Universe(){
		universeage = Main.getRandomDouble(1, 10); 
		universeage = Function.exponentialFunction(0.9, universeage);
		universeage = (universeage*1.1) + 2;
		id = "U" + save.Saves.numberofsaves;
	}
	public Universe(double age){
		universeage = age;
		id = "U" + save.Saves.numberofsaves;
	}
	
	public void initiateUniverse(){
		maingalaxy = new galactic.Main(false);
		maingalaxy.initiateGalaxy();
		maingalaxy.display();
		satellitegalaxy = new galactic.Satellite[maingalaxy.numbersatellites];
		for (int i = 0; i < maingalaxy.numbersatellites; i++){
			satellitegalaxy[i] = new galactic.Satellite(true, i + 1);
			satellitegalaxy[i].initiateGalaxy();
			System.out.println();
			satellitegalaxy[i].display();
		}
		//maingalaxy.createSectors();
		//maingalaxy.displayDensities();
		
	}
	
	public static double lyToUniversalUnits(int ly){
		return ly/10000;
	}
}
