package galactic;

public class Satellite extends Galaxy{
	public double radiuserror;
	
	public Satellite(boolean satellite, int id){
		super(satellite);
		getID(id);
		//getCoords();
	}

	public void initiateGalaxy(){
		galaxymass = calcMass();
		numberstars = calcNumberStars();
		galaxyage = calcGalaxyAge();
		meanradius = calcMeanRadius();
		radiuserror = calcRadiusError();
		radius1 = calcRadius1();
		radius2 = calcRadius2();
		maxradius = calcMaxRadius();
		meandensity = calcMeanDensity();
		actualradius = calcActualRadius();
	}
	public void createSectors() {
		
	}
	
	public void display(){
		universal.Main.log("Mass = " + galaxymass + "\n");
		universal.Main.log("#Stars = " + numberstars + "\n");
		universal.Main.log("Age = " + galaxyage + "\n");
		universal.Main.log("Mean Radius = " + meanradius + "\n");
		universal.Main.log("Error = " + radiuserror + "\n");
		universal.Main.log("R1 = " + radius1 + "\n");
		universal.Main.log("R2 = " + radius2 + "\n");
		universal.Main.log("Max Radius = " + maxradius + "\n");
		universal.Main.log("Density = " + meandensity + "\n");
	}

	public double calcMass(){
		double mainmass = universal.Main.universe.maingalaxy.galaxymass; // uses main galaxy mass
		double mass  = universal.Main.getRandomDouble(4, mainmass-1);
		mass = ((mass - 4) / (mainmass - 5)) * 10;
		mass = universal.Function.exponentialFunction(0.95, mass);
		mass = ((mass / 10) * (mainmass - 5)) + 4;
		return mass;
	}
	public double calcGalaxyAge(){
		return universal.Main.getRandomDouble(0, 4);
	}
	public double calcRadiusError(){
		double radiuserror = 4 - galaxyage;
		radiuserror = radiuserror / 8;
		return radiuserror;
	}
	public double calcRadius1(){
		double errorfactor = 1 + radiuserror;
		return errorfactor * meanradius;
	}
	public double calcRadius2(){
		double errorfactor = 1 - radiuserror;
		return errorfactor * meanradius;
	}
	
	public void getCoords(){
		rgalaxy = (int) (galaxymass * universal.Main.universe.maingalaxy.galaxymass);
	}
	
	public String getID(int id) {
		return "G" + id;
	}
}
