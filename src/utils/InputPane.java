package utils;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class InputPane extends JDialog implements KeyListener, ActionListener {
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
	public InputPane(String text, String title, JFrame owner) {
		super(owner, title, true);
		panel = new JPanel();
		textField = new JTextField();
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		label = new JLabel(text);
		
		this.maxCharacterSize = 90;
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
		this.textField.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 2L;
			@Override
			public void insertString(int offs, String str, AttributeSet a) 
		            throws BadLocationException {
		        //Da Buchstaben verschieden groß sind
		        Font f = textField.getFont();
		        FontMetrics fm = getFontMetrics(f);
		        
		        int len = fm.stringWidth(textField.getText()+str+".txt");
		        if(len <= maxCharacterSize && str.matches("[a-zA-Z0-9_-]")) {
		        	super.insertString(offs, str, a);
		        } else {
		        	Toolkit.getDefaultToolkit().beep();
		        }
		    }
		});
		
		
		
		this.label.setSize(180, 20);
		this.label.setBounds(40, 10, 220, 25);
		
		this.okButton.addActionListener(this);
		this.okButton.setBounds(40, 80, 100, 30);
		
		this.cancelButton.addActionListener(this);
		this.cancelButton.setBounds(160, 80, 100, 30);
		
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
	
	
}
