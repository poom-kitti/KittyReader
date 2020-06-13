import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class KittyReader {
	private byte[] buffer;
	private int currentIndex;
	private int totalChar;
	private InputStream input;
	private final static int BUFFER_DEFAULT_SIZE = 1024;
	private int listChar;
	
	public KittyReader() {
		this(BUFFER_DEFAULT_SIZE);
	}
	
	public KittyReader(int bufSize) {
		input = System.in;
		buffer = new byte[bufSize];
		currentIndex = 0;
		totalChar = 0;
	}
	
	public void readInput() throws Exception {
		if (totalChar<0) throw new Exception();
		currentIndex = 0;
		totalChar = input.read(buffer);		
	}
	
	private int nextByte() {
		if (currentIndex >= totalChar) return 0;
		return buffer[currentIndex++];
	}
	
	private boolean isIgnoreChar(int n) {
		if (n=='\r' || n==' ' || n==',' || n=='\n' || n=='\t' || n==-1) return true;
		return false;
	}
	
	public boolean hasInput() {
		return totalChar!=0 && currentIndex < totalChar;
	}
	
	public int nextInt() {
		if (!hasInput()) throw new IllegalStateException();
		int returnInt=0;
		int charac = nextByte();
		int sign = 1;
		while (isIgnoreChar(charac)) charac = nextByte();
		if (charac == 0) throw new IndexOutOfBoundsException();
		if (charac == '-') {
			sign = -1;
			charac = nextByte();
		}
		while (!isIgnoreChar(charac) && charac!=0) {
			if (charac >= '0' && charac <= '9') {
				returnInt *= 10;
				returnInt += charac - '0';
				charac = nextByte();
			}
			else throw new InputMismatchException();
		}
		return sign * returnInt;
	}
	
	public float nextFloat() {
		return nextFloat(false, nextByte());
	}
	
	private float nextFloat(boolean inList, int currentChar) {
		if (!hasInput()) throw new IllegalStateException();
		float returnFloat=0;
		int charac = currentChar;
		int sign = 1;
		while (isIgnoreChar(charac)) charac = nextByte();
		if (charac == 0) throw new IndexOutOfBoundsException();
		if (charac == '-') {
			sign = -1;
			charac = nextByte();
		}
		while (!isIgnoreChar(charac) && charac!=0 && charac!='.') {
			if (charac >= '0' && charac <= '9') {
				returnFloat *= 10;
				returnFloat += charac - '0';
				charac = nextByte();
			}
			else if (charac == ']' && inList) break;
			else throw new InputMismatchException();
		}
		if (charac=='.') {
			float decimalPlace = 1;
			charac = nextByte();
			while (!isIgnoreChar(charac) && charac!=0) {
				if (charac >= '0' && charac <= '9') {
					decimalPlace /= 10;
					returnFloat += (charac - '0') * decimalPlace;
					charac = nextByte();
				}
				else if (charac == ']' && inList) break;
				else throw new InputMismatchException();
			}
		}
		if (inList) listChar = charac;
		return sign * returnFloat;
	}
	
	public String nextWord() {
		if (!hasInput()) throw new IllegalStateException();
		StringBuilder returnStr = new StringBuilder();
		int charac = nextByte();
		while (isIgnoreChar(charac)) charac = nextByte();
		if (charac == 0) throw new IndexOutOfBoundsException();
		while (!isIgnoreChar(charac) && charac!=0) {
			returnStr.append((char) charac);
			charac = nextByte();
			
		}
		return returnStr.toString();
	}
	
	public Object[] nextList() {
		if (!hasInput()) throw new IllegalStateException();
		ArrayList<Object> returnList = new ArrayList<Object>();
		listChar = nextByte();
		while (isIgnoreChar(listChar)) listChar = nextByte();
		if (listChar == 0) throw new IndexOutOfBoundsException();
		if (listChar != '[') throw new InputMismatchException();
		listChar = nextByte();
		while (listChar != 0 && listChar != ']') {
			while (isIgnoreChar(listChar)) listChar = nextByte();
			if (listChar == '"') {
				StringBuilder word = new StringBuilder();
				listChar = nextByte();
				while (listChar != 0 && listChar != '"') {
					word.append((char) listChar);
					listChar = nextByte();
				}
				listChar = nextByte();
				returnList.add(word.toString());
			}
			else returnList.add(nextFloat(true, listChar));
		}
		if (listChar == 0) throw new InputMismatchException();
		return returnList.toArray();
	}
	
	public String restOfInput() {
		if (!hasInput()) throw new IllegalStateException();
		StringBuilder returnStr = new StringBuilder();
		int charac = nextByte();
		while (isIgnoreChar(charac)) charac = nextByte();
		if (charac == 0) throw new IndexOutOfBoundsException();
		while (currentIndex < totalChar) {
			returnStr.append((char) charac);
			charac = nextByte();
		}
		return returnStr.toString();
	}
	
	public void clearInput() {
		totalChar = 0;
	}
	
	public void close() throws IOException {
		input.close();
	}
}
