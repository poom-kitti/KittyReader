import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class KittyReader {
	private byte[] buffer; //Keep the ASCII characters from input
	private int currentIndex; //Current position in buffer array
	private int totalChar; //Total ASCII characters user has input
	private InputStream input; //Will set as System.in
	private final static int BUFFER_DEFAULT_SIZE = 1024;
	private int listChar; //Special variable to keep ASCII character if reading a list
	
	public KittyReader() {
		this(BUFFER_DEFAULT_SIZE);
	}
	
	//Constructor to set buffer size
	public KittyReader(int bufSize) {
		input = System.in;
		buffer = new byte[bufSize];
		currentIndex = 0;
		totalChar = 0;
	}
	
	//Read user input
	public void readInput() throws Exception {
		if (totalChar<0) throw new Exception();
		currentIndex = 0;
		totalChar = input.read(buffer);		
	}
	
	//Return the next ASCII character from buffer array
	private int nextByte() {
		if (currentIndex >= totalChar) return 0;
		return buffer[currentIndex++];
	}
	
	//Characters we want to ignore
	private boolean isIgnoreChar(int n) {
		if (n=='\r' || n==' ' || n==',' || n=='\n' || n=='\t' || n==-1) return true;
		return false;
	}
	
	//Determine whether we have finish reading the user input
	public boolean hasInput() {
		return totalChar!=0 && currentIndex < totalChar;
	}
	
	//Return the next integer
	//Integer cannot be followed directly by a special character
	//ex. 123! is not allowed
	public int nextInt() {
		if (!hasInput()) throw new IllegalStateException();
		int returnInt=0;
		int charac = nextByte();
		int sign = 1; 
		while (isIgnoreChar(charac)) charac = nextByte();
		if (charac == 0) throw new IndexOutOfBoundsException();
		
		//Deal with negative integer
		if (charac == '-') {
			sign = -1;
			charac = nextByte();
		}
		
		//Read all numbers
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
	
	//Return the next floating number
	//Floating number cannot have special character in-between or directly after it
	//ex. 123.456! / 12!3.456 / 123.!456 are not allowed
	public float nextFloat() {
		return nextFloat(false, nextByte());
	}
	
	//Return the next floating number
	//If it is called from method nextList() will also update the variable listChar
	private float nextFloat(boolean inList, int currentChar) {
		if (!hasInput()) throw new IllegalStateException();
		float returnFloat=0;
		int charac = currentChar;
		int sign = 1; 
		while (isIgnoreChar(charac)) charac = nextByte(); 
		if (charac == 0) throw new IndexOutOfBoundsException();
		
		//Deal with negative float number
		if (charac == '-') {
			sign = -1;
			charac = nextByte();
		}
		
		//Deal with the whole part
		while (!isIgnoreChar(charac) && charac!=0 && charac!='.') {
			if (charac >= '0' && charac <= '9') {
				returnFloat *= 10;
				returnFloat += charac - '0';
				charac = nextByte();
			}
			else if (charac == ']' && inList) break; //Deal when called from nextList()
			else throw new InputMismatchException();
		}
		
		//Deal with decimals
		if (charac=='.') {
			float decimalPlace = 1;
			charac = nextByte();
			while (!isIgnoreChar(charac) && charac!=0) {
				if (charac >= '0' && charac <= '9') {
					decimalPlace /= 10;
					returnFloat += (charac - '0') * decimalPlace;
					charac = nextByte();
				}
				else if (charac == ']' && inList) break; //Deal when called from nextList()
				else throw new InputMismatchException();
			}
		}
		if (inList) listChar = charac; //Update variable listChar if called from nextList()
		return sign * returnFloat;
	}
	
	//Return the next string word that may contain special characters,
	//except for ignore characters (see isIgnoreChar)
	//ex. "hello" / "[world]" are allowed, but "me," will return just "me"
	public String nextWord() {
		if (!hasInput()) throw new IllegalStateException();
		StringBuilder returnStr = new StringBuilder();
		int charac = nextByte();
		while (isIgnoreChar(charac)) charac = nextByte(); 
		if (charac == 0) throw new IndexOutOfBoundsException();
		
		//Get all characters until find ignore character
		while (!isIgnoreChar(charac) && charac!=0) {
			returnStr.append((char) charac);
			charac = nextByte();
			
		}
		return returnStr.toString();
	}
	
	//Return an array of objects
	//String elements should begin and end with ' " ' (ex. "cool, you are!")
	//Example user input to read --> [123, "hello world", -3.14]
	//To use the object element, parse the type correctly
	public Object[] nextList() {
		if (!hasInput()) throw new IllegalStateException();
		ArrayList<Object> returnList = new ArrayList<Object>();
		listChar = nextByte();
		while (isIgnoreChar(listChar)) listChar = nextByte();
		if (listChar == 0) throw new IndexOutOfBoundsException();
		if (listChar != '[') throw new InputMismatchException(); //Only run if first found "["
		listChar = nextByte();
		
		//Add in elements until find "]"
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
	
	//Return rest of the input that is not read yet
	public String restOfInput() {
		if (!hasInput()) throw new IllegalStateException();
		StringBuilder returnStr = new StringBuilder();
		int charac = nextByte();
		while (isIgnoreChar(charac)) charac = nextByte();
		if (charac == 0) throw new IndexOutOfBoundsException();
		
		//Read all leftover characters
		while (currentIndex < totalChar) {
			returnStr.append((char) charac);
			charac = nextByte();
		}
		return returnStr.toString();
	}
	
	//Lazily clear the user input
	public void clearInput() {
		totalChar = 0;
	}
	
	//Close the InputStream
	public void close() throws IOException {
		input.close();
	}
}
