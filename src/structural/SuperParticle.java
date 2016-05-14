package structural;

public class SuperParticle {
	public double xx, yy;
	public double direction, magnitude;
	public double mass;

	public final static double GRAVITATIONAL_CONSTANT = 1,
			EXPANSION_CONSTANT = 1;

	public SuperParticle(int xx, int yy){
		this.xx = xx;
		this.yy = yy;

		genVector();
	}

	

	
	private void genVector(){
		direction = core.Main.getRandomDouble(0, 360);
		magnitude = 1 - core.Function.gaussianDistribution(0.1, 0, core.Main.getRandomDouble(0, 1));
	}
	/** @author Teddy @ calculating the vectors for attraction for to all other superparticles within 50 pixels. then combining the vectors to create the new direction and magnitude of the vector in the next stage.
	 * 
	 * @return
	 */
	public double calcNewVector(){
		double magnitude2, direction2;
		double[] polarCoord;
		for (int l = 0; l < core.Main.universe.resolution; l++)
			for (int w = 0; w < core.Main.universe.resolution; w++){
				if (xx == core.Main.universe.superParticle[l][w].xx && yy == core.Main.universe.superParticle[l][w].yy) continue;

				if (core.Function.distancEquation(xx, yy, core.Main.universe.superParticle[l][w].xx, core.Main.universe.superParticle[l][w].yy) < 50){
					polarCoord = (core.Main.universe.superParticle[l][w].xx - xx, core.Main.universe.superParticle[l][w].yy - yy);
					magnitude2 = (core.Main.universe.superParticle[l][w].magnitude * GRAVITATIONAL_CONSTANT) / Math.pow(polarCoord[1], 2);
					direction2 = polarCoord[0];
					
				}
						
			}
				
	}
}
