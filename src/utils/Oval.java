package utils;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Oval extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Color color;
	//Konstruktor
	public Oval(Color color) {
		this.color = color;
		this.setSize(100, 100);
	}
	
	
	//Methoden
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(this.color);
		
		g.fillOval(2, 2, this.getWidth()-4, this.getHeight()-4);
	}
	
	public void setColor(Color color) {
		this.color = color;
		this.repaint();
	}
}
