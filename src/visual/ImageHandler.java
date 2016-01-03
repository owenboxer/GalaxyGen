package visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHandler {
	public static BufferedImage loadImage(String path){
		BufferedImage image = null;
		try{
			image = ImageIO.read(ImageHandler.class.getResource(path));
		} catch(Exception e){
			e.printStackTrace();
		}
		return image;
	}
	
	public static void writeImage (BufferedImage image, String format, String dir) {
		System.out.println("Image saved to \"" + dir + "\"");
		try {
		    File outputfile = new File(dir);
		    ImageIO.write(image, format, outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
