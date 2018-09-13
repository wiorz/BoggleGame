package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Dictionary {

	protected Set<String> allWords;
	private final String FILENAME = "BoggleWords.txt";

	public Dictionary() {
		allWords = new TreeSet<>();
	}

	// The boggleWords format is one word per line.
	public void readFile() throws FileNotFoundException {
		Scanner input = new Scanner(new File(FILENAME));
		while (input.hasNextLine()) {
			allWords.add(input.nextLine().toUpperCase().trim());
		}
		input.close();
	}

	public void readFileWithFileName(String fileNameIn) throws FileNotFoundException {
		Scanner input = new Scanner(new File(fileNameIn));
		while (input.hasNextLine()) {
			allWords.add(input.nextLine().toUpperCase());
		}
		input.close();
	}

	public boolean containsWord(String str) {
		// Always check for upper case
		return allWords.contains(str.toUpperCase());
	}

	public int getSize() {
		return allWords.size();
	}

	public boolean isEmpty() {
		return allWords.isEmpty();
	}
	
	public Set<String> getAllWords(){
		return allWords;
	}

	// Use for toString() below to dynamically resize the string format.
	public int getLongestWordLength() {
		int result = 0;
		for (String word : allWords) {
			result = Math.max(result, word.length());
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder resultBld = new StringBuilder();
		// To dynamically resize the columns to the longest word.
		String longestStringFormat = "%-" + getLongestWordLength() + "s";
		int count = 0;
		for (String word : allWords) {
			resultBld.append(String.format(longestStringFormat, word) + " ");
			count++;

			if (count % 5 == 0 && count != 0) {
				resultBld.trimToSize();
				resultBld.append("\n");
			}

		}
		return resultBld.toString().trim();
	}

}
