package structural;

public class Sector {
	public int radial, polar, numberstars, temperature, birthrate;
	public double size, mass, density, radiation, proportion[] = new double[65];
	
	public static long interval = 1000000;
	public static int lifetime[] = new int[70];
	public static void setLiftimes(){
		//consider moving to a save file
		universal.FileIO lifetimes = new universal.FileIO("spectraldata", "lifetimes");
	}
	
	//the following variable represent the breakdown of the sectors ISM
	double molecular, ionized, gmcdensity, coronaldensity; 

	public Sector(int radial, int polar, double rawdensity){
		this.radial = radial;
		this.polar = polar;
		this.density = rawdensity;
	}
	
	public void initiateSector(){
		size = calcSize();
		mass = calcMass();
		setDefaults();
	}
	
	private void setDefaults(){
		numberstars = 0; //assume that no time elapsed during formation of galaxy
		temperature = 100; //measured in Kelvin, 100K is average temperature of H I regions
		birthrate = 0; //will change immediately
		radiation = 0; //as there are no stars or ionized hydrogen, there is no radiation
		molecular = 100; //assume entire region is H I region
		ionized = 0; //assume no sections of the region are H II until stars form
		gmcdensity = 0; //assume no time has elapsed, so there are no gmcs formed yet
		coronaldensity = 0; //since there are no stars, there are no coronal gasses
	}
	
	
	private double calcSize(){
		double area1 = Math.PI * Math.pow((radial + 1), 2) / 180, 
				area2 = Math.PI * Math.pow(radial, 2) / 180;
		return area1 - area2;
	}
	private double calcMass(){
		return size * density;
	}
	
}
