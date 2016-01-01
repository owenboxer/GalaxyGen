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
	}
	public void createSectors(){

		double density[][] = new double[30][maxradius];
		double armwidth;
		double amplitude, circumference, offset, nonarmwidth;
		double y, x = 0, f = 0, radius, polar, artificialyaxis, artificialx = 0;
		boolean greaterthany = false;
		armwidth = ((1.5 * Math.PI) + ((grade - 2.5) / 5)) / majorarms;

		for (double r = 0; r < maxradius; r++){

			radius = r + 1;
			amplitude = universal.Function.linearFunction(-0.4, (radius / maxradius)) - 2;
			circumference = 2 * Math.PI * radius;
			offset = 0;
			//offset = ((((universal.Function.exponentialFunction(0.7, (r / maxradius) * 10) - 0.993116))
			//		* ((grade + 1) / radius)) * 2 * Math.PI) * 50;
			nonarmwidth = (circumference - (armwidth * majorarms)) / majorarms;
			y = amplitude / 2;
			artificialyaxis = 0;
			greaterthany = false;

			for (polar = 0; polar < 30; polar++){

				x = (polar / 30) * circumference;
				artificialx = x - artificialyaxis;

				if (greaterthany){

					f = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(((2 * Math.PI * 
							artificialx) / armwidth) + Math.PI + (Math.PI * 0.333333) + offset)));

					System.out.println("Upper Equation: x = " + x + " f = " + f + " y = " + y + " Art = " + artificialx);

					if (f <= y){
						greaterthany = false;
						artificialyaxis += 0.666667 * armwidth;
						System.out.println("Intercept = " + artificialyaxis);
						polar--;
						continue;
					}
				}
				else {

					f = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(((2 * Math.PI *
							artificialx) / nonarmwidth) + Math.PI + (Math.PI * 1.666667) + offset)));

					System.out.println("Lower Equation: x = " + x + " f = " + f + " y = " + y + " Art = " + artificialx);

					if (f > y){
						greaterthany = true;
						artificialyaxis += 0.333333 * nonarmwidth;
						//System.out.println("Intercept = " + artificialyaxis);
						polar--;
						continue;
					}
				}
				
				density[(int) polar][(int) r] = f;
			}
		}
		
		sector = new structural.Sector[30][maxradius];
		for (int i = 0; i < 30; i++)
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
		if (barsize == 0) return universal.Main.getRandomInt(2, 6);
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
