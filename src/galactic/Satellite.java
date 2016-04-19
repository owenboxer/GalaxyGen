package galactic;

public class Satellite extends Galaxy{
	public double radiusError;
	public boolean globularCluster;

	public Satellite(int id){
		getID(id);
	}

	public void initiateGalaxy(){
		galaxyMass = calcMass();
		if (galaxyMass < 5) globularCluster = true;
		numberStars = calcNumberStars();
		galaxyAge = calcGalaxyAge();
		meanRadius = calcMeanRadius();
		radiusError = calcRadiusError();
		radius1 = calcRadius1();
		radius2 = calcRadius2();
		maxRadius = calcMaxRadius();
		meanDensity = calcMeanDensity();
		actualRadius = calcActualRadius();
		//getUniversalRadius();
	}
	public void createSectors() {
	}

	public void display(){
		System.out.println(id);
		System.out.println("Radius = " + actualRadius);
		System.out.println("Theta = " + tGalaxy);
		System.out.println("Radial = " + rGalaxy);
	}

	public double calcMass(){
		double mainmass = universal.Main.universe.mainGalaxy.galaxyMass; // uses main galaxy mass
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
		double radiuserror = 4 - galaxyAge;
		radiuserror = radiuserror / 8;
		return radiuserror;
	}
	public double calcRadius1(){
		double errorfactor = 1 + radiusError;
		return errorfactor * meanRadius;
	}
	public double calcRadius2(){
		double errorfactor = 1 - radiusError;
		return errorfactor * meanRadius;
	}

	public void getUniversalRadius(){
		double radius = (galaxyMass - 4) / (universal.Main.universe.mainGalaxy.galaxyMass - 4) * 10;
		radius = universal.Function.exponentialFunction(0.7, radius);
		radius += 5;
		rGalaxy = radius;
	}

	public String getID(int id) {
		return "G" + id;
	}
}
