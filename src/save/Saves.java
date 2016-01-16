package save;

public class Saves {
	FileIO savefiles = new FileIO("res", "savefiles");
	universal.Universe universe[];
	String id[];
	int age[];
	
	public static int numberofsaves = 0;
	
	Saves(){
		savefiles.readFile();
		universe = new universal.Universe[savefiles.lines.size()];
		id = new String[universe.length];
		age = new int[universe.length];
		
		breakDownRawString();
	}
	
	private void breakDownRawString(){
		char x; 
		String temp = "";
		
		for (int i = 0; i < savefiles.lines.size(); i++){
			for (int j = 0; j < savefiles.lines.elementAt(i).length(); j++){
				x = savefiles.lines.elementAt(i).charAt(j);
				if (x == '-'){
					id[i] = temp;
					temp = "";
					continue;
				}
				temp = new StringBuilder().append(temp).append(x).toString();
			}
			age[i] = Integer.parseInt(temp);
			temp = "";
		}
	}
	
	public static void loadUniverse(String id){
		
	}
}
