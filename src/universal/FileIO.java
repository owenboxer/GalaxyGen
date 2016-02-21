package universal;

import java.io.*;
import java.util.*;

public class FileIO{
	public Vector<String> line = null;
	public String name;
	private BufferedReader in = null;
	
	public FileIO(String fileplace, String filename){
		name = "res/" + fileplace + "/" + filename + ".txt";
		line = new Vector<String>(0,1);
		initializeBufferedReader();
	}
	
	private void initializeBufferedReader(){
		try{
			in = new BufferedReader(new FileReader(name));
		} catch (IOException ex){
			System.err.println("Error\n" + ex);
		}
	}
	
	public void readFile(){
		String line = null;
		try{
			while((line = in.readLine()) != null){
				this.line.addElement(line);
			}
		}catch (IOException ex){
			System.err.println("Error\n" + ex);
		}
	}
	
	public void saveFile(){
		File save = new File(name);
		PrintWriter sout = null;
		try {
			sout = new PrintWriter(save);
		} catch (IOException ex) {
			System.err.println("Error\n" + ex);
		}
		for (int i=0; i<line.size(); i++){
			String line = this.line.elementAt(i);
			sout.println(line);
		}
		sout.close();
	}
}