package utils;
import java.awt.Color;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.MetalSliderUI;

@SuppressWarnings("serial")
public class SliderMenuItem extends JSlider{
	private CompoundBorder border;
	private TitledBorder namedBorder;
	private Dictionary<Integer, JLabel> label;
	public SliderMenuItem(int min, int max, int value, String name) {
		this.label = new Hashtable<Integer, JLabel>();
		for (int i=0; i<max; i++) {  
			this.label.put(i, new JLabel(i + name));
		}
		this.setLabelTable(this.label);
		
		
		
		this.namedBorder = new TitledBorder("");
		this.setBackground(new Color(140, 140, 140));
		
		this.border = new CompoundBorder(this.namedBorder, new EmptyBorder(10, 10, 10, 10));
		setBorder(this.border);
		this.setToolTipText(this.getValue() + "");
		this.setMaximum(max);
		this.setMinimum(min);
		this.setMajorTickSpacing(1);
		
		this.setUI(new MetalSliderUI() {
		    protected void scrollDueToClickInTrack(int direction) {

		        int value = slider.getValue(); 

		        if (slider.getOrientation() == JSlider.HORIZONTAL) {
		            value = this.valueForXPosition(slider.getMousePosition().x);
		        } else if (slider.getOrientation() == JSlider.VERTICAL) {
		            value = this.valueForYPosition(slider.getMousePosition().y);
		        }
		        slider.setValue(value);
		    }
		});
		
		this.setLabelTable(label);
		setValue(value);
	}
    
	public void setBorderName(String name) {
		this.namedBorder.setTitle(name);
	}
    
	public void setTexte(String[] array) {
		Dictionary<Integer, JLabel> helplabel = new Hashtable<Integer, JLabel>();
		for (int i=0; i<array.length; i++) {  
			helplabel.put(i, new JLabel(array[i] + ""));
		}
		this.setLabelTable(helplabel);
	}
}