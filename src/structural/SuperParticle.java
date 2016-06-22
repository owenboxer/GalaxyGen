package structural;

public class SuperParticle {
	public double xx, yy;
	public double vxNew, vyNew, vxOld, vyOld;
	public double mass = 1;

	public boolean reference = false, delete = false;

	public final static double GRAVITATIONAL_CONSTANT = 0.1, EXPANSION_CONSTANT = 0, SPEED_OF_LIGHT = 10;

	public SuperParticle(int xx, int yy){
		this.xx = xx;
		this.yy = yy;

		genVector();
	}

	private void genVector(){
		double direction = core.Main.getRandomDouble(0, 360),
				magnitude = core.Function.gaussianDistribution(0.1, 0, core.Main.getRandomDouble(0, 1)) / 8;

		double coord[] = core.Function.polarToCartesian(direction, magnitude);
		xx += coord[0];
		yy += coord[1];
	}
	public double getRelativeMass(double velocity){
		double mRelative = mass / (Math.sqrt(1 - Math.pow(velocity / SPEED_OF_LIGHT, 2)));
		if (Double.isNaN(mRelative)){
			System.out.println("m = " + mass + "\tv = " + velocity + "\tmRel = " + mRelative);
		}
		return mRelative;
	}
	/** @author Teddy @ calculating the vectors for attraction for to all other superparticles within 50 pixels. 
	 * then combining the vectors to create the new direction and magnitude of the vector in the next stage of
	 * the model
	 * 
	 * @return
	 */
	public void calcVector(int skip){
		vxNew = vxOld;
		vyNew = vyOld;

		double magnitude2, direction2;
		double[] cartesian = new double[2];

		cartesian = core.Function.cartesianToPolar(xx - (core.Main.universe.resolution / 2), yy - 
				(core.Main.universe.resolution / 2));
		cartesian = core.Function.polarToCartesian(cartesian[0], EXPANSION_CONSTANT / Math.pow(cartesian[1], 2));

		//vxNew += cartesian[0];
		//vyNew += cartesian[1];

		double vRelative, mRelative, radius;

		for (int i = 0; i < core.Main.universe.superParticle.size(); i++){
			//checks particle is not comparing to itself
			if (i == skip) continue;

			//calculates magnitude of velocity taking into account relativity
			vRelative = core.Function.cartesianToPolar(core.Main.universe.superParticle.get(i).vxOld - vxOld, 
					core.Main.universe.superParticle.get(i).vyOld - vyOld)[1];
			mRelative = core.Main.universe.superParticle.get(i).getRelativeMass(vRelative);
			radius = core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx,
					core.Main.universe.superParticle.get(i).yy);
			//if (radius < 0.5) System.out.println("i1 = " + skip + "\ti2 = " + i);
			magnitude2 = (GRAVITATIONAL_CONSTANT * mRelative) / (Math.pow(radius, 2) * Math.sqrt(1 - (2 * 
					GRAVITATIONAL_CONSTANT * mRelative) / (Math.pow(SPEED_OF_LIGHT, 2) * radius)));

			//adds new vector to pre-existing vector
			direction2 = Math.toDegrees(Math.acos((core.Main.universe.superParticle.get(i).xx - xx) /
					core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx,
					core.Main.universe.superParticle.get(i).yy)));
			if (yy > core.Main.universe.superParticle.get(i).yy) direction2 = (-1 * direction2) + 360;
			cartesian = core.Function.polarToCartesian(direction2, magnitude2);
			vxNew += cartesian[0];
			vyNew += cartesian[1];
		}
	}

	public void moveParticle(){
		xx += vxNew;
		yy += vyNew;

		vxOld = vxNew;
		vyOld = vyNew;
	}
	public boolean checkForScope(){
		boolean check = true;

		if (delete) return check;

		if (xx < 0 - (1.25 * core.Main.universe.resolution) || yy < 0 - (1.25 * core.Main.universe.resolution)){
			check = false;
			delete = true;
		}
		else if (xx > 1.25 * core.Main.universe.resolution || yy > 1.25 * core.Main.universe.resolution){
			check = false;
			delete = true;
		}

		return check;
	}
	public boolean checkForProximity(int skip){
		boolean check = false;

		if (delete) return check;

		for (int i = 0; i < core.Main.universe.superParticle.size(); i++){
			if (i == skip) continue;
			if (core.Function.distanceEquation(xx, yy, core.Main.universe.superParticle.get(i).xx, 
					core.Main.universe.superParticle.get(i).yy) < 0.5){
				mass += core.Main.universe.superParticle.get(i).mass;
				if (core.Main.universe.superParticle.get(i).reference) reference = true;
				core.Main.universe.superParticle.get(i).delete = true;
				check = true;
				//System.out.println("i1 = " + skip + "\ti2 = " + i);
			}
		}

		return check;
	}
}
