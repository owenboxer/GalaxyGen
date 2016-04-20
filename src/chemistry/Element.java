package chemistry;

public class Element {
	int atomicNumber;
	Isotope[] isotope = null;

	public Element(int atomicNumber){
		this.atomicNumber = atomicNumber;
	}

	public void createIsotopes(){
		String[] packedIsotopes = util.FileHandler.readFile("res/elementdata/isotopes.txt")
	}
}
