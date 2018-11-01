/*
 * @(#) Box.java 1.8 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.Game;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/**
 * Class that creates each individual box and adds a random letter or a loaded letter to it
 *
 * @author Neil Houston - neh1
 * @author Nelson Almedia - nea2
 *
 * @version 1.1 Initial development
 * @version 1.2 Base methods implemented
 * @version 1.3 Test code to create a box is implemented
 * @version 1.4 Method to randomly generate letter string implemented and test code replaced with working box code
 * @version 1.5 Method to set up each box with a letter implemented
 * @version 1.6 Method to return how many of each letter can be used
 * @version 1.7 Second constructor added to create boxes for a loaded game
 * @version 1.8 code refactoring
 */
public class Box implements IBox {
    private static final Random rand = new Random();
    private static final HashMap<String, Integer> StringMapper = new HashMap<>();
    private Rectangle rect;
    private StackPane stack;
    private String boxLetter;
    private boolean isClicked;
    private boolean hasGeneratedChar = false;
    private Text text;
    private ArrayList<String> lettersArray = new ArrayList<>();
    /**
     * Constructor that builds a randomly generated box and assigns a randomly generated string to it
     *
     * Random strings are added using the setupChar method
     *
     * @param colValue - column value of the grid
     * @param rowValue - row value of the grid
     * @param sideValue - sidevalue that the grid is on
     */
    public Box(int colValue, int rowValue, int sideValue) {
        isClicked = false;
        //rectangle stuff
        rect = new Rectangle(60, 60); rect.setFill(Color.WHEAT); rect.setStroke(Color.BLACK); rect.setStrokeWidth(2);
        //this can be done better but this is the easiest way
        while (!hasGeneratedChar) {
            setUpChar();
        }
        text = new Text(boxLetter); stack = new StackPane(); stack.setUserData(new Position(sideValue, colValue, rowValue));
        stack.getChildren().addAll(rect, text);
    }
    /**
     *
     *  Constructor that builds a loaded game and assigns the letters from the loaded game file to the boxes
     *
     *  Strings are added using the setupChar method
     *
     * @param colValue - column value of the grid
     * @param rowValue - row value of the grid
     * @param sideValue - sidevalue that the grid is on
     * @param ch - the loaded letters to be added to the grid
     */
    public Box(int colValue, int rowValue, int sideValue, char ch) {
        isClicked = false;
        //A new rectangle
        rect = new Rectangle(60, 60); rect.setFill(Color.WHEAT); rect.setStroke(Color.BLACK); rect.setStrokeWidth(2);
        if (ch == 'q') boxLetter = "qu";
        else boxLetter = Character.toString(ch); text = new Text(boxLetter); stack = new StackPane();
        stack.setUserData(new Position(sideValue, colValue, rowValue)); stack.getChildren().addAll(rect, text);
    }
    /**
     *
     * Getter that returns the stack(stackpane)
     *
     * @return stack
     */
    @Override
    public StackPane getStackPane() {
        return stack;
    }
    /**
     * Getter that returns the box
     *
     * @return rect
     */
    @Override
    public Rectangle getRect() {
        return rect;
    }
    /**
     * Getter to return the box letter string
     *
     * @return boxletter
     */
    @Override
    public String getString() {
        return boxLetter;
    }
    /**
     * Generates a random string of letters and adds them to an array.
     *
     * The letters in the array is then randomized and the letters are returned
     *
     * @return
     */
    @Override
    public String generateRandomString() {
        String addingLetters; String letters = "abcdefghijklmnoprstuvwxyz"; String Qu = "qu";
        for (int i = 0; i < 25; i++) {
            addingLetters = String.valueOf(letters.charAt(new Random().nextInt(20))); lettersArray.add(addingLetters);
        } lettersArray.add(Qu);
        return lettersArray.get(rand.nextInt(lettersArray.size()));
    }
    /**
     * Takes the boxletters and add thems to a box.
     *
     * Goes through each column and row  and adds it
     */
    @Override
    public void setUpChar() {
        //generate char, check for max char number, update HashMap
        boxLetter = generateRandomString();
        //if we already have that character in the grid
        if (StringMapper.containsKey(boxLetter)) {
            //if the number of that character does not exceed the limit
            if (StringMapper.get(boxLetter) < getStringLimitValue(boxLetter)) {
                //insert it and update the map
                StringMapper.put(boxLetter, StringMapper.get(boxLetter) + 1);
                hasGeneratedChar = true;//stop the loop in the constructor
            }
        }
        //if we don't have it its ok
        else {
            StringMapper.put(boxLetter, 1);
            hasGeneratedChar = true;//stop the loop in the constructor
        }
    }
    /**
     * Checks if a box has been clicked
     * @return
     */
    @Override
    public boolean isClicked() {
        return isClicked;
    }
    /**
     * Sets the isClicked boolean to be true
     *
     * @param isIt sets the value to true if the box is clicked
     */
    @Override
    public void setIsClicked(boolean isIt) {
        isClicked = isIt;
    }
    /**
     *
     * Sets the amount times a letter can be used or added to a grid in one instance
     *
     * Each letter can only occur a certain amount of times
     * @param c the letter to be checked
     * @return how many times a letter can occur
     */
    @Override
    public int getStringLimitValue(String c) {
        switch (c) {
            case "a":
                return 9; case "b":
                return 2; case "c":
                return 2; case "d":
                return 4; case "e":
                return 12; case "f":
                return 2; case "g":
                return 3; case "h":
                return 2; case "i":
                return 9; case "j":
                return 1; case "k":
                return 1; case "l":
                return 4; case "m":
                return 2; case "n":
                return 6; case "o":
                return 8; case "p":
                return 2; case "r":
                return 6; case "s":
                return 4; case "t":
                return 6; case "u":
                return 3; case "v":
                return 2; case "w":
                return 2; case "x":
                return 1; case "y":
                return 2; case "z":
                return 1; case "qu":
                return 1;
        } return 0;
    }
}