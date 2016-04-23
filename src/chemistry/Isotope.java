package chemistry;

public class Isotope {
	boolean radioactive;
	int massNumber;
	double halfLife;

	int daughterAtomicNumber[], daughterMassNumber[];
	double probability[];

	public Isotope(int massNumber){
		radioactive = false;
		this.massNumber = massNumber;
	}
	public Isotope(int massNumber, double halfLife, String daughterIsotope){
		radioactive = true;
		this.massNumber = massNumber;
		this.halfLife = halfLife;
		unpackDaughterIsotope(daughterIsotope);
	}

	private void unpackDaughterIsotope(String daughterIsotope){
		char c = ' ';
		int daughterCounter = 1;
		for (int letter = 0; letter < daughterIsotope.length(); letter++){
			c = daughterIsotope.charAt(letter);
			if (c == '-') daughterCounter++;
		}
		
		daughterAtomicNumber = new int[daughterCounter];
		daughterMassNumber = new int[daughterCounter];
		probability = new double[daughterCounter];

		String temp = "";
		int test = 0, i = 0;
		boolean number = true;
		for (int letter = 0; letter < daughterIsotope.length(); letter++){
			c = daughterIsotope.charAt(letter);

			if (c == '-'){
				probability[i] = Double.valueOf(temp).doubleValue();
				i++;
				temp = "";
				continue;
			}
			if (c == ' '){
				daughterAtomicNumber[i] = getDaughterAtomic(temp);
				temp = "";
				number = true;
				continue;
			}
			if (number){
				try{
					if (c != '.') test = Integer.valueOf(c + "");
				} catch(NumberFormatException ex){
					daughterMassNumber[i] = Integer.valueOf(temp).intValue();
					temp = "";
					number = false;
				}
			}

			temp = new StringBuilder().append(temp).append(c).toString();

			if (letter == daughterIsotope.length() - 1){
				if (i > 0){
					probability[i] = Double.valueOf(temp).doubleValue();
					break;
				}
				daughterAtomicNumber[i] = getDaughterAtomic(temp);
				probability[i] = 1;
			}
		}

		/*for (int daughter = 0; daughter < daughterCounter; daughter++)
		 *	System.out.println(daughterAtomicNumber[daughter] + " " +
		 *			daughterMassNumber[daughter] + " " + probability[daughter]);
		 */
	}
	private int getDaughterAtomic(String symbol){
		int atomicNumber = 0;
		for (int e = 0; e < 94; e++)
			if (core.Universe.element[e].symbol.equals(symbol)){
				atomicNumber = core.Universe.element[e].atomicNumber;
				break;
			}

		return atomicNumber;
	}
}
