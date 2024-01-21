package wiederholungen;
import java.awt.Graphics;

import javax.swing.JPanel;

import pack.VierGView;

public class VierGWhPanel extends JPanel {
/**Dieses Panel dient der abspielung einer Wiederholung einer gespielten runde.
 * Man kann wählen ob es mit Animation abgespielt wird oder ohne. Ob es Automatisch spielt oder
 * man mittels Button jeden zug vor und zurück geht.
 */
	private static final long serialVersionUID = 1L;


	//Attribute
	private VierGWhModel m1;
	private VierGView v1;
	private boolean s1drop[][];
	private boolean s2drop[][];
	private int yPos1, xPos1, yMove1, yStart1, yPos2, xPos2, yMove2, yStart2, stelle1s1, stelle2s1, stelle1s2, stelle2s2;


	private boolean deleteAktiv;
	//Konstruktor
	public VierGWhPanel(VierGWhModel m, VierGView v) {
		this.m1 = m;
		this.v1 = v;
		
		this.s1drop = new boolean[7][6];
		this.s2drop = new boolean[7][6];
		
		this.yPos1 = -101;
		this.xPos1 = 0;
		this.yMove1 = 5;
		this.yStart1 = -100;
		
		this.yPos2 = -101;
		this.xPos2 = 0;
		this.yMove2 = 5;
		this.yStart2 = -100;
	}
	
	
	//Methoden
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x = 100;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				g.drawRect(1+i*x, j*x, x, x);
			}
		}
		
		
		if (this.v1.isAnimationOn() && !this.deleteAktiv) {
			this.v1.enableButtons(false, VierGView.PLAY);
			this.v1.enableButtons(false, VierGView.VOR);
			this.v1.enableButtons(false, VierGView.ZURÜCK);
			this.v1.setEnabledAnimationCheck(false);
			this.v1.setBorder(this.m1.defaultBorder, VierGView.VOR);
			
			g.setColor(this.v1.getUsedColor(m1.SPIELER1));
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (this.m1.sr1[i][j]) {
						g.fillOval(6+x*i, 5+x*j, x-10, x-10);
					}
				}
			}
			g.setColor(this.v1.getUsedColor(m1.SPIELER2));
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (this.m1.sr2[i][j]) {
						g.fillOval(6+x*i, 5+x*j, x-10, x-10);
					}
				}
			}
			
			g.setColor(this.v1.getUsedColor(m1.SPIELER1));
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (this.s1drop[i][j]) {
						this.yPos1 = 6+x*j;
						this.xPos1 = 6+x*i;
						this.stelle1s1 = i;
						this.stelle2s1 = j;
					}
				}
			}
			if (this.s1drop[stelle1s1][stelle2s1]) {
				g.fillOval(this.xPos1, this.yStart1, x-10, x-10);
				this.yStart1 += this.yMove1;
			}	
			
			
			
			g.setColor(this.v1.getUsedColor(m1.SPIELER2));
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (this.s2drop[i][j]) {
						this.yPos2 = 6+x*j;
						this.xPos2 = 6+x*i;
						this.stelle1s2 = i;
						this.stelle2s2 = j;
					}
				}
			}
			if (this.s2drop[stelle1s2][stelle2s2]) {
				g.fillOval(this.xPos2, this.yStart2, x-10, x-10);
				this.yStart2 += this.yMove2;
			}	
			
			
			
			if (this.yStart1 < this.yPos1 || this.yStart2 < this.yPos2) {
				//System.out.println("1.  START: " + this.yStart1 + " POS: " +  this.yPos1);
				//System.out.println("2.  START: " + this.yStart2 + " POS: " +  this.yPos2);
				if (this.yStart1 < this.yPos1) {
					try {
						Thread.sleep(this.m1.getAnimationTime(this.v1.getSliderValue()));
					} catch (InterruptedException e) {}
					this.repaint();
				} else if (this.yStart2 < this.yPos2) {
					try {
						Thread.sleep(this.m1.getAnimationTime(this.v1.getSliderValue()));
					} catch (InterruptedException e) {}
					this.repaint();
				}
			} else {
				if (this.m1.sr[stelle1s1][stelle2s1]) {
					this.m1.sr1[stelle1s1][stelle2s1] = true;
				} 
				
				if (this.m1.sr[stelle1s2][stelle2s2]) {
					this.m1.sr2[stelle1s2][stelle2s2] = true;
				}
				
				this.yPos1 =  -101;
				this.xPos1 = 0;
				this.yStart1 = -100;
				this.yPos2 =  -101;
				this.xPos2 = 0;
				this.yStart2 = -100;
				this.s2drop = new boolean[7][6];
				this.s1drop = new boolean[7][6];
				
				if (v1.isAutorunOn() == false) {
					this.v1.enableButtons(true, VierGView.PLAY);
					this.v1.enableButtons(true, VierGView.VOR);
					this.v1.enableButtons(true, VierGView.ZURÜCK);
					this.v1.setEnabledAnimationCheck(true);
				}
				
				if (this.m1.getAktZug() >= this.m1.getZugAnzahl()) {
					this.v1.enableButtons(false, VierGView.VOR);
					this.v1.setBorder(this.m1.defaultBorder, VierGView.VOR);
				}
				if (this.m1.getAktZug() <= 0) { 
					this.v1.enableButtons(false, VierGView.ZURÜCK);
					this.v1.setBorder(this.m1.defaultBorder, VierGView.ZURÜCK);
				}

			}
		} else {
			
			
			
			g.setColor(this.v1.getUsedColor(m1.SPIELER1));
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (this.m1.sr1[i][j]) {
						g.fillOval(6+x*i, 5+x*j, x-10, x-10);
					}
					
					if (this.s1drop[i][j]) {
						this.yPos1 = 5+x*j;
						this.xPos1 = 6+x*i;
						this.stelle1s1 = i;
						this.stelle2s1 = j;
					}
				}
			}
			if (this.s1drop[stelle1s1][stelle2s1]) {
				g.fillOval(this.xPos1, this.yPos1, x-10, x-10);
			}

			g.setColor(this.v1.getUsedColor(m1.SPIELER2));
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 6; j++) {
					if (this.m1.sr2[i][j]) {
						g.fillOval(6+x*i, 5+x*j, x-10, x-10);
					}
					
					if (this.s2drop[i][j]) {
						this.yPos2 = 5+x*j;
						this.xPos2 = 6+x*i;
						this.stelle1s2 = i;
						this.stelle2s2 = j;
					}
				}
			}
			if (this.s2drop[stelle1s2][stelle2s2]) {
				g.fillOval(this.xPos2, this.yPos2, x-10, x-10);
			}	
			
			if (this.m1.sr[stelle1s1][stelle2s1]) {
				this.m1.sr1[stelle1s1][stelle2s1] = true;
			} 
			if (this.m1.sr[stelle1s2][stelle2s2]) {
				this.m1.sr2[stelle1s2][stelle2s2] = true;
			}
			
			this.s1drop = new boolean[7][6];
			this.s2drop = new boolean[7][6];
			deleteAktiv = false;
			
			if (v1.isAutorunOn() == false) {
				this.v1.enableButtons(true, VierGView.PLAY);
				this.v1.enableButtons(true, VierGView.VOR);
				this.v1.enableButtons(true, VierGView.ZURÜCK);
			}
			
			if (this.m1.getAktZug() >= this.m1.getZugAnzahl()) {
				this.v1.enableButtons(false, VierGView.VOR);
				this.v1.setBorder(this.m1.defaultBorder, VierGView.VOR);
			}
			if (this.m1.getAktZug() <= 0) { 
				this.v1.enableButtons(false, VierGView.ZURÜCK);
				this.v1.setBorder(this.m1.defaultBorder, VierGView.ZURÜCK);
			}
			
			this.yPos1 =  -101;
			this.xPos1 = 0;
			this.yStart1 = -100;
			this.yPos2 =  -101;
			this.xPos2 = 0;
			this.yStart2 = -100;
		}
	}

	public boolean startDrop(int x) {
		this.s2drop = new boolean[7][6];
		this.s1drop = new boolean[7][6];
		if (x == -1) {
			this.repaint();
			return false;
		}
		int row = 0;
		
		for (int i = 0; i<=5; i++) {
			if (this.m1.sr[x][i] == false) {
				row++;
			} 
		}
		row--;
		this.m1.sr[x][row] = true;
		
		if (this.m1.istSpieler1()) {
			this.m1.spielerDran(m1.SPIELER2);
			this.s1drop[x][row] = true;
		} else if (this.m1.istSpieler2()) {
			this.m1.spielerDran(m1.SPIELER1);
			this.s2drop[x][row] = true;
		}
		this.repaint();
		if (row<=0) return true;
		return false;
		
	}
	public boolean deleteDrop(int x) {
		if (x == -1) {
			this.repaint();
			return false;
		}
		int row = 0;
		for (int i = 0; i<=5; i++) {
			if (this.m1.sr[x][i] == false) {
				row++;
			} 
		}
		//row--;
		System.out.println(row);
		this.m1.sr[x][row] = false;
		this.m1.sr1[x][row] = false;
		this.m1.sr2[x][row] = false;
		
		if (this.m1.istSpieler1()) {
			this.m1.spielerDran(m1.SPIELER2);
		} else if (this.m1.istSpieler2()) {
			this.m1.spielerDran(m1.SPIELER1);
		}
		this.deleteAktiv = true;
		this.repaint();
		if (row<=0) return true;
		return false;
		
	}
}
