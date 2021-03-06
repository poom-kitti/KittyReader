# KittyReader

## What is this?
[KittyReader](https://github.com/poom-kitti/KittyReader/blob/master/src/KittyReader.java) is a Java class created to quickly read from a user input using Java standard input.

## Available Methods
1. `readInput()`	Read a new user input
2. `hasInput()`		Return boolean whether there is an unread portion of the user input
3. `nextInt()`		Return the next integer from the user input
4. `nextFloat()`	Return the next floating point number from the user input
5. `nextWord()`		Return the next single String word from the user input
7. `nextList()`		Return the next Object array from the user input 
    - Input ex. `[123, "hello world", -3.14]`
8. `restOfInput()`	Return the String of the rest of the user input that has not been read
9. `clearInput()`	Clear the current user input
10. `close()`		Close the System.in to prevent resource leakage

## Usage
1. Instantiate the KittyReader class
```
KittyReader reader = new KittyReader();
```
2. Read from user input
```
reader.readInput();
```
3. Get the variables from the input
```
int firstNum = reader.nextInt();
```
4. Repeat step 2 to read another input
5. Close the reader
```
reader.close()
```

## Important Note
Most of the code is referenced from [Shivam Sharma](https://hackthejava.wordpress.com/2016/09/16/inputoutput-in-java/). Please check out his article!
