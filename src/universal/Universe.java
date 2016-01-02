package universal;

public class Universe {
	public double universeage;
	public galactic.Main maingalaxy;
	public galactic.Satellite satellitegalaxy[];
	
	public Universe(){
		universeage = Main.getRandomDouble(1, 10); 
		universeage = Function.exponentialFunction(0.9, universeage);
		universeage = (universeage*1.1) + 2;
	}
	
	public void initiateUniverse(){
		maingalaxy = new galactic.Main(false);
		maingalaxy.initiateGalaxy();
		maingalaxy.display();
		maingalaxy.createSectors();
		maingalaxy.displayDensities();
		/*satellitegalaxy = new galactic.Satellite[maingalaxy.numbersatellites];
		for (int i = 0; i<satellitegalaxy.length; i++){ 
			satellitegalaxy[i] = new galactic.Satellite(true, i);
			satellitegalaxy[i].initiateGalaxy();
			satellitegalaxy[i].display();
		}*/
	}
	
	public boolean checkSatelliteCoords(double x1, double y1, double r1){
		boolean check = true;
		double x2 = 0, y2 = 0, 
				r2 = maingalaxy.maxradius;
		double minimum = r1 + r2 + 1;
		if (Function.radialCheckFunction(x1, y1, x2, y2, minimum)) check = false;
		
		else for (int i = 0; i<satellitegalaxy.length; i++){
			if (!(satellitegalaxy[i] instanceof galactic.Satellite)) break;
			x2 = satellitegalaxy[i].xsat;
			y2 = satellitegalaxy[i].ysat; 
			r2 = satellitegalaxy[i].maxradius;
			minimum = r1 + r2 + 1;
			if (Function.radialCheckFunction(x1, y1, x2, y2, minimum)){
				check = false;
				break;
			}
		}
		return check;
	}
	
	public static double lyToUniversalUnits(int ly){
		return ly/10000;
	}
}
