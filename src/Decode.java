package random;

import java.util.ArrayList;

public class Decode {

	private ArrayList<Character> base64Table;
	
	//Constructs and initializes a new Decode object with the base64 equivalence table
	public Decode(ArrayList<Character> base64Table) {
		this.base64Table = base64Table;
	}
	
	//decodes the given String from base64 into ASCII text.
	public String decode(String encoded) {
		String decoded = "";
		byte[] bytes = resolveAgainstTable(encoded);
		String[] strings = toBinary(bytes);
		ArrayList<Character> chars = interpretBinary(strings);
		for (char c : chars) {
			decoded += c;
		}
		return decoded;
	}
	
	//returns the byte values of the characters in the encoded message.
	private byte[] resolveAgainstTable(String encoded) {
		byte[] result = new byte[encoded.length()];
		for (int i = 0; i < encoded.length(); i++) {
			if (encoded.charAt(i) == '=') {
				result[i] = 0;
			} else {
				result[i] = (byte)base64Table.indexOf(encoded.charAt(i));
			}
		}
		return result;
	}
	
	//returns a String array containing the binary representation of every four bytes in the
	//specified byte array combined.
	private String[] toBinary(byte[] bytes) {
		String[] result = new String[bytes.length / 4];
		String addition = "";
		for (int count = 0; count < bytes.length; count++) {
			byte n = bytes[count];
			//i starts at 32, to ignore the two largest bits of the binary string,
			//it will be always two 0's due to the nature of it.
			for (int i = 32; i > 1; i /= 2) {
				addition += n / i;
				n %= i;
			}
			addition += n % 2;
			if (count % 4 == 3) {
				result[count / 4] = addition;
				addition = "";
			}
		}
		return result;
	}
	
	//takes a String[] of binary representations of 3 characters, returns an 
	//ArrayList<Character> with all of the characters represented by the strings.
	private ArrayList<Character> interpretBinary(String[] strings) {
		ArrayList<Character> letters = new ArrayList<Character>();
		for (int i = 0; i < strings.length * 3; i++) {
			int n = 0;
			String letter = strings[i / 3].substring(8 * (i % 3), 8 * (i % 3) + 8);
			for (int j = 0; j < letter.length(); j++) {
				if (letter.charAt(letter.length() - 1 - j) == '1') {
					if (j == 0) {
						n += 1;
					} else {
						n += (int)Math.pow(2, j);
					}
				}
			}
			letters.add((char)n);
		}
		return letters;
	}
}
