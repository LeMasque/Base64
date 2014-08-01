package random;

import java.util.ArrayList;

public class Encode {
	
	public ArrayList<Character> base64Table;
	
	//initializes the encoder. requires what you'd like to encode in string format
	//requires the base64 Table.
	public Encode(ArrayList<Character> base64Table) {
		this.base64Table = base64Table;
	}
	
	//encodes the string you created this object with.
	public String generateCode(String message) { 
		byte[] bytes = message.getBytes();
		boolean padding = false;
		boolean extraPadding = false;
		if (bytes.length % 3 == 2) {
			padding = true;
		} else if (bytes.length % 3 == 1) {
			padding = true;
			extraPadding = true;
		}
		String[] binMsg = toBinary(bytes, padding, extraPadding);
		byte[] base = breakDown(binMsg, padding, extraPadding);
		String encoded = "";
		for (byte b : base) {
			if (b == 64) {
				encoded += "=";
			} else {
				encoded += base64Table.get(b);
			}
		}
		return encoded;
	}
	
	//turns a byte array into a string array made up of binary representations of every
	//3 bytes in the byte array. 
	private String[] toBinary(byte[] bytes, boolean padding, boolean extraPadding) {
		String[] bin = new String[bytes.length / 3 + (padding ? 1 : 0)];
		String addition = "";
		for (int count = 0; count < bytes.length; count++) {
			byte n = bytes[count];
			for (int i = 128; i > 1; i /= 2) {
				addition += n / i;
				n %= i;
			}
			addition += n % 2;
			if (count % 3 == 2) {
				bin[count / 3] = addition;
				addition = "";
			} 
		}
		if (padding) {
			addition += "00000000";
			if (extraPadding) {
				addition += "00000000";
			} 
			bin[bin.length - 1] = addition;
		}
		return bin;
	}	
	
	//takes a String array of binary numbers and then breaks those down into four new
	//char values and returns them in the form of a byte array.
	private byte[] breakDown(String[] bin, boolean padding, boolean extraPadding) {
		byte[] result = new byte[bin.length * 4];
		int count = 0;
		for (String s : bin) {
			for (int i = 0; i <= 18; i += 6) {
				String herp = s.substring(i, i + 6);
				result[count] = Byte.valueOf(herp, 2);
				if (extraPadding && count >= result.length - 2) { 
					result[count] = 64;
				} else if (padding && count >= result.length - 1) { 
					result[count] = 64;
				}
				count++;
			}
		}
		return result;
	}
}
	
