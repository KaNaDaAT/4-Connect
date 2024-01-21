package pack;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JFrame;

import ki.Board;
import ki.KI;
import utils.InputPane;

public class VierGModel {
	
	//Atribute
	public final int SPIELER1 = 0;
	public final int SPIELER2 = 1;
	
	public boolean s1; //spieler 1
	public boolean s2; //spieler 2
	
	private String spielerName1;
	private String spielerName2;
	
	private Color s1Color; //standard mäßig rot
	private Color s2Color; //standard mäßig blau
	
	public boolean sr[][];
	public boolean sr1[][];
	public boolean sr2[][];
	
	private int spielverlauf[];
	private int Sieger;
	private int startSpieler;
	private int aktZug;
	private String[] keys;
	private char[] key;
	
	private String dir;
	
	public boolean kiActive;
	public KI ki;
	public Board board;
	
	//Konstruktor
	public VierGModel() {
		this.sr = new boolean[7][6]; //Array starts from left-up with 0 --- first one is the column, second one is the row
		this.sr1 = new boolean[7][6];
		this.sr2 = new boolean[7][6];
		
		this.keys = new String[3];
		this.keys[0] = "040602HaarigerHarald1309Anull?!Warum&&Nicht()Einfacher!!!*";
		this.keys[1] = "dgfdhfjmshkj5676832igerHarald98/(/&/&''*mshkj5676ÖSKAHJH";
		this.keys[2] = "Aber Bitte Mit S0NN3()/%%!Aldarhazfdbu??)(Z7980";
		
		this.s1Color = new Color(234, 92, 44);
		this.s2Color = new Color(66, 185, 244);
		
		this.spielerName1 = "Spieler 1";
		this.spielerName2 = "Spieler 2";
		
		this.spielverlauf = new int[42];
		for (int i = 0; i < this.spielverlauf.length; i++) {
			this.spielverlauf[i] = -1;
		}
		
		this.Sieger = -1;
		
		this.board = new Board();
		this.ki = new KI(this.board); 
        
		this.dir = System.getProperty("user.dir");
		this.zufallsSpieler();
	}
	
	
	//Methoden
	public void setColor(int spieler, Color color) {
		if (spieler == this.SPIELER1) {
			this.s1Color = color;
		} else if (spieler == this.SPIELER2) {
			this.s2Color = color;
		}
	}
	
	public Color getColor(int spieler) {
		if (spieler == this.SPIELER1) {
			return this.s1Color;
		} else if (spieler == this.SPIELER2) {
			return this.s2Color;
		}
		return null;
	}
	
	public String getHexColor(int spieler) {
		if (spieler == this.SPIELER1) {
			return String.format("#%02x%02x%02x", this.s1Color.getRed(), this.s1Color.getGreen(), this.s1Color.getBlue());
		} else if (spieler == this.SPIELER2) {
			return String.format("#%02x%02x%02x", this.s2Color.getRed(), this.s2Color.getGreen(), this.s2Color.getBlue());
		}
		return null;
	}
	
	public void zufallsSpieler() {
		int zufallsS = (int )(Math.random()*2);
		this.startSpieler = zufallsS;
		if (zufallsS == this.SPIELER1) {
			this.s2 = false;
			this.s1 = true;
		} else if (zufallsS == this.SPIELER2) {
			this.s1 = false;
			this.s2 = true;
		}
	}
	
