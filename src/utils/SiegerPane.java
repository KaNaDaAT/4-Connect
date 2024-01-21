package utils;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SiegerPane extends JDialog implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	//Attrbute
	public JTextField textField;
	public JButton okButton;
	public JButton cancelButton;
	private JLabel label;
	private JPanel panel;
	
	public int maxCharacterSize;
	private String back;
	
	//Konstruktor
	public SiegerPane(String text, String title, JFrame owner) {
		super(owner, title, true);
		panel = new JPanel();
		textField = new JTextField();
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		
		label = new JLabel(text);
		label.setFont(new Font("Serif", Font.PLAIN, 15));
		
		
		this.maxCharacterSize = 78;
		this.setTitle(title);
		this.add(this.panel);
		this.setSize(300, 150);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(owner);
		this.setAlwaysOnTop(this.isAlwaysOnTopSupported());

		this.panel.setLayout(null);
		
		this.textField.setSize(180, 20);
		this.textField.setBounds(40, 40, 220, 25);
		this.textField.addKeyListener(this);
		
		
		
		this.label.setSize(260, 20);
		this.label.setBounds(20, 10, 260, 25);
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.okButton.addActionListener(this);
		this.okButton.setBounds(40, 80, 100, 30);
		this.okButton.setFocusPainted(false);
		
		this.cancelButton.addActionListener(this);
		this.cancelButton.setBounds(160, 80, 100, 30);
		this.cancelButton.setFocusPainted(false);
		
		this.setContentPane(this.panel);
		this.panel.add(this.textField);
		this.panel.add(this.cancelButton);
		this.panel.add(this.okButton);
		this.panel.add(this.label);
	}
	
	//Methoden
	public String start() {
		this.setVisible(true);
		return this.back;
	}
	
	public void setLabelText(String text) {
		this.label.setText(text);
	}
	
	//KeyListener
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.cancelButton) {
			this.back = null;
			this.dispose();
		}
		if (e.getSource() == this.okButton) {
			this.back = this.textField.getText();
			dispose();
		}
	}

	public void showInput(boolean b) {
		this.textField.setVisible(b);
	}

	public void showCancel(boolean b) {
		this.cancelButton.setVisible(b);
	}

	public void showOk(boolean b) {
		this.okButton.setVisible(b);
		
	}
	
	public void positionOk(int x, int y, int xSize, int ySize) {
		this.okButton.setBounds(x, y, xSize, ySize);
	}
	
	public void positionCancel(int x, int y, int xSize, int ySize) {
		this.cancelButton.setBounds(x, y, xSize, ySize);
	}
	
	public void positionInput(int x, int y, int xSize, int ySize) {
		this.textField.setBounds(x, y, xSize, ySize);
	}
	
	public void positionLabel(int x, int y, int xSize, int ySize) {
		this.label.setBounds(x, y, xSize, ySize);
	}
	
	public int getLabelWidth() {
		FontMetrics fm = getFontMetrics(this.label.getFont());
		return fm.stringWidth(this.label.getText());
	}
	
	public void setLabelFont(Font font) {
		this.label.setFont(font);
	}

	public void fontOk(Font font) {
		this.okButton.setFont(font);
	}
	
	public void fontCancel(Font font) {
		this.cancelButton.setFont(font);
	}
	
	public void textOk(String name) {
		this.okButton.setName(name);
	}
	
	public void textCancel(String name) {
		this.cancelButton.setName(name);
	}
}
