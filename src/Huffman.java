import java.io.*;

import java.util.*;

public class Huffman {
	static int fileSize = 0;
	static int howManyCharType = 0;
	public static HuffmanCode[] HffmanCodesArray;
	public static HuffmanCode[] HffmanCodesArrayDecompress;
	static int howManyCharTypeDecompress = 0;
	static int compressedFileSize;
	static int fileIsNull;
	// Inner class for a node in the Huffman tree
	private static class Node implements Comparable<Node> {
		char character;
		int frequency;
		Node left;
		Node right;

		@Override
		public String toString() {
			return "Node [character=" + character + ", frequency=" + frequency + ", left=" + left + ", right=" + right
					+ "]";
		}

		Node(char character, int frequency) {
			this.character = character;
			this.frequency = frequency;
			this.left = null;
			this.right = null;
		}

		public Node() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compareTo(Node other) {
			return this.frequency - other.frequency;
		}
	}

	// Build the Huffman tree
		private static Node buildTree(int[] frequencyTable) {
			PriorityQueue<Node> pq = new PriorityQueue<>();
			for (int i = 0; i < frequencyTable.length; i++) {
				if (frequencyTable[i] > 0) {
					pq.add(new Node((char) i, frequencyTable[i]));
				}
			}
			Node arr[] = new Node[pq.size()];
			while (pq.size() > 1) {
				Node left = pq.poll();
				Node right = pq.poll();
				Node newNode = new Node('\0', left.frequency + right.frequency);
				newNode.left = left;
				newNode.right = right;
				pq.add(newNode);
			}

			pq.toArray(arr);
			// System.out.println(arr.length);

			return pq.poll();
		}

	// Build the code table
	private static void buildCodeTable(Map<Character, String> codeTable, Node node, String s) {

		if (node.left == null && node.right == null) {
			codeTable.put(node.character, s);
			return;
		}

		buildCodeTable(codeTable, node.left, s + '0');
		buildCodeTable(codeTable, node.right, s + '1');
		// System.out.println(codeTable.toString());
	}



