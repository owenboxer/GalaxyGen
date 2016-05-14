package structural;

public class SuperParticle {
	public double xx, yy;
	public double direction, magnitude;
	public double mass;

	public final static double GRAVITATIONAL_CONSTANT = 1,
			EXPANSION_CONSTANT = 1, SPEED_OF_LIGHT = 1;

	public SuperParticle(int xx, int yy){
		this.xx = xx;
		this.yy = yy;
		
		genVector();
		
		//System.out.println("v: " + magnitude);
	}

	

	
	private void genVector(){
		direction = core.Main.getRandomDouble(0, 360);
		magnitude = core.Function.gaussianDistribution(0.1, 0, core.Main.getRandomDouble(0, 1)) / 4;
	}
	/** @author Teddy @ calculating the vectors for attraction for to all other superparticles within 50 pixels. then combining 
	 * the vectors to create the new direction and magnitude of the vector in the next stage of the model
	 * 
	 * @return
	 */
	public void calcNewVector(){
		double magnitude2, direction2;
		double[] polarCoord;
		for (int l = 0; l < core.Main.universe.resolution; l++)
			for (int w = 0; w < core.Main.universe.resolution; w++){
				if (xx == core.Main.universe.superParticle[l][w].xx && yy == core.Main.universe.superParticle[l][w].yy) continue;
				if (core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle[l][w].xx, core.Main.universe.superParticle[l][w].yy) < 25){
					polarCoord = core.Function.cartesianToPolar(core.Main.universe.superParticle[l][w].xx - xx, core.Main.universe.superParticle[l][w].yy - yy);
					magnitude2 = (/*core.Main.universe.superParticle[l][w].magnitude * */ GRAVITATIONAL_CONSTANT) / Math.pow(polarCoord[1], 2);
					direction2 = polarCoord[0];
					direction = direction - direction2;
					if (direction < 0){
						direction = direction + 360;
					}
					else if (direction > 180){
						direction = (360 - direction) * -1;
					}
					magnitude = Math.sqrt(Math.pow(magnitude, 2) + Math.pow(magnitude2, 2) - (magnitude * magnitude2 * 2 * Math.cos(Math.toDegrees(direction))));
					//System.out.println("v: " + magnitude);
				}			
			}		
	}

	public void moveParticle(){
		calcNewVector();

		double[] cartesianCoord = new double[2];
		cartesianCoord = core.Function.polarToCartesian(direction, magnitude * SPEED_OF_LIGHT);

		xx += cartesianCoord[0];
		yy += cartesianCoord[1];

		//System.out.println("xx: " + xx + "\tyy: " + yy + "\tv: " + magnitude);
	}
}
