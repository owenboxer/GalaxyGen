package universal;

import java.util.Random;

public class Main {
	public static Universe universe;
	
	public static void main(String[] args){
		universe = new Universe();
		universe.initiateUniverse(); 
	}


	public static int getRandomInt(int low, int high){
		Random rng = new Random();
		high++;
		return rng.nextInt(high)+low;
	}
	public static double getRandomDouble(double low, double high){
		Random rng = new Random();
		double rnum = rng.nextDouble();
		return ((high - low) * rnum) + low;
	}

	public static void log(String word){
		System.out.print(word);
	}
}