	// Compress the input file
	public static void compress(File inputFile) throws IOException {
		//reading and writing bytes from and to a file is an efficient way to process and store data in a Huffman encoding project.
		
		howManyCharType=0;
		FileInputStream inputStream = new FileInputStream(inputFile);
		String inputFilePath = inputFile.getPath();
		fileSize = inputStream.available();// find the full size of the file
		byte[] buffer = new byte[1024]; // to read the file as bytes
		int freq[] = new int[256];// frequancy
		int size = inputStream.read(buffer, 0, 1024);// read 1024 bytes from file
		int numberOfCharInFile = 0;
		int index = -1;
		while (size > 0) {//read every character from the file and count how many character type and count the frequency of each character and count how many character in the file

			for (int i = 0; i < size; i++) {
				index = buffer[i];// get the character
				if (index < 0)
					index += 256;
				if (freq[index] == 0) {
					freq[index]++;
					numberOfCharInFile++;
					howManyCharType++;
				} else {
					freq[index]++;
					numberOfCharInFile++;
				}
			}
			size = inputStream.read(buffer, 0, 1024);
		}

		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0;// clear the buffer array
		Node root = buildTree(freq);//build the tree
		HffmanCodesArray = new HuffmanCode[howManyCharType];//create a huffmanCode array

		Map<Character, String> codeTable = new HashMap<>();//map to store every character and it's code
		
		if(root==null) {//if there is a tree
			fileIsNull=1;
			inputStream.close();
			return;
		}
		fileIsNull=-1;
		buildCodeTable(codeTable, root, "");//Build the map
		for (int i = 0, counter = 0; i < freq.length; i++) {//now add to the array each character and it's frequency and its code and code length
			if (freq[i] != 0) {
				HffmanCodesArray[counter] = new HuffmanCode((char) i, freq[i]);
				String code = codeTable.get((char) i);
				HffmanCodesArray[counter].huffCode = code;
				HffmanCodesArray[counter].codeLength = code.length();
				counter++;
			}
		}
		String[] HuffmanCodesArray = new String[256];
		for (int i = 0; i < howManyCharType; i++) {
			HuffmanCodesArray[(int) HffmanCodesArray[i].character] = new String(HffmanCodesArray[i].huffCode);// store
																												// huffman
																												// code
																												// for
																												// each
																												// character
		}
		//counter here to keep track to the output buffer if filled or not
		String outFileName = new StringTokenizer(inputFile.getAbsolutePath(), ".").nextToken() + ".huf";
		File outputfile = new File(outFileName);// set the output file name and path
		FileOutputStream output = new FileOutputStream(outFileName);
		byte[] outbuffer = new byte[1024];
		int counter = 0;	
		String inputFilePathN = inputFile.getPath();
		int dotIndex = inputFilePathN.indexOf('.');
		  String sub ="";
		if (dotIndex != -1) {
			sub= inputFilePathN.substring(dotIndex, inputFilePathN.length());

		}
		for (int i = 0; i < sub.length(); i++)
			outbuffer[counter++] = (byte) sub.charAt(i);// store the file extension
		outbuffer[counter++] = '\n';
		String nbchar = String.valueOf(numberOfCharInFile);
		for (int i = 0; i < nbchar.length(); i++) {
			outbuffer[counter++] = (byte) nbchar.charAt(i);// store the number of characters in the file

		}
		outbuffer[counter++] = '\n';
		// Number of Distinct Characters
		for (int i = 0; i < String.valueOf(howManyCharType).length(); i++)
			outbuffer[counter++] = (byte) String.valueOf(howManyCharType).charAt(i);// store how many character in the
																					// file
		outbuffer[counter++] = '\n';
		output.write(outbuffer, 0, counter);
		counter = 0;
		for (int i = 0; i < outbuffer.length; i++)
			outbuffer[i] = 0;// clear the array

		// The HuffCode for Each Character
		//1- Write the character as a single byte to the output buffer.
		//2- Write the length of the Huffman code as a single byte to the output buffer.
		//3- Store the Huffman code as a sequence of bytes in the code array.
		//4- Write the contents of the code array to the output buffer.
		for (int i = 0; i < howManyCharType; i++) {

			if (counter == 1024) {// if the output buffer filled
				output.write(outbuffer);
				counter = 0;
			}
			outbuffer[counter++] = (byte) HffmanCodesArray[i].character;//set the character as a single byte
			if (counter == 1024) {// if the output buffer filled
				output.write(outbuffer);
				counter = 0;
			}
			outbuffer[counter++] = (byte) HffmanCodesArray[i].codeLength;// write the codes lengths in the file as a single byte

			String result = "";
			Long x;
			if (HffmanCodesArray[i].huffCode.length() > 15) {// if the length is more than 15 digits divide the code
																// into tow pieces(not very urgent because it's very hard to find an code have a more than 15 bits code )
				for (int z = 0; z < HffmanCodesArray[i].huffCode.length() / 2; z++) {
					result += HffmanCodesArray[i].huffCode.charAt(z) + "";
				}
				x = Long.parseLong(result);
				result = "";
				for (int z = (HffmanCodesArray[i].huffCode.length() + 1) / 2; z < HffmanCodesArray[i].huffCode
						.length(); z++) {
					result += HffmanCodesArray[i].huffCode.charAt(z) + "";
				}
				x += Long.parseLong(result);

			} else {// if not store it immediately
				x = Long.parseLong(HffmanCodesArray[i].huffCode);
			}
			byte[] code = new byte[50];// to store the code in it
			int l = 0;//to keep track to how many number of bytes stored in code[] because the code may be is more then 8 bits (more than byte)

			if (x == 0) {
				outbuffer[counter++] = 0;// set zero
				if (counter == 1024) {// write and reset the counter
					output.write(outbuffer);
					counter = 0;
				}

				outbuffer[counter++] = 0;// set zero
				if (counter == 1024) {// write and reset the counter
					output.write(outbuffer);
					counter = 0;
				}
			} else {// if the code not zero
				while (x != 0) {
					if (counter == 1024) {// write and reset the counter
						output.write(outbuffer);
						counter = 0;
					}
					//this if the code is more than 8 bits
					code[l++] = (byte) (x % 256);// get the first code element(left one),(2^8=256)	
					x /= 256;//delete the first code element(left one),(2^8=256)

				}
				//l here to store how many bytes stored in the code array
				outbuffer[counter++] = (byte) l;// add the result into buffer
				if (counter == 1024) {// write and reset the counter
					output.write(outbuffer);
					counter = 0;
				}
				for (int j = 0; j < l; j++) {
					outbuffer[counter++] = code[j];// set the code into output buffer

					if (counter == 1024) {// write and reset the counter
						output.write(outbuffer);
						counter = 0;
					}
				}
			}
			if (counter == 1024) {// write and reset the counter
				output.write(outbuffer);
				counter = 0;
			}

			outbuffer[counter++] = '\n';

		}
		// Print Out The Header
		output.write(outbuffer, 0, counter);
		for (int i = 0; i < outbuffer.length; i++)// reset the buffer
			outbuffer[i] = 0;
		inputStream.close();
		inputStream = new FileInputStream(inputFilePath);// to read the file from begging
		counter = 0;
		size = inputStream.read(buffer, 0, 1024);// read bytes
		do {
			for (int i = 0; i < size; i++) {// for each character in the data readed
				index = buffer[i];// get ASCII value of the character
				if (index < 0)// If the Value was negative
					index += 256;

				for (int j = 0; j < HuffmanCodesArray[index].length(); j++) {// for all huffman code bits
					char ch = HuffmanCodesArray[index].charAt(j);
					if (ch == '1')// if element is one set the a bit in buffer to one if it's not one don't do
									// anything because it's already zero
						outbuffer[counter / 8] = (byte) (outbuffer[counter / 8] | 1 << 7 - counter % 8);// this will set
																										// a bit to 1
					counter++;
					if (counter / 8 == 1024) {// diveid counter by 8 because here we are working with bits
						output.write(outbuffer);
						for (int k = 0; k < outbuffer.length; k++)
							outbuffer[k] = 0;
						counter = 0;
					}
				}
			}
			size = inputStream.read(buffer, 0, 1024);// read a new data
		} while (size > 0);
		inputStream.close();// stop reading
		output.write(outbuffer, 0, (counter / 8) + 1);// write the data
		output.close();// stop writing
		inputStream = new FileInputStream(outputfile);
		compressedFileSize = inputStream.available();// store the size of compressed file
		inputStream.close();
		if (inputFile.exists())
			inputFile.delete();

	}



