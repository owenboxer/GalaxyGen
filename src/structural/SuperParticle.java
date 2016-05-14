package structural;

public class SuperParticle {
	public double xx, yy;
	public double direction, magnitude;

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
}
