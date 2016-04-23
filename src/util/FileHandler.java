package util;

import java.io.*;
import java.util.ArrayList;

/** FileHandler.java
 * 
 * Just a utility that centralizes file handling into one class, 
 * making it more easier and more systematic.
 */
public class FileHandler {

	public static String[] readFile(String path){
		ArrayList<String> dataPoint = new ArrayList<String>(0);

		try{
			BufferedReader in = new BufferedReader(new FileReader(path));

			String line = null;
			while ((line = in.readLine()) != null)
				dataPoint.add(line);

			in.close();
		} catch (IOException ex){
			System.err.println(ex);
		}

		String[] data = new String[dataPoint.size()];
		data = dataPoint.toArray(data);
		
		return data;
	}
	public static String[] readFile(String path, int lineMin, int lineMax){
		int lineNumber = 0, pointNumber = 0;
		String[] dataPoint = new String[lineMax - lineMin];

		try{
			BufferedReader in = new BufferedReader(new FileReader(path));

			String line = null;
			while ((line = in.readLine()) != null){
				if (lineNumber >= lineMin && lineNumber < lineMax){
					dataPoint[pointNumber] = line;
					pointNumber++;
				}
				if (lineNumber >= lineMax) break;
				lineNumber++;
			}

			in.close();
		} catch (IOException ex){
			System.err.println(ex);
		}

		return dataPoint;
	}
	public static void writeToFile(String[] newDataPoint, String path){
		try{
			PrintWriter out = new PrintWriter(path);
			for (int i = 0; i < newDataPoint.length; i++){
				out.println(newDataPoint[i]);
			}
			out.close();
		} catch (IOException ex){
			System.err.println(ex);
		}
	}
	public static void deleteFile(String path){
		File file = new File(path);
		file.delete();
	}

	public static void makeDirectory(String path){
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}
	public static boolean checkExists(String path){
		return new File(path).exists();
	}
}