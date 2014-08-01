package random;

import java.util.*;
import javax.swing.*;
import java.io.*;

public class B64UI {
	
	public static void main(String[] args) throws FileNotFoundException {
		//run the GUI
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	B64gui gooey = new B64gui();
            	gooey.pack();
                gooey.setVisible(true);
            }
        });
		
		//Console stuff:
		Scanner token = new Scanner(System.in);
		Scanner line = new Scanner(System.in);
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String misc = "0123456789+/";
		char[] upper = alphabet.toCharArray();
		char[] lower = alphabet.toLowerCase().toCharArray();
		char[] etc = misc.toCharArray();
		ArrayList<Character> b64Table = new ArrayList<Character>();
		for (int i = 0; i < 64; i++) {
			if (i < 26) {
				b64Table.add(upper[i]);
			} else if (i > 25 && i < 52) {
				b64Table.add(lower[i - 26]);
			} else {
				b64Table.add(etc[i - 52]);
			}
		}
		Encode encoder = new Encode(b64Table);
		Decode decoder = new Decode(b64Table);
		boolean done = false;
		String confirm;
		String message;
		System.out.println("I will encode or decode whatever string you give me"
				+ " into or from Base64");
		while (!done) {
			System.out.println("do you have a file you wish to give me? "
					+ "(type: \"y/n\" OR <anything> to quit)");
			confirm = token.next().toLowerCase();
			message = "";
			if (confirm.startsWith("y")) {
				System.out.print("what's your file called? ");
				String file_name = line.nextLine();
				Scanner file = new Scanner(new File(file_name));
				while (file.hasNextLine()) {
					message += file.nextLine();
				}
				file.close();
			} else if (confirm.startsWith("n")) {
				System.out.println("what would you like me to work with then? (type below)");
				message = line.nextLine();
			} else {
				done = true;
				System.exit(0);
			}
			if (!done) {
				System.out.println("cool, now, would you like that encoded or decoded? (e/d)");
				String what_do = token.next();
				while (!what_do.startsWith("e") && !what_do.startsWith("d")) {
					System.out.println("please type \'e\' or \'d\' for either encoding or decoding.");
					what_do = token.next();
				}
				if (what_do.startsWith("e")) {
					String encoded = encoder.generateCode(message);
					System.out.println("encoded message: " + encoded);
				} else if (what_do.startsWith("d")) {
					String decoded = decoder.decode(message);
					System.out.println("decoded message: " + decoded);
				}
				System.out.println();
			}
		}
		token.close();
		line.close();
	}
}
