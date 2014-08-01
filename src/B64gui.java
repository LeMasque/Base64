package random;

import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class B64gui extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public ArrayList<Character> b64Table;
	private Encode encode;
	private Decode decode;
	private JButton jbnEncode, jbnDecode, jbnOpen;
	private JFileChooser jfcOpen;
	private JMenu jmnFile, jmnTools, jmnHelp;
	private JMenuItem jmiExit, jmiAbout;
	private JCheckBox jcbAlwaysOnTop, jcbWordWrap;
	private JPanel jplMaster;
	private JTextArea jtaInput, jtaOutput;

	public B64gui() {
		ArrayList<Character> b64Table = new ArrayList<Character>();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String misc = "0123456789+/";
		char[] upper = alphabet.toCharArray();
		char[] lower = alphabet.toLowerCase().toCharArray();
		char[] etc = misc.toCharArray();
		for (int i = 0; i < 64; i++) {
			if (i < 26) {
				b64Table.add(upper[i]);
			} else if (i > 25 && i < 52) {
				b64Table.add(lower[i - 26]);
			} else {
				b64Table.add(etc[i - 52]);
			}
		}
		this.b64Table = b64Table;
		encode = new Encode(b64Table);
		decode = new Decode(b64Table);
		initGui();
	}
	
	private void initGui() {
		
		/********************** Menu bar things **********************/
		jmnFile = new JMenu("File");
		jmnFile.setMnemonic(KeyEvent.VK_F);
		
		jmnTools = new JMenu("Tools");
		jmnTools.setMnemonic(KeyEvent.VK_T);
		
		jmnHelp = new JMenu("Help");
		jmnHelp.setMnemonic(KeyEvent.VK_H);
		
		jmiExit = new JMenuItem("Exit");
		jmiExit.addActionListener(this);
		jmnFile.add(jmiExit);

		jcbAlwaysOnTop = new JCheckBox("Always on top");
		jcbAlwaysOnTop.addActionListener(this);
		jcbWordWrap = new JCheckBox("Word Wrap");
		jcbWordWrap.addActionListener(this);
		jmnTools.add(jcbAlwaysOnTop);
		jmnTools.add(jcbWordWrap);
		
		jmiAbout = new JMenuItem("About");
		jmiAbout.addActionListener(this);
		jmnHelp.add(jmiAbout);
		
		JMenuBar menu = new JMenuBar();
		menu.add(jmnFile);
		menu.add(jmnTools);
		menu.add(jmnHelp);
		this.setJMenuBar(menu);
		
		/*********************** Make the components ************************/

		jbnEncode = new JButton("Encode");
		jbnEncode.setToolTipText("Encodes the given text into Base64");
		jbnEncode.setMnemonic(KeyEvent.VK_E);
		jbnEncode.addActionListener(this);
		
		jbnDecode = new JButton("Decode");
		jbnDecode.setToolTipText("Decodes the given text from Base64");
		jbnDecode.setMnemonic(KeyEvent.VK_D);
		jbnDecode.addActionListener(this);
		
		jbnOpen = new JButton("Open File...");
		jbnOpen.setToolTipText("Pastes in all text from the specified file into the input field.");
		jbnOpen.setMnemonic(KeyEvent.VK_O);
		jbnOpen.addActionListener(this);
		
		jfcOpen = new JFileChooser("File Chooser");
		jfcOpen.setDialogTitle("Open...");
		jfcOpen.setDialogType(JFileChooser.OPEN_DIALOG);
		jfcOpen.setMultiSelectionEnabled(false);
		jfcOpen.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfcOpen.addActionListener(this);
		
		JLabel input = new JLabel("Input");
		
		jtaInput = new JTextArea("Input", 5, 6);
		jtaInput.setEditable(true);
		jtaInput.setLineWrap(true);
		jtaInput.setOpaque(true);
		jtaInput.setSelectionEnd(jtaInput.getText().length());
		JScrollPane iScroll = new JScrollPane(jtaInput);
		iScroll.setAutoscrolls(true);
		iScroll.setOpaque(true);
		iScroll.setEnabled(true);
		iScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JLabel output = new JLabel("Output");
		
		jtaOutput = new JTextArea("Output", 5, 6);
		jtaOutput.setEditable(false);
		jtaOutput.setLineWrap(true);
		jtaOutput.setOpaque(true);
		JScrollPane oScroll = new JScrollPane(jtaOutput);
		oScroll.setAutoscrolls(true);
		oScroll.setOpaque(true);
		oScroll.setEnabled(true);
		oScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		/************************ Place Components in Panel *************************/
		
		jplMaster = new JPanel();
		jplMaster.setLayout(new GridBagLayout());

		GridBagConstraints iLabel = new GridBagConstraints();
		iLabel.gridx = 0;
		iLabel.gridy = 2;
		iLabel.insets = new Insets(5,5,5,5);
		jplMaster.add(input, iLabel);
		
		GridBagConstraints iArea = new GridBagConstraints();
		iArea.gridx = 1;
		iArea.gridy = 0;
		iArea.insets = new Insets(5,5,5,5);
		iArea.gridwidth = 5;
		iArea.gridheight = 5;
		iArea.fill = GridBagConstraints.BOTH;
		jplMaster.add(iScroll, iArea); //jtaInput
		
		GridBagConstraints eButton = new GridBagConstraints();
		eButton.gridx = 0;
		eButton.gridy = 6;
		eButton.insets = new Insets(5,5,5,5);
		eButton.gridwidth = 2; 
		jplMaster.add(jbnEncode, eButton);
		
		GridBagConstraints dButton = new GridBagConstraints();
		dButton.gridx = 2;
		dButton.gridy = 6;
		dButton.insets = new Insets(5,5,5,5);
		dButton.gridwidth = 2;
		jplMaster.add(jbnDecode, dButton);
		
		GridBagConstraints oButton = new GridBagConstraints();
		oButton.gridx = 4;
		oButton.gridy = 6;
		oButton.insets = new Insets(5,5,5,5);
		oButton.gridwidth = 2;
		jplMaster.add(jbnOpen, oButton);
		
		GridBagConstraints oLabel = new GridBagConstraints();
		oLabel.gridx = 0;
		oLabel.gridy = 8;
		oLabel.insets = new Insets(5,5,5,5);
		jplMaster.add(output, oLabel);
		
		GridBagConstraints oArea = new GridBagConstraints();
		oArea.gridx = 1;
		oArea.gridy = 7;
		oArea.insets = new Insets(5,5,5,5);
		oArea.gridheight = 5;
		oArea.gridwidth = 5;
		oArea.fill = GridBagConstraints.BOTH;
		jplMaster.add(oScroll, oArea); //jtaOutput
		
		this.getContentPane().add(jplMaster);
		
		
		/********************************* The Outside Frame **********************************/
		
		this.setTitle("Base64 Encoder/Decoder");
		this.setResizable(false);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);  
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbnEncode) {
			if (jtaInput.getText().equals("") || jtaInput.getText().equals(null)) {
				jtaOutput.setText("<There is nothing to encode>");
			} else {
				jtaOutput.setText(encode.generateCode(jtaInput.getText()));
			}
		} else if (e.getSource() == jbnDecode) {
			jtaOutput.setText(decode.decode(jtaInput.getText()));
		} else if (e.getSource() == jbnOpen) {
			jfcOpen.showOpenDialog(getContentPane()); 
		} else if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
			//File chooser's open button
			try {
				Scanner fileLines = new Scanner(jfcOpen.getSelectedFile());
				String line = "";
				while (fileLines.hasNextLine()) {
					line += fileLines.nextLine();
				}
				jtaInput.setText(line);
				fileLines.close();
			} catch (FileNotFoundException e1) {
				jtaOutput.setText("<File not found>");
			}
		} else if (JFileChooser.CANCEL_SELECTION.equals(e.getActionCommand())) {
			//File chooser's close button
			jtaOutput.setText("<No File was chosen>");
		} else if (e.getSource() == jmiExit) {
			System.exit(0);
		} else if (e.getSource() == jcbAlwaysOnTop) {
			if (this.isAlwaysOnTop()) {
				this.setAlwaysOnTop(false);
			} else {
				this.setAlwaysOnTop(true);
			}
		} else if (e.getSource() == jcbWordWrap) {
			if (jtaInput.getWrapStyleWord()) {
				jtaInput.setWrapStyleWord(false);
			} else {
				jtaInput.setWrapStyleWord(true);
			}
		} else if (e.getSource() == jmiAbout) {
			aboutSection();
		}
	}
	
	private void aboutSection() {
		JFrame about = new JFrame("About");
		JPanel aboutSection = new JPanel();
		JPanel filler = new JPanel();
		JPanel filler2 = new JPanel();
		JPanel filler3 = new JPanel();
		JLabel title = new JLabel("About Base64 Encoder/Decoder:");
		JTextArea explanation = new JTextArea();
		aboutSection.setLayout(new GridBagLayout());
		GridBagConstraints cons1 = new GridBagConstraints();
		GridBagConstraints cons2 = new GridBagConstraints();
		GridBagConstraints cons3 = new GridBagConstraints();
		GridBagConstraints cons4 = new GridBagConstraints();
		GridBagConstraints cons5 = new GridBagConstraints();
		cons1.gridx = 1;
		cons1.gridy = 0;
		cons1.gridheight = 5;
		cons1.gridwidth = 10;
		cons1.insets = new Insets(3, 5, 0, 5);
		aboutSection.add(title, cons1);
		cons2.gridx = 1;
		cons2.gridy = 10;
		cons2.gridwidth = 10;
		cons2.gridheight = 6;
		cons2.insets = new Insets(0, 5, 3, 5);
		aboutSection.add(explanation, cons2);
		cons3.gridx = 1;
		cons3.gridy = 5;
		cons3.gridwidth = 10;
		cons3.gridheight = 5;
		aboutSection.add(filler, cons3);
		cons4.gridx = 0;
		cons4.gridy = 0;
		cons4.gridwidth = 1;
		cons4.fill = GridBagConstraints.VERTICAL;
		aboutSection.add(filler2, cons4);
		cons5.gridx = 11;
		cons5.gridy = 0;
		cons5.gridwidth = 1;
		cons5.fill = GridBagConstraints.VERTICAL;
		aboutSection.add(filler3, cons5);
		explanation.append("I made this little program to encode ASCII text to Base64,\nor to");
		explanation.append(" decode Base64 back into ASCII, human-readable text.\n");
		explanation.append("This program was a project of mine to practice some \nprogramming");
		explanation.append(" skills, but also to learn how to use Java Swing\nand make a GUI.");
		explanation.append(" I learned quite a bit from this project. \nI hope you enjoy it, ");
		explanation.append("even if it is more of a novelty than a\npractical application. \n");
		explanation.append("\n\t Authored by: Spencer Walden (July 30, 2014)");
		about.getContentPane().add(aboutSection);
		about.setLocationRelativeTo(this);
		about.pack();
		about.setAlwaysOnTop(true);
		about.setVisible(true);
	}
}