	public boolean hatGewonnenSpieler1() {
		this.Sieger = 0;
		/*Wagrecht*/
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				if(sr1[i][j] && sr1[i+1][j] && sr1[i+2][j] && sr1[i+3][j]) return true;
			}
		}
		/*Senkrecht*/
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				if(sr1[j][i] && sr1[j][i+1] && sr1[j][i+2] && sr1[j][i+3]) return true;
			}
		}
		/*Diagonal*/
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if(sr1[i][j] && sr1[i+1][j+1] && sr1[i+2][j+2] && sr1[i+3][j+3]) return true;
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j >= 0; j--) {
				if(sr1[i][j+3] && sr1[i+1][j+2] && sr1[i+2][j+1] && sr1[i+3][j]) return true;
			}
		}
		this.Sieger = -1;
		return false;
	}
	
	public boolean hatGewonnenSpieler2() {
		this.Sieger = 1;
		/*Wagrecht*/
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				if(sr2[i][j] && sr2[i+1][j] && sr2[i+2][j] && sr2[i+3][j]) return true;
			}
		}
		/*Senkrecht*/
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				if(sr2[j][i] && sr2[j][i+1] && sr2[j][i+2] && sr2[j][i+3]) return true;
			}
		}
		/*Diagonal*/
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if(sr2[i][j] && sr2[i+1][j+1] && sr2[i+2][j+2] && sr2[i+3][j+3]) return true;
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j >= 0; j--) {
				if(sr2[i][j+3] && sr2[i+1][j+2] && sr2[i+2][j+1] && sr2[i+3][j]) return true;
			}
		}
		this.Sieger = -1;
		return false;
	}

	public String getSpielername(int spieler) {
		if (this.SPIELER1 == spieler) {
			return this.spielerName1;
		} else if (this.SPIELER2 == spieler) {
			return this.spielerName2;
		} 
		return null;
	}


	public void restart() {
		this.sr = new boolean[7][6];
		this.sr1 = new boolean[7][6];
		this.sr2 = new boolean[7][6];
		this.aktZug = 0;
		this.spielverlauf = new int[42];
		for (int i = 0; i < this.spielverlauf.length; i++) {
			this.spielverlauf[i] = -1;
		}
		this.Sieger = -1;
	}
	
	public void zeigBooleans() {
		System.out.println("Spieler 1\n");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print(sr1[j][i]);
			}
			System.out.println();
		}
		System.out.println("\n\n");
		
		
		System.out.println("Spieler 2\n");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print(sr2[j][i]);
			}
			System.out.println();
		}
		System.out.println("\n\n");
		
		
		System.out.println("Feld\n");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print(sr[j][i]);
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}
	
	
	public void setSpielverlauf(int i) {
		this.spielverlauf[this.aktZug] = i;
		this.aktZug++;
	}
	
	public int getSpielverlaufStelle(int i) {
		if (i>0 && i <42)
			return this.spielverlauf[i];
		else
			return -2;
	}
	
	public int getStartSpieler() {
		return this.startSpieler;
	}
	
	
	public boolean unentschieden() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (sr[i][j] == false) {
					return false;
					
				}
			}
		}
		this.Sieger = 2;
		return true;
	}

	private String getSpeicherText() {
		String text = "";
		String tab = "\t";
		String br = "\n";
		text += "Spieler1 Name:" + tab + this.spielerName1 + br;
		text += "Spieler1 Farbe:" + tab + this.getHexColor(this.SPIELER1) + br;
		text += "Spieler2 Name:" + tab + this.spielerName2 + br;
		text += "Spieler2 Farbe:" + tab + this.getHexColor(this.SPIELER2) + br + br;
		
		text += "Spieler-Start:" + tab + this.getStartSpieler() + br + br;
		
		for (int i = 0; i < 42; i++) {
			if (this.spielverlauf[i] >= 0) {
				text += "Spalte:" + tab + (this.spielverlauf[i]+1) + br;
			} else {
				text += br;
				break;
			}
		}
		
		text += "Sieger:" + tab + this.Sieger;
				
		return text;
	}
	
	private String getCipherdSpeicherText() {
		String cipherd = this.getSpeicherText();
		Random rdm = new Random();
		int random = rdm.nextInt(2);
		this.key = this.keys[random].toCharArray();
		cipherd = this.crypt(cipherd);
		return cipherd;
	}

	public boolean speichern(JFrame parent) {
		InputPane inputPanel = new InputPane("Name of the new File:", "File-Name", parent);
		while (true) {
			String eingabe = inputPanel.start();
			if (eingabe != null && !eingabe.equals("")) {
				
				File file = new File(dir + "/saves/" + eingabe+".vgwh");
				if (!(Files.exists(Paths.get(file.getPath())))) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					} 
					this.speicherFile(file);
					return false;
				} 
			} else break;
		}
		
		return true;
	}
	

	private void speicherFile(File file) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(this.getCipherdSpeicherText());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setSpielerName(String spielername, int spieler) {
		if (this.SPIELER1 == spieler) {
			this.spielerName1 = spielername;
		} else if (this.SPIELER2 == spieler) {
			this.spielerName2= spielername;
		} 
	}
	
	public String crypt(String text) {
		char[] plain = text.toCharArray();
		char[] output = new char[plain.length];
		String crypted = "";
		for (int i = 0; i < plain.length; i++) {
			int result = (plain[i] + this.key[i % this.key.length]) % 128;
			output[i] = (char) result;
		}
		crypted = String.valueOf(output);
		return crypted;
	}
	
}
