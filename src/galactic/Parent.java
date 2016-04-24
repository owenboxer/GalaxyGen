package galactic;

import util.FileHandler;

public class Parent extends Galaxy {

	public int numberSatellites, barSize, majorArms, minorArms;
	public double soi, grade;

	public Parent() {
		super(0);
	}

	protected void packData(){
		packedData = new String[5];
		packedData[0] = new StringBuilder().append(galaxyAge).toString();
		packedData[1] = new StringBuilder().append(barSize).toString();
		packedData[2] = new StringBuilder().append(grade).toString();
		packedData[3] = new StringBuilder().append(majorArms).toString();
		packedData[4] = new StringBuilder().append(minorArms).toString();

		//densities:
		packedDensities = new String[18000];
		packedIonized = new String[18000];
		for (int t = 0; t < 180; t++)
			for (int r = 0; r < 100; r++){
				packedDensities[(180 * r) + t] = new StringBuilder().append(density[t][r]).toString();
				packedIonized[(180 * r) + t] = new StringBuilder().append(ionizedMatter[t][r]).toString();
			}
		
		//for (int i = 0; i < packedData.length; i++)
		
	}
	protected void unpackData(){
		galaxyAge = Double.valueOf(packedData[0]).doubleValue();
		barSize = Integer.valueOf(packedData[1]).intValue();
		grade = Double.valueOf(packedData[2]).doubleValue();
		majorArms = Integer.valueOf(packedData[3]).intValue();
		minorArms = Integer.valueOf(packedData[4]).intValue();
		packedData = null;

		for (int t = 0; t < 180; t++)
			for (int r = 0; r < 100; r++) {
				density[t][r] = Double.valueOf(packedDensities[(180 * r) + t]).doubleValue();
				ionizedMatter[t][r] = Double.valueOf(packedIonized[(180 * r) + t]).doubleValue();
		}
		packedDensities = null;
		packedIonized = null;
	}

	public void initiateGalaxy() {
		rGalaxy = 0;
		tGalaxy = 0;
		galaxyMass = calcMass();
		maxRadius = calcMaxRadius();
		maxTheta = calcMaxTheta();
		hiResDensity = new double[maxTheta][maxRadius];
		
		if (core.Main.universe.fromSave) {
			numberSatellites = getNumberSatellites();
			unpackData();
		}
		else {
			numberSatellites = calcNumberSatellites();
			galaxyAge = calcGalaxyAge();
			barSize = calcBarSize();
			grade = calcGrade();
			majorArms = calcMajorArms();
			minorArms = calcMinorArms();
		}
	}

	public void createSectors(){
		setHiResDensities();
		density = core.Function.changeResolutionPolar(hiResDensity, 100, 180);

		double number = 0;
		sector = new structural.Sector[180][100];
		for (int t = 0; t < 180; t++)
			for (int r = 0; r < 100; r++) {
				sector[t][r] = new structural.Sector(t, r, density[t][r], galaxyAge);
				sector[t][r].initiateSector();
				number += sector[t][r].total;
			}

		// System.out.println(number + " " + galaxyMass);

		// structural.Sector test = new structural.Sector(0, 0, 10, 13.12);
		// test.initiateSector();
		/*
		 * structural.Sector test[] = new structural.Sector[100]; for (double i
		 * = 0; i < 100; i++){ test[(int) i] = new structural.Sector(0, (int) i,
		 * (double) (100 - i) / 10, 13.21); test[(int) i].initiateSector();
		 * number += test[(int) i].total; }
		 */

		for (int t = 0; t < 180; t++)
			for (int r = 0; r < 100; r++)
				ionizedMatter[t][r] = sector[t][r].ionizedMatter;
	}

