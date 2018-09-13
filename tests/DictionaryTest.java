package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import model.Dictionary;

public class DictionaryTest {

	@Test
	public void testConstructorAndGetSize() {
		Dictionary dict = new Dictionary();
		System.out.println(dict.getClass());
		assertEquals(0, dict.getSize());
		assertTrue(dict.isEmpty());
		
	}
	
	@Test
	public void testReadFile() throws FileNotFoundException {
		Dictionary dict = new Dictionary();
		dict.readFile();
		assertNotEquals(0, dict.getSize());
		System.out.println(dict.toString());
		
	}
	
	@Test
	public void testContainsWord() throws FileNotFoundException {
		Dictionary dict = new Dictionary();
		dict.readFile();
		assertTrue(dict.containsWord("teleCAsT"));
		assertFalse(dict.containsWord("teleTubby"));
	}

}
