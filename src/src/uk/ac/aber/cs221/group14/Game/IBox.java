/*
 * @(#) IBox.java 1.1 2018/04/30
 *
* Copyright (c) 2018 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group14.Game;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Interface that defines the inner workings of the Box class
 *
 * Defines:
 *
 * - method to get the StackPane
 *
 * - method to get the rectangle
 *
 * - method to get the string letter
 *
 * - method to generate random strings to be assigned to the grid
 *
 * - method to setup the grid with the letters
 *
 * - method to check if a box is clicked or not
 *
 * - method to set the box to be clicked
 *
 * - method that returns how many of each letter is available
 *
 * @author neh1 - Neil Houston
 * @version 1.1 Creation of Interface
 *
 */
public interface IBox {

    /**
     * Base method to return the stackpane
     * stackpane used to set up the grid with boxes
     * @return stackpane of boxes
     */
    public abstract StackPane getStackPane();

    /**
     * Base method to get the individual boxes
     * @return letter box
     */
    public abstract Rectangle getRect();

    /**
     * Base method to return the string value assigned to a specific box
     * @return boxletters
     */
    public abstract String getString();

    /**
     * Base method to generate a random string to be assigned to a box
     *
     * @return generated random string
     */
    public abstract String generateRandomString();

    /**
     * Base method to set up individual letter box with its own randomly generated string
     */
    public void setUpChar();

    /**
     * Base method to check if a box is clicked or not
     *
     * @return is a box clicked
     */
    public boolean isClicked();

    /**
     * Base method to set the boolean value of isClicked to true
     *
     * @param isIt sets the value to true if the box is clicked
     */
    public void setIsClicked(boolean isIt);

    /**
     * Base method to set a limit on how many of each letter is available on the grid at one time
     *
     * @param c the letter to be checked
     * @return the value of how many times a letter can occur
     */
    public abstract int getStringLimitValue(String c);

}
