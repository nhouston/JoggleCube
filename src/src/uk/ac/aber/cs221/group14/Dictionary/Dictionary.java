/*
* @(#) Dictionary.java 1.1 2018/04/30
*
* Copyright (c) 2018 Aberystwyth University.
* All rights reserved.
*
*/
package uk.ac.aber.cs221.group14.Dictionary;

/**
 * Interface that defines the inner workings of the Dictionary
 * Defines :
 *
 *  - search method, to check if a word is valid in the dictionary
 *
 *  - add method - used to add the method to the relevant data structure
 *
 * @author neh1 - Neil Houston
 * @version 1.1 Creation of Interface
 *
 */
interface Dictionary {

    /**
     * Searches for the dictionary to check if the word is valid
     *
     * @param name - The word that is being searched for
     * @return if the word valid it returns a true boolean value
     */
    public boolean search(String name);

    /**
     * Add word to the relevant data structure
     * @param name - The word being added
     * @return false if the word is not added
     */
    public boolean add(String name);
}
