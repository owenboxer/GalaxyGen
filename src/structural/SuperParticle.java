package structural;

public class SuperParticle {
	public double xx, yy, vx, vy;
	public double direction, magnitude;
	public double mass = 1;

	public final static double GRAVITATIONAL_CONSTANT = 0.1, FRICTION_CONSTANT = 1,
			EXPANSION_CONSTANT = 10;

	public SuperParticle(int xx, int yy){
		this.xx = xx;
		this.yy = yy;
		
		genVector();
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
	public void calcVector(){
		double magnitude2, direction2;
		double[] cartesian = new double[2];

		vx = magnitude * Math.cos(Math.toRadians(direction));
		vy = magnitude * Math.sin(Math.toRadians(direction));

		cartesian = core.Function.cartesianToPolar(xx, yy);
		cartesian = core.Function.polarToCartesian(cartesian[0], EXPANSION_CONSTANT / Math.pow(cartesian[1], 2));

		vx += cartesian[0];
		vy += cartesian[1];

		for (int i = 0; i < core.Main.universe.superParticle.size(); i++){
			if (xx == core.Main.universe.superParticle.get(i).xx && yy == core.Main.universe.superParticle.get(i).yy) continue;	
			if (core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, core.Main.universe.superParticle.get(i).yy) < 500){
				//if (core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, core.Main.universe.superParticle.get(i).yy) < 10)
					magnitude2 = (core.Main.universe.superParticle.get(i).mass * GRAVITATIONAL_CONSTANT) / Math.pow(FRICTION_CONSTANT * core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, core.Main.universe.superParticle.get(i).yy), 2);
				//else 
					//magnitude2 = DARK_MATTER_CONSTANT / Math.pow(core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, core.Main.universe.superParticle.get(i).yy), 2);
				direction2 = Math.toDegrees(Math.acos((core.Main.universe.superParticle.get(i).xx - xx) / core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, core.Main.universe.superParticle.get(i).yy)));
				if (yy > core.Main.universe.superParticle.get(i).yy) direction2 = (-1 * direction2) + 360;
				cartesian = core.Function.polarToCartesian(direction2, magnitude2);
				vx += cartesian[0];
				vy += cartesian[1];
			}			
		}
	}

	public void moveParticle(){
		xx += vx;
		yy += vy;
	}
	public boolean checkForScope(){
		boolean check = true;

		if (xx < 0 - (1.25 * core.Main.universe.resolution) || yy < 0 - (1.25 * core.Main.universe.resolution))
			check = false;
		else if (xx > 1.25 * core.Main.universe.resolution || yy > 1.25 * core.Main.universe.resolution)
			check = false;

		return check;
	}
	public void checkForProximity(){
		for (int i = 0; i < core.Main.universe.superParticle.size(); i++){
			if (xx == core.Main.universe.superParticle.get(i).xx && yy == core.Main.universe.superParticle
					.get(i).yy)
				continue;
			if (core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, 
					core.Main.universe.superParticle.get(i).yy) < 0.5){
				mass += core.Main.universe.superParticle.get(i).mass;
				core.Main.universe.superParticle.remove(i);
			}
		}
	}
}
