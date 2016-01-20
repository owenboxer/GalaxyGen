package save;

public class Saves {
	String id = null;
	int universeage = 0;
	FileIO activesave = null;
	
	public static int numberofsaves = 0;
	
	Saves(){
		int i = 0;
		boolean done = false;
		while (!done){
			if (!FileIO.checkForFile("res/U" + i + "/universe.txt")) done = true;
			else i++;
		}

		numberofsaves = i;
	}
	
	public void openSave(String saveid){
		activesave = new FileIO(saveid + "/universe.txt");
		id = saveid;
		
	}
	
}
