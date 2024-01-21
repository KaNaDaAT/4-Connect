package utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.Painter;
import javax.swing.UIDefaults;

public class SliderSkin {
	private JSlider slider;
	private UIDefaults uidefault;
	private Color backcolor;
	public SliderSkin(JSlider slider) {
		
		this.uidefault = new UIDefaults();
		this.slider = slider;
		this.backcolor = new Color(255, 255, 255);
		
	}
	public JComponent makeUI() {
		this.uidefault.put("Slider:SliderTrack[Enabled].backgroundPainter", new Painter<JSlider>() {
			@Override 
			public void paint(Graphics2D g, JSlider c, int w, int h) {
				int arc         = 10;
				int trackHeight = 8;
				int trackWidth  = w - 2;
				int fillTop     = 4;
				int fillLeft    = 1;

				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(new BasicStroke(1.5f));
				g.setColor(Color.GRAY);
				g.fillRoundRect(fillLeft, fillTop, trackWidth, trackHeight, arc, arc);

				int fillBottom = fillTop + trackHeight;
				int fillRight  = xPositionForValue(
						c.getValue(), c,
						new Rectangle(fillLeft, fillTop, trackWidth, fillBottom - fillTop));
				g.setColor(backcolor);
				g.fillRoundRect(fillLeft+1, fillTop, fillRight - fillLeft, fillBottom - fillTop, arc, arc);

				g.setColor(Color.lightGray);
				g.drawRoundRect(fillLeft, fillTop, trackWidth, trackHeight, arc, arc);
			}
			
			protected int xPositionForValue(int value, JSlider slider, Rectangle trackRect) {
		        int min = slider.getMinimum();
		        int max = slider.getMaximum();
		        int trackLength = trackRect.width;
		        double valueRange = (double) max - (double) min;
		        double pixelsPerValue = (double) trackLength / valueRange;
		        int trackLeft = trackRect.x;
		        int trackRight = trackRect.x + (trackRect.width - 1);
		        int xPosition;
		
		        xPosition = trackLeft;
		        xPosition += Math.round(pixelsPerValue * ((double) value - min));
		
		        xPosition = Math.max(trackLeft, xPosition);
		        xPosition = Math.min(trackRight, xPosition);
		
		        return xPosition;
		    }
		});
	    this.slider.putClientProperty("Nimbus.Overrides", uidefault);
	    return this.slider;
	}
	
	public void setColor(Color color) {
		this.backcolor = color;
	}
	
	public Color getColor() {
		return this.backcolor;
	}
}