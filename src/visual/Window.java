package visual;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.Input;

//import utility.Input;

public class Window {
	JFrame frame;
	BufferedImage img;
	JLabel label;
	public Window(BufferedImage img) {
		this.img = img;
		frame = new JFrame();
		frame.addWindowListener(new FrameListener());
	    frame.setResizable(true);
		frame.setFocusable(true);
	    
	    ImageIcon icon = new ImageIcon(img);
	    label = new JLabel(icon);
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
		frame.addKeyListener(new Input());
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

class FrameListener extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		core.Main.universe.deleteAll();
		System.exit(0);
	}
}