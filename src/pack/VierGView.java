package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import ki.Board;
import ki.Difficulty;
import ki.KI;
import utils.ColorChooser;
import utils.Oval;
import utils.SiegerPane;
import utils.SliderMenuItem;
import utils.TriangleButton;
import wiederholungen.VierGWhModel;
import wiederholungen.VierGWhPanel;

//Nirmala UI

public class VierGView extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final int PAUSE = 0;
	public static final int PLAY = 1;
	public static final int VOR = 2;
	public static final int ZURÜCK = 3;
	public static final int SPEICHERN = 4;

	// Attribute
	private VierGModel m1;
	private VierGWhModel m2;
	private VierGController c1;
	private VierGPanel p1;
	private VierGWhPanel p2;

	private TriangleButton button1, button2, button3, button4, button5, button6, button7;

	private Dimension dim;

	private Image icon;

	/** Menü-Bar **/
	private JMenu menu1;
	private JMenu menu2;
	private JMenu menu3;
	private JMenuBar menuBar;
	private JMenuItem item1;
	private JMenuItem item2;
	private SliderMenuItem slider;
	private JPanel menuBarPanel1, menuBarPanel2, difficultysPanel;
	private ButtonGroup difficultysGroup;
	private JCheckBox kiGameCheck;
	private JRadioButton easyRadio, normalRadio, hardRadio, extremeRadio;
	private int spacing;
	public boolean buttonsDisabled[] = new boolean[7];

	/** Platzhalter Panels **/
	private Container container;

	/** Hauptpanel **/
	private JPanel mainpanel;

	/** Menu Panel **/
	private JPanel spielmenu, menupanel1, menupanel2, menupanel3; // Spielmenü
	private JButton startButton, playButton, pauseButton, forwardButton, backwardsButton;
	private JTextField spielerName1Field, spielerName2Field, spielerFarbe1Field, spielerFarbe2Field;
	private JLabel spieler1Label, spieler2Label;
	private Oval spieler1FarbeOval, spieler2FarbeOval;
	private ColorChooser spieler1Chooser, spieler2Chooser;

	private JPanel whmenu, menuWHpanel1, menuWHpanel2, menuWHpanel3; // Wiederholungsmenü
	private JCheckBox animationCheck, spezFarbeCheck;
	public JTextField name1, name2;
	private Oval spieler1WhOval, spieler2WhOval;
	private JButton startChooser;
	private JTextPane gewinnerLabel, whFile;
	private JPanel whPanel;

	private SiegerPane winnerPanel;
	private JButton speicherButton;
	private String dir;

	private boolean animationOn;
	private boolean autorunOn;

	protected boolean isStartPressed;

	// Konstruktor
	public VierGView(VierGModel model1, VierGWhModel model2, VierGController c) {

		this.dir = System.getProperty("user.dir");
		this.m1 = model1;
		this.m2 = model2;
		this.c1 = c;
		this.p1 = new VierGPanel(this.m1, this);
		this.p2 = new VierGWhPanel(this.m2, this);
		this.setFocusable(true);

		this.animationOn = true;

		// this.setAlwaysOnTop(true);

		/* Einstellungen */
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("Vier Gewinnt");
		this.setBackground(new Color(254, 255, 226));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Größe setzen */
		this.setSize(911, 703);
		this.setPreferredSize(new Dimension(910, 680));
		this.setResizable(false);

		/* Icon */
		this.icon = new ImageIcon(System.getProperty("user.dir") + "/icon/4g.png").getImage();
		this.setIconImage(this.icon);

		/** Seiten Panel (MENÜ) **/
		// Spiel
		Border whiteline = BorderFactory.createLineBorder(Color.white);
		TitledBorder title1, title2, optiontitle, einstellungtitle, difficultystitle;
		title1 = BorderFactory.createTitledBorder(whiteline, "Spieler 1");
		title2 = BorderFactory.createTitledBorder(whiteline, "Spieler 2");
		optiontitle = BorderFactory.createTitledBorder(whiteline, "Optionen");
		einstellungtitle = BorderFactory.createTitledBorder(whiteline, "Spiel-Einstellungen");
		difficultystitle = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				"Difficultysgrad");

		this.spielmenu = new JPanel();
		this.spielmenu.setBackground(new Color(174, 208, 226));
		this.spielmenu.setPreferredSize(new Dimension(this.getWidth() / 5 + 20, this.getHeight()));
		this.spielmenu.setSize(new Dimension(this.getWidth() / 5 + 20, this.getHeight()));
		this.spielmenu.setVisible(true);
		this.spielmenu.setLayout(null);

		// Wiederholung
		this.whmenu = new JPanel();
		this.whmenu.setBackground(new Color(174, 208, 226));
		this.whmenu.setPreferredSize(new Dimension(this.getWidth() / 5 + 20, this.getHeight()));
		this.whmenu.setSize(new Dimension(this.getWidth() / 5 + 20, this.getHeight()));
		this.whmenu.setVisible(true);
		this.whmenu.setLayout(null);

		/** MENÜ-BAR **/
		this.menuBar = new JMenuBar();
		this.menuBar.setBackground(new Color(255, 255, 255));
		this.menu1 = new JMenu("Menü");
		this.menu2 = new JMenu("WH-Optionen");
		this.menu3 = new JMenu("Einstellungen");

		this.menuBar.add(this.menu1);
		this.menuBar.add(this.menu2);
		this.menuBar.add(this.menu2);
		this.setJMenuBar(this.menuBar);

		this.item1 = new JMenuItem("Spielen");
		this.menu1.add(this.item1);
		this.item1.addActionListener(this.c1);
		this.item2 = new JMenuItem("Wiederholung");
		this.menu1.add(this.item2);
		this.item2.addActionListener(this.c1);

		/** Menü WH Optionen */

		String[] array = { "0.5x", "1.0x", "2.0x", "4.0x" };
		this.slider = new SliderMenuItem(0, 3, 1, "x");
		this.slider.setTexte(array);
		this.slider.setBorderName("Geschwindigkeit");
		this.slider.setBackground(new Color(140, 140, 140));
		this.slider.setVisible(true);
		this.slider.setPaintLabels(true);
		this.slider.setPaintTicks(true);
		this.slider.setPreferredSize(new Dimension(200, 65));
		this.slider.addChangeListener(this.c1);
		this.slider.setToolTipText("");

		this.menuBarPanel1 = new JPanel();
		this.menuBarPanel1.setPreferredSize(new Dimension(200, 70));
		this.menuBarPanel1.setBackground(new Color(140, 140, 140));
		this.menuBarPanel1.add(this.slider, BorderLayout.CENTER);

		this.menu2.add(this.menuBarPanel1);

		// this.abstand = 80;
		/** Menü Spiel Optionen */

		this.menuBarPanel2 = new JPanel();
		this.menuBarPanel2.setPreferredSize(new Dimension(220, 130 + this.spacing));
		this.menuBarPanel2.setBackground(new Color(140, 140, 140));
		this.menuBarPanel2.setBorder(einstellungtitle);
		this.menuBarPanel2.setLayout(null);
		this.menu3.add(this.menuBarPanel2);

		this.kiGameCheck = new JCheckBox("Bot-Gegner");
		this.kiGameCheck.setBounds(3, 20, 100, 25);
		this.kiGameCheck.setBackground(this.menuBarPanel2.getBackground());
		this.kiGameCheck.setFocusPainted(false);
		this.kiGameCheck.setSelected(false);
		this.kiGameCheck.addActionListener(this.c1);
		this.menuBarPanel2.add(this.kiGameCheck);

		/** KI Difficultyspanel **/

		this.difficultysPanel = new JPanel();
		this.difficultysPanel.setPreferredSize(new Dimension(200, this.spacing));
		this.difficultysPanel.setBackground(new Color(140, 140, 140));
		this.difficultysPanel.setBorder(difficultystitle);
		this.difficultysPanel.setLayout(null);
		this.difficultysPanel.setBounds(10, 50, 200, this.spacing);
		this.menuBarPanel2.add(difficultysPanel);

		this.easyRadio = new JRadioButton("Leichter     Bot");
		this.easyRadio.setBounds(10, 21, 150, 15);
		this.easyRadio.setBackground(this.difficultysPanel.getBackground());
		this.easyRadio.setFocusPainted(false);
		this.easyRadio.setSelected(true);
		this.easyRadio.addActionListener(this.c1);
		this.difficultysPanel.add(this.easyRadio);

		this.normalRadio = new JRadioButton("Normaler   Bot");
		this.normalRadio.setBounds(10, 36, 150, 15);
		this.normalRadio.setBackground(this.difficultysPanel.getBackground());
		this.normalRadio.setFocusPainted(false);
		this.normalRadio.setSelected(false);
		this.normalRadio.addActionListener(this.c1);
		this.difficultysPanel.add(this.normalRadio);

		this.hardRadio = new JRadioButton("Schwerer  Bot");
		this.hardRadio.setBounds(10, 51, 150, 15);
		this.hardRadio.setBackground(this.difficultysPanel.getBackground());
		this.hardRadio.setFocusPainted(false);
		this.hardRadio.setSelected(false);
		this.hardRadio.addActionListener(this.c1);
		this.difficultysPanel.add(this.hardRadio);

		this.extremeRadio = new JRadioButton("Extremer   Bot");
		this.extremeRadio.setBounds(10, 66, 150, 15);
		this.extremeRadio.setBackground(this.difficultysPanel.getBackground());
		this.extremeRadio.setFocusPainted(false);
		this.extremeRadio.setSelected(false);
		this.extremeRadio.addActionListener(this.c1);
		this.difficultysPanel.add(this.extremeRadio);

		this.difficultysGroup = new ButtonGroup();
		this.difficultysGroup.add(this.easyRadio);
		this.difficultysGroup.add(this.normalRadio);
		this.difficultysGroup.add(this.hardRadio);
		this.difficultysGroup.add(this.extremeRadio);

		/** Menü Wiederholung Ordnung **/

		this.backwardsButton = new JButton("\u23f8");
		try {
			BufferedImage buttonIcon = ImageIO.read(new File(dir + "/img/pfeil links.png"));
			this.backwardsButton = new JButton(new ImageIcon(buttonIcon));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		this.backwardsButton
				.setBorder(BorderFactory.createEtchedBorder(0, new Color(10, 10, 10), new Color(255, 255, 255)));
		this.backwardsButton.addActionListener(this.c1);
		this.backwardsButton.setBounds(6, 5, 40, 40);
		this.backwardsButton.setPreferredSize(new Dimension(40, 40));
		this.backwardsButton.setFocusPainted(false);
		this.backwardsButton.setBackground(new Color(170, 200, 220));
		this.backwardsButton.addMouseListener(this.c1);
		this.enableButtons(false, VierGView.ZURÜCK);

		this.playButton = new JButton("\u25B6"); // \u25b6 \u23f8
		try {
			BufferedImage buttonIcon = ImageIO.read(new File(dir + "/img/play.png"));
			this.playButton = new JButton(new ImageIcon(buttonIcon));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		this.playButton.setBorder(BorderFactory.createEtchedBorder(0, new Color(10, 10, 10), new Color(255, 255, 255)));
		this.playButton.addActionListener(this.c1);
		this.playButton.setBounds(59, 5, 40, 40);
		this.playButton.setFocusPainted(false);
		this.playButton.setBackground(new Color(170, 200, 220));
		this.playButton.addMouseListener(this.c1);

		this.pauseButton = new JButton("\u23f8");
		try {
			BufferedImage buttonIcon = ImageIO.read(new File(dir + "/img/pause.png"));
			this.pauseButton = new JButton(new ImageIcon(buttonIcon));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		this.pauseButton
				.setBorder(BorderFactory.createEtchedBorder(0, new Color(10, 10, 10), new Color(255, 255, 255)));
		this.pauseButton.addActionListener(this.c1);
		this.pauseButton.setBounds(104, 5, 40, 40);
		this.pauseButton.setPreferredSize(new Dimension(40, 40));
		this.pauseButton.setFocusPainted(false);
		this.pauseButton.setBackground(new Color(170, 200, 220));
		this.pauseButton.addMouseListener(this.c1);
		this.enableButtons(false, VierGView.PAUSE);

		this.forwardButton = new JButton("\u23f8");
		try {
			BufferedImage buttonIcon = ImageIO.read(new File(dir + "/img/pfeil rechts.png"));
			this.forwardButton = new JButton(new ImageIcon(buttonIcon));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		this.forwardButton.setBorder(BorderFactory.createEtchedBorder(0, new Color(10, 10, 10), new Color(255, 255, 255)));
		this.forwardButton.addActionListener(this.c1);
		this.forwardButton.setBounds(157, 5, 40, 40);
		this.forwardButton.setPreferredSize(new Dimension(40, 40));
		this.forwardButton.setFocusPainted(false);
		this.forwardButton.setBackground(new Color(170, 200, 220));
		this.forwardButton.addMouseListener(this.c1);

		this.whmenu.add(this.playButton);
		this.whmenu.add(this.pauseButton);
		this.whmenu.add(this.forwardButton);
		this.whmenu.add(this.backwardsButton);

		this.menuWHpanel1 = new JPanel();
		this.menuWHpanel1.setBackground(new Color(174, 208, 226));
		this.menuWHpanel1.setVisible(true);
		this.menuWHpanel1.setLayout(null);
		this.menuWHpanel1.setBorder(title1);
		this.menuWHpanel1.setBounds(0, 46, 202, 200);

		this.menuWHpanel2 = new JPanel();
		this.menuWHpanel2.setBackground(new Color(174, 208, 226));
		this.menuWHpanel2.setVisible(true);
		this.menuWHpanel2.setLayout(null);
		this.menuWHpanel2.setBorder(title2);
		this.menuWHpanel2.setBounds(0, 249, 202, 200);

		this.menuWHpanel3 = new JPanel();
		this.menuWHpanel3.setBackground(new Color(174, 208, 226));
		this.menuWHpanel3.setVisible(true);
		this.menuWHpanel3.setLayout(null);
		this.menuWHpanel3.setBounds(0, 449, 202, 201);
		this.menuWHpanel3.setBorder(optiontitle);
		this.menuWHpanel3.setSize(202, 199);

		this.whmenu.add(menuWHpanel1);
		this.whmenu.add(menuWHpanel2);
		this.whmenu.add(menuWHpanel3);

		this.whPanel = new JPanel();
		this.whPanel.setPreferredSize(new Dimension(this.getWidth(), 49));
		this.whPanel.setBackground(this.getBackground());
		this.whPanel.setLayout(null);

		JPanel helppanel = new JPanel();
		helppanel.setBounds(0, 0, 600, 49);
		helppanel.setBackground(this.whPanel.getBackground());
		helppanel.setLayout(null);

		this.gewinnerLabel = new JTextPane();
		this.gewinnerLabel.setBounds(0, 0, 600, 49);
		this.gewinnerLabel.setText("---");
		this.gewinnerLabel.setFont(new Font("Nirmala UI", Font.PLAIN, 32));
		this.gewinnerLabel.setEditable(false);
		this.gewinnerLabel.setBackground(this.whPanel.getBackground());

		this.whPanel.add(helppanel);
		helppanel.add(this.gewinnerLabel, BorderLayout.WEST);

		// Componenten auf dem WH Panel
		this.animationCheck = new JCheckBox("Animation");
		this.animationCheck.setBounds(3, 20, 190, 25);
		this.animationCheck.setBackground(this.whmenu.getBackground());
		this.animationCheck.setFocusPainted(false);
		this.animationCheck.setSelected(true);
		this.animationCheck.addActionListener(this.c1);
		this.menuWHpanel3.add(this.animationCheck);

		this.spezFarbeCheck = new JCheckBox("Spezifische Farbe");
		this.spezFarbeCheck.setBounds(3, 42, 190, 25);
		this.spezFarbeCheck.setBackground(this.whmenu.getBackground());
		this.spezFarbeCheck.setFocusPainted(false);
		this.spezFarbeCheck.setSelected(true);
		this.spezFarbeCheck.addActionListener(this.c1);
		this.menuWHpanel3.add(this.spezFarbeCheck);

		this.name1 = new JTextField();
		this.name1.setBounds(10, 42, 125, 20);
		this.name1.setText("Spieler 1");
		this.name1.setEnabled(false);
		this.menuWHpanel1.add(this.name1);

		this.name2 = new JTextField();
		this.name2.setBounds(10, 42, 125, 20);
		this.name2.setText("Spieler 2");
		this.name2.setEnabled(false);
		this.menuWHpanel2.add(this.name2);

		JLabel label1 = new JLabel("Name:");
		label1.setBounds(10, 25, 60, 15);
		this.menuWHpanel1.add(label1);

		JLabel label2 = new JLabel("Name:");
		label2.setBounds(10, 25, 60, 15);
		this.menuWHpanel2.add(label2);

		this.spieler1WhOval = new Oval(new Color(0, 0, 0));
		this.spieler1WhOval.setBounds(10, 80, 100, 100);
		this.spieler1WhOval.setBackground(new Color(174, 208, 226));
		this.spieler1WhOval.setColor(m2.getColor(m2.SPIELER1));
		this.menuWHpanel1.add(this.spieler1WhOval);

		this.spieler2WhOval = new Oval(new Color(0, 0, 0));
		this.spieler2WhOval.setBounds(10, 80, 100, 100);
		this.spieler2WhOval.setBackground(new Color(174, 208, 226));
		this.spieler2WhOval.setColor(m2.getColor(m2.SPIELER2));
		this.menuWHpanel2.add(this.spieler2WhOval);

		/** Menü Spiel Ordnung **/

		this.menupanel1 = new JPanel();
		this.menupanel1.setBackground(new Color(174, 208, 226));
		this.menupanel1.setVisible(true);
		this.menupanel1.setLayout(null);
		this.menupanel1.setBorder(title1);
		this.menupanel1.setBounds(0, 46, 202, 250);

		this.menupanel2 = new JPanel();
		this.menupanel2.setBackground(new Color(174, 208, 226));
		this.menupanel2.setVisible(true);
		this.menupanel2.setLayout(null);
		this.menupanel2.setBorder(title2);
		this.menupanel2.setBounds(0, 301, 202, 250);

		this.menupanel3 = new JPanel();
		this.menupanel3.setBackground(new Color(174, 208, 226));
		this.menupanel3.setVisible(true);
		this.menupanel3.setLayout(null);
		this.menupanel3.setBounds(0, 551, 202, 99);
		this.menupanel3.setSize(202, 99);

		this.spielmenu.add(menupanel1);
		this.spielmenu.add(menupanel2);
		this.spielmenu.add(menupanel3);

		/* Komponenten am Menü */

		/* Spieler 1 */
		this.spieler1Label = new JLabel("Name:");
		this.spieler1Label.setBounds(10, 25, 60, 15);
		this.menupanel1.add(this.spieler1Label);

		/* Spieler 2 */
		this.spieler2Label = new JLabel("Name:");
		this.spieler2Label.setBounds(10, 25, 60, 15);
		this.menupanel2.add(this.spieler2Label);

		/* Spieler 1 */
		this.spielerName1Field = new JTextField();
		this.spielerName1Field.setBounds(10, 42, 125, 20);
		this.spielerName1Field.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 2L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				String text = spielerName1Field.getText() + str;
				// Da Buchstaben verschieden groß sind
				Font f = spielerName1Field.getFont();
				FontMetrics fm = getFontMetrics(f);
				int len = fm.stringWidth(text);
				if (len < 120) {
					super.insertString(offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});
		this.spielerName1Field.setText("Spieler 1");
		this.menupanel1.add(this.spielerName1Field);

		/* Spieler 2 */
		this.spielerName2Field = new JTextField();
		this.spielerName2Field.setBounds(10, 42, 125, 20);
		this.spielerName2Field.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 2L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				String text = spielerName2Field.getText() + str;
				// Da Buchstaben verschieden groß sind
				Font f = spielerName2Field.getFont();
				FontMetrics fm = getFontMetrics(f);
				int len = fm.stringWidth(text);
				if (len < 120) {
					super.insertString(offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});
		this.spielerName2Field.setText("Spieler 2");
		this.menupanel2.add(this.spielerName2Field);

		/* Spieler 1 */
		this.spielerFarbe1Field = new JTextField();
		this.spielerFarbe1Field.setBounds(142, 42, 50, 20);
		this.spielerFarbe1Field.setEditable(false);
		this.menupanel1.add(this.spielerFarbe1Field);
		this.spielerFarbe1Field.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 2L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				int len = (spielerFarbe1Field.getText() + str).length();
				if (len <= 6) {
					super.insertString(offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});

		/* Spieler 2 */
		this.spielerFarbe2Field = new JTextField();
		this.spielerFarbe2Field.setBounds(142, 42, 50, 20);
		this.spielerFarbe2Field.setEditable(false);
		this.menupanel2.add(this.spielerFarbe2Field);
		this.spielerFarbe2Field.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 2L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

				int len = (spielerFarbe2Field.getText() + str).length();
				if (len <= 6) {
					super.insertString(offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});

		/* Spieler 1 */
		this.spieler1FarbeOval = new Oval(new Color(0, 0, 0));
		this.spieler1FarbeOval.setBounds(10, 143, 100, 100);
		this.spieler1FarbeOval.setBackground(new Color(174, 208, 226));
		this.menupanel1.add(this.spieler1FarbeOval);

		/* Spieler 2 */
		this.spieler2FarbeOval = new Oval(new Color(0, 0, 0));
		this.spieler2FarbeOval.setBounds(10, 143, 100, 100);
		this.spieler2FarbeOval.setBackground(new Color(174, 208, 226));
		this.menupanel2.add(this.spieler2FarbeOval);

		/* SiegerPane */ // muss jetzt schon da wenn unter Chooser das aussehen verändert wird
		this.winnerPanel = new SiegerPane("Sieg für: " + this.m1.getSpielername(this.m1.SPIELER1), "", this);

		this.winnerPanel.showInput(false);
		this.winnerPanel.showCancel(false);

		this.winnerPanel.setSize(260, 120);

		this.winnerPanel.positionOk(5, 40, 115, 35);
		this.winnerPanel.fontOk(new Font("Serif", Font.BOLD, 14));

		this.winnerPanel.positionLabel((int) ((this.winnerPanel.getSize().getWidth() - 240) / 2), 5, 240, 20);
		this.winnerPanel.setLabelFont(new Font("TimesNewRoman", Font.PLAIN, 14));

		this.speicherButton = new JButton("WH Speichern");
		this.speicherButton.setFocusPainted(false);
		this.speicherButton.setBounds(130, 40, 115, 35);
		this.speicherButton.addActionListener(this.c1);
		this.winnerPanel.getContentPane().add(this.speicherButton);

		this.winnerPanel.showOk(true);

		/* Spieler 1 */
		this.spieler1Chooser = new ColorChooser();
		this.spieler1Chooser.setBounds(3, 70, 190, 80);
		this.spieler1Chooser.setBackground(new Color(174, 208, 226));
		this.spieler1Chooser.setValues(new Color(234, 92, 44));
		this.setSpieler1FarbText();
		this.spieler1Chooser.red.addChangeListener(this.c1);
		this.spieler1Chooser.green.addChangeListener(this.c1);
		this.spieler1Chooser.blue.addChangeListener(this.c1);
		this.menupanel1.add(this.spieler1Chooser);

		/* Spieler 2 */
		this.spieler2Chooser = new ColorChooser();
		this.spieler2Chooser.setBounds(3, 70, 190, 80);
		this.spieler2Chooser.setBackground(new Color(174, 208, 226));
		this.spieler2Chooser.setValues(new Color(66, 185, 244));
		this.setSpieler2FarbText();
		this.spieler2Chooser.red.addChangeListener(this.c1);
		this.spieler2Chooser.green.addChangeListener(this.c1);
		this.spieler2Chooser.blue.addChangeListener(this.c1);
		this.menupanel2.add(this.spieler2Chooser);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}

		/* Start */
		this.startButton = new JButton("Start");
		this.startButton.setActionCommand("Start");
		this.startButton.setSize(new Dimension(160, 50));
		this.startButton.setBounds((this.menupanel3.getWidth() - this.startButton.getWidth()) / 2, 25,
				this.startButton.getWidth(), this.startButton.getHeight());
		this.startButton.addActionListener(this.c1);
		this.menupanel3.add(this.startButton);

		/* Start Chooser */
		this.startChooser = new JButton("Laden");
		this.startChooser.setSize(new Dimension(160, 50));
		this.startChooser.setBounds((this.menupanel3.getWidth() - this.startChooser.getWidth()) / 2, 140,
				this.startChooser.getWidth(), this.startChooser.getHeight());
		this.startChooser.addActionListener(this.c1);
		this.menuWHpanel3.add(this.startChooser);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
		}

		/* Genutzes File */
		this.whFile = new JTextPane();
		this.whFile.setText("Geladen: ---");
		this.whFile.setSize(new Dimension(160, 20));
		this.whFile.setBounds((this.menupanel3.getWidth() - this.whFile.getWidth()) / 2, 120, this.whFile.getWidth(),
				this.whFile.getHeight());
		this.whFile.setFont(new Font("Calibri", Font.PLAIN, 14));
		this.whFile.setEditable(false);
		this.whFile.setBackground(this.menuWHpanel3.getBackground());
		this.menuWHpanel3.add(this.whFile);

		/* Positioning */
		int x = (this.dim.width - this.getSize().width) / 2;
		int y = (this.dim.height - this.getSize().height) / 5 * 2;
		this.setLocation(x, y);

		/* Buttons */
		this.button1 = new TriangleButton("Reihe 1", "1");
		this.button2 = new TriangleButton("Reihe 2", "2");
		this.button3 = new TriangleButton("Reihe 3", "3");
		this.button4 = new TriangleButton("Reihe 4", "4");
		this.button5 = new TriangleButton("Reihe 5", "5");
		this.button6 = new TriangleButton("Reihe 6", "6");
		this.button7 = new TriangleButton("Reihe 7", "7");

		this.button1.addActionListener(c1);
		this.button2.addActionListener(c1);
		this.button3.addActionListener(c1);
		this.button4.addActionListener(c1);
		this.button5.addActionListener(c1);
		this.button6.addActionListener(c1);
		this.button7.addActionListener(c1);
		
		
		/*Key Events*/
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isStartPressed) return;
				switch(e.getActionCommand()) {
					case "1":
						button1.doClick(0);
						break;
					case "2":
						button2.doClick(0);
						break;
					case "3":
						button3.doClick(0);
						break;
					case "4":
						button4.doClick(0);
						break;
					case "5":
						button5.doClick(0);
						break;
					case "6":
						button6.doClick(0);
						break;
					case "7":
						button7.doClick(0);
						break;
					default: 
						System.err.println("Error:\t" + e.getActionCommand()+" is not valid!");
				}
			}
		};

		InputMap im = p1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, false), "1");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, false), "2");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, false), "3");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0, false), "4");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0, false), "5");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0, false), "6");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, false), "7");

		ActionMap am = p1.getActionMap();
		am.put("1", action);
		am.put("2", action);
		am.put("3", action);
		am.put("4", action);
		am.put("5", action);
		am.put("6", action);
		am.put("7", action);
		
		/*Player Colors*/

		m1.setColor(m1.SPIELER1, this.spieler1Chooser.getColor());
		m1.setColor(m1.SPIELER2, this.spieler2Chooser.getColor());

		if (this.m1.s1)
			this.setButtonColor(this.m1.SPIELER1);
		if (this.m1.s2)
			this.setButtonColor(this.m1.SPIELER2);

		/* Layout */
		this.mainpanel = new JPanel();
		this.mainpanel.setBackground(new Color(254, 255, 226));
		this.mainpanel.setVisible(true);
		this.mainpanel.setLayout(new BorderLayout());
		this.p1.setBackground(new Color(254, 255, 226));
		this.p2.setBackground(new Color(254, 255, 226));
		this.setLayout(new BorderLayout());

		this.container = new Container();
		this.container.setLayout(new GridLayout(1, 9));
		this.container.add(this.button1);
		this.container.add(this.button2);
		this.container.add(this.button3);
		this.container.add(this.button4);
		this.container.add(this.button5);
		this.container.add(this.button6);
		this.container.add(this.button7);

		this.mainpanel.add(this.container, BorderLayout.NORTH);
		this.mainpanel.add(p1, BorderLayout.CENTER);

		// this.add(this.whmenu, BorderLayout.EAST);
		this.add(this.spielmenu, BorderLayout.EAST);
		this.add(this.mainpanel, BorderLayout.CENTER);
		this.aktivierSpiel();

		this.enableButtons(false);
		this.setButtonColorStart();
		this.m1.zufallsSpieler();

		this.setVisible(true);
	}

	// Methoden

	public void aktivierSpiel() {
		this.menu2.setVisible(false);
		this.menuBar.remove(this.menu2);

		this.menu3.setVisible(true);
		this.menuBar.add(this.menu3);

		this.p2.setVisible(false);
		this.mainpanel.remove(this.p2);
		this.mainpanel.add(this.p1);

		this.whmenu.setVisible(false);
		this.remove(this.whmenu);

		this.whPanel.setVisible(false);
		this.p1.setVisible(true);

		this.mainpanel.remove(container);
		this.container.setVisible(true);
		this.mainpanel.add(container, BorderLayout.NORTH);

		this.spielmenu.setVisible(false);
		this.remove(this.spielmenu);
		this.add(this.spielmenu, BorderLayout.EAST);
		this.spielmenu.setVisible(true);
	}

	public void aktivierWh() {
		this.menu3.setVisible(false);
		this.menuBar.remove(this.menu3);

		this.menuBar.add(this.menu2);
		this.menu2.setVisible(true);

		this.whPanel.setVisible(true);
		this.mainpanel.add(this.whPanel, BorderLayout.NORTH);

		this.p1.setVisible(false);
		this.mainpanel.remove(this.p1);
		this.mainpanel.add(this.p2);
		this.container.setVisible(false);

		this.spielmenu.setVisible(false);
		this.remove(this.spielmenu);

		this.p2.setVisible(true);

		this.whmenu.setVisible(false);
		this.remove(this.whmenu);
		this.add(this.whmenu, BorderLayout.EAST);
		this.whmenu.setVisible(true);
	}

	public boolean areNamesDiffrent() {
		if (spielerName1Field.getText().equals(spielerName2Field.getText()))
			return false;
		return true;
	}

	public void markNameText() {
		this.spielerName2Field.requestFocus();
		this.spielerName2Field.selectAll();
		// this.spielerName2Field.setText("");
	}

	public boolean startDrop(int i) {
		boolean b = this.p1.startDrop(i);
		return b;
	}

	public boolean startDropWh(int i) {
		boolean b = this.p2.startDrop(i);
		return b;
	}

	public boolean deleteDropWh(int i) {
		boolean b = this.p2.deleteDrop(i);
		return b;
	}

	public Color getUsedColor(int spieler) {
		if (this.isSpezFarbeCheckSelected()) {
			return this.m2.getColor(spieler);
		} else {
			return this.m2.getDefaultColor(spieler);
		}
	}

	public void setButtonColor(int spieler) {
		if (this.button1.isEnabled())
			this.button1.setBackground(this.m1.getColor(spieler));
		if (this.button2.isEnabled())
			this.button2.setBackground(this.m1.getColor(spieler));
		if (this.button3.isEnabled())
			this.button3.setBackground(this.m1.getColor(spieler));
		if (this.button4.isEnabled())
			this.button4.setBackground(this.m1.getColor(spieler));
		if (this.button5.isEnabled())
			this.button5.setBackground(this.m1.getColor(spieler));
		if (this.button6.isEnabled())
			this.button6.setBackground(this.m1.getColor(spieler));
		if (this.button7.isEnabled())
			this.button7.setBackground(this.m1.getColor(spieler));
	}

	public void setButtonColorStart() {
		this.button1.setBackground(Color.lightGray);
		this.button2.setBackground(Color.lightGray);
		this.button3.setBackground(Color.lightGray);
		this.button4.setBackground(Color.lightGray);
		this.button5.setBackground(Color.lightGray);
		this.button6.setBackground(Color.lightGray);
		this.button7.setBackground(Color.lightGray);
	}

	public void disableButton(int i) {
		switch (i) {
			case 0:
				this.button1.setEnabled(false);
				this.button1.setBackgroundColor(Color.lightGray);
				break;
			case 1:
				this.button2.setEnabled(false);
				this.button2.setBackgroundColor(Color.lightGray);
				break;
			case 2:
				this.button3.setEnabled(false);
				this.button3.setBackgroundColor(Color.lightGray);
				break;
			case 3:
				this.button4.setEnabled(false);
				this.button4.setBackgroundColor(Color.lightGray);
				break;
			case 4:
				this.button5.setEnabled(false);
				this.button5.setBackgroundColor(Color.lightGray);
				break;
			case 5:
				this.button6.setEnabled(false);
				this.button6.setBackgroundColor(Color.lightGray);
				break;
			case 6:
				this.button7.setEnabled(false);
				this.button7.setBackgroundColor(Color.lightGray);
				break;
			default:
				break;
		}
	}

	public void clickButton(int spalte) {
		switch (spalte) {
			case 0:
				this.button1.doClick();
				break;
			case 1:
				this.button2.doClick();
				break;
			case 2:
				this.button3.doClick();
				break;
			case 3:
				this.button4.doClick();
				break;
			case 4:
				this.button5.doClick();
				break;
			case 5:
				this.button6.doClick();
				break;
			case 6:
				this.button7.doClick();
				break;
			default:
				break;
		}
	}

	public void enableButtonsIfRowNotFull() {
		if (this.isStartPressed) {
			if (!this.buttonsDisabled[0])
				this.button1.setEnabled(true);
			if (!this.buttonsDisabled[1])
				this.button2.setEnabled(true);
			if (!this.buttonsDisabled[2])
				this.button3.setEnabled(true);
			if (!this.buttonsDisabled[3])
				this.button4.setEnabled(true);
			if (!this.buttonsDisabled[4])
				this.button5.setEnabled(true);
			if (!this.buttonsDisabled[5])
				this.button6.setEnabled(true);
			if (!this.buttonsDisabled[6])
				this.button7.setEnabled(true);
		}
	}

	public void enableButtons(boolean b) {
		this.button1.setEnabled(b);
		this.button2.setEnabled(b);
		this.button3.setEnabled(b);
		this.button4.setEnabled(b);
		this.button5.setEnabled(b);
		this.button6.setEnabled(b);
		this.button7.setEnabled(b);
	}

	public boolean isAnimationOn() {
		return this.animationOn;
	}

	public boolean isAnimationCheckSelected() {
		return this.animationCheck.isSelected();
	}

	public void setAnimationOn(boolean b) {
		this.animationOn = b;
	}

	public void setEnabledAnimationCheck(boolean b) {
		this.animationCheck.setEnabled(b);
	}

	public boolean isSpezFarbeCheckSelected() {
		return this.spezFarbeCheck.isSelected();
	}

	public void setSpezFarbe(boolean b) {
		if (b) {
			this.spieler1WhOval.setColor(m2.getColor(m2.SPIELER1));
			this.spieler2WhOval.setColor(m2.getColor(m2.SPIELER2));
		} else {
			this.spieler1WhOval.setColor(m2.getDefaultColor(m2.SPIELER1));
			this.spieler2WhOval.setColor(m2.getDefaultColor(m2.SPIELER2));
		}
	}

	public boolean isKiSpielerCheckSelected() {
		return this.kiGameCheck.isSelected();
	}

	public void machKiSpiel(boolean b) {
		if (b) {
			this.spielerName2Field.setEnabled(false);
			this.spielerName2Field.setText("Bot");
			this.spacing = 96;
			this.menuBarPanel2.setPreferredSize(new Dimension(220, 130 + this.spacing));
			this.menuBarPanel2.setSize(new Dimension(220, 130 + this.spacing));

			this.difficultysPanel.setPreferredSize(new Dimension(200, this.spacing));
			this.difficultysPanel.setBounds(10, 50, 200, this.spacing);
			this.menu3.setPopupMenuVisible(false);
			this.menu3.setPopupMenuVisible(true);
		} else {
			this.spielerName2Field.setEnabled(true);
			this.spielerName2Field.setText("Spieler 2");
			this.spacing = 0;
			this.menuBarPanel2.setPreferredSize(new Dimension(220, 130 + this.spacing));
			this.menuBarPanel2.setSize(new Dimension(220, 130 + this.spacing));
			
			this.difficultysPanel.setPreferredSize(new Dimension(200, this.spacing));
			this.difficultysPanel.setBounds(10, 50, 200, this.spacing);
			this.menu3.setPopupMenuVisible(false);
			this.menu3.setPopupMenuVisible(true);
		}
	}

	public boolean isAutorunOn() {
		return autorunOn;
	}

	public void setAutorun(boolean b) {
		this.autorunOn = b;
	}

	public int getSliderValue() {
		return this.slider.getValue();
	}

	public void Sieg(int spieler) {
		Difficulty help = this.m1.ki.getDifficulty();
		this.menu3.setEnabled(true);
		this.isStartPressed = false;
		String text = "Unentschieden";
		if (spieler != -1) {
			text = "Sieg für: " + this.getSpielerName(spieler);
		}
		this.winnerPanel.setLabelText(text);
		this.winnerPanel.start();
		this.m1.restart();

		this.enableButtons(false);
		this.setButtonColorStart();

		this.spielerName1Field.setEnabled(true);
		if (this.m1.kiActive == false)
			this.spielerName2Field.setEnabled(true);
		this.spieler1Chooser.setChooserEnabled(true);
		this.spieler2Chooser.setChooserEnabled(true);
		this.startButton.setEnabled(true);

		this.m1.board = new Board();
		this.m1.ki = new KI(this.m1.board);
		this.m1.ki.setDifficulty(help);

		this.repaint();
		this.p1.repaint();
	}

	public void startGame() {
		this.menu3.setEnabled(false);
		this.buttonsDisabled = new boolean[7];
		this.enableButtons(true);
		this.spielerName1Field.setEnabled(false);
		this.spielerName2Field.setEnabled(false);
		this.spieler1Chooser.setChooserEnabled(false);
		this.spieler2Chooser.setChooserEnabled(false);
		m1.setColor(m1.SPIELER1, this.spieler1Chooser.getColor());
		m1.setColor(m1.SPIELER2, this.spieler2Chooser.getColor());

		this.m1.setSpielerName(this.spielerName1Field.getText(), this.m1.SPIELER1);
		this.m1.setSpielerName(this.spielerName2Field.getText(), this.m1.SPIELER2);

		if (this.m1.s1)
			this.setButtonColor(this.m1.SPIELER1);
		if (this.m1.s2)
			this.setButtonColor(this.m1.SPIELER2);

		this.startButton.setEnabled(false);
		this.m1.zufallsSpieler();
		this.repaint();
	}

	public String getSpielerName(int spieler) {
		if (this.m1.SPIELER1 == spieler) {
			return this.spielerName1Field.getText();
		} else if (this.m1.SPIELER2 == spieler) {
			return this.spielerName2Field.getText();
		}
		return null;
	}

	public String getSpielerFarbe(int spieler) {
		if (this.m1.SPIELER1 == spieler) {
			return this.spielerFarbe1Field.getText();
		} else if (this.m1.SPIELER2 == spieler) {
			return this.spielerFarbe1Field.getText();
		}
		return null;
	}

	public void setSpieler1FarbText() {
		String farbcode = String.format("%02x%02x%02x", this.spieler1Chooser.red.getValue(),
				this.spieler1Chooser.green.getValue(), this.spieler1Chooser.blue.getValue());
		this.spielerFarbe1Field.setText(farbcode);
		this.spieler1FarbeOval.setColor(Color.decode("#" + farbcode));

	}

	public void setSpieler2FarbText() {
		String farbcode = String.format("%02x%02x%02x", this.spieler2Chooser.red.getValue(),
				this.spieler2Chooser.green.getValue(), this.spieler2Chooser.blue.getValue());
		this.spielerFarbe2Field.setText(farbcode);
		this.spieler2FarbeOval.setColor(Color.decode("#" + farbcode));

	}

	public void enableButtons(boolean b, int i) {
		if (i == VierGView.PLAY) {
			this.playButton.setEnabled(b);
		} else if (i == VierGView.PAUSE) {
			this.pauseButton.setEnabled(b);
		} else if (i == VierGView.VOR) {
			this.forwardButton.setEnabled(b);
		} else if (i == VierGView.ZURÜCK) {
			this.backwardsButton.setEnabled(b);
		} else if (i == VierGView.SPEICHERN) {
			this.speicherButton.setEnabled(b);
		}
	}

	public boolean isEnabledButton(Object source) {
		if (source == this.playButton) {
			if (this.playButton.isEnabled())
				return true;
		} else if (source == this.pauseButton) {
			if (this.pauseButton.isEnabled())
				return true;
		} else if (source == this.forwardButton) {
			if (this.forwardButton.isEnabled())
				return true;
		} else if (source == this.backwardsButton) {
			if (this.backwardsButton.isEnabled())
				return true;
		}
		return false;
	}

	public void setBorder(Border border, Object source) {
		int i = -1;
		try {
			i = Integer.parseInt(source.toString());
		} catch (Exception exception) {
		}

		if (source == this.playButton || i == VierGView.PLAY) {
			this.playButton.setBorder(border);
		} else if (source == this.pauseButton || i == VierGView.PAUSE) {
			this.pauseButton.setBorder(border);
		} else if (source == this.forwardButton || i == VierGView.VOR) {
			this.forwardButton.setBorder(border);
		} else if (source == this.backwardsButton || i == VierGView.ZURÜCK) {
			this.backwardsButton.setBorder(border);
		}
	}

	public void setButtonColor(Color color, Object source) {
		if (source == this.playButton) {
			this.playButton.setBackground(color);
		} else if (source == this.pauseButton) {
			this.pauseButton.setBackground(color);
		} else if (source == this.forwardButton) {
			this.forwardButton.setBackground(color);
		} else if (source == this.backwardsButton) {
			this.backwardsButton.setBackground(color);
		}
	}

	public void setWhLabelText(String gewinnerText) {
		this.gewinnerLabel.setText(gewinnerText);
	}

	public void setFileNameText(String fileName) {
		this.whFile.setText(fileName);
	}

	public void kiZug() {
		this.c1.kiSpielerDran = true;
	}

	/* Button-Source Prüfungen */

	public boolean isButtonStart(Object source) {
		if (this.startButton == source)
			return true;
		return false;
	}

	public boolean isButtonChooser(Object source) {
		if (this.startChooser == source)
			return true;
		return false;
	}

	public boolean isButtonSpeichern(Object source) {
		if (this.speicherButton == source)
			return true;
		return false;
	}

	public boolean isButtonPlay(Object source) {
		if (this.playButton == source)
			return true;
		return false;
	}

	public boolean isButtonPause(Object source) {
		if (this.pauseButton == source)
			return true;
		return false;
	}

	public boolean isButtonVor(Object source) {
		if (this.forwardButton == source)
			return true;
		return false;
	}

	public boolean isButtonZurück(Object source) {
		if (this.backwardsButton == source)
			return true;
		return false;
	}

	public boolean isMenuSpielen(Object source) {
		if (this.item1 == source)
			return true;
		return false;
	}

	public boolean isMenuWiederholung(Object source) {
		if (this.item2 == source)
			return true;
		return false;
	}

	public boolean isAnimationCheck(Object source) {
		if (this.animationCheck == source)
			return true;
		return false;
	}

	public boolean isSpezFarbeCheck(Object source) {
		if (this.spezFarbeCheck == source)
			return true;
		return false;
	}

	public boolean isKiSpielCheck(Object source) {
		if (this.kiGameCheck == source)
			return true;
		return false;
	}

	public boolean isChooser1Red(Object source) {
		if (this.spieler1Chooser.red == source)
			return true;
		return false;
	}

	public boolean isChooser1Green(Object source) {
		if (this.spieler1Chooser.green == source)
			return true;
		return false;
	}

	public boolean isChooser1Blue(Object source) {
		if (this.spieler1Chooser.blue == source)
			return true;
		return false;
	}

	public boolean isChooser2Red(Object source) {
		if (this.spieler2Chooser.red == source)
			return true;
		return false;
	}

	public boolean isChooser2Green(Object source) {
		if (this.spieler2Chooser.green == source)
			return true;
		return false;
	}

	public boolean isChooser2Blue(Object source) {
		if (this.spieler2Chooser.blue == source)
			return true;
		return false;
	}

	public boolean isSlider(Object source) {
		if (this.slider == source)
			return true;
		return false;
	}

	public boolean isLeichtR(Object source) {
		if (this.easyRadio == source)
			return true;
		return false;
	}

	public boolean isNormalR(Object source) {
		if (this.normalRadio == source)
			return true;
		return false;
	}

	public boolean isSchwerR(Object source) {
		if (this.hardRadio == source)
			return true;
		return false;
	}

	public boolean isExtremR(Object source) {
		if (this.extremeRadio == source)
			return true;
		return false;
	}

	public void repaintAll() {
		this.p1.repaint();
		this.p2.repaint();
	}

	public void repaintSlider() {
		this.slider.repaint();
	}

}
