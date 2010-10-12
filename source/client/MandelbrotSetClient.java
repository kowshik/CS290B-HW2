package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import system.Computer;
import api.Space;
import api.Task;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Computes the Mandelbrot Set for a given input on a remote machine and
 * displays the result in a GUI
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public class MandelbrotSetClient {

	// Size of JFrame displayed on the screen
	private static int N_PIXELS = 500;

	private static double MANDELBROT_LIMIT = 2.0;

	// Spectrum of colours to represent the Mandelbrot Set on the screen
	private static Color[] myColours;

	// Number of calls to the remote server
	private static int NUMBER_OF_CALLS = 5;

	public static void main(String[] args) throws Exception {

		String computeSpaceServer = args[0];
		
		int iterLimit = 512;
		MandelbrotSetJob mandelbrotJob = new MandelbrotSetJob(-0.7510975859375, 0.1315680625, 0.01611, 1024, iterLimit);
		MandelbrotSetClient.myColours = new Color[iterLimit + 1];
		for (int i = 1; i <= iterLimit + 1; i++) {
			myColours[i - 1] = new Color(i / (float) (iterLimit + 1), i
					/ (float) (iterLimit + 1), i / (float) (iterLimit + 1));
		}

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			
			
			Space space=(Space) Naming.lookup("//" + computeSpaceServer + "/"+Space.SERVICE_NAME);
			mandelbrotJob.generateTasks(space);
			mandelbrotJob.collectResults(space);
			int[][] counts = mandelbrotJob.getAllResults();
			JLabel mandelbrotLabel = displayMandelbrotSetTaskReturnValue(counts);
			System.out.println("Computer ready");
			// display JLabels: graphic images
			JFrame frame = new JFrame("Result Visualizations");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container container = frame.getContentPane();
			container.setLayout(new BorderLayout());
			container.add(new JScrollPane(mandelbrotLabel), BorderLayout.WEST);
			frame.pack();
			frame.setVisible(true);
			
		} catch (RemoteException e) {
			System.err.println("ComputerImpl exception : ");
			e.printStackTrace();

		} catch (MalformedURLException e) {
			System.err.println("ComputerImpl exception : ");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("ComputerImpl exception : ");
			e.printStackTrace();
		}
		

		
	}

	
	private static JLabel displayMandelbrotSetTaskReturnValue(int[][] counts) {
		Image image = new BufferedImage(N_PIXELS, N_PIXELS,
				BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();
		for (int i = 0; i < counts.length; i++) {
			for (int j = 0; j < counts.length; j++) {
				graphics.setColor(getColor(counts[i][j]));
				graphics.fillRect(i, j, 1, 1);
			}
		}
		ImageIcon imageIcon = new ImageIcon(image);
		return new JLabel(imageIcon);
	}

	private static Color getColor(int i) {
		if (i == MANDELBROT_LIMIT) {
			return Color.BLACK;
		}
		return myColours[i];
	}
}
