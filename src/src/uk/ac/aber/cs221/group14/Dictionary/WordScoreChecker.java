/*@(#) WordScoreChecker.java 1.8 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.Dictionary;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * Class that generates the Dictionary and scores the words if they are valid
 *
 * @author Neil Houston - neh1
 * @author Nelson Almedia - nea2
 *
 * @version 1.1 Initial development
 * @version 1.2 Implementation of Hashset
 * @version 1.3 Add and search methods developed
 * @version 1.4 Speeds tested
 * @version 1.5 Switch statement added to return the scrabble score values
 * @version 1.6 Method to score the words
 * @version 1.7 Java runnable added to use java threading
 * @version 1.8 Updates to make code more robust
 */
public class WordScoreChecker implements Dictionary, Runnable {

private final HashSet<String> wordDict;

public WordScoreChecker() {
    wordDict = new HashSet<>();
}


    /**
     * Reads the words.txt file into a Hashset
     * This method becomes the dictionary
     *
     * Does not return anything
     *
     * @exception FileNotFoundException used when file cannot be opened
     */
    private void dict() throws FileNotFoundException {
    String fileName = "words.txt";

    String line;

    try {
        FileReader fileReader = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while ((line = bufferedReader.readLine()) != null) {
            if (line.length() < 2) {
                wordDict.remove(line);

            } else {
                wordDict.add(line);
            }
        } bufferedReader.close();
    } catch (FileNotFoundException ex) {
        System.out.println("Unable to open file '" + fileName + "'");
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

    /**
     * Creates the JoggleCube score value
     *
     * @param word - word that has been submitted to be score checked
     * @return scrabble score value of the word to the power of 2
     */
    public int getWordValue(String word) {
    int value = 0; for (int x = 0; x < word.length(); ++x) {
        value += getStringScrabbleValue(String.valueOf(word.charAt(x)));
    } return (int) Math.pow(value, 2);
}

    /**
     * Assigns the scrabble score value for the word
     * @param ch - String used to look up the value for the letter
     * @return the scrabble value related to the relevant case
     */
    private double getStringScrabbleValue(String ch) {
    switch (ch) {
        case "a":
            return 1; case "b":
            return 3; case "c":
            return 3; case "d":
            return 2; case "e":
            return 1; case "f":
            return 4; case "g":
            return 2; case "h":
            return 4; case "i":
            return 1; case "j":
            return 8; case "k":
            return 5; case "l":
            return 1; case "m":
            return 3; case "n":
            return 1; case "o":
            return 1; case "p":
            return 3; case "r":
            return 1; case "s":
            return 1; case "t":
            return 1; case "u":
            return 1; case "v":
            return 4; case "w":
            return 4; case "x":
            return 8; case "y":
            return 4; case "z":
            return 10; case "qu":
            return 8;
    } return 0;
}
    /**
     * Searches for the dictionary to check if the word is valid
     *
     * Calls dict(); method to be able to search the dictionary
     *
     * @param word - The word that is being searched for
     * @return if the word valid it returns a true boolean value
     */
@Override
public boolean search(String word) {
    try {
        dict();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return wordDict.contains(word);
}
    /**
     * Add word to the relevant data structure
     * @param name - The word being added
     * @return false if the word is not added
     */
@Override
public boolean add(String name) {
    return false;
}

@Override
public void run() {
    try {
        dict();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    try {
        Thread.sleep(500L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}