	public void setHiResDensities() {

		double armWidth = (maxRadius + (grade / 5)) / majorArms; // armwidth
																	// remains
																	// constant
		if (barSize == 1)
			armWidth = 0.75 * armWidth;
		else if (barSize == 2)
			armWidth = 0.9 * armWidth;

		// these variables change with radius
		double amplitude, circumference, nonArmWidth, artificialArm, artificialNonArm, cutoff, radius, startingPoint;

		// these variables change with polar
		double x, preDensity, theta, artificialYAxis, artificialX;

		// required for switching between equations
		boolean greaterThanCutoff, pastFirstLower;

		double centerDefinition = 0;
		boolean centerDefined = false;

		// runs though radial values
		for (double r = 0; r < maxRadius; r++) {

			// setting variables that are dependent on radius
			radius = r + 1; // since a circle with a radius of 0 has a
							// circumference of 0, radius must
							// begin at 1
			amplitude = core.Function.linearFunction(-0.4, 8, (r / maxRadius) * 10);
			circumference = 2 * Math.PI * radius;
			nonArmWidth = (circumference - (armWidth * majorArms)) / majorArms;
			cutoff = amplitude / 2;

			// pre-setting some polar-dependent variables
			artificialYAxis = 0; // this is required because
			artificialArm = 1.5 * armWidth;
			artificialNonArm = 3.0 * nonArmWidth;
			greaterThanCutoff = false;
			pastFirstLower = false;

			// finding starting point so that the peaks of the upper cosine
			// functions line up
			startingPoint = nonArmWidth - (((Math.PI * radius) / majorArms) - (0.5 * armWidth));

			// since the the width of the combined arms is greater than the
			// circumferences of circles
			// at the galactic center, these values must be set by other means
			if (armWidth * majorArms > circumference)
				for (theta = 0; theta < maxTheta; theta++)
					hiResDensity[(int) theta][(int) r] = 0;

			// running through theta values
			else
				for (theta = 0; theta < maxTheta; theta++) {

					if (!centerDefined) {
						centerDefinition = radius;
						centerDefined = true;
					}

					x = (theta / maxTheta) * circumference;

					if (!pastFirstLower)
						artificialX = startingPoint + x;
					else
						artificialX = x - artificialYAxis;

					if (greaterThanCutoff) {

						preDensity = (0.75 * amplitude) + (0.5 * amplitude * (Math
								.cos(((2 * Math.PI * artificialX) / artificialArm) + Math.PI + (Math.PI * 0.333334))));

						// System.out.println("Upper Equation: x = " + x + "
						// artx = " + artificialx +
						// " density = " + predensity + " cutoff = " + cutoff);

						if (preDensity <= cutoff) {
							greaterThanCutoff = false;
							artificialYAxis += armWidth;
							theta--;
							continue;
						}
						if (artificialX >= armWidth) { // checks for skipping
							greaterThanCutoff = false;
							artificialYAxis += armWidth;
							theta--;
							continue;
						}
						hiResDensity[(int) theta][(int) r] = preDensity;
					} else {
						preDensity = (0.75 * amplitude) + (0.5 * amplitude * (Math.cos(
								((2 * Math.PI * artificialX) / artificialNonArm) + Math.PI + (Math.PI * 1.666667))));

						// System.out.println("Lower Equation: x = " + x + "
						// artx = " + artificialx +
						// " density = " + predensity + " cutoff = " + cutoff);

						if (preDensity > cutoff) {
							greaterThanCutoff = true;
							if (!pastFirstLower)
								artificialYAxis += startingPoint;
							else
								artificialYAxis += nonArmWidth;
							theta--;
							pastFirstLower = true;
							continue;
						}
						if (artificialX >= nonArmWidth) {
							greaterThanCutoff = true;
							artificialYAxis += nonArmWidth;
							theta--;
							continue;
						}
					}
				}
		}

		// tapering arms in center
		double slope;
		for (int t = 0; t < maxTheta; t++) {
			slope = -1 * ((10 - hiResDensity[t][(int) centerDefinition]) / 10);
			for (double r = 0; r < centerDefinition; r++) {
				preDensity = core.Function.linearFunction(slope, 10, (r / centerDefinition) * 10);
				hiResDensity[t][(int) r] = preDensity;
			}
		}

		// expanding scope slightly
		double temp[][] = hiResDensity;
		hiResDensity = new double[maxTheta][(int) (maxRadius * 1.25)];
		for (int t = 0; t < maxTheta; t++)
			for (int r = 0; r < maxRadius; r++)
				hiResDensity[t][r] = temp[t][r];
		maxRadius *= 1.25;

		/*
		 * if (minorarms > 0){ temp = density;
		 * 
		 * int mainarm[] = new int[minorarms], startingradius, startingtheta,
		 * minorarmlength; double arm[][][] = new
		 * double[minorarms][maxtheta][maxradius], mainarmwidth; mainarmwidth =
		 * (int) (maxtheta / majorarms); boolean left[] = new
		 * boolean[minorarms], retry;
		 * 
		 * for (int i = 0; i < minorarms; i++){
		 * 
		 * //placing arms retry = false; left[i] = true;
		 * 
		 * mainarm[i] = universal.Main.getRandomInt(0, majorarms - 1); for (int
		 * fi = 0; fi < i; fi++) if (mainarm[i] == mainarm[fi]){ if (left[fi] ==
		 * true) left[i] = false; else left[i] = true; } for (int fi = 0; fi <
		 * i; fi++) if (left[i] == left[fi]) retry = true; if (retry){ i--;
		 * continue; } startingtheta = (int) ((mainarm[i] * mainarmwidth) + (0.5
		 * * mainarmwidth)); if (startingtheta > maxtheta) startingtheta -=
		 * maxtheta; startingradius = universal.Main.getRandomInt(0, (int) (0.5
		 * * maxradius) - 1); minorarmlength = universal.Main.getRandomInt((int)
		 * (maxradius * 0.5), maxradius - startingradius);
		 * 
		 * System.out.println("HI");
		 * 
		 * //copying arm for (int t = (int) (startingtheta - (mainarmwidth *
		 * 0.5)); t < startingtheta + (mainarmwidth * 0.5) - 1; t++) for (int r
		 * = startingradius; r < startingradius + minorarmlength - 1; r++){ if
		 * (density[t][r] > 3) arm[i][t][r] = density[t][r]; }
		 * 
		 * //modifying arm for (int t = 0; t < maxtheta; t++) for (int r = 0; r
		 * < maxradius; r++){ if (r > minorarmlength - startingradius)
		 * arm[i][t][r] = 0; else arm[i][t][r] = arm[i][t][r + startingradius];
		 * }
		 * 
		 * if (left[i]) arm[i] = unilateralOffset(arm[i], maxradius / (double)
		 * (-10 / majorarms)); else arm[i] = unilateralOffset(arm[i], maxradius
		 * / (double) (10 / majorarms));
		 * 
		 * for (int t = 0; t < maxtheta; t++) for (int r = 0; r < maxradius;
		 * r++){ if (r < startingradius) arm[i][t][r] = 0; else arm[i][t][r] =
		 * arm[i][t][r - startingradius] - 2; } }
		 * 
		 * //combining arms for (int t = 0; t < maxtheta; t++) for (int r = 0; r
		 * < maxradius; r++) for (int i = 0; i < minorarms; i++){
		 * System.out.println(arm[i][t][r] + "  " + density[t][r]; if
		 * (arm[i][t][r] > density[t][r]){ System.out.println("hi");
		 * density[t][r] = arm[i][t][r]; } }
		 * 
		 * if (!density.equals(temp)) System.out.println("Change");
		 * 
		 * }
		 */

		// adding bar
		if (barSize > 0) {

			// adding bulge
			for (double r = 0; r < centerDefinition; r++)
				for (int t = 0; t < maxTheta; t++) {
					x = (r / centerDefinition) * 10;
					preDensity = -0.05 * Math.pow(x, 2) + 10;
					if (hiResDensity[t][(int) r] > preDensity)
						continue;
					hiResDensity[t][(int) r] = preDensity;
				}

			// splitting galaxy in two
			double arm1[][] = new double[maxTheta][maxRadius];
			for (int r = 0; r < maxRadius; r++)
				for (int t = 0; t < maxTheta; t++) {
					if (t < 0.5 * maxTheta) {
						arm1[t][r] = hiResDensity[t][r];
					} else {
						arm1[t][r] = 0;
					}
				}

			arm1 = offsetArms(arm1);
			double resolution = (int) (2.75 * maxRadius);
			arm1 = core.Function.arrayToCartesian(arm1, (int) resolution, maxTheta, maxRadius);

			// offsetting arm origin
			double barlength;
			if (barSize == 1)
				barlength = 0.12;
			else
				barlength = 0.25;

			temp = arm1;
			arm1 = new double[(int) (resolution * (barlength + 1))][(int) (resolution * 1.5)];

			for (int xx = 0; xx < (int) resolution; xx++) {
				for (int yy = (int) 0; yy < resolution; yy++) {
					arm1[xx][(int) (yy + (resolution * (barlength * 0.5)))] = temp[xx][yy];
				}
			}
			temp = new double[0][0];
			resolution = (int) (resolution * (barlength + 1));

			// create arm2 as mirror
			double arm2[][] = new double[(int) resolution][(int) resolution];

			for (int xx = 0; xx < resolution; xx++)
				for (int yy = 0; yy < resolution; yy++)
					arm2[xx][yy] = arm1[(int) (resolution - xx - 1)][(int) (resolution - yy - 1)];

			// combines arms to one array
			hiResDensity = new double[(int) resolution][(int) resolution];

			for (int xx = 0; xx < resolution; xx++)
				for (int yy = 0; yy < resolution; yy++) {
					hiResDensity[xx][yy] = 0;
					if (arm1[xx][yy] > hiResDensity[xx][yy]) {
						hiResDensity[xx][yy] = arm1[xx][yy];
					}
					if (arm2[xx][yy] > hiResDensity[xx][yy]) {
						hiResDensity[xx][yy] = arm2[xx][yy];
					}
				}

			// finds space for bar
			int barXLimit = 0, barYLimit = 0;
			double checkLimit = 0;
			boolean broke = false;

			if (barSize == 1)
				checkLimit = 0.55;
			else
				checkLimit = 0.6;

			for (int xx = (int) (0.5 * resolution); xx < resolution; xx++) {
				for (int yy = (int) (0.5 * resolution); yy < checkLimit * resolution; yy++)
					if (hiResDensity[xx][yy] > 0) {
						barYLimit = (yy - (int) (0.5 * resolution));
						broke = true;
						break;
					}
				if (broke)
					break;
			}

			for (int xx = (int) (0.5 * resolution); xx < resolution; xx++)
				if (hiResDensity[xx][(int) ((0.5 * resolution) - barYLimit + 1)] > 0) {
					barXLimit = xx - (int) (0.5 * resolution);
					break;
				}

			// draws bar between nuclei
			double barDensity;

			for (int xx = (int) (0.5 * resolution) - barXLimit; xx < (0.5 * resolution) + barXLimit; xx++)
				for (int yy = (int) ((0.5 * resolution) - barYLimit); yy < (0.5 * resolution) + barYLimit; yy++) {
					if (hiResDensity[xx][yy] == 0) {
						barDensity = hiResDensity[xx - (int) (resolution / 90)][yy];
						for (int fxx = xx; fxx < (0.5 * resolution) + barXLimit; fxx++) {
							if (fxx > (0.5 * resolution) && hiResDensity[fxx][yy] > 0)
								break;
							hiResDensity[fxx][yy] = barDensity;
						}
					}
				}
			hiResDensity = core.Function.arrayToPolar(hiResDensity, maxTheta, maxRadius);
		}

		else {
			hiResDensity = offsetArms(hiResDensity);
		}

		// adding bulge
		if (barSize == 2)
			centerDefinition *= 1.1;
		if (barSize == 0 && majorArms == 2)
			centerDefinition *= 2.5;

		for (double r = 0; r < centerDefinition; r++)
			for (int t = 0; t < maxTheta; t++) {
				x = (r / centerDefinition) * 10;
				preDensity = -0.05 * Math.pow(x, 2) + 10;
				if (hiResDensity[t][(int) r] > preDensity)
					continue;
				hiResDensity[t][(int) r] = preDensity;
			}

		// shrinking to minimum
		temp = hiResDensity;
		int edge = 0;

		boolean check = false;

		for (int r = maxRadius - 1; r >= 0; r--) {
			for (int t = maxTheta - 1; t >= 0; t--)
				if (temp[t][r] > 0) {
					edge = r + 1;
					check = true;
					break;
				}
			if (check)
				break;
		}

		maxRadius = edge;
		hiResDensity = new double[maxTheta][maxRadius];

		for (int t = 0; t < maxTheta; t++)
			for (int r = 0; r < maxRadius; r++)
				hiResDensity[t][r] = temp[t][r];

		// filling background with cutoff based densities
		temp = new double[maxTheta][maxRadius];

		for (double r = 0; r < maxRadius; r++) {
			cutoff = core.Function.linearFunction(-0.4, 8, (r / maxRadius) * 10) / 2 - 1;
			for (int t = 0; t < maxTheta; t++)
				temp[t][(int) r] += cutoff;
		}

		// combining background with arms
		for (int t = 0; t < maxTheta; t++)
			for (int r = 0; r < maxRadius; r++)
				if (hiResDensity[t][r] == 0)
					hiResDensity[t][r] = temp[t][r];
	}
	private double[][] offsetArms(double[][] raw) {
		double unmodified[] = new double[maxTheta], offset, newValue;

		for (double i = 0; i < maxRadius; i++) {
			for (int j = 0; j < maxTheta; j++)
				unmodified[j] = raw[j][(int) i];

			offset = i * ((grade / 6.0) + 1);
			if (barSize == 2)
				offset = offset / 2;
			if (barSize == 1)
				offset = offset / 1.2;
			offset = (offset / 100) * 150;

			for (double j = 0; j < maxTheta; j++) {
				newValue = offset + j;
				while (newValue >= maxTheta) {
					newValue -= maxTheta;
				}
				raw[(int) j][(int) i] = unmodified[(int) newValue];
			}
		}
		return raw;
	}
	public double[][] unilateralOffset(double[][] raw, double offset) {
		if (offset < 0)
			offset = maxTheta - offset;

		double unmodified[] = new double[maxTheta], newValue;

		for (double i = 0; i < maxRadius; i++) {
			for (int j = 0; j < maxTheta; j++)
				unmodified[j] = raw[j][(int) i];

			for (double j = 0; j < maxTheta; j++) {
				newValue = offset + j;
				while (newValue >= maxTheta) {
					newValue -= maxTheta;
				}
				raw[(int) j][(int) i] = unmodified[(int) newValue];
			}
		}
		return raw;
	}


