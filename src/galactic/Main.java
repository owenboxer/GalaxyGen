package galactic;

public class Main extends Galaxy{

	/* Lines 14 - 20: Variables and constructor
	 * Lines - : Setting and displaying variables
	 * Lines - : Algorithm for creating arms
	 * Lines - : Post-generation steps for all galaxy types
	 * Lines - : Post-generation for barred galaxies
	 * Lines - : Addition of minor arms
	 * Lines - : Methods for calculating aspects of galaxy
	 */

	public int numbersatellites, barsize, majorarms, minorarms, grade;
	public double soi;

	public Main(boolean satellite){
		super(satellite);
		getID(0);
	}

	public void initiateClone(){
		
	}
	public void initiateGalaxy() {
		galaxymass = calcMass();
		numberstars = calcNumberStars();
		galaxyage = calcGalaxyAge();
		meanradius = calcMeanRadius();
		radius1 = calcRadius1();
		radius2 = calcRadius2();
		maxradius = calcMaxRadius();
		numbersatellites = calcNumberSatellites();
		barsize = calcBarSize();
		grade = calcGrade();
		majorarms = calcMajorArms();
		minorarms = calcMinorArms();
		meandensity = calcMeanDensity();
		maxtheta = calcMaxTheta();
	}
	public void display(){
		universal.Main.log("Mass = " + galaxymass + "\n");
		universal.Main.log("#Stars = " + numberstars + "\n");
		universal.Main.log("Age = " + galaxyage + "\n");
		universal.Main.log("Mean Radius = " + meanradius + "\n");
		universal.Main.log("R1 = " + radius1 + "\n");
		universal.Main.log("R2 = " + radius2 + "\n");
		universal.Main.log("Max Radius = " + maxradius + "\n");
		universal.Main.log("#Satellites = " + numbersatellites + "\n");
		universal.Main.log("Bar# = " + barsize + "\n");
		universal.Main.log("Grade# = " + grade + "\n");
		universal.Main.log("#Major Arms = " + majorarms + "\n");
		universal.Main.log("#Minor Arms = " + minorarms + "\n");
		universal.Main.log("Density = " + meandensity + "\n");
	}

	public void createSectors(){

		double density[][] = new double[maxtheta][maxradius];
		final double armwidth = (maxradius + (grade / 5)) / majorarms; //armwidth remains constant
		double amplitude, circumference, nonarmwidth, artificialarm, artificialnonarm, cutoff, 
				radius, startingpoint; //these variables change with radius
		double x, predensity, theta, artificialyaxis, artificialx; //these variables change with polar
		boolean greaterthancutoff, pastfirstlower; //required for switching between equations

		//runs though radial values
		for (double r = 0; r < maxradius; r++){

			//setting variables that are dependent on radius
			radius = r + 1; //since a circle with a radius of 0 has a circumference of 0, radius must 
							//begin at 1
			amplitude = universal.Function.linearFunction(-0.4, 8, (r / maxradius) * 10);
			circumference = 2 * Math.PI * radius;
			nonarmwidth = (circumference - (armwidth * majorarms)) / majorarms;
			cutoff = amplitude / 2;

			//pre-setting some polar-dependent variables
			artificialyaxis = 0; //this is required because 
			artificialarm = 1.5 * armwidth;
			artificialnonarm = 3.0 * nonarmwidth;
			greaterthancutoff = false;
			pastfirstlower = false;
			
			//finding starting point so that the peaks of the upper cosine functions line up
			startingpoint = nonarmwidth - (((Math.PI * radius) / majorarms) - 
					(0.5 * armwidth));
			
			//since the the width of the combined arms is greater than the circumferences of circles 
			//at the galactic center, these values must be set by other means
			if (armwidth * majorarms > circumference)
				for (theta = 0; theta < maxtheta; theta++)
					density[(int) theta][(int) r] = 0;

			//running through theta values
			else for (theta = 0; theta < maxtheta; theta++){

				x = (theta / maxtheta) * circumference;
				
				if (!pastfirstlower) artificialx = startingpoint + x;
				else artificialx = x - artificialyaxis;

				if (greaterthancutoff){

					predensity = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(((2 * Math.PI * 
							artificialx) / artificialarm) + Math.PI + (Math.PI * 0.333334))));

					//System.out.println("Upper Equation: x = " + x + " artx = " + artificialx + 
							//" density = " + predensity + " cutoff = " + cutoff);

					if (predensity <= cutoff){
						greaterthancutoff = false;
						artificialyaxis += armwidth;
						theta--;
						continue;
					}
					if (artificialx >= armwidth){ //checks for skipping
						greaterthancutoff = false;
						artificialyaxis += armwidth;
						theta--;
						continue;
					}
				}
				else {

					predensity = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(((2 * Math.PI *
							artificialx) / artificialnonarm) + Math.PI + (Math.PI * 1.666667))));

					//System.out.println("Lower Equation: x = " + x + " artx = " + artificialx + 
							//" density = " + predensity + " cutoff = " + cutoff);

