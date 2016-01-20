package galactic;

public class Main extends Galaxy{

	public int numbersatellites, barsize, majorarms, minorarms;
	public double soi, grade;

	public Main(boolean satellite){
		super(satellite);
		getID(0);
	}

	public void initiateGalaxy() {
		rgalaxy = 0;
		tgalaxy = 0;
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
		actualradius = calcActualRadius();
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
		double armwidth = (maxradius + (grade / 5)) / majorarms; //armwidth remains constant
		if (barsize == 1) armwidth = 0.75 * armwidth;
		else if (barsize == 2) armwidth = 0.9 * armwidth;
		double amplitude, circumference, nonarmwidth, artificialarm, artificialnonarm, cutoff, 
				radius, startingpoint; //these variables change with radius
		double x, predensity, theta, artificialyaxis, artificialx; //these variables change with polar
		boolean greaterthancutoff, pastfirstlower; //required for switching between equations
		double centerdefinition = 0;
		boolean centerdefined = false;

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

				if (!centerdefined){
					centerdefinition = radius;
					centerdefined = true;
				}

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
					density[(int) theta][(int) r] = predensity;
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
			}
		}
		
		//tapering arms in center
		double slope;
		for (int t = 0; t < maxtheta; t++){
			slope = -1 * ((10 - density[t][(int) centerdefinition]) / 10);
			for (double r = 0; r < centerdefinition; r++){
				predensity = universal.Function.linearFunction(slope, 10, (r / centerdefinition) * 10);
				density[t][(int) r] = predensity;
			}
		}
		
		//expanding scope slightly
		double temp[][] = density;
		density = new double[maxtheta][(int) (maxradius * 1.25)];
		for (int t = 0; t < maxtheta; t++)
			for (int r = 0; r < maxradius; r++)
				density[t][r] = temp[t][r];
		maxradius *= 1.25;

		/*if (minorarms > 0){
			temp = density;
			
			int mainarm[] = new int[minorarms], startingradius, startingtheta, minorarmlength;
			double arm[][][] = new double[minorarms][maxtheta][maxradius], mainarmwidth;
			mainarmwidth = (int) (maxtheta / majorarms);
			boolean left[] = new boolean[minorarms], retry;
			
			for (int i = 0; i < minorarms; i++){
				
				//placing arms
				retry = false;
				left[i] = true;
				
				mainarm[i] = universal.Main.getRandomInt(0, majorarms - 1);
				for (int fi = 0; fi < i; fi++)
					if (mainarm[i] == mainarm[fi]){
						if (left[fi] == true) left[i] = false;
						else left[i] = true;
					}
				for (int fi = 0; fi < i; fi++)
					if (left[i] == left[fi]) retry = true;
				if (retry){
					i--;
					continue;
				}
				startingtheta = (int) ((mainarm[i] * mainarmwidth) + (0.5 * mainarmwidth));
				if (startingtheta > maxtheta) startingtheta -= maxtheta;
				startingradius = universal.Main.getRandomInt(0, (int) (0.5 * maxradius) - 1);
				minorarmlength = universal.Main.getRandomInt((int) (maxradius * 0.5), 
						maxradius - startingradius);
				
				System.out.println("HI");
				
				//copying arm
				for (int t = (int) (startingtheta - (mainarmwidth * 0.5)); t < startingtheta +
						(mainarmwidth * 0.5) - 1; t++)
					for (int r = startingradius; r < startingradius + minorarmlength - 1; r++){
						if (density[t][r] > 3) arm[i][t][r] = density[t][r];
					}
				
				//modifying arm
				for (int t = 0; t < maxtheta; t++)
					for (int r = 0; r < maxradius; r++){
						if (r > minorarmlength - startingradius) arm[i][t][r] = 0;
						else arm[i][t][r] = arm[i][t][r + startingradius];
					}
				
				if (left[i]) arm[i] = unilateralOffset(arm[i], maxradius / (double) (-10 / majorarms));
				else arm[i] = unilateralOffset(arm[i], maxradius / (double) (10 / majorarms));
				
				for (int t = 0; t < maxtheta; t++)
					for (int r = 0; r < maxradius; r++){
						if (r < startingradius) arm[i][t][r] = 0;
						else arm[i][t][r] = arm[i][t][r - startingradius] - 2;
					}
			}
			
			//combining arms
			for (int t = 0; t < maxtheta; t++)
				for (int r = 0; r < maxradius; r++)
					for (int i = 0; i < minorarms; i++){
						System.out.println(arm[i][t][r] + "  " + density[t][r];
						if (arm[i][t][r] > density[t][r]){
							System.out.println("hi");
							density[t][r] = arm[i][t][r];
						}
					}
			
			if (!density.equals(temp)) System.out.println("Change");
					
		}*/
		
		//adding bar
		if (barsize > 0){
	
			//adding bulge
			for (double r = 0; r < centerdefinition; r++)
				for (int t = 0; t < maxtheta; t++){
					x = (r / centerdefinition) * 10;
					predensity = -0.05 * Math.pow(x, 2) + 10;
					if (density[t][(int) r] > predensity) continue;
					density[t][(int) r] = predensity;
				}
	
			//splitting galaxy in two
			double arm1[][] = new double[maxtheta][maxradius];
			for (int r = 0; r < maxradius; r++)
				for (int t = 0; t < maxtheta; t++){
					if (t < 0.5 * maxtheta){
						arm1[t][r] = density[t][r];
					}
					else {
						arm1[t][r] = 0;
					}
				}
	
			arm1 = offsetArms(arm1);
			double resolution = (int) (2.75 * maxradius);
			arm1 = convertToCartesian(arm1, (int) resolution);

			//offsetting arm origin
			double barlength;
			if (barsize == 1) barlength = 0.12;
			else barlength = 0.25;

			temp = arm1;
			arm1 = new double[(int) (resolution * (barlength + 1))][(int) (resolution * 1.5)];

			for (int xx = 0; xx < (int) resolution; xx++){
				for (int yy = (int) 0; yy < resolution; yy++){
					arm1[xx][(int) (yy + (resolution * (barlength * 0.5)))] = temp[xx][yy];	
				}
			}
			temp = new double[0][0];
			resolution = (int) (resolution * (barlength + 1));

			//create arm2 as mirror
			double arm2[][] = new double[(int) resolution][(int) resolution];

			for (int xx = 0; xx < resolution; xx++)
				for (int yy = 0; yy < resolution; yy++)
					arm2[xx][yy] = arm1[(int) (resolution - xx - 1)][(int) (resolution - yy - 1)];
 
			//combines arms to one array
			density = new double[(int) resolution][(int) resolution];

			for(int xx = 0; xx < resolution; xx++)
				for (int yy = 0; yy < resolution; yy++){
					density[xx][yy] = 0;
					if (arm1[xx][yy] > density[xx][yy]){
						density[xx][yy] = arm1[xx][yy];
					}
					if (arm2[xx][yy] > density[xx][yy]){
						density[xx][yy] = arm2[xx][yy];
					}
				}

			//finds space for bar
			int barxlimit = 0, barylimit = 0;
			double checklimit = 0;
			boolean broke = false;

			if (barsize == 1) checklimit = 0.55;
			else checklimit = 0.6;

			for (int xx = (int) (0.5 * resolution); xx < resolution; xx++){
				for (int yy = (int) (0.5 * resolution); yy < checklimit * resolution; yy++)
					if (density[xx][yy] > 0){ 
						barylimit = (yy - (int) (0.5 * resolution));
						broke = true;
						break;
					}
				if (broke) break;
			}

			for (int xx = (int) (0.5 * resolution); xx < resolution; xx++)
				if (density[xx][(int) ((0.5 * resolution) - barylimit + 1)] > 0){
					barxlimit = xx - (int) (0.5 * resolution);
					break;
				}

			//draws bar between nuclei
			double bardensity;

			for (int xx = (int) (0.5 * resolution) - barxlimit; xx < (0.5 * resolution) + barxlimit; 
					xx++)
				for (int yy = (int) ((0.5 * resolution) - barylimit); yy < (0.5 * resolution) 
						+ barylimit; yy++){
					if (density[xx][yy] == 0){
						bardensity = density[xx - (int) (resolution / 90)][yy];
						for (int fxx = xx; fxx < (0.5 * resolution) + barxlimit; fxx++){
							if (fxx > (0.5 * resolution) && density[fxx][yy] > 0) break;
							density[fxx][yy] = bardensity;
						}
					}
				}
			density = convertToPolar(density);
		}

		else {
			density = offsetArms(density);
		}

		//adding bulge
		if (barsize == 2) centerdefinition *= 1.1;  
		if (barsize == 0 && majorarms == 2) centerdefinition *= 2.5;

		for (double r = 0; r < centerdefinition; r++)
			for (int t = 0; t < maxtheta; t++){
				x = (r / centerdefinition) * 10;
				predensity = -0.05 * Math.pow(x, 2) + 10;
				if (density[t][(int) r] > predensity) continue;
				density[t][(int) r] = predensity;
			}

		//shrinking to minimum
		temp = density;
		int edge = 0;

		boolean check = false;

		for (int r = maxradius - 1; r >= 0; r--){
			for (int t = maxtheta - 1; t >= 0; t--)
				if (temp[t][r] > 0){
					edge = r + 1;
					System.out.println(edge);
					check = true;
					break;
				}
			if (check) break;
		}

		maxradius = edge;
		density = new double[maxtheta][maxradius];

		for (int t = 0; t < maxtheta; t++)
			for (int r = 0; r < maxradius; r++)
				density[t][r] = temp[t][r];

		//filling background with cutoff based densities
		temp = new double[maxtheta][maxradius];

		for (double r = 0; r < maxradius; r++){
			cutoff = universal.Function.linearFunction(-0.4, 8, (r / maxradius) * 10) / 2 - 1;
			for (int t = 0; t < maxtheta; t++)
				temp[t][(int) r] += cutoff;
		}

		//combining background with arms
		for (int t = 0; t < maxtheta; t++)
			for (int r = 0; r < maxradius; r++)
				if (density[t][r] == 0) density[t][r] = temp[t][r]; 

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
	
			offset = i * ((grade / 6.0) + 1);
			if (barsize == 2) offset = offset / 2;
			if (barsize == 1) offset = offset / 1.2;
			offset = (offset / 100) * 150;

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
	public double[][] unilateralOffset(double[][] raw, double offset){
		if (offset < 0) offset = maxtheta - offset;

		double unmodified[] = new double[maxtheta], newvalue; 

		for (double i = 0; i < maxradius; i++){
			for (int j = 0; j < maxtheta; j++)
				unmodified[j] = raw[j][(int) i];
	
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
		if (barsize == 0){
			int arms = universal.Main.getRandomInt(0, 1);
			if (arms == 0) arms = 2;
			else arms = universal.Main.getRandomInt(4, 6);
			return arms;
		}
		return 2;
	}
	public int calcMinorArms(){
		if (barsize == 2) return 0;
		if (majorarms > 2) return 6 - majorarms;
		return universal.Main.getRandomInt(2, 4);
	}
	public double calcSOI(){
		return radius1 + 1;
	}
	public String getID(int id){
		return "G0";
	}
}