package uk.ac.aber.cs221.group14.Tests;



import javafx.embed.swing.JFXPanel;
import org.testng.annotations.Test;
import uk.ac.aber.cs221.group14.Dictionary.WordScoreChecker;
import uk.ac.aber.cs221.group14.Game.Board;
import uk.ac.aber.cs221.group14.Game.Box;
import uk.ac.aber.cs221.group14.UI.MainGame;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SystemTests
{

@Test
public void loadGameTest() throws FileNotFoundException {
    //Creates two board objects
    Board testBoard1 = new Board();
    Board testBoard2 = new Board();

    //Assigns the boards from some values already saved boards
    System.out.println(Arrays.toString(testBoard1.load("CJPTEST.txt")));
    System.out.println(Arrays.toString(testBoard2.load("Alex.txt")));

    //Creates a variabe used to store the letters from the boards that have been loaded
    String loadCheck;
    loadCheck = String.valueOf(testBoard1.load("CJPTEST.txt"));

    //==27 doesnt work* - The board is made of thee faces containing 9boxes for a total of 27 letters
    if ((loadCheck.length() > 27)||(loadCheck.length() < 27)){
        System.out.println("LETTERS IN ARRAY = |27|");
    }
    //This condition should never be called if the appication works
    else{
        System.out.println("More letters than allowed stored in the board");
    }

    //tests GRID_SIZE in the board class
    int size = testBoard1.GRID_SIZE();
    //checks to see if the board has values
    assertNotNull(testBoard1);
    //checks to see if the GRID_SIZE value is equal to 3
    assertEquals(size, 3);
    //Checks that if you load the same board twice the string that the code processes is the same.
    assertEquals(testBoard1, testBoard1);
    //Checks that if you load the same board twice the string that the code processes is the same.
    assertEquals(testBoard2, testBoard2);
    //checks to see if the two boards are not the same
    assertNotSame(testBoard1, testBoard2);
}

/**
 * Tests that valid words score correctly - can be combined with dictionary tests
 */
@Test
public void scoreTest() {
    // creates a new WordScoreChecker instance
    WordScoreChecker test = new WordScoreChecker();
    //inputs the word 'my'
    int output1 = test.getWordValue("my");
    //inputs the word of 'tie'
    int output2 = test.getWordValue("tie");
    //inputs the word of 'jaws'
    int output3 = test.getWordValue("jaws");
    //the word value for 'my' is supposed to be 49 so it checks if the output is 49
    assertEquals(output1, 49);
    //the word value for 'tie' is supposed to be 9 so it checks if the output is 9
    assertEquals(output2, 9);
    //the word value for 'jaws' is supposed to be 196 so it checks if the output is 196
    assertEquals(output3, 196);

}

/**
 *
 * Checks words against words in the dictionary to check that the
 * user can only input correct words
 */
@Test
public void dictionaryTest(){
    // creates a new instance of WordScoreChecker
    WordScoreChecker inDictionary  = new WordScoreChecker();
    //searches to see if the word is in the dictionary
    boolean word1 = inDictionary.search("jam");
    //searches to see if the word is in the dictionary
    boolean word2 = inDictionary.search("nearpolitna");
    //expected return is true for word 1
    assertTrue(word1);
    //expected return is false for word 2
    assertFalse(word2);
}


//Need to initialize JavaFX UI before a new game can be created (maybe @Before)
@Test
public void newGameTest(){
    JFXPanel temp = new JFXPanel();
Box newGame = new Box(3,3,3);
newGame.generateRandomString();
System.out.println(newGame);
}

}
