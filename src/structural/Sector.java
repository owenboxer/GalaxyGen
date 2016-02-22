package structural;

public class Sector {
	public int radial, polar, numberstars[] = new int[50], numberstarstotal, birthrate, deathrate;
	public double size, mass, density, radiation, proportion[] = new double[70];
	private long age, interval;
	
	private static final double SPECTRAL_FRACTION_CONSTANT = 0.0142857143; // 1 / 70
	
	private static long lifetime[] = null;
	private void setLifetimes(){
		lifetime = new long[70]; //70 spectral types
		universal.FileIO lifetimes = new universal.FileIO("spectraldata", "lifetimes");
		lifetimes.readFile();
		for (int i = 0; i < 70; i++)
			lifetime[i] = Long.valueOf(lifetimes.line.elementAt(i)).longValue();
	}
	private static double luminosity[] = null;
	private void setLuminosities(){
		luminosity = new double[70];
		universal.FileIO luminosities = new universal.FileIO("spectraldata", "luminosities");
		luminosities.readFile();
		for (int i = 0; i < 70; i++)
			luminosity[i] = Double.valueOf(luminosities.line.elementAt(i)).doubleValue();
	}
	
	//the following variable represent the breakdown of the sectors ISM
	double molecular, ionized, gmcdensity, coronaldensity;

	public Sector(int radial, int polar, double rawdensity, double galaxyage){
		this.radial = radial;
		this.polar = polar;
		this.density = rawdensity;
		this.age = (long) (galaxyage * 1000000000);
	}
	
	public void initiateSector(){
		size = calcSize();
		mass = calcMass();
		if (lifetime == null) setLifetimes();
		if (luminosity == null) setLuminosities();
		runSimulation();
	}
	
	private void runSimulation(){
		interval = age / 50;
		age = interval * 50; //so there is no rounding error
		
		for (long year = 0; year < age; year += interval){
			coronaldensity = calcCoronalDensity();
			ionized = 25 * coronaldensity;
			if (ionized > 100) ionized = 100;
			molecular = 100 - ionized;
			radiation = ionized / 10;
			birthrate = (int) (molecular * mass);
			numberstars[(int) (year / interval)] = birthrate;

			proportion = calcProportions(year);
			mass -= birthrate / 100;
			mass += deathrate / 100;
		}
	}
	private double calcCoronalDensity(){
		double coronalradiation = 0;
		for (int i = 0; i < 70; i++)
			coronalradiation += proportion[i] * luminosity[i];
		coronalradiation *= numberstarstotal;
		return coronalradiation / size;
	}
	/*private int calcDeathRate(int year){
		double amtdead = 0;
		
		for (int gen = 0; gen < year; gen += interval)
			for (int type = 0; type < 70; type++)
				if (lifetime[type] < gen) 
					
		amtdead -= deathrate;
		return (int) amtdead;
	}*/
	private double[] calcProportions(long year){
		double roughtype[] = new double[70];
		double amtdead = 0;
		
		for (long gen = 0; gen < year; gen += interval)
			for (int type = 0; type < 70; type++){
				if (lifetime[type] < gen)
					amtdead += numberstars[(int) (gen / interval)] * SPECTRAL_FRACTION_CONSTANT;
				else 
					roughtype[type] += numberstars[(int) (gen / interval)] * SPECTRAL_FRACTION_CONSTANT;
			}
		
		amtdead -= deathrate;
		deathrate = (int) amtdead;
		
		numberstarstotal += birthrate;
		numberstarstotal -= deathrate;
		/*numberstarstotal = 0;
		
		for (int type = 0; type < 70; type++)
			numberstarstotal += roughtype[type];*/
		
		for (int type = 0; type < 70; type++)
			roughtype[type] /= numberstarstotal;
		
		return roughtype;
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
