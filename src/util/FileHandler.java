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
	public static void writeToFile(String[] newDataPoint, String path, int start){
		ArrayList<String> dataPoint = new ArrayList<String>(0);

		try{
			BufferedReader in = new BufferedReader(new FileReader(path));

			String line = null;
			while ((line = in.readLine()) != null)
				dataPoint.add(line);

			in.close();
		} catch (IOException ex){
			//If file does not exist, then it will be created by PrintWriter
		}

		for (int i = 0; i < newDataPoint.length; i++)
			dataPoint.add(i + start, newDataPoint[i]);

		try{
			PrintWriter out = new PrintWriter(path);
			for (int i = 0; i < dataPoint.size(); i++)
				out.println(dataPoint.get(i));
			out.close();
		} catch (IOException ex){
			System.err.println(ex);
		}
	}
	public static void deleteFile(String path){
		File file = new File(path);
		file.delete();
	}
}