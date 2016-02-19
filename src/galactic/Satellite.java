package galactic;

public class Satellite extends Galaxy{
	public double radiuserror;
	public boolean globularcluster;
	
	public Satellite(int id){
		getID(id);
	}

	public void initiateGalaxy(){
		galaxymass = calcMass();
		if (galaxymass < 5) globularcluster = true;
		numberstars = calcNumberStars();
		galaxyage = calcGalaxyAge();
		meanradius = calcMeanRadius();
		radiuserror = calcRadiusError();
		radius1 = calcRadius1();
		radius2 = calcRadius2();
		maxradius = calcMaxRadius();
		meandensity = calcMeanDensity();
		actualradius = calcActualRadius();
		//getUniversalRadius();
	}
	public void createSectors() {
		
	}
	
	public void display(){
		System.out.println(id);
		System.out.println("Radius = " + actualradius);
		System.out.println("Theta = " + tgalaxy);
		System.out.println("Radial = " + rgalaxy);
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
	
	public void getUniversalRadius(){
		double radius = (galaxymass - 4) / (universal.Main.universe.maingalaxy.galaxymass - 4) * 10;
		radius = universal.Function.exponentialFunction(0.7, radius);
		radius += 5;
		rgalaxy = radius;
	}
	
	public String getID(int id) {
		return "G" + id;
	}
}