					if (predensity > cutoff){
						greaterthancutoff = true;
						if (!pastfirstlower) artificialyaxis += startingpoint;
						else artificialyaxis += nonarmwidth;
						theta--;
						pastfirstlower = true;
						continue;
					}
					if (artificialx >= nonarmwidth){
						greaterthancutoff = true;
						artificialyaxis += nonarmwidth;
						theta--;
						continue;
					}
				}
				
				density[(int) theta][(int) r] = predensity;
			}
		}
		
		//tapering arms in center
		double centerdefinition = 0;
		for (int i = 0; i < maxradius; i++){
			if (density[0][i] == 0) continue;
			centerdefinition = i;
			break;
		}
		double slope;
		for (int i = 0; i < maxtheta; i++){
			slope = -1 * ((10 - density[i][(int) centerdefinition]) / 10);
			for (double j = 0; j < centerdefinition; j++){
				predensity = universal.Function.linearFunction(slope, 10, (j / centerdefinition) * 10);
				density[i][(int) j] = predensity;
			}
		}
		
		for (double i = 0; i < centerdefinition; i++)
			for (int j = 0; j < maxtheta; j++){
				x = (i / centerdefinition) * 10;
				predensity = -0.05 * Math.pow(x, 2) + 10;
				if (density[j][(int) i] > predensity) continue;
				density[j][(int) i] = predensity;
			}
		
		//adding bar
		if (barsize > 0){
			double resolution = (int) (2.75 * maxradius);
			
			double arm1[][] = new double[maxtheta][maxradius];
			for (int i = 0; i < maxradius; i++)
				for (int j = 0; j < maxtheta; j++){
					if (j < 0.5 * maxtheta){
						arm1[j][i] = density[j][i];
					}
					else {
						arm1[j][i] = 0;
					}
				}
			arm1 = offsetArms(arm1);
			
			arm1 = convertToCartesian(arm1, (int) resolution);
			
			//stretches arms
			double coord[];
			for (x = 0; x < resolution; x++)
				for (double y = 0; y < resolution; y++){
					coord = universal.Function.stretch(x, y, 0.71, 1, (int) (0.4 *resolution));
					if (coord[0] >= resolution) continue;
					arm1[(int) x][(int) y] = arm1[(int) coord[0]][(int) y];
				}
			
			//create arm2 as mirror
			double arm2[][] = new double[(int) resolution][(int) resolution];
			
			for (int i = 0; i < resolution; i++)
				for (int j = 0; j < resolution; j++)
					arm2[i][j] = arm1[(int) (resolution - i - 1)][(int) (resolution - j - 1)];
			
			//combines arms to one array
			density = new double[(int) resolution][(int) resolution];
			
			for(int i = 0; i < resolution; i++)
				for (int j = 0; j < resolution; j++){
					density[j][i] = 0;
					if (arm1[j][i] > density[j][i]){
						density[j][i] = arm1[j][i];
					}
					if (arm2[j][i] > density[j][i]){
						density[j][i] = arm2[j][i];
					}
				}
			
			//draws bar between nuclei
			double bardensity;

			for (int j = (int) (resolution * 0.15); j < (resolution * 0.85); j++)
				for (int i = (int) (resolution * 0.3); i < (resolution * 0.7); i++){
					if (density[j][i] == 0){
						bardensity = density[j - 5][i];
						for (int fj = j; fj < resolution; fj++){
							if (bardensity > 9 && density[fj][i] > 8.5) break;
							else if (bardensity > 4 && density[fj][i] > bardensity) break;
							else if (density[fj][i] > 0) break;
							density[fj][i] = bardensity;
						}
					}
				}

			density = convertToPolar(density);
		}
		
		else {
			density = offsetArms(density);
		}
		
		sector = new structural.Sector[maxtheta][maxradius];
		for (int i = 0; i < maxtheta; i++)
			for (int j = 0; j < maxradius; j++){
				sector[i][j] = new structural.Sector(i, j, density[i][j]);
			}
	}
	
	private double[][] offsetArms(double[][] raw){
		double unmodified[] = new double[maxtheta], offset, newvalue; 

		for (double i = 0; i < maxradius; i++){
			for (int j = 0; j < maxtheta; j++)
				unmodified[j] = raw[j][(int) i];
			
			offset = (i * ((grade / 6.0) + 1) * (Math.pow(maxradius, 2) / 100000)) / 2;
			if (barsize > 0) offset = offset / 2;
			
			for (double j = 0; j < maxtheta; j++){
				newvalue = offset + j;
				while(newvalue >= maxtheta) {
					newvalue -= maxtheta;
				}
				raw[(int) j][(int) i] = unmodified[(int) newvalue];
			}
		}
		return raw;
	}
	
	public double calcMass(){
		double universeage = universal.Main.universe.universeage;
		return (((universeage-2)/11) * 5) + 8; //scales to 1 - 5, then offsets by 8 
	}
	public double calcGalaxyAge() {
		double rnum = universal.Main.getRandomDouble(4, 8);
		return rnum;
	}
	public double calcRadius1() {
		return meanradius + 1; //increasing radius by one with small error
	}
	public double calcRadius2() {
		return meanradius - 1; //decreasing radius by one with small error
	}
	public int calcNumberSatellites(){
		double degree = (galaxymass - 6)/2;
		int numsat = (int) Math.pow(10, degree);
		return numsat;
	}
	public int calcBarSize(){
		return universal.Main.getRandomInt(0, 2);
	}
	public int calcGrade(){
		return universal.Main.getRandomInt(0, 5);
	}
	public int calcMajorArms(){
		if (barsize == 0) return universal.Main.getRandomInt(3, 8);
		return 2;
	}
	public int calcMinorArms(){
		int numarms =(int) ((2 - barsize) * majorarms * 1.5);
		return numarms;
	}
	public double calcSOI(){
		return radius1 + 1;
	}
	public String getID(int id){
		return "G0";
	}
}
