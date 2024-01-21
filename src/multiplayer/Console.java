package multiplayer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Console extends JFrame implements KeyListener, MouseListener, ActionListener {
	public Multiplayer mp;
	
	public JTextArea console;
	public JScrollPane scroll;
	
	public String consoleString;
	public String lastLine;
	public String command;
	public String mode;
	
	public JPopupMenu popup;
	public JMenuItem copy, cut, paste, clear;
	
	private boolean inputAllowed;
	private boolean isCommand;
	private boolean isSet;
	
	
	public Console(Multiplayer mp) {
		this.mp = mp;
		
		this.setTitle("Console");
		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.lastLine = "";
		this.consoleString = "";
		this.mode = "[Input]: ";
		
		this.console = new JTextArea();
		this.console.setLineWrap(true);
		this.console.setWrapStyleWord(true);
		this.console.addKeyListener(this);
		this.console.addMouseListener(this);
		this.scroll = new JScrollPane(this.console);
		
		this.add(this.scroll);
		
		this.popup = new JPopupMenu();
		this.popup.setSize(70, 40);
		
		this.cut = new JMenuItem("Cut");
		this.cut.addActionListener(this);
		this.copy = new JMenuItem("Copy");  
		this.copy.addActionListener(this);
		this.paste = new JMenuItem("Paste");  
		this.paste.addActionListener(this);
		this.clear = new JMenuItem("Clear");
		this.clear.addActionListener(this);
		
		this.popup.add(this.cut); 
		this.popup.add(this.copy); 
		this.popup.add(this.paste);
		this.popup.addSeparator();
		this.popup.add(this.clear);    
		
		console.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (!str.equals("\n") && inputAllowed || isSet) {
					if (insertAt(offs, console.getText(), str).startsWith(consoleString + mode) || isSet)
					super.insertString(offs, str, a);
				}
		    }
			
			@Override
			public void remove(int offs, int len) throws BadLocationException {
				String text = console.getText().substring(0, offs);
				if (text.startsWith(consoleString + mode) || isSet) {
					super.remove(offs, len);
				}
			}
		});
		
		
		this.setVisible(true);
	}
	
	public void print(String text) {
		lastLine = console.getText().substring(console.getText().lastIndexOf("\n")+1, console.getText().length());
		this.isSet = true;
		
		if (!isCommand) {
			this.consoleString = consoleString + text;
			this.console.setText(consoleString + lastLine);
		} else {
			this.console.setText(console.getText() + text);
			this.consoleString = console.getText();
		}
		
		//System.out.println("-----1-----\n" + consoleString + "\n-----1-----\n\n-----2-----\n" + this.console.getText() + "\n-----2-----\n\n\n");
		
		this.isSet = false;
		this.console.setCaretPosition(this.console.getText().length());
	}
	
	public void allowInput(boolean allow) {
		if (allow) {
			this.isSet = true;
			this.console.setText(console.getText() + mode);
			this.isSet = false;
			this.console.setCaretPosition(this.console.getText().length());
		}
		this.inputAllowed = allow;
	}
	
	public int countOfStringInString(String lookIn, String lookFor) {
		int count = 0;
		char[] indexes = lookFor.toCharArray();
		
		for (int i = 0; i <= lookIn.length()-lookFor.length(); i++) {
			for (int j = 0; j < lookFor.length(); j++) {
				if (indexes[j] != lookIn.charAt(i+j)) {
					break;
				}
				if (j == lookFor.length()-1) count++;
			}
		}
		
		return count;
	}
	
	public String insertAt(int index, String str, String in) {
		return str.substring(0, index) + in + str.substring(index, str.length());
	}
	
	
	
	public String[] commands = {"help", "send", "mode", "calculate"};
	public String[] modes = {"[Input]: ", "[Send]: "};
	public String[] cmd;
	
	public void doCommand(String input) {
		Arrays.sort(commands);
		this.cmd = input.split(" ");
		
		if (mode.equals(modes[1])) {
			input = " " + input;
			this.cmd = input.split(" ");
			sendCmd();
			return;
		} 
		switch(this.cmd[0]) {
			case "help":
				helpCmd();
				break;
			case "send":
				sendCmd();
				break;
			case "mode": 
				modeCmd();
				break;
			case "calculate":
			case "calc":
				calculateCmd();
				break;
			
			default:
				print("\nUnknown command. Use 'help' for a list of commands!");
		}
	}
	
	public void sendCmd() {
		String message = "[" + this.getTitle() + "]: ";
		for (int i = 1; i < cmd.length; i++) {
			message += cmd[i] + " ";
		}
		if (this.mp.send(message)) {
			print("\nMessage was send!");
		} else {
			print("\nWere not able to send a message!");
		}
	}
	
	public void helpCmd() {
		String out = "";
		
		for (int i = 0; i < commands.length; i++) {
			out += "\n" + commands[i];
		}
		print(out);
	}
	
	public void calculateCmd() {
		String expression = "";
		for (int i = 1; i < cmd.length; i++) {
			expression += cmd[i] + " ";
		}
		
		expression = expression.replaceAll("%pi", "Math.PI");
		expression = expression.replaceAll("%e", "Math.E");
		
		ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
		 
        try {
            Object result = engine.eval(expression);
            print("\n" + expression + " = " + result);
        }
        catch (ScriptException e) {
        	print("\nError!");
        }
	}
	
	public void modeCmd() {
		if (cmd.length == 2) {
			switch (cmd[1]) {
			case "input": 
				this.mode = modes[0];
				break;
			case "send":
				this.mode = modes[1];
				break;
			default:
				print("\nUnknown parameter. Use 'help mode' for a list of parameters!");
			}
		} else {
			print("\nMissing Parameter!\nmode (input | send)");
		}
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_ENTER) {
			this.inputAllowed = false; 
			this.isCommand = true;
			this.command = this.console.getText().substring(this.console.getText().lastIndexOf("\n" + mode)+mode.length()+1, this.console.getText().length());
			doCommand(command);
			
			this.consoleString = console.getText()+"\n";
			this.isSet = true;
			this.console.setText(consoleString);
			this.isSet = false;
			this.console.setCaretPosition(this.console.getText().length());
			allowInput(true);
			this.isCommand = false;
		}
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.mode = modes[0];
			this.isSet = true;
			this.console.setText(consoleString + modes[0]);
			this.isSet = false;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent key) {}

	@Override
	public void keyTyped(KeyEvent key) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			int x = e.getX();
			int y = e.getY();
			
			this.popup.show(this, x+15, y+25);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.clear)) {
			this.isSet = true;
			this.mode = "[Input]: ";
			this.console.setText(mode);
			this.consoleString = "";
			this.command = "";
			this.lastLine = "";
			this.isSet = false;
		}
	}
}
