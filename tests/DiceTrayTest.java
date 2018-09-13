package tests;

// A unit test for class DiceTray
//
// authors: Rick Mercer and Ivan Ko 
//
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.DiceTray;

public class DiceTrayTest {

  private static char[][] tray = { // Always use upper case letters in the dice tray
      { 'A', 'B', 'C', 'D' },

      { 'E', 'F', 'G', 'H' },

      { 'I', 'J', 'K', 'L' },

      { 'M', 'N', 'O', 'P' } };

  @Test
  public void testStringFindWhenThereStartingInUpperLeftCorner() {
    DiceTray bt = new DiceTray(tray);
    assertTrue(bt.found("ABC"));
    assertTrue(bt.found("AbC")); // Must be case insensitive
    assertTrue(bt.found("abE"));
    assertTrue(bt.found("abF"));
    assertTrue(bt.found("abG"));
    assertTrue(bt.found("ABCD"));
    assertFalse(bt.found("ABD"));
    assertFalse(bt.found("ABA"));
    
    // ...
    assertTrue(bt.found("ABFEJINM"));
    assertTrue(bt.found("AbCdHgFeIjKLpONm"));
    assertTrue(bt.found("ABCDHLPOKJNMIEFG"));
  }
  
  @Test
  public void testDiceTrayContructor() {
	  DiceTray bt = new DiceTray();
	  System.out.println(bt);
  }
  
}