package wiederholungen;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class VierGWhModel {
	
	//Atribute
	public final int SPIELER1 = 0;
	public final int SPIELER2 = 1;
	
	private boolean s1; //spieler 1
	private boolean s2; //spieler 2
	
	private String spielerName1;
	private String spielerName2;
	
	private Color s1Color;
	private Color s2Color;
	
	private Color s1DefaultColor;
	private Color s2DefaultColor;
	
	public boolean sr[][];
	public boolean sr1[][];
	public boolean sr2[][];
	public int spielverlauf[];
	
	public final Border hoverBorder, defaultBorder;
	
	private String dir;
	private int Sieger;
	private int zugAnzahl;
	private int aktZug;
	private File saveFile;
	private String keys[];
	private char[] key;
	
	//Konstruktor
	public VierGWhModel() {
		this.keys = new String[3];
		this.keys[0] = "040602HaarigerHarald1309Anull?!Warum&&Nicht()Einfacher!!!*";
		this.keys[1] = "dgfdhfjmshkj5676832igerHarald98/(/&/&''*mshkj5676ÖSKAHJH";
		this.keys[2] = "Aber Bitte Mit S0NN3()/%%!Aldarhazfdbu??)(Z7980";
		this.key = keys[0].toCharArray();
		this.defaultBorder = BorderFactory.createEtchedBorder(0, new Color(10, 10, 10), new Color(255, 255, 255));
		this.hoverBorder = BorderFactory.createEtchedBorder(1, new Color(10, 10, 10), new Color(150, 180, 200));
		
		this.dir = System.getProperty("user.dir");
		this.sr = new boolean[7][6]; //Array starts from left-up with 0 --- first one is the column, second one is the row
		this.sr1 = new boolean[7][6];
		this.sr2 = new boolean[7][6];
		
		this.spielverlauf = new int[42];
		for (int i = 0; i < this.spielverlauf.length; i++) {
			this.spielverlauf[i] = -1;
		}
		
		
		this.s1Color = new Color(234, 92, 44);
		this.s2Color = new Color(66, 185, 244);
		
		this.s1DefaultColor = new Color(234, 92, 44);
		this.s2DefaultColor = new Color(66, 185, 244);
		
		
		this.spielerName1 = "Spieler 1";
		this.spielerName2 = "Spieler 2";
		
		zufallsSpieler();
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
	public Color getDefaultColor(int spieler) {
		if (spieler == this.SPIELER1) {
			return this.s1DefaultColor;
		} else if (spieler == this.SPIELER2) {
			return this.s2DefaultColor;
		}
		return null;
	}
	
	public void zufallsSpieler() {
		Random zufall = new Random();
		int zufallsS =  zufall.nextInt(2);
		if (zufallsS == this.SPIELER1) {
			this.s2 = false;
			this.s1 = true;
		}
		if (zufallsS == this.SPIELER2) {
			this.s1 = false;
			this.s2 = true;
		}
	}

	public String getSpielername(int spieler) {
		if (this.SPIELER1 == spieler) {
			return this.spielerName1;
		} else if (this.SPIELER2 == spieler) {
			return this.spielerName2;
		} 
		return null;
	}

	public void setSpielerName(String spielername, int spieler) {
		if (this.SPIELER1 == spieler) {
			this.spielerName1 = spielername;
		} else if (this.SPIELER2 == spieler) {
			this.spielerName2= spielername;
		} 
	}
	

	public void spielerDran(int spieler) {
		if (this.SPIELER1 == spieler) {
			this.s1 = true;
			this.s2 = false;
		} else if (this.SPIELER2 == spieler) {
			this.s2 = true;
			this.s1 = false;
		} else if (spieler == 0) {
			this.s2 = false;
			this.s1 = false;
		}
	}
	
	public void restart() {
		this.sr = new boolean[7][6];
		this.sr1 = new boolean[7][6];
		this.sr2 = new boolean[7][6];
		
		this.spielerName1 = "Spieler 1";
		this.spielerName2 = "Spieler 2";
		
		this.spielverlauf = new int[42];
		for (int i = 0; i < this.spielverlauf.length; i++) {
			this.spielverlauf[i] = -1;
		}
		this.aktZug = 0;
		this.zugAnzahl = 0;
	}
	
	public boolean istSpieler1() {
		return this.s1;
	}
	
	public boolean istSpieler2() {
		return this.s2;
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
	
	public String readFile(File file) {
        String content = "";
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            //e.printStackTrace();
        	content = "";
        }
        return content;
    }
	
	public void progressFile(File file) {
		int stelle = 0;
		
		try {
            String content = "";
            if (file.getName().endsWith(".vgwh")) {
            	content = readFile(file);
            	content = encrypt(content, 0);
	            content = content.replace("\t","");
	            String[] lines = content.split("\n");
	            for (String line : lines) {
	            	if (line.contains("Sieger:")) {
						this.Sieger = Integer.parseInt(line.substring(line.indexOf(':') + 1, line.indexOf(':') + 2));
					}
					if (line.contains("Spieler1 Name:")) {
						this.spielerName1 = line.substring(line.indexOf(':') + 1);
					}
					if (line.contains("Spieler2 Name:")) {
						this.spielerName2 = line.substring(line.indexOf(':') + 1);
					}
					if (line.contains("Spieler1 Farbe:")) {
						String spielerFarbe = line.substring(line.indexOf(':') + 1);
						spielerFarbe = spielerFarbe.substring(0, 7);
						Color color = Color.decode(spielerFarbe);
						this.s1Color = color;
					}
					if (line.contains("Spieler2 Farbe:")) {
						String spielerFarbe = line.substring(line.indexOf(':') + 1);
						spielerFarbe = spielerFarbe.substring(0, 7);
						Color color = Color.decode(spielerFarbe);
						this.s2Color = color;
					}
					if (line.contains("Spieler-Start:")) {
						String startSpieler = line.substring(line.indexOf(':') + 1);
						int i = Integer.parseInt(startSpieler);
						s1 = false;
						s2 = false;
						if (i == this.SPIELER1) {
							
							s1 = true;
						} else if (i == this.SPIELER2) {
							s2 = true;
						}
					}
					if (line.contains("Spalte:")) {
						this.spielverlauf[stelle] = Integer.parseInt(line.substring(line.indexOf(':') + 1, line.indexOf(':') + 2))-1;
						stelle++;
					}
				} 
            }
            this.zugAnzahl = stelle;
        } catch (Exception e) {e.printStackTrace();}
		
	}
	
	public int getSieger() {
		return this.Sieger;
	}
	public int getZugAnzahl() {
		return this.zugAnzahl;
	}
	
	
	public String getDir() {
		return this.dir + "/saves/";
	}
	
	public int getAktZug() {
		return aktZug;
	}
	
	public void setAktZugVor() {
		this.aktZug ++;
		if (this.aktZug > zugAnzahl) this.aktZug--;
	}
	
	public void setAktZugZurück() {
		this.aktZug --;
		if (this.aktZug < 0) this.aktZug++;
	}
	
	public int getAktZugSpalte() {
		if (this.aktZug >= 42) return -1;
		return spielverlauf[this.aktZug];
	}

	public int getAnimationAbstande(int sliderValue) {
		switch (sliderValue) {
		case 0: 	return 2000;
		case 1: 	return 1000;
		case 2:		return 750;
		case 3:		return 500;
			default: 	return 1000;
		}
	}

	public long getAnimationTime(int sliderValue) {
		switch (sliderValue) {
		case 0: 	return 10;
		case 1: 	return 4;
		case 2:		return 2;
		case 3:		return 1;
			default: 	return 10;
		}
	}

	public void setSaveFile(File selectedFile) {
		this.saveFile = selectedFile;
	}

	public File getSaveFile() {
		return this.saveFile;
	}
	
	
	public String encrypt(String text, int keyIndex) throws UnexpectedException {
		char[] plain = text.toCharArray();
		char[] output = new char[plain.length];
		String crypted = "";
		int result;
		for (int i = 0; i < plain.length; i++) {
		if (plain[i] - this.key[i % this.key.length] < 0) 
			result = (plain[i] - this.key[i % this.key.length]) + 128;
			else result = (plain[i] - this.key[i % this.key.length]) % 128;
			output[i] = (char) result;
		}
		crypted = String.valueOf(output);
		if (!crypted.startsWith("Spieler1 Name:")) {
			try {
				this.key = keys[keyIndex].toCharArray();
				System.out.println("Key: " + keys[keyIndex]);
				crypted = encrypt(text, keyIndex + 1);
			} catch (Exception e) {
				throw new UnexpectedException("A key is wrong\n\tat " + this.getClass());
			}
		}
		return crypted;
	}
	
}
