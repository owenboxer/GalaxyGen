package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Scanner;

import core.Main;
import visual.GalaxyDrawer;
import visual.ImageHandler;

public class Input extends KeyAdapter implements Runnable {
	static int keyP;
	static int keyR;
	static int n;
	
	public static int getN(){
		System.out.println("Starting " + n + " * " + n + " Gas Clouds");
		return n;
	}

	public  static void inputGasClouds(){
		Scanner reader = new Scanner (System.in);
		System.out.println("Enter the number of gas clouds: ");
		n = (int) Math.sqrt(reader.nextInt());
	}
	public Input() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public void keyPressed(KeyEvent e) {
		keyP = e.getKeyCode();
		keyR = 0;
		
		// GLOBAL KEY PRESS
		if (keyP == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}


	public void keyReleased(KeyEvent e) {
		keyR = e.getKeyCode();
		keyP = 0;
	}

	public static boolean checkPress(int k) {
		return (k == keyP);
	}
	public static boolean checkRelease(int k) {
		return (k == keyR);
	}

	public void run() {
		while (true) {
			System.out.print("");// FOR SOME REASON THIS MAKES IT WORK
			if (keyP == KeyEvent.VK_R) {
				//GalaxyDrawer.window.close();
				Main.universe.createGalaxies();
				keyP = 0;
			}
			if (keyP == KeyEvent.VK_S) {
				int i = 0;
				while((new File("screenshots/screenshot" + i + ".png")).exists()) {
					i++;
				}
				ImageHandler.writeImage(GalaxyDrawer.image, "png", "screenshots/screenshot" + i + ".png");
			}
		}
	}

}
