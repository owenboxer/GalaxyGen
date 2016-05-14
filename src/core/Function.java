package core;

public class Function {
	
	public static double exponentialFunction(double ratio, double x){
		double y, k = 0;
		if (ratio == 0.7) k = 0.1003;
		else if (ratio == 0.9) k = 0.301;
		else if (ratio == 0.95) k = 0.602;
		y = (Math.pow(Math.pow(10, k), x)) / (Math.pow(Math.pow(10, k), (10 - (1 / k))));
		return y;
	}
	public static double invertedExponentialFunction(double ratio, double x){
		double y, k = 0;
		if (ratio == 0.9) k = 0.301;
		else if (ratio == 0.7) k = 0.1003;
		y = Math.pow(Math.pow(10, k), (1 / k)) / Math.pow(Math.pow(10, k), x);
		return y;
	}
	
	public static double linearFunction(double m, double b, double x){
		double y;
		y = (m * x) + b;
		return y;
	}

	public static boolean radialCheckFunction(double x1, double y1, 
			double x2, double y2, double r){
		boolean check = false;
		x1 = Math.pow((x1 - x2), 2);
		y1 = Math.pow((y1 - y2), 2);
		x1 = Math.sqrt(x1 + y1);
		if (x1 <= r) check = true;
		return check;
	}
	
	public static double[] polarToCartesian(double p, double r){
		double cartesian[] = new double[2];
		cartesian[0] = r * Math.cos(Math.toRadians(p));
		cartesian[1] = r * Math.sin(Math.toRadians(p));
		return cartesian;
	}
	public static double[] cartesianToPolar(double x, double y){
		double polar[] = new double[2];
		polar[0] = Math.toDegrees(Math.atan2(y, x)) + 180;
		polar[1] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return polar;
	}
	public static double[] stretch(double x, double y, double scalexaxis, double scaleyaxis, int zero) {
		double coord[] = new double[2];
		x += zero;
		coord[0] = x * scalexaxis;
		coord[1] = y * scaleyaxis;
		x -= zero;
		return coord;
	}
	
	public static double[][] changeResolutionCartesian(double[][] origin, int resolution){
		double[][] newresolution = new double[resolution][resolution];
		double conversionfactor = (double) origin.length / (double) resolution;
		for (int x = 0; x < resolution; x++)
			for (int y = 0; y < resolution; y++)
				newresolution[x][y] = origin[(int) (x * conversionfactor)][(int) (y * conversionfactor)];
		return newresolution;
	}
	public static double[][] changeResolutionPolar(double[][] origin, int maxradius, int maxtheta){
		double[][] newresolution = new double[maxtheta][maxradius];
		double thetaconversionfactor = (double) origin.length / (double) maxtheta,
				radiusconversionfactor = (double) origin[0].length / (double) maxradius;
		for (int t = 0; t < maxtheta; t++)
			for (int r = 0; r < maxradius; r++)
				newresolution[t][r] = origin[(int) (t * thetaconversionfactor)]
						[(int) (r * radiusconversionfactor)];
		return newresolution;
	}
	public static double[][] arrayToCartesian(double[][] polar, int resolution, int maxtheta, int maxradius){
		int radius = resolution / 2;
		double cartesian[][] = new double[resolution][resolution];
		int x, y;
		double coord[] = new double[2], theta, radial;
		for (int i = 0; i < resolution; i++)
			for (int j = 0; j < resolution; j++){
				// Moves galaxy to center
				x = (i - radius);
				y = (j - radius);
				coord = cartesianToPolar(x, y);
				theta = coord[0];
				radial = coord[1];
				theta -= theta % (360.0 / (maxtheta - 1));
				theta = theta / (360.0 / (maxtheta - 1));
				if (radial >= radius) continue;
				radial = maxradius * (radial / radius);
				cartesian[j][i] = polar[(int) theta][(int) radial];
			}
		
		return cartesian;
	}
	public static double[][] arrayToPolar(double[][] cartesian, int maxtheta, int maxradius){
		double polar[][] = new double[maxtheta][maxradius];
		double coord[] = new double[2], x, y;
		for (int i = 0; i < maxradius; i++)
			for (double j = 0; j < maxtheta; j++){
				x = maxtheta;
				coord = polarToCartesian(j / (x / 360.0), i);
				x = coord[0];
				y = coord[1];
				x = (0.5 * cartesian.length) * (x / maxradius);
				y = (0.5 * cartesian.length) * (y / maxradius);
				x += (0.5 * cartesian.length);
				y += (0.5 * cartesian.length);
				polar[(int) j][i] = cartesian[(int) x][(int) y];
			}
		return polar;
	}

	public static double distanceEquation(double x1, double y1, double x2, double y2){
		double d = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
		return d;
	}
}
