package model;

import java.util.Random;

// author: Ivan Ko

public class Dice {

	private char[] faces;
	private int diceTypeNum;
	protected final char[][] allLegalCombinations = { { 'L', 'R', 'Y', 'T', 'T', 'E' }, // L R Y T T E 
		{ 'V', 'T', 'H', 'R', 'W', 'E' }, // V T H R W E
		{ 'E', 'G', 'H', 'W', 'N', 'E' }, // E G H W N E
		{ 'S', 'E', 'O', 'T', 'I', 'S' }, // S E O T I S
		{ 'A', 'N', 'A', 'E', 'E', 'G' }, // A N A E E G
		{ 'M', 'T', 'O', 'I', 'C', 'U' }, // M T O I C U
		{ 'O', 'A', 'T', 'T', 'O', 'W' }, // O A T T O W
		{ 'I', 'D', 'S', 'Y', 'T', 'T' }, // I D S Y T T  
		{ 'E', 'N', 'S', 'I', 'E', 'U' }, // E N S I E U
		{ 'H', 'C', 'P', 'O', 'A', 'S' }, // H C P O A S
		{ 'X', 'L', 'D', 'E', 'R', 'I' }, // X L D E R I 
		{ 'A', 'F', 'P', 'K', 'F', 'S' }, // A F P K F S  
		{ 'O', 'B', 'B', 'A', 'O', 'J' }, // O B B A O J
		{ 'N', 'M', 'I', 'H', 'U', 'Q' }, // N M I H U Q
		{ 'Z', 'N', 'R', 'N', 'H', 'L' }, // Z N R N H L
		{ 'Y', 'L', 'D', 'E', 'V', 'R' } }; // Y L D E V R;

	// Basic constructor for instantiating a new dice.
	// Since no charArray is given, a random legal combination will be picked. 
	public Dice() {

		faces = new char[6];
		diceTypeNum = -1;
		
	}

	// Constructor when given custom dice array 
	public Dice(char[] chrArray) {
		faces = chrArray;
		Random customNum = new Random();
		// Force absolute value here so we can make sure we set it to negative,
		// which will be an unique value and likely won't collide with other diceNum.
		int diceNum = Math.abs(customNum.nextInt());
		diceTypeNum = diceNum * -1;
	}

	// Only work after the empty constructor has been used.
	public void allCombinationPrintTest() {
		if (allLegalCombinations == null) {
			return;
		}
		for (char[] row : allLegalCombinations) {
			for (char letter : row) {
				System.out.print(letter);
			}
			System.out.println();
		}
	}

	// Roll the dice to get a random char.
	public char roll() {
		Random randPick = new Random();
		int randFaceInd = randPick.nextInt(allLegalCombinations[0].length);
		return faces[randFaceInd];
	}
	
	public int getDiceTypeNum() {
		return this.diceTypeNum;
	}

	public int getRandDiceCombinationNum() {
		Random randPick = new Random();
		int randNum = randPick.nextInt(allLegalCombinations.length);
		return randNum;
	}
	
	// Use in combination with getRandDiceCombinationNum
	// mainly so that we can monitor duplication in DiceTray.
	public void resetDiceToTargetCombination(int num) {
		faces = allLegalCombinations[num];
		diceTypeNum = num;
	}
	
	public void setCurDiceToNewCombination() {
		int randNum = getRandDiceCombinationNum();
		faces = allLegalCombinations[randNum];
		diceTypeNum = randNum;
	}
	
	public int getAllCombinationRowNum() {
		return allLegalCombinations.length;
	}
	
	@Override
	public String toString() {
		StringBuilder resultBld = new StringBuilder();
		for(char letter : faces) {
			resultBld.append(letter + " ");
		}
		resultBld.trimToSize();
		resultBld.append("\n");
		return resultBld.toString();
	}

}
