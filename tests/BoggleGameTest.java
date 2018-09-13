package tests;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import model.BoggleGame;




public class BoggleGameTest {
	private static char[][] tray = { // Always use upper case letters in the dice tray
		      { 'A', 'B', 'C', 'D' },

		      { 'E', 'F', 'G', 'H' },

		      { 'I', 'J', 'K', 'L' },

		      { 'M', 'N', 'O', 'P' } };

	@Test
	public void testConstructor() throws FileNotFoundException {
		BoggleGame game = new BoggleGame(tray);
		System.out.println(game.getDiceTray().toString());
		System.out.println(game.getDictionary().toString());
	}
	
	@Test
	public void testGetAllPossibleWords() throws FileNotFoundException {
		BoggleGame game = new BoggleGame(tray);
		assertEquals("", game.getAllPossibleWordsAsString());
		game.setAllPossibleWords();
		System.out.println(game.getAllPossibleWordsAsString());
	}
	
	@Test
	public void testComputeAgainstUserString() throws FileNotFoundException {
		BoggleGame game = new BoggleGame(tray);
		// game.setAllPossibleWords();
		// Check for case insensitivity, and also re-used words.
		game.computeAgainstUserString("fie fIN , what InK nooooo pol,    wait fin fie");
		System.out.println(game.getAllCorrectlyGuessedWordsAsString());
		assertEquals("FIE FIN INK", game.getAllCorrectlyGuessedWordsAsString());
		System.out.println(game.getAllIncorrectlyGuessedWordsAsString());
		assertEquals(",      NOOOOO POL,   WAIT   WHAT", game.getAllIncorrectlyGuessedWordsAsString());
		System.out.println(game.getTotalScore());
		assertEquals(3, game.getTotalScore());
		System.out.println(game.getAllWordsMissedAsString());
		assertEquals("FINK  GLOP  KNIFE LOP   MINK\n" + 
				"POL", game.getAllWordsMissedAsString());
		System.out.println(game.getTotalMissedWordsCount());
		assertEquals(6, game.getTotalMissedWordsCount());
		
	}
	
	

}