	// Decompress the input file
	public static void decompress(File inputFile) throws IOException {
		int size = 0;
		boolean newLineFlag = true;
		String fileName = null;
		fileName = inputFile.getPath();
		File file = new File(fileName);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[1024];// to read the file as bytes
		int counter = 0;
		int bufferCounter = 0;
		String originalFileName = "";
		int dotIndex = fileName.indexOf('.');
		  String sub ="";
		if (dotIndex != -1) {
			sub= fileName.substring(0, dotIndex);

		}
		char[] dataReaded = new char[200];
		size = inputStream.read(buffer, 0, 1024);
		while (newLineFlag) {
			if (counter == 200) {
				break;
			}
			if (buffer[counter] != '\n') {
				dataReaded[counter++] = (char) buffer[bufferCounter++];

			} else {

				newLineFlag = false;
				break;
			}
		}
		bufferCounter++;
		originalFileName = String.valueOf(dataReaded, 0, counter);
		sub=sub+originalFileName;
		originalFileName=sub;
		long NumberOfCharsInFile = 0;
		counter = 0;
		newLineFlag = true;
		while (newLineFlag) {
			if (bufferCounter == 1024) {
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}
			if (buffer[bufferCounter] != '\n') {
				dataReaded[counter++] = (char) buffer[bufferCounter++];
			} else {
				newLineFlag = false;
			}

		}
		
		NumberOfCharsInFile = Integer.parseInt(String.valueOf(dataReaded, 0, counter));
		bufferCounter++;
		// Get the Number of Distinct characters
		int numberOfCharacter = 0;
		counter = 0;
		newLineFlag = true;
		while (newLineFlag) {
			if (bufferCounter == 1024) {
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}
			if (buffer[bufferCounter] != '\n')
				dataReaded[counter++] = (char) buffer[bufferCounter++];
			else
				newLineFlag = false;
		}
		numberOfCharacter = Integer.parseInt(String.valueOf(dataReaded, 0, counter));
		bufferCounter++;

		// Reading the huff Code
		HffmanCodesArrayDecompress = new HuffmanCode[numberOfCharacter];
		howManyCharTypeDecompress = numberOfCharacter;
		for (int i = 0; i < numberOfCharacter; i++) {// for each character
			HffmanCodesArrayDecompress[i] = new HuffmanCode((char) Byte.toUnsignedInt(buffer[bufferCounter++]));//convert a byte into int then to char
			if (bufferCounter == 1024) {// read new data
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}
			HffmanCodesArrayDecompress[i].codeLength = buffer[bufferCounter++];// read the length witch is at first

			if (bufferCounter == 1024) {
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}
			int l = buffer[bufferCounter++];// store the length of huffman code(how many bytes)//not used because there is no character more then 8 bits ******
			if (bufferCounter == 1024) {
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}
			if (l == 0)
				bufferCounter++;
			if (bufferCounter == 1024) {
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}
			long x = 0;
			for (int j = 0; j < l; j++) {
				x += Byte.toUnsignedLong(buffer[bufferCounter++]) * (1 << 8 * j);// store the bits readed into a long
																					// value(we shift 8 bits multible by
																					// the code length because it's
																					// stored as bytes and we read bits)
				if (bufferCounter == 1024) {
					size = inputStream.read(buffer, 0, 1024);
					bufferCounter = 0;
				}
			}
			HffmanCodesArrayDecompress[i].huffCode = String.valueOf(x);
			if (HffmanCodesArrayDecompress[i].huffCode.length() != HffmanCodesArrayDecompress[i].codeLength) {// because
																												// the
																												// code
																												// may
																												// be
																												// divided
																												// into
																												// tow
																												// pieces
				String s = "";
				for (int j = 0; j < HffmanCodesArrayDecompress[i].codeLength
						- HffmanCodesArrayDecompress[i].huffCode.length(); j++)// add the other piece
					s += "0";
				s += HffmanCodesArrayDecompress[i].huffCode;
				HffmanCodesArrayDecompress[i].huffCode = s;// set the new huffman code
			}
			bufferCounter++;
			if (bufferCounter == 1024) {
				size = inputStream.read(buffer, 0, 1024);
				bufferCounter = 0;
			}

		}

		for (int i = 0; i < dataReaded.length; i++)
			dataReaded[i] = '\0';

		if (bufferCounter == 1024) {
			size = inputStream.read(buffer, 0, 1024);
			bufferCounter = 0;
		}

		int index = bufferCounter;

		bufferCounter = 0;
		byte[] outputBuffer = new byte[1024];
		counter = 0;
		FileOutputStream output = new FileOutputStream(originalFileName);
		Node root1 = buildTree();
		Node tree1 = root1;
		long count = 0;

		newLineFlag = false;

		do {
			// System.out.println(bufferCounter);
			while (tree1.left != null || tree1.right != null) {

				if ((buffer[index] & (1 << 7 - bufferCounter)) == 0)
					tree1 = tree1.left;
				else
					tree1 = tree1.right;
				bufferCounter++;
				if (bufferCounter == 8) {
					bufferCounter = 0;
					index++;
					if (index == 1024) {
						size = inputStream.read(buffer, 0, 1024);
						index = 0;
						if (size == -1)
							newLineFlag = true;
					}
				}
			}
			if (newLineFlag)
				break;
			outputBuffer[counter++] = (byte) tree1.character;

			if (counter == 1024) {
				for (int i = 0; i < outputBuffer.length; i++) {
					// System.out.println(outputBuffer[i]);
				}
				output.write(outputBuffer);
				counter = 0;
			}
			count++;

			tree1 = root1;
			if (count == NumberOfCharsInFile)
				break;
		} while (size != -1);

		output.write(outputBuffer, 0, counter);
		output.close();
		inputStream.close();
		if (file.exists())
			file.delete();
		

	}

	public static Node buildTree() {
		// Mapping between character codes and characters
		// HffmanCodesArrayDecompress;
		Map<String, Character> codeMap = new HashMap<>();
		for (int i = 0; i < HffmanCodesArrayDecompress.length; i++) {
			codeMap.put(HffmanCodesArrayDecompress[i].getHuffCode(), HffmanCodesArrayDecompress[i].getCharacter());
		}

		Node root = new Node('\0', 1);
		Node node = root;

		// Build the tree by following the character codes
		for (int s = 0; s < HffmanCodesArrayDecompress.length; s++) {
			String code = HffmanCodesArrayDecompress[s].huffCode;
			for (char c : code.toCharArray()) {
				if (c == '0') {
					if (node.left == null) {
						node.left = new Node('\0', 1);
					}
					node = node.left;
				} else {
					if (node.right == null) {
						node.right = new Node('\0', 1);
					}
					node = node.right;
				}
			}
			node.character = codeMap.get(code);
			node = root;
		}

		return root;
	}


}