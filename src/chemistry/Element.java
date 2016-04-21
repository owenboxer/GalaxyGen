package chemistry;

public class Element {
	int atomicNumber;
	String name, symbol;
	Isotope[] isotope = null;

	int start, end; //tells methods where to read start reading from the text file

	public Element(int atomicNumber){
		this.atomicNumber = atomicNumber;
		String[] packedIsotopes = util.FileHandler.readFile("res/elementdata/isotope.txt");

		String dataLine = null;
		char c = ' ';
		String temp = "";
		for (int line = 0; line < packedIsotopes.length; line++){
			if (packedIsotopes[line].contains("{")){
				for (int letter = 0; letter < packedIsotopes[line].length(); letter++){
					c = packedIsotopes[line].charAt(letter);
					if (c == '\t') break;
					temp = new StringBuilder().append(temp).append(c).toString();
				}
				if (!temp.equals("}") && Integer.valueOf(temp) == atomicNumber){
					dataLine = packedIsotopes[line];
					start = line;
					break;
				}
			}
			temp = "";
		}		

		temp = "";
		int word = 0;
		for (int letter = 0; letter < dataLine.length(); letter++){
			c = dataLine.charAt(letter);
			if (word != 0){
				if (c == '\t'){
					if (word == 1){
						name = temp;
						temp = "";
						word++;
						continue;
					}
					else {
						symbol = temp;
						break;
					}
				}
				temp = new StringBuilder().append(temp).append(c).toString();
			}
			if (c == '\t') word++;
		}

		System.out.println(atomicNumber + " " + name + " " + symbol);
	}

	public void createIsotopes(){
		String[] packedIsotopes = util.FileHandler.readFile("res/elementdata/isotope.txt");

		char c = ' ';
		for (int line = start; line < packedIsotopes.length; line++){
			c = packedIsotopes[line].charAt(0);
			if (c == '}'){
				end = line;
				break;
			}
		}
		
		String temp = "";

		isotope = new Isotope[end - start + 1];

		temp = "";
		int word = 0, massNumber = 0;
		double halfLife = 0;
		for (int line = start + 1; line < end; line++){
			for (int letter = 0; letter < packedIsotopes[line].length(); letter++){
				c = packedIsotopes[line].charAt(letter);
				if (c == '\t'){
					
					if (word == 0){
						massNumber = Integer.valueOf(temp).intValue();
						word++;
						temp = "";
						continue;
					}
					halfLife = Double.valueOf(temp).doubleValue();
					word++;
					temp = "";
					continue;
				}
				if (c == 'n' && word == 1){
					isotope[line - start + 1] = new Isotope(massNumber);
					break;
				}

				temp = new StringBuilder().append(temp).append(c).toString();
				if (letter == packedIsotopes[line].length() - 1){
					isotope[line - start + 1] = new Isotope(massNumber, halfLife, temp);
					break;
				}
			}
			temp = "";
			word = 0;
		}
	}	
}
