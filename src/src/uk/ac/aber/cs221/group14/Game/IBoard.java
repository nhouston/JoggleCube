/*
 * @(#) IBoard.java 1.1 2018/04/30
 *
* Copyright (c) 2018 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group14.Game;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface that defines the inner workings of the Board class
 *
 * Defines :
 *
 *  - method to deselect all the letters
 *
 * - method to show the side that is currently selected
 *
 * - method to hightlight the available positions upon click
 *
 * - method to save the games
 *
 * - method to load the games
 *
 * @author neh1 - Neil Houston
 * @version 1.1 Creation of Interface
 *
 */
interface IBoard {

    /**
     * Base method to deselectAllLetters
     */
    public void deselectAllLetters();

    /**
     * Base method to show the currently selected side
     */

    public void showSelectedSide();

    /**
     * Base method to hightlight each position
     */
    public void highlightPositions();


    /**
     *
     * Base method for saving the games
     *
     * @param username - name of the user
     * @param filename - filename to be saved
     * @param score - game score to be saved
     * @throws IOException - if save loaction cannot be found
     */
    public void save(String username,String filename,int score) throws IOException;

    /**
     *
     * Base methods for loading the games
     *
     * @param fn - name of game file to load
     * @return - letters from the game file
     * @throws FileNotFoundException file could not be found in the saves location
     */
    public String[] load(String fn) throws FileNotFoundException;

}
