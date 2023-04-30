



public class HuffmanCode implements Comparable<HuffmanCode> {
	char character;
	int counter;
	String huffCode;
	int codeLength;

	public HuffmanCode(char character) {
		this.character = character;
	}

	public HuffmanCode(char character, int counter) {
		this.character = character;
		this.counter = counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void sethuffCode(String huffCode) {
		this.huffCode = huffCode;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	public char getCharacter() {
		return character;
	}

	public int getCounter() {
		return counter;
	}

	public String getHuffCode() {
		return huffCode;
	}

	public int getCodeLength() {
		return codeLength;
	}

	@Override
	public String toString() {
		return "HuffCode{" + "character=" + (int) character + ", counter=" + counter + ", huffCode=" + huffCode
				+ ", codeLength=" + codeLength + '}';
	}

	@Override
	public int compareTo(HuffmanCode t) {
		return huffCode.compareTo(t.huffCode);
	}

}
