package pack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

import ki.Difficulty;
import utils.SingleRootFileSystemView;
import wiederholungen.VierGWhModel;

public class VierGController implements ActionListener, ChangeListener, MouseListener, KeyListener {
	// Attribute
	private VierGView v1;
	private VierGModel m1;
	private VierGWhModel m2;

	private Timer timer;
	private ActionListener time;
	private Timer timerAnimation;
	private ActionListener animation;
	private boolean isTimerRunning;

	private JButton deletebutton;
	private JFileChooser chooser;
	private JFrame loadSave;
	public boolean kiSpielerDran;

	public VierGController() {
		this.m1 = new VierGModel();
		this.m2 = new VierGWhModel();
		this.v1 = new VierGView(this.m1, this.m2, this);

		this.chooser = new JFileChooser(m2.getDir());
		this.chooser.addActionListener(this);

		this.loadSave = new JFrame("Load save");
		this.loadSave.add(chooser);
		this.loadSave.pack();

		this.deletebutton = new JButton("Löschen");
		this.deletebutton.addActionListener(this);

		time = new ActionListener() { // TEST ob Sieg einzige möglichkeit bis her wie es geklappt hat
			public void actionPerformed(ActionEvent evt) {
				/*
				 * int gameResult = -1; if (m1.kiActive) { gameResult =
				 * m1.ki.gameResult(m1.board); }
				 */
				if (m1.hatGewonnenSpieler1()) {
					v1.Sieg(m1.SPIELER1);
					v1.enableButtons(true, VierGView.SPEICHERN);
				} else if (m1.hatGewonnenSpieler2()) {
					v1.Sieg(m1.SPIELER2);
					v1.enableButtons(true, VierGView.SPEICHERN);
				} else if (m1.unentschieden()) {
					v1.Sieg(-1);
					v1.enableButtons(true, VierGView.SPEICHERN);
				} else if (m1.kiActive) {
					if (kiSpielerDran) {
						kiSpielerDran = false;
						int spalte = m1.ki.getAIMove();
						if (spalte >= 0 || spalte <= 7) {
							m1.board.placeMove(spalte, 1);
							v1.enableButtonsIfRowNotFull();
							v1.clickButton(spalte);
							v1.enableButtons(false);
						}
					}
				}
			}
		};
		this.timer = new Timer((int) (250), time);
		this.timer.stop();

		animation = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				v1.startDropWh(m2.getAktZugSpalte());
				m2.setAktZugVor();
			}
		};
		this.timerAnimation = new Timer((int) (1000), animation);
		this.timerAnimation.stop();
	}

	public void loadSave() {
		UIManager.put("FileChooser.readOnly", Boolean.TRUE); // Keine Ordner erstellen

		this.loadSave.remove(this.chooser);
		/* Nur ein Dir nicht veränderbar */
		File root = new File(m2.getDir());
		FileSystemView fsv = new SingleRootFileSystemView(root);
		this.chooser = new JFileChooser(fsv);

		/* Nur Files */
		this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		/* Aussehen */
		disableButtonByClassNameSuffix(chooser, "GoHomeAction");
		disableButtonByClassNameSuffix(chooser, "ChangeToParentDirectoryAction");
		disableButtonByClassNameSuffix(chooser, "lookInLabelText");

		/* Neuen Löschen-Button am FileChooser und nach links bekommen */
		JPanel helppane1 = (JPanel) chooser.getComponent(3);
		JPanel helppane2 = (JPanel) helppane1.getComponent(3);
		JButton openbutton = (JButton) helppane2.getComponent(0);
		JButton cancelbutton = (JButton) helppane2.getComponent(1);
		cancelbutton.setText("Zurück");
		helppane2.remove(openbutton);
		helppane2.remove(cancelbutton);
		helppane2.add(this.deletebutton);
		helppane2.add(openbutton);
		helppane2.add(cancelbutton);

		this.chooser.setApproveButtonText("Laden");
		this.chooser.setApproveButtonToolTipText("Zum Starten einer Wiederholung");
		this.deletebutton.setToolTipText("Zum Löschen einer Wiederholung");
		/* Chooser mit Listener erstellen */
		this.loadSave.add(chooser);
		chooser.addActionListener(this);
		this.loadSave.repaint();
		this.loadSave.setVisible(true);
	}

	private static boolean disableButtonByClassNameSuffix(Container container, String classNameSuffix) {
		final int componentCount = container.getComponentCount();
		for (int i = 0; i < componentCount; i++) {
			final Component c = container.getComponent(i);
			if (c instanceof AbstractButton) {
				for (ActionListener l : ((AbstractButton) c).getActionListeners()) {
					if (l.getClass().getName().endsWith(classNameSuffix)) {
						c.setVisible(false);
						return true;
					}
				}
			} else if (c instanceof Container && disableButtonByClassNameSuffix((Container) c, classNameSuffix)) {
				return true;
			}
		}
		return false;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		key = key - KeyEvent.VK_1;
		if(key < 0 || key > 6) return;
		this.v1.clickButton(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JFileChooser) { // Ob von Filechooser
			if (e.getActionCommand().equals("CancelSelection")) { // Geschlossen

				this.loadSave.setVisible(false);
				this.loadSave.remove(chooser);
			} else if (e.getActionCommand().equals("ApproveSelection")) { // Öffnen
				this.m2.restart();
				this.m2.setSaveFile(((JFileChooser) e.getSource()).getSelectedFile());
				this.v1.repaintAll();
				this.m2.progressFile(m2.getSaveFile());
				this.v1.setFileNameText("Geladen: " + this.m2.getSaveFile().getName());
				if (m2.getSieger() != 2) {
					this.v1.setWhLabelText("Sieger: " + this.m2.getSpielername(this.m2.getSieger()));

				} else {
					this.v1.setWhLabelText("Unentschieden");
				}
				this.loadSave.setVisible(false);
				this.loadSave.remove(chooser);
				this.v1.setSpezFarbe(this.v1.isSpezFarbeCheckSelected());

				this.v1.name1.setText(this.m2.getSpielername(this.m2.SPIELER1));
				this.v1.name2.setText(this.m2.getSpielername(this.m2.SPIELER2));
				this.v1.repaint();
				this.v1.repaintAll();

			}
		}
		if (e.getSource() == this.deletebutton) {
			File file = this.chooser.getSelectedFile();
			boolean next = false;
			if (file != null) {
				if (file != null) {
					if (Files.exists(Paths.get(file.getPath())))
						next = true;
					if (next) {
						JLabel label = (new JLabel("Are you sure you want to delete this Save?", JLabel.CENTER));
						int check = JOptionPane.showOptionDialog(this.chooser, label, "Delete",
								JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
								new String[] { "Delete", "cancel" }, "Delete");
						if (check == 0) {
							if (this.m2.getSaveFile() != null) {
								if (this.m2.getSaveFile().equals(file)) {
									this.v1.setFileNameText("Geladen:    ---");
									this.v1.setWhLabelText("---");
									this.m2.restart();
									this.v1.repaintAll();
								}
							}
							file.delete();
							this.loadSave();
						}
					}
				}
			}
		}
		if (e.getActionCommand().equals(("1"))) {
			this.v1.buttonsDisabled[0] = this.v1.startDrop(0);
			if (this.v1.buttonsDisabled[0])
				this.v1.disableButton(0);
		}
		if (e.getActionCommand().equals(("2"))) {
			this.v1.buttonsDisabled[1] = this.v1.startDrop(1);
			if (this.v1.buttonsDisabled[1])
				this.v1.disableButton(1);
		}
		if (e.getActionCommand().equals(("3"))) {
			this.v1.buttonsDisabled[2] = this.v1.startDrop(2);
			if (this.v1.buttonsDisabled[2])
				this.v1.disableButton(2);
		}
		if (e.getActionCommand().equals(("4"))) {
			this.v1.buttonsDisabled[3] = this.v1.startDrop(3);
			if (this.v1.buttonsDisabled[3])
				this.v1.disableButton(3);

		}
		if (e.getActionCommand().equals(("5"))) {
			this.v1.buttonsDisabled[4] = this.v1.startDrop(4);
			if (this.v1.buttonsDisabled[4])
				this.v1.disableButton(4);
		}
		if (e.getActionCommand().equals(("6"))) {
			this.v1.buttonsDisabled[5] = this.v1.startDrop(5);
			if (this.v1.buttonsDisabled[5])
				this.v1.disableButton(5);
		}
		if (e.getActionCommand().equals(("7"))) {
			this.v1.buttonsDisabled[6] = this.v1.startDrop(6);
			if (this.v1.buttonsDisabled[6])
				this.v1.disableButton(6);
		}

		if (v1.isButtonSpeichern(e.getSource())) {
			boolean b = this.m1.speichern(this.v1);
			this.v1.enableButtons(b, VierGView.SPEICHERN);
		}

		if (this.v1.isButtonPause(e.getSource())) {
			this.isTimerRunning = false;

			this.v1.enableButtons(false, VierGView.PAUSE);
			this.v1.enableButtons(true, VierGView.PLAY);
			this.v1.setEnabledAnimationCheck(true);

			if (this.m2.getAktZug() < this.m2.getZugAnzahl()) {
				this.v1.enableButtons(true, VierGView.VOR);
			}
			if (this.m2.getAktZug() > 0) {
				this.v1.enableButtons(true, VierGView.ZURÜCK);
			}

			this.v1.setBorder(this.m2.defaultBorder, e.getSource());

			this.v1.setAutorun(false);
			this.timerAnimation.stop();
		}
		if (this.v1.isButtonPlay(e.getSource())) {
			this.isTimerRunning = true;

			this.v1.enableButtons(false, VierGView.PLAY);
			this.v1.enableButtons(true, VierGView.PAUSE);
			this.v1.enableButtons(false, VierGView.VOR);
			this.v1.enableButtons(false, VierGView.ZURÜCK);
			this.v1.setBorder(this.m2.defaultBorder, e.getSource());
			this.v1.setEnabledAnimationCheck(false);

			this.v1.setAutorun(true);
			this.timerAnimation.start();
		}

		if (this.v1.isButtonVor(e.getSource())) {

			this.v1.startDropWh(this.m2.getAktZugSpalte());
			this.m2.setAktZugVor();
			this.v1.enableButtons(false, VierGView.PLAY);

			if (this.m2.getAktZug() >= this.m2.getZugAnzahl()) {
				this.v1.enableButtons(false, VierGView.VOR);
				this.v1.setBorder(this.m2.defaultBorder, e.getSource());
			} else {
				this.v1.enableButtons(true, VierGView.ZURÜCK);
			}
		}
		if (this.v1.isButtonZurück(e.getSource())) {
			this.m2.setAktZugZurück();
			this.v1.deleteDropWh(this.m2.getAktZugSpalte());

			if (this.m2.getAktZug() <= 0) {
				this.v1.enableButtons(false, VierGView.ZURÜCK);
				this.v1.setBorder(this.m2.defaultBorder, e.getSource());
			} else {
				this.v1.enableButtons(true, VierGView.VOR);
			}
		}

		if (v1.isButtonStart(e.getSource())) {
			if (this.v1.areNamesDiffrent()) {
				v1.startGame();
				if (this.v1.isKiSpielerCheckSelected()) {
					this.m1.kiActive = true;
				} else {
					this.m1.kiActive = false;
				}
				this.timer.start();
				m1.restart();
				this.v1.isStartPressed = true;
			} else {
				this.v1.markNameText();
			}
		}

		if (v1.isLeichtR(e.getSource())) {
			this.m1.ki.setDifficulty(Difficulty.EASY);
		}
		if (v1.isNormalR(e.getSource())) {
			this.m1.ki.setDifficulty(Difficulty.NORMAL);
		}
		if (v1.isSchwerR(e.getSource())) {
			this.m1.ki.setDifficulty(Difficulty.HARD);
		}
		if (v1.isExtremR(e.getSource())) {
			this.m1.ki.setDifficulty(Difficulty.EXTREME);
		}

		if (v1.isButtonChooser(e.getSource())) {
			this.loadSave();
		}

		if (v1.isMenuSpielen(e.getSource())) {
			this.v1.aktivierSpiel();
		}
		if (v1.isMenuWiederholung(e.getSource())) {
			this.v1.aktivierWh();
		}
		if (v1.isAnimationCheck(e.getSource())) {
			this.v1.setAnimationOn(this.v1.isAnimationCheckSelected());
		}
		if (v1.isSpezFarbeCheck(e.getSource())) {
			this.v1.setSpezFarbe(this.v1.isSpezFarbeCheckSelected());
			this.v1.repaintAll();
		}
		if (v1.isKiSpielCheck(e.getSource())) {
			this.v1.machKiSpiel(this.v1.isKiSpielerCheckSelected());
		}

		if (this.m1.s1)
			this.v1.setButtonColor(this.m1.SPIELER1);
		if (this.m1.s2)
			this.v1.setButtonColor(this.m1.SPIELER2);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (v1.isChooser1Red(e.getSource()) || v1.isChooser1Green(e.getSource()) || v1.isChooser1Blue(e.getSource())) {
			v1.setSpieler1FarbText();
		}
		if (v1.isChooser2Red(e.getSource()) || v1.isChooser2Green(e.getSource()) || v1.isChooser2Blue(e.getSource())) {
			v1.setSpieler2FarbText();
		}
		if (v1.isSlider(e.getSource())) {
			this.v1.repaintSlider();
			timerAnimation.stop();
			timerAnimation.removeActionListener(this.animation);
			timerAnimation = new Timer(this.m2.getAnimationAbstande(this.v1.getSliderValue()), this.animation);
			if (this.isTimerRunning)
				timerAnimation.start();
			this.v1.repaintSlider();
		}
	}

	@Override
	public void mouseClicked(MouseEvent source) {
	}

	@Override
	public void mouseEntered(MouseEvent source) {
		// System.out.println("mouse entered");
		if (this.v1.isEnabledButton(source.getSource()))
			this.v1.setBorder(this.m2.hoverBorder, source.getSource());
	}

	@Override
	public void mouseExited(MouseEvent source) {
		// System.out.println("mouse exited");
		this.v1.setBorder(this.m2.defaultBorder, source.getSource());
	}

	@Override
	public void mousePressed(MouseEvent source) {
		// System.out.println("mouse pressed");
		if (this.v1.isEnabledButton(source.getSource()))
			this.v1.setButtonColor(new Color(212, 225, 247), source.getSource());
	}

	@Override
	public void mouseReleased(MouseEvent source) {
		// System.out.println("mouse released");
		this.v1.setButtonColor(new Color(170, 200, 220), source.getSource());
	}

}
