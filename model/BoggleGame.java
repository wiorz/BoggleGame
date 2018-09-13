package model;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class BoggleGame {

	private DiceTray diceTray;
	protected Dictionary dict;
	private Set<String> allPossibleWordsSet;
	private int totalScore;
	private Scanner userInput;
	private Set<String> correctlyGuessedWords;
	private Set<String> incorrectlyGuessedWords;
	private Set<String> allMissedWordsSet;
	private int totalCorrectGuessesCount;
	private int totalMissedWordsCount;

	public BoggleGame() throws FileNotFoundException {
		diceTray = new DiceTray();
		dict = new Dictionary();
		dict.readFile();
		allPossibleWordsSet = new TreeSet<>();
		totalScore = 0;
		// Note that userInput is NOT initialized here.
		correctlyGuessedWords = new TreeSet<>();
		incorrectlyGuessedWords = new TreeSet<>();
		allMissedWordsSet = new TreeSet<>();
		totalCorrectGuessesCount = 0; 
		totalMissedWordsCount = 0;
	}

	// Mainly used for testing.
	public BoggleGame(char[][] array) throws FileNotFoundException {
		diceTray = new DiceTray(array);
		dict = new Dictionary();
		dict.readFile();
		allPossibleWordsSet = new TreeSet<>();
		totalScore = 0;
		// Note that userInput is NOT initialized here.
		correctlyGuessedWords = new TreeSet<>();
		incorrectlyGuessedWords = new TreeSet<>();
		allMissedWordsSet = new TreeSet<>();
		totalCorrectGuessesCount = 0;
		totalMissedWordsCount = 0;

	}

	public Dictionary getDictionary() {
		return dict;
	}

	public DiceTray getDiceTray() {
		return diceTray;
	}

	public String getDiceTrayAsString() {
		return diceTray.toString();
	}

	public int getTotalScore() {
		return totalScore;
	}

	// Set the allPossibleWordsSet by passing in each word in the 
	// dictionary to diceTray.found().
	// Run this before getAllPossibleWordsAsString() for best results.
	public void setAllPossibleWords() {
		Set<String> allWordsInDict = dict.getAllWords();
		for (String word : allWordsInDict) {
			if (diceTray.found(word)) {
				allPossibleWordsSet.add(word);
			}
		}
	}
	
	public void resetGameData() {
		diceTray = new DiceTray();
		allPossibleWordsSet = new TreeSet<>();
		totalScore = 0;
		correctlyGuessedWords = new TreeSet<>();
		incorrectlyGuessedWords = new TreeSet<>();
		allMissedWordsSet = new TreeSet<>();
		totalCorrectGuessesCount = 0; 
		totalMissedWordsCount = 0;
		setAllPossibleWords();
		
	}

	private int getLongestWordLengthInSet(Set<String> setIn) {
		int longest = 0;
		for (String word : setIn) {
			longest = Math.max(longest, word.length());
		}
		return longest;
	}

	// Need to run setPossibleWrds() first to return the proper string.
	public String getAllPossibleWordsAsString() {
		StringBuilder resultBld = new StringBuilder();
		int count = 0;
		String longestStringFormat = "%-" + getLongestWordLengthInSet(allPossibleWordsSet) + "s";
		for (String word : allPossibleWordsSet) {
			if (count < 4) {
				resultBld.append(String.format(longestStringFormat, word) + " ");
				count++;
			}
			else if (count == 4) {
				resultBld.append(word);
				resultBld.trimToSize();
				resultBld.append("\n");
				count = 0;
			}
		}

		return resultBld.toString().trim();
	}

	// Return the score base on the length of the word, 
	// the word is a correct guess.
	private int calculateWordScore(String str) {
		int result = 0;
		int wordLength = str.length();
		if (wordLength < 5) {
			result = 1;
		} else if (wordLength == 5) {
			result = 2;
		} else if (wordLength == 6) {
			result = 3;
		} else if (wordLength == 7) {
			result = 5;
		} else {
			result = 11;
		}
		return result;
	}

	// Need an user string input to run.
	public void computeAgainstUserString(String str) {
		// Important to set the correct set first!!!
		setAllPossibleWords();
		// Always set userInput to upperCase.
		userInput = new Scanner(str.toUpperCase());

		while (userInput.hasNext()) {
			String curWord = userInput.next();
			// Only add word and score if it's 1) a legit word, 2) have not been already used/guessed.
			// Need the boolean check because the first check also applies to the else statement. 
			if (allPossibleWordsSet.contains(curWord) && curWord.length() > 2) {
				if (!(correctlyGuessedWords.contains(curWord) || incorrectlyGuessedWords.contains(curWord))) {
					totalScore += calculateWordScore(curWord);
					correctlyGuessedWords.add(curWord);
					totalCorrectGuessesCount++;
				}
			} else {
				incorrectlyGuessedWords.add(curWord);
			}
		}

		// Generate all missed words and update missed words count.
		for (String word : allPossibleWordsSet) {
			// Update allMissedWordsSet if user didn't guess that word. 
			if (!correctlyGuessedWords.contains(word)) {
				allMissedWordsSet.add(word);
				totalMissedWordsCount++;
			}
		}

	}

	// Yes, I know the next three methods are very similar, but it's easier to call them
	// from another class using these methods directly, instead of 
	public String getAllCorrectlyGuessedWordsAsString() {
		StringBuilder resultBld = new StringBuilder();
		int count = 0;
		String longestStringFormat = "%-" + getLongestWordLengthInSet(correctlyGuessedWords) + "s";
		for (String word : correctlyGuessedWords) {
			if (count < 4) {
				resultBld.append(String.format(longestStringFormat, word) + " ");
				count++;
			}
			else if (count == 4) {
				resultBld.append(word);
				resultBld.trimToSize();
				resultBld.append("\n");
				count = 0;
			}
		}

		return resultBld.toString().trim();
	}

	public String getAllIncorrectlyGuessedWordsAsString() {
		StringBuilder resultBld = new StringBuilder();
		int count = 0;
		String longestStringFormat = "%-" + getLongestWordLengthInSet(incorrectlyGuessedWords) + "s";
		for (String word : incorrectlyGuessedWords) {
			if (count < 4) {
				resultBld.append(String.format(longestStringFormat, word) + " ");
				count++;
			}
			else if (count == 4) {
				resultBld.append(word);
				resultBld.trimToSize();
				resultBld.append("\n");
				count = 0;
			}
		}

		return resultBld.toString().trim();
	}

	public String getAllWordsMissedAsString() {
		StringBuilder resultBld = new StringBuilder();
		int count = 0;
		String longestStringFormat = "%-" + getLongestWordLengthInSet(allMissedWordsSet) + "s";
		for (String word : allMissedWordsSet) {
			if (count < 4) {
				resultBld.append(String.format(longestStringFormat, word) + " ");
				count++;
			}
			else if (count == 4) {
				resultBld.append(word);
				resultBld.trimToSize();
				resultBld.append("\n");
				count = 0;
			}
		}

		return resultBld.toString().trim();
	}

	public int getTotalCorrectGuessesCount() {
		return totalCorrectGuessesCount;
	}
	
	public int getTotalMissedWordsCount() {
		return totalMissedWordsCount;
	}

}
