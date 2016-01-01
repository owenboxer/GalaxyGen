package structural;

public class Sector {
	public int radial, polar, size, numberstars;
	public double rawdensity, density, radiation, proportion[];
	
	public Sector(int radial, int rawpolar, double density){
		this.radial = radial;
		polar = rawpolar*12;
		this.rawdensity = density;
	}
	
	public void initiateSector(){
		size = calcSize();
	}
	
	int calcSize(){
		double outer = Math.PI * Math.pow((radial + 1), 2), inner = Math.PI * Math.pow(radial, 2);
		double size = (outer - inner) / 30;
		return (int) size;
	}
	double calcDensity(){
		double density = (rawdensity / 10) - 0.5;
		double galacticdensity = universal.Main.universe.maingalaxy.meandensity;
		return density + galacticdensity;
	}
}
