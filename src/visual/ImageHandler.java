package visual;

import java.awt.image.BufferedImage;
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
}
