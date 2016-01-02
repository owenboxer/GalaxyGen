package visual;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window {
	BufferedImage img;
	public Window(BufferedImage img) {
		this.img = img;
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    frame.setResizable(true);
		ImageIcon icon = new ImageIcon(img);
		frame.add(new JLabel(icon));
		frame.pack();
		frame.setVisible(true);
	}
}