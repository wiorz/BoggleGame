package model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// Represent a major piece of Boggle. 
//
// authors: Rick Mercer and Ivan Ko 
//
public class DiceTray {
	protected char[][] tray;
	private int totalRow;
	private int totalCol;
	private int startRow;
	private int startCol;
	private static final char TRIED = '.';
	private Set<Integer> usedDiceSet;

	// Constructor takes a 2D array of characters that represents the
	// Boggle DiceTray with 16 dice already rolled in a known fixed state.
	public DiceTray(char[][] array) {
		// set start to -1 for quick determination if the beginning of the word is even
		// possible.
		this.startRow = -1;
		this.startCol = -1;
		this.tray = array;
		this.totalRow = this.tray.length;
		this.totalCol = this.tray[0].length;
		usedDiceSet = new HashSet<>();
	}

	// This constructor creates a a DiceTray that simulates the rolling of the 16
	// Boggle dice to begin a game of Boggle. The same die must not always appear
	// in the same location. Here are the letters of each of the 16 six-sided dice
	// where six consecutive letters represent one die. L R Y T T E are the letters
	// on one Boggle die-the one in the upper left corner-for example:
	//
	// L R Y T T E ... V T H R W E ... E G H W N E ... S E O T I S
	// A N A E E G ... I D S Y T T ... O A T T O W ... M T O I C U
	// A F P K F S ... X L D E R I ... H C P O A S ... E N S I E U
	// Y L D E V R ... Z N R N H L ... N M I H U Qu .. O B B A O J
	//
	public DiceTray() {
		this.startRow = -1;
		this.startCol = -1;
		usedDiceSet = new HashSet<>();
		
		this.tray = new char[4][4];
		this.totalRow = this.tray.length;
		this.totalCol = this.tray[0].length;
		
		// Get first dice. We'll reroll this anyway, not need to add
		// to usedDiceSet. 
		Dice myDice = new Dice();
		
		for(int i = 0; i < this.totalRow; i++) {
			for(int j = 0; j < this.totalCol; j++) {
				myDice = getDiceWithUnusedCombination();
				char tempChar = myDice.roll();
				tray[i][j] = tempChar;
			}
		}		
	}
	
	// Use by getDiceWithUnusedCombination() below. 
	private int getUnusedDiceCombinationNum(Dice curDice) {
		Random base = new Random();
		int result = base.nextInt(curDice.getAllCombinationRowNum());
		// Make sure the combination is not used before.
		while(usedDiceSet.contains(result)) {
			result = base.nextInt(curDice.getAllCombinationRowNum());
		}
		return result;
	}
	
	private Dice getDiceWithUnusedCombination() {
		Dice newDice = new Dice();
		newDice.setCurDiceToNewCombination();
		int diceNum = newDice.getDiceTypeNum();
		// If the combination is already in use, find one that is new.
		if(usedDiceSet.contains(diceNum)) {
			diceNum = getUnusedDiceCombinationNum(newDice);
		}
		usedDiceSet.add(diceNum);
		// Reset dice here.
		newDice.resetDiceToTargetCombination(diceNum);
		
		return newDice;
	}

	// Return true if str is found in the this DiceTray according to Boggle
	// rules. Note: This method does NOT check to see if the word is in the 
	// list of words.
	//
	// Precondition: str.length() >= 3
	public boolean found(String str) {
		// Make string case insensitive by forcing to UpperCase.
		str = str.toUpperCase();
		// Set startRow and startCol here, if not found, it remains 0.
		getStartCor(str);
		// If can't even find the first character then there's no reason to do recursion. 
		if (this.startRow == -1 || this.startCol == -1) {
			return false;
		} else {
			// Given different starting point, do recursion until that word is found.
			for (int i = 0; i < totalRow; i++) {
				for (int j = 0; j < totalCol; j++) {
					if (foundHelper(i, j, str)) {
						return true;
					}
				}
			}
			return false;
		}
	}

	private boolean foundHelper(int row, int col, String str) {
		// Found word, because we got this far.
		if (str.length() == 0) {
			return true;
		} // Condition check for outofBound or the letter doesn't match. 
		else if (row < 0 || row >= this.totalRow || col < 0 || col >= this.totalCol
				|| this.tray[row][col] != str.charAt(0)) {
			return false;
		} else {

			boolean foundWord = false;
			char oldChar = this.tray[row][col];
			// Mark path.
			this.tray[row][col] = TRIED;

			// Eight possible directions to go
			//  _4_| 5 |_6_
			//  _3_| * |_7_
			//  _2_| 1 |_8_

			// 1
			foundWord = foundHelper(row + 1, col, str.substring(1)) ||
					// 2
					foundHelper(row + 1, col - 1, str.substring(1)) ||
					// 3... and more, see below.
					foundHelper(row, col - 1, str.substring(1)) ||

					foundHelper(row - 1, col - 1, str.substring(1)) ||

					foundHelper(row - 1, col, str.substring(1)) ||

					foundHelper(row - 1, col + 1, str.substring(1)) ||

					foundHelper(row, col + 1, str.substring(1)) ||

					foundHelper(row + 1, col + 1, str.substring(1));

			// Reset trace.
			this.tray[row][col] = oldChar;

			return foundWord;
		}
	}

	// Find where to being the search, else do nothing and the default value 
	// remains -1 for both startRow and startCol.
	private void getStartCor(String str) {
		for (int i = 0; i < totalRow; i++) {
			for (int j = 0; j < totalCol; j++) {
				// Attempt find the first character.
				if (this.tray[i][j] == str.charAt(0)) {
					this.startRow = i;
					this.startCol = j;
					return;
				}
			}
		}
	}
	
	public char[][] getTray(){
		return tray;
	}
	
	@Override
	public String toString() {
		StringBuilder resultBld = new StringBuilder();
		for(char[] row : tray) {
			for(char letter : row) {
				resultBld.append(letter + " ");
			}
			resultBld.trimToSize();
			resultBld.append("\n");
		}
		return resultBld.toString();
	}
}
