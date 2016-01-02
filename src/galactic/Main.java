package galactic;

public class Main extends Galaxy{
	public int numbersatellites, barsize, grade, majorarms, minorarms;
	public double soi;
	
	public Main(boolean satellite){
		super(satellite);
		getID(0);
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
	public void createSectors(){

		double density[][] = new double[maxtheta][maxradius];
		double armwidth;
		double amplitude, circumference, nonarmwidth, artificialarm, artificialnonarm, cutoff, 
				radius;
		double x = 0, predensity = 0, polar, artificialyaxis, artificialx = 0;
		boolean greaterthancutoff = false;
		armwidth = (maxradius + (grade / 5)) / majorarms;

		for (double r = 0; r < maxradius; r++){

			radius = r + 1;
			amplitude = universal.Function.linearFunction(-0.4, (r / maxradius) * 10) - 2;
			circumference = 2 * Math.PI * radius;
			nonarmwidth = (circumference - (armwidth * majorarms)) / majorarms;
			cutoff = amplitude / 2;
			artificialyaxis = 0;
			artificialarm = 1.5 * armwidth;
			artificialnonarm = 3.0 * nonarmwidth;
			greaterthancutoff = false;
			
			if (armwidth * majorarms > circumference)
				for (polar = 0; polar < maxtheta; polar++)
					density[(int) polar][(int) r] = amplitude * 1.25;

			else for (polar = 0; polar < maxtheta; polar++){

				x = (polar / maxtheta) * circumference;
				artificialx = x - artificialyaxis;

				if (greaterthancutoff){

					predensity = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(((2 * Math.PI * 
							artificialx) / artificialarm) + Math.PI + (Math.PI * 0.333333))));

					if (predensity <= cutoff + 0.0005){ //accounts for Math.PI error
						greaterthancutoff = false;
						artificialyaxis += armwidth;
						polar--;
						continue;
					}
					if (artificialx >= armwidth){ //checks for skipping
						greaterthancutoff = false;
						artificialyaxis += armwidth;
						polar--;
						continue;
					}
				}
				else {

					predensity = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(((2 * Math.PI *
							artificialx) / artificialnonarm) + Math.PI + (Math.PI * 1.666667))));

					if (predensity > cutoff){
						greaterthancutoff = true;
						artificialyaxis += nonarmwidth;
						polar--;
						continue;
					}
					if (artificialx >= nonarmwidth){
						greaterthancutoff = true;
						artificialyaxis += armwidth;
						polar--;
						continue;
					}
				}
				
				density[(int) polar][(int) r] = predensity;
			}
		}
		
		double unmodified[] = new double[maxtheta];
		double offset; 
		int newvalue; 
		
		for (double i = 0; i < maxradius; i++){
			for (int j = 0; j < maxtheta; j++)
				unmodified[j] = density[j][(int) i];
			
			offset = ((universal.Function.exponentialFunction(0.7, (i / maxradius) * 10) 
					- 0.993116)) * ((grade + 1) / (i + 1)) * maxradius * maxtheta * 0.01;
			
			for (int j = 0; j < maxtheta; j++){
				newvalue = (int) offset + j;
				while(newvalue >= maxtheta) {
					newvalue -= maxtheta;
				}
				density[j][(int) i] = unmodified[newvalue];
			}
		}
		
		sector = new structural.Sector[maxtheta][maxradius];
		for (int i = 0; i < maxtheta; i++)
			for (int j = 0; j < maxradius; j++){
				sector[i][j] = new structural.Sector(i, j, density[i][j]);
			}
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
		if (barsize == 0) return universal.Main.getRandomInt(3, 6);
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
