package pack;

import java.awt.Graphics;

import javax.swing.JPanel;

public class VierGPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//Attribute
	private VierGModel m1;
	private VierGView v1;
	private boolean s1drop[][];
	private boolean s2drop[][];
	private int yPos1, xPos1, yMove1, yStart1, yPos2, xPos2, yMove2, yStart2, stelle1s1, stelle2s1, stelle1s2, stelle2s2;
	//Konstruktor
	public VierGPanel(VierGModel m, VierGView v) {
		this.m1 = m;
		this.v1 = v;
		this.s1drop = new boolean[7][6];
		this.s2drop = new boolean[7][6];
		
		this.yPos1 = -101;
		this.xPos1 = 0;
		this.yMove1 = 10;
		this.yStart1 = -100;
		
		this.yPos2 = -101;
		this.xPos2 = 0;
		this.yMove2 = 10;
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
		
		
		g.setColor(this.m1.getColor(m1.SPIELER1));
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (this.m1.sr1[i][j]) {
					g.fillOval(6+x*i, 5+x*j, x-10, x-10);
				}
			}
		}
		g.setColor(this.m1.getColor(m1.SPIELER2));
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (this.m1.sr2[i][j]) {
					g.fillOval(6+x*i, 5+x*j, x-10, x-10);
				}
			}
		}
		
		this.v1.enableButtons(false);
		g.setColor(this.m1.getColor(m1.SPIELER1));
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
			g.fillOval(this.xPos1, this.yStart1+5, x-10, x-10);
			this.yStart1 += this.yMove1;
		}	
		
		
		
		g.setColor(this.m1.getColor(m1.SPIELER2));
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
			g.fillOval(this.xPos2, this.yStart2+5, x-10, x-10);
			this.yStart2 += this.yMove2;
		}	
		
		
		
		if (this.yStart1 < this.yPos1 || this.yStart2 < this.yPos2) {
			if (this.yStart1 < this.yPos1) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				//System.out.println(this.yStart1 + " " + this.yPos1 );
				this.repaint();
			} else if (this.yStart2 < this.yPos2) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				//System.out.println(this.yStart2 + " " + this.yPos2 );
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
			if (this.m1.kiActive && this.v1.isStartPressed) {
				if (this.m1.s1) {
					this.v1.enableButtonsIfRowNotFull();
				} else {
					this.v1.kiZug();
				}
			} else {
				this.v1.enableButtonsIfRowNotFull();
			}
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
		
		if (this.m1.s1) {
			this.m1.board.placeMove(x, (byte)2); 
			this.m1.s1 = false;
			this.m1.s2 = true;
			this.s1drop[x][row] = true;
		} else if (this.m1.s2) {
			this.m1.s2 = false;
			this.m1.s1 = true;
			this.s2drop[x][row] = true;
		}
		this.repaint();
		this.m1.setSpielverlauf(x);
		if (row<=0) return true;
		return false;
		
	}
	
	
}
