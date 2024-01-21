package utils;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChooser extends JPanel implements ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JSlider red, green, blue;
	private SliderSkin sliderskin1, sliderskin2, sliderskin3;
	public JLabel redT, greenT, blueT;
	public ColorChooser() {
		this.setSize(190, 80);
		this.setLayout(null);
		
		/*Slider*/
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {}
		
		
		
		this.red = new JSlider(0, 255, 0); 
		this.red.setFocusCycleRoot(false);
		this.red.addChangeListener(this);
		this.red.setPreferredSize(new Dimension(155, 20));
		this.sliderskin1 = new SliderSkin(this.red);
		this.red = (JSlider) sliderskin1.makeUI();
		this.red.setBounds(5, 5 , 155, 20);
		
		
		this.green = new JSlider(0, 255, 0); 
		this.green.setFocusCycleRoot(false);
		this.green.addChangeListener(this);
		this.green.setPreferredSize(new Dimension(155, 20));
		this.sliderskin2 = new SliderSkin(this.green);
		this.green = (JSlider) sliderskin2.makeUI();
		this.green.setBounds(5, 30 , 155, 20);
		
		
		this.blue = new JSlider(0, 255, 0); 
		this.blue.setFocusCycleRoot(false);
		this.blue.addChangeListener(this);
		this.blue.setPreferredSize(new Dimension(155, 20));
		this.sliderskin3 = new SliderSkin(this.blue);
		this.blue = (JSlider) sliderskin3.makeUI();
		this.blue.setBounds(5, 55 , 155, 20);
		
		
		this.redT = new JLabel("0");
		this.redT.setBounds(165, 4, 35, 22);
		
		this.greenT = new JLabel("0");
		this.greenT.setBounds(165, 29, 35, 22);
		
		this.blueT = new JLabel("0");
		this.blueT.setBounds(165, 53, 35, 22);
		
		
		
		this.add(this.red);
		this.add(this.redT);
		this.add(this.green);
		this.add(this.greenT);
		this.add(this.blue);
		this.add(this.blueT);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (this.red == e.getSource()) {
			this.sliderskin1.setColor(new Color(255, 255-this.red.getValue(), 255-this.red.getValue()));
			this.redT.setText(this.red.getValue() + "");
		}
		if (this.green == e.getSource()) {
			this.sliderskin2.setColor(new Color(255-this.green.getValue(), 255, 255-this.green.getValue())); 
			this.greenT.setText(this.green.getValue() + "");
		}
		if (this.blue == e.getSource()) {
			this.sliderskin3.setColor(new Color(255-this.blue.getValue(), 255-this.blue.getValue(), 255)); 
			this.blueT.setText(this.blue.getValue() + "");
		}
	}

	public void setValues(Color color) {
		this.red.setValue(color.getRed());
		this.green.setValue(color.getGreen());
		this.blue.setValue(color.getBlue());
	}

	public Color getColor() {
		return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
	}

	public void setChooserEnabled(boolean b) {
		this.red.setEnabled(b);
		this.green.setEnabled(b);
		this.blue.setEnabled(b);
	}
}

