package visual;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//import utility.Input;

public class Window {
	JFrame frame;
	BufferedImage img;
	JLabel label;
	public Window(BufferedImage img) {
		this.img = img;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    frame.setResizable(true);
		frame.setFocusable(true);
	    
	    ImageIcon icon = new ImageIcon(img);
	    label = new JLabel(icon);
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
		//frame.addKeyListener(new Input());
	}
	
	public void close() {
		frame.dispose();
	}
	
	public void setImage(BufferedImage img) {
		ImageIcon icon = new ImageIcon(img);
		frame.remove(label);
		label = new JLabel(icon);
		frame.add(label);
		frame.pack();
	}
}