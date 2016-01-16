package save;

import java.io.*;
import java.util.*;

public class FileIO{
	public Vector<String> lines = new Vector<String>(0, 1);
	public String name;
	private BufferedReader in = null;

	FileIO(String fileplace, String filename){
		name = "res/" + fileplace + "/" + filename + ".txt";
		createFile();
		initializeBufferedReader();
	}

	public static void createDirectory(String dirplace, String rawdirname){
		String dirname = "res/" + dirplace + "/" + rawdirname;
		File file = new File(dirname);
		file.mkdirs();
	}
	private void createFile(){
		try {
			File file = new File(name);
			file.createNewFile();
		      
	    } catch (IOException e) {
	    }
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
				lines.addElement(line);
			}
		}catch (IOException ex){
			System.err.println("Error\n" + ex);
		}
		//for (int i = 0; i < word.size(); i++) System.out.println(word.elementAt(i));
	}
	public void saveFile(){
		File save = new File(name);
		PrintWriter sout = null;
		try {
			sout = new PrintWriter(save);
		} catch (IOException ex) {
			System.err.println("Error\n" + ex);
		}
		for (int i = 0; i < lines.size(); i++){
			String line = lines.elementAt(i);
			sout.println(line);
		}
		sout.close();
	}
}
