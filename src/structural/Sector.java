package structural;

public class Sector {
	public int radial, polar;
	public double size, density, radiation;
	
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
		for (int i = 0; i < 70; i++){
			luminosity[i] = Double.valueOf(luminosities.line.elementAt(i)).doubleValue();
		}
	}
	private static double synthesis[] = null;
	private void setSynthesisProbabilities(){
		synthesis = new double[70];
		universal.FileIO syntheses = new universal.FileIO("spectraldata", "synthesis");
		syntheses.readFile();
		for (int i = 0; i < 70; i++){
			synthesis[i] = Double.valueOf(syntheses.line.elementAt(i)).doubleValue();
		}
	}
	private static double mass[] = null;
	private void setMasses(){
		mass = new double[70];
		universal.FileIO masses = new universal.FileIO("spectraldata", "mass");
		masses.readFile();
		for (int i = 0; i < 70; i++){
			mass[i] = Double.valueOf(masses.line.elementAt(i)).doubleValue();
		}
	}
	
	//the following variable determine how many generations there are in the simulation
	int generations;
	private long age, interval = 100000000;
	
	//the following variable represent the breakdown of the sectors ISM
	double stellarMatter = 0, gaseousMatter = 1; //breakdown of stars vs. non-stars
	double ionizedMatter = 0, molecularMatter = 1; //breakdown of ionized hydrogen level
	double lightMatter[], mediumMatter[], heavyMatter[]; //breakdown of matter composition
	double spectralDistribution[] = new double[70]; //breakdown of spectral type
	public double birthRate[], deathRate = 0, total = 0;
	double rotationSpeed, friction;
	double supernovaPressure = 0;

	public Sector(int polar, int radial, double rawDensity, double galaxyAge){
		this.radial = radial;
		this.polar = polar;
		this.density = Math.pow(rawDensity, 2) / 10;
		this.age = (long) (galaxyAge * 1000000000);
	}
	
	public void initiateSector(){

		if (lifetime == null) setLifetimes();
		if (luminosity == null) setLuminosities();
		if (synthesis == null) setSynthesisProbabilities();
		if (mass == null) setMasses();

		for (double type = 0; type < 70; type++) spectralDistribution[(int) type] = 0; //1 / (70 - type);
		for (int gen = 0; gen < generations; gen++) birthRate[gen] = 0;

		this.age -= this.age % interval;
		generations = (int) (this.age / interval);

		lightMatter = new double[generations];
		mediumMatter = new double[generations];
		heavyMatter = new double[generations];
		birthRate = new double[generations];

		if (radial >= 10) rotationSpeed = Math.sqrt(11); //takes into account the effects of dark matter
		else rotationSpeed = Math.sqrt((double) radial + 1);
		size = Math.pow(radial + 1, 2) - Math.pow(radial, 2);

		runSimulation();
		adjustScale();
	}
	
	private void runSimulation(){
		for (long time = 0; time < age; time += interval){

			getIonizedMatter();
			molecularMatter = 1 - ionizedMatter;
			if (molecularMatter < 0) molecularMatter = 0;

			birthRate[(int) (time / interval)] = Math.pow(8 * gaseousMatter * molecularMatter, density);
			//birthrate[(int) (time / interval)] += supernovaPressure;
			
			getSpectralDistributions(time);

			final double Ksm = 0.00000025;
			stellarMatter += (birthRate[(int) (time / interval)] - deathRate) * Ksm;
			gaseousMatter = 1 - stellarMatter;
			//System.out.println(birthrate[(int) (time / interval)]);
			//System.out.println(deathrate);
		}
		double number = 0;
		for (int i = 60; i < 70; i++) number += spectralDistribution[i];
		//System.out.println(total);
	}
	private void getIonizedMatter(){
		double radiation = 0;
		for (int type = 0; type < 70; type++){
			radiation += spectralDistribution[type] * (double) (luminosity[type]);
		}
		friction = (Math.pow(density + supernovaPressure, 2)) / rotationSpeed;
		radiation *= (1.5 * ionizedMatter + 5.5 * stellarMatter + 0.025 * gaseousMatter) * friction;
		final double K = 1;
		ionizedMatter = radiation / K;
		if (ionizedMatter > 1) ionizedMatter = Math.cbrt(ionizedMatter);
		
	}
	private void getSpectralDistributions(long current){ //finds other values in addition to spectral distributions
		double spectralType[] = new double[70], amtDead = 0;
		supernovaPressure = 0;
		
		for (int type = 0; type < 70; type++){
			spectralType[type] = spectralDistribution[type] * total;
		}
		
		for(int type = 0; type < 70; type++){
			for (long time = 0; time < current; time += interval){
				if (lifetime[type] <= current - time && lifetime[type] > current - interval - time){
					if (type < 14) supernovaPressure += mass[type] * synthesis[type] * (double) birthRate[(int) (time / interval)];
					amtDead += synthesis[type] * birthRate[(int) (time / interval)];
					spectralType[type] -= synthesis[type] * (double) birthRate[(int) (time / interval)];
				}
			}
			//ADD COMPOSITION CALCULATIONS HERE
			
			spectralType[type] += synthesis[type] * birthRate[(int) (current / interval)];
		}
		
		supernovaPressure *= 0.01 * (molecularMatter + (0.1 * ionizedMatter));
		deathRate = amtDead;
		
		total += birthRate[(int) (current / interval)];
		total -= deathRate;
		
		double total2 = 0;
		for (int type = 0; type < 70; type++) total2 += spectralType[type];
		for (int type = 0; type < 70; type++)
			spectralDistribution[type] = spectralType[type] / total2;
	}

	private void adjustScale(){
		total *= size; //keeping density constant, adjusts to fit size to entire galaxy
		double K = 0.0001; //corrects for amount of stars in the galaxy
		total *= K * Math.pow(universal.Main.universe.mainGalaxy.galaxyMass - 7, 10); //MUST UPDATE FOR BEFORE IMPLEMENTING SATELLITE GALAXIES
	}
	
}
