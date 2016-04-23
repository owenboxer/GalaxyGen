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
		galaxyAge = calcGalaxyAge();
		radiusError = calcRadiusError();
		maxRadius = calcMaxRadius();
	}
	public void createSectors() {
	}

	public void display(){
		System.out.println(id);
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