	public void display() {
	}


	public double calcMass() {
		double universeAge = core.Main.universe.universeAge;
		return (((universeAge - 2) / 11) * 5) + 8; // scales to 1 - 5, then
													// offsets by 8
	}
	public double calcGalaxyAge() {
		double rNum = core.Main.getRandomDouble(4, 8);
		return rNum;
	}
	public int calcNumberSatellites() {
		double degree = (galaxyMass - 6) / 2;
		int numSat = (int) Math.pow(10, degree);
		return numSat;
	}
	public int calcBarSize() {
		return 0;//core.Main.getRandomInt(0, 2);
	}
	public int calcGrade() {
		return core.Main.getRandomInt(0, 5);
	}
	public int calcMajorArms() {
		if (barSize == 0) {
			int arms = core.Main.getRandomInt(0, 1);
			if (arms == 0)
				arms = 2;
			else
				arms = core.Main.getRandomInt(4, 6);
			return arms;
		}
		return 3;
	}
	public int calcMinorArms() {
		if (barSize == 2)
			return 0;
		if (majorArms > 2)
			return 6 - majorArms;
		return core.Main.getRandomInt(2, 4);
	}

	public int getNumberSatellites() {
		int numberSatellites = 0;
		String test;

		do {
			numberSatellites++;
			test = "G" + numberSatellites;
		} while (FileHandler.checkExists(core.Main.universe.dirPath + test));
		numberSatellites -= 1;

		return numberSatellites;
	}
}