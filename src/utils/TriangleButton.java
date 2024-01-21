package utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JButton;
public class TriangleButton extends JButton {
	private static final long serialVersionUID = 1L;
	public TriangleButton(String label) {
		super(label);
		// These statements enlarge the button so that it 
		// becomes a circle rather than an oval.
		Dimension size = new Dimension (100, 49);
		setPreferredSize(size);
		this.setFocusCycleRoot(false);
		this.setFocusPainted(false);
		// This call causes the JButton not to paint 
		// the background.
		// This allows us to paint a round background.
		setContentAreaFilled(false);
	}
	
	public TriangleButton(String label, String actionCommand) {
		super(label);
		super.setActionCommand(actionCommand);
		// These statements enlarge the button so that it 
		// becomes a circle rather than an oval.
		Dimension size = new Dimension (100, 49);
		setPreferredSize(size);
		this.setFocusCycleRoot(false);
		this.setFocusPainted(false);
		// This call causes the JButton not to paint 
		// the background.
		// This allows us to paint a round background.
		setContentAreaFilled(false);
	}
 
	// Paint the round background and label.
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(Color.lightGray);
		} else {
			g.setColor(getBackground());
		}
		int x3Points[] = {getSize().width/2, 0, getSize().width};
		int y3Points[] = {getSize().height, 0,0};
		g.fillPolygon(x3Points, y3Points, x3Points.length); 
		super.paintComponent(g);
	}

	// Paint the border of the button using a simple stroke.
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		int x3Points[] = {getSize().width/2, 0, getSize().width};
		int y3Points[] = {getSize().height, 0,0};
		g.drawPolygon(x3Points, y3Points, x3Points.length); 
	}

	// Hit detection.
	Polygon polygon;
	public boolean contains(int x, int y) {
		// If the button has changed size, 
		// make a new shape object.
		if (polygon == null || !polygon.getBounds().equals(getBounds())) {
			int x3Points[] = {getSize().width/2, 0, getSize().width};
			int y3Points[] = {getSize().height, 0,0};
			polygon = new Polygon(x3Points, y3Points, 3);
		}
		return polygon.contains(x, y);
  	}
  
	public void setSize(int x, int y) {
		this.setPreferredSize(new Dimension(x, y));
	}
	
	
	public void setSize(Dimension dim) {
		this.setPreferredSize(dim);
	}
  
	public void setBackgroundColor(Color color) {
		this.setBackground(color);
		
	}
	public void setForgroundColor(Color color) {
		this.setForeground(color);
	}
	
	
	
	
	
	
}