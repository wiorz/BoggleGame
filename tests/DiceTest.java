package tests;

import org.junit.Test;

import model.Dice;
public class DiceTest {

	@Test
	public void testSetDice() {
		Dice d1 = new Dice();
		d1.setCurDiceToNewCombination();
		System.out.println(d1);
	}
	
	
	

}
