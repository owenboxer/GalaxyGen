package universal;

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
		byte quadrant;
		if (p < 90) quadrant = 1;
		else if (p < 180){
			quadrant = 2;
			p -= 90;
		}
		else if (p < 270){
			quadrant = 3;
			p -= 180;
		}
		else {
			quadrant = 4;
			p -= 270;
		}
		
		double cartesian[] = new double[2], x = r * Math.sin(p), y = r * Math.cos(p);
		switch(quadrant){
		case 1:
			cartesian[0] = x;
			cartesian[1] = y;
			break;
		case 2:
			cartesian[0] = -1 * y;
			cartesian[1] = x;
			break;
		case 3:
			cartesian[0] = -1 * x;
			cartesian[1] = -1 * y;
			break;
		case 4:
			cartesian[0] = y;
			cartesian[1] = -1 * x;
			break;
		}
		
		return cartesian;
	}
	public static double[] cartesianToPolar(double x, double y){
		double polar[] = new double[2];
		polar[0] = Math.toDegrees(Math.atan2(y, x)) + 180;
		polar[1] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return polar;
	}
}
