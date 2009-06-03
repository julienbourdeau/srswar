package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author kbok
 * This is the main window of the game. It does not contains much, though, because all
 * operations are made from external classes.
 */
public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new MainWindow using the given size.
	 * @param width Width of the new window.
	 * @param height Height of the new window.
	 */
	public MainWindow(int width, int height)
	{
		getContentPane().setPreferredSize(new Dimension(width, height));
		setTitle("J.M.A.X.");
	}
}
