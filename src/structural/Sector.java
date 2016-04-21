package structural;

public class Sector {
	public int radial, polar;
	public double size, density, radiation, trueDensity;
	
	private static long lifetime[] = null;
	private static void setLifetimes(){
		lifetime = new long[70]; //70 spectral types
		String[] packedLifetime = util.FileHandler.readFile("res/spectraldata/lifetime.txt");
		for (int i = 0; i < 70; i++)
			lifetime[i] = Long.valueOf(packedLifetime[i]).longValue();
	}
	private static double luminosity[] = null;
	private static void setLuminosities(){
		luminosity = new double[70];
		String[] packedLuminosity = util.FileHandler.readFile("res/spectraldata/luminosity.txt");
		for (int i = 0; i < 70; i++)
			luminosity[i] = Double.valueOf(packedLuminosity[i]).doubleValue();
	}
	private static double synthesis[] = null;
	private static void setSynthesisProbabilities(){
		synthesis = new double[70];
		String[] packedSynthesis = util.FileHandler.readFile("res/spectraldata/synthesis.txt");
		for (int i = 0; i < 70; i++)
			synthesis[i] = Double.valueOf(packedSynthesis[i]).doubleValue();
	}
	private static double mass[] = null;
	private void setMasses(){
		mass = new double[70];
		String[] packedMass = util.FileHandler.readFile("res/spectraldata/mass.txt");
		for (int i = 0; i < 70; i++)
			mass[i] = Double.valueOf(packedMass[i]).doubleValue();
	}
	
	//the following variable determine how many generations there are in the simulation
	int generations;
	private long age, interval = 100000000;
	
	//the following variables represent the breakdown of the sectors ISM
	double stellarMatter = 0, gaseousMatter = 1; //breakdown of stars vs. non-stars
	public double ionizedMatter = 0, molecularMatter = 1; //breakdown of ionized hydrogen level
	double spectralDistribution[] = new double[70]; //breakdown of spectral type
	public double birthRate[], deathRate = 0, total = 0;
	double rotationSpeed, friction;
	double supernovaPressure = 0;

	//the following variables are related to the breakdown of matter by chemical element, related to their synthesis in stars
	

	public Sector(int polar, int radial, double rawDensity, double galaxyAge){
		this.radial = radial;
		this.polar = polar;
		this.density = Math.pow(rawDensity, 2) / 10;
		this.age = (long) (galaxyAge * 1000000000);
	}
	
	/**
	 * @author owencarter
	 * This function initiates a sector, setting its variables and running its simulation.
	 * It should be run only for sectors that require data related to birthrate.
	 */
	public void initiateSector(){

		if (lifetime == null) setLifetimes();
		if (luminosity == null) setLuminosities();
		if (synthesis == null) setSynthesisProbabilities();
		if (mass == null) setMasses();

		for (double type = 0; type < 70; type++) spectralDistribution[(int) type] = 0; //1 / (70 - type);
		for (int gen = 0; gen < generations; gen++) birthRate[gen] = 0;

		this.age -= this.age % interval;
		generations = (int) (this.age / interval);

		birthRate = new double[generations];

		if (radial >= 10) rotationSpeed = Math.sqrt(11); //takes into account the effects of dark matter
		else rotationSpeed = Math.sqrt((double) radial + 1);
		size = Math.pow(radial + 1, 2) - Math.pow(radial, 2);

		runSimulation();

		trueDensity = total;

		adjustScale();
	}
	
	/**
	 * @author owencarter
	 * 
	 * 	This method runs a simulation of sector over a period equivalent to the age of the 
	 * galaxy. It assumes that no stars exist at the beginning. The amount of stars born
	 * is directly related to the amount of molecular (non-ionized) gas in the sector, as
	 * ionized gas is not as dense as molecular gas. 
	 * 	There are several factors that lead to the formation of ionized gas. First and 
	 * foremost is the stellar radiation produced by stars, which is related to the
	 * luminosity of those stars. Another significant source of ionized gas is the motion
	 * of gas through the galaxy, which is dependent on the distance of that gas from the
	 * galactic center. Pre-existing ionized gas also leads to the formation of more 
	 * ionized gas. Finally, molecular gas can also lead to the formation of ionized gas 
	 * because of x-ray radiation that it produces. 
	 * 	Another factor that plays into the birthrate of stars is the amount of pressure
	 * generated by supernovae in the region. Pressure from supernovae can lead to the
	 * collapse of GMCs (large clouds of molecular gas) and the formation of Bok Globules
	 * (smaller clouds of molecular gas), both of which are regions of rapid star 
	 * formation.
	 * 	The model also incorporates star deaths. As the sector ages, the model calculates
	 * for each generation of stars previous the amount of stars that should have died, as
	 * well as the amount of stars that resulted in supernovae.
	 * 
	 * 	The results from the model will vary based upon the density of a given sector as 
	 * well as the distance of that sector from the galactic center. In order to simplify
	 * the description of the results from this model, we will assume the galaxy being
	 * modeled is an elliptical galaxy, where density decreases as distance from the
	 * galactic center increases. 
	 * 	At the very center of the galaxy, birthrate is incredibly high to begin, due to
	 * the high density of the sectors in that region. However, after the first generation
	 * of stars form, the amount of stars formed after that are quickly reduced due to
	 * high levels of ionized gas. There is, however, significant pressure from supernovae
	 * in following generations that keeps the birthrates in center of the galaxy above 0.
	 * However, after a long period, the vast majority of the stars in the region are old,
	 * small stars, due to significantly reduced birthrates. 
	 *	Farther out from the center, about a third of the way to the edge of the galaxy,
	 * initial birthrates are not as high as in the center, but the sectors in this region
	 * recover more quickly from high levels of ionized gas and after a while begin to 
	 * produce more stars, eventually reaching an equilibrium. Since the model does not
	 * take into account the reduced amount of matter available for forming stars (this
	 * would only deplete after a much longer period), the model stays at this equilibrium.
	 * As a result of higher birthrates, there are significantly more large stars in this
	 * region.
	 * 	Nearing the edge of the galaxy, there are far fewer stars due to low density of 
	 * gas. The sectors never have high levels of ionized gas, instead simply slowly 
	 * increase in birthrate. As a result, outer regions have a slightly lower proportion 
	 * of large stars, though still more than in the galactic center. 
	 */
	private void runSimulation(){
		for (long time = 0; time < age; time += interval){

			getIonizedMatter();
			molecularMatter = 1 - ionizedMatter;
			if (molecularMatter < 0) molecularMatter = 0;

			birthRate[(int) (time / interval)] = Math.pow(3 * gaseousMatter * molecularMatter, density);
			birthRate[(int) (time / interval)] += supernovaPressure;
			
			getSpectralDistributions(time);

			final double Ksm = 0.0000025;
			stellarMatter += (birthRate[(int) (time / interval)] - deathRate) * Ksm;
			gaseousMatter = 1 - stellarMatter;
			//System.out.println(ionizedMatter);
		}
		double number = 0;
		for (int i = 0; i < 10; i++) number += spectralDistribution[i];
		//System.out.println(total);
	}
	private void getIonizedMatter(){
		double radiation = 0;
		for (int type = 0; type < 70; type++){
			radiation += spectralDistribution[type] * (double) (luminosity[type]);
		}
		friction = (Math.pow(density + supernovaPressure, 2)) / rotationSpeed;
		radiation *= (1 * ionizedMatter + 3.5 * stellarMatter + 0.025 * gaseousMatter) * friction;
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
			spectralType[type] += synthesis[type] * birthRate[(int) (current / interval)];
		}
		
		supernovaPressure *= 100 * (molecularMatter + (0.1 * ionizedMatter));
		deathRate = amtDead;
		
		total += birthRate[(int) (current / interval)];
		total -= deathRate;
		
		double total2 = 0;
		for (int type = 0; type < 70; type++) total2 += spectralType[type];
		for (int type = 0; type < 70; type++)
			spectralDistribution[type] = spectralType[type] / total2;
	}

	private void calcMatterDistribution(){
		
	}

	private void adjustScale(){
		total *= size; //keeping density constant, adjusts to fit size to entire galaxy
		double K = 0.5; //corrects for amount of stars in the galaxy
		total *= K * Math.pow(Math.sqrt(universal.Main.universe.mainGalaxy.galaxyMass - 7), 10); //MUST UPDATE FOR BEFORE IMPLEMENTING SATELLITE GALAXIES
	}
	
}
