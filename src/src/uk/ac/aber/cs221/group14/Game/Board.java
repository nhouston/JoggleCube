/*
 * @(#) Board.java 1.8 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.Game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * Class that creates the board for the game.
 *
 * Used to deselect letters, show the selected side, hightlight the positions on the grid
 *
 * @author Neil Houston - neh1
 * @author Nelson Almedia - nea2
 *
 * @version 1.1 Initial development
 * @version 1.2 Method structures added
 * @version 1.3 deselectAllLetters, showSelectedSide implemented
 * @version 1.4 Hightlight Positions method implmented - hardcoded to make sure highlighted positions are correct
 * @version 1.5 Save method fully implemented
 * @version 1.6 Load method fully implemented
 * @version 1.7 Scoring Save method added to save scores to a separate file
 * @version 1.8 Java Runnable Thread added and code refactoring completed
 */

public class Board implements IBoard, Runnable {

public int GRID_SIZE = 3;
public Box[][][] rect = new Box[GRID_SIZE][GRID_SIZE][GRID_SIZE];
private ArrayList<Position> adjacentPos = new ArrayList<>(8);
public int currentSide;
public Box lastClickedBox;

private String name;
public int score;
private String letters;

    /**
     * Method to deselect all the letters in the grid
     *
     * Will be used when a user wants to cancel a word, to remove the colors of the highlighting added
     * by highlight positions
     */

    @Override
public void deselectAllLetters() {
    for (int x = 0; x < GRID_SIZE; x++) {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int z = 0; z < GRID_SIZE; z++) {
                rect[x][y][z].getRect().setFill(Color.WHEAT);
            }
        }
    }
}

    /**
     * Method to show the selected side.
     *
     * Shows the currently selected grid face.
     *
     * Sets the currently selected grid faces border to be Green - shows it is selected
     * Otherwise if the grid face is not selected, sets the face border to black
     */

@Override
public void showSelectedSide() {
/*    for (int x = 0; x < GRID_SIZE; ++x) {
        if (x == currentSide) {//set border to green
            for (int y = 0; y < GRID_SIZE; ++y) {
                for (int z = 0; z < GRID_SIZE; ++z) {
                    rect[x][y][z].getRect().setStroke(Color.LIMEGREEN);
                }
            }
        } else {
            //not current side set border to black
            for (int y = 0; y < GRID_SIZE; ++y) {
                for (int z = 0; z < GRID_SIZE; ++z) {
                    rect[x][y][z].getRect().setStroke(Color.BLACK);
                }
            }
        }
    }*/
}

    /**
     * Used to save scores of the user which will later be used to display the highscores
     *
     * Saves the scores and the users' name into a separate .txt file - appends to the end of the file, does not overwrite
     *
     * @param username - users name to saved
     * @param score - score from game saved, becomes the highscore for that game
     * @param SCORES - name of the .txt file
     */
    public void scoreSave(String username, int score, String SCORES) {
    StringBuilder scores = new StringBuilder();scores.append(score).append("  ");scores.append(username);


    FileWriter fw = null; PrintWriter pw = null; try {
        fw = new FileWriter("src/uk/ac/aber/cs221/group14/HighscoreFile/SCORES.txt", true); pw = new PrintWriter(fw);

        pw.write(scores.toString()); pw.write("\n"); pw.close(); fw.close();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

    /**
     *
     * Saves the current game that is currently being played
     *
     * @param username - name of the user
     * @param filename - filename to be saved
     * @param score - game score to be saved
     * @throws IOException - if save location cannot be found
     */

@Override
public void save(String username, String filename, int score) {

    StringBuilder fileContent = new StringBuilder(); fileContent.append(username);
    fileContent.append(System.getProperty("line.separator")); fileContent.append(score);
    fileContent.append(System.getProperty("line.separator")); String temp;

    for (int x = 0; x < GRID_SIZE; ++x) {
        for (int y = 0; y < GRID_SIZE; ++y) {
            for (int z = 0; z < GRID_SIZE; ++z) {
                //access every box, append to the stringbuilder
                temp = rect[x][y][z].getString(); if (temp.length() > 1) {
                    fileContent.append("q");
                } else {
                    fileContent.append(temp);
                }
            }
        }
    }

    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/uk/ac/aber/cs221/group14/saves/" + filename), "utf-8"))) {
        writer.write(fileContent.toString()); writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     *
     * Load a game that has been selected to be played, method is called and
     * takes the string file name from the choose load file scene
     *
     * @param fn - name of game file to load
     * @return - letters from the game file
     * @throws FileNotFoundException file could not be found in the saves location
     */
@Override
public String[] load(String fn) throws FileNotFoundException {
    String []fileData = new String [3];
    try {
        Scanner infile = new Scanner(new FileReader("src/uk/ac/aber/cs221/group14/saves/" + fn));
        //read in each line as one string
        while (infile.hasNextLine()) {
            infile.useDelimiter("\r?\n|\r"); fileData[2] = infile.next(); // read in number of frames
            fileData[0] = infile.next(); // read in number of rows contained within a frame
            fileData[1] = infile.next();
        } infile.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return fileData;
}

    /**
     * Method used to highlight the positions on the grid.
     * Uses the currently clicked position for reference and highlights/displays the available adjacent positions available
     * to that letter
     */
    @Override
public void highlightPositions() {
    for (Position removeHighlighted : adjacentPos) {
        rect[removeHighlighted.x][removeHighlighted.y][removeHighlighted.z].getRect().setFill(Color.WHEAT);
    }
    //empty the list
    adjacentPos.clear();

    Position p = ((Position) lastClickedBox.getStackPane().getUserData()); int col = p.z; int row = p.x; int side = p.y;
    //System.err.println("Side is: " + side + " Row is : " + row + " Column is: " + col);

    switch (side) {
        //top plane highlight top and mid
        case 0:
            switch (col) {
                case 0:
                    switch (row) {
                        //top left
                        case 0:
                            adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 1));
                            //mid plane
                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1));

                            break; case 1:
                            adjacentPos.add(new Position(0, 0, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 0, 2));

                            //mid cube

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 0, 2)); break;
                        //btm left
                        case 2:
                            adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 1, 2));

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 0, 2)); break;
                    } break; case 1:
                    switch (row) {
                        //top mid
                        case 0:
                            adjacentPos.add(new Position(0, 0, 0)); adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(1, 0, 0));

                            adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); break;
                        //mid mid
                        case 1:
                            adjacentPos.add(new Position(0, 0, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(0, 0, 2)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 2, 2));

                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 0, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 2)); break;
                        //btm mid
                        case 2:
                            adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(0, 0, 2)); adjacentPos.add(new Position(0, 2, 2));

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 0, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 2));

                            break;
                    } break; case 2:
                    switch (row) {
                        //top right
                        case 0:
                            adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1));

                            adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); break;
                        //mid right
                        case 1:
                            adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 2)); adjacentPos.add(new Position(0, 1, 2));

                            adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 1)); break;
                        //btm right
                        case 2:
                            adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 2, 1));

                            adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 2, 2)); break;
                    } break;
            } break;
        //mid plane - highlight mid, top and btm
        case 1:
            switch (col) {
                case 0:
                    switch (row) {
                        //top left
                        case 0:
                            //highlight
                            //side - col -row
                            //top plane
                            adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 0, 0));
                            //mid plane
                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1));

                            adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 0, 0));
                            //0:1
                            //1:0 and 1:1
                            break;
                        //mid left
                        //highlight
                        case 1:
                            adjacentPos.add(new Position(0, 0, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 0, 2)); adjacentPos.add(new Position(0, 0, 1));

                            //mid cube
                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 0, 2));

                            adjacentPos.add(new Position(2, 0, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 0, 2)); adjacentPos.add(new Position(2, 0, 1)); break;
                        //btm left
                        case 2:
                            adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 0, 2));

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 1, 2));

                            adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 0, 2));

                            break;
                    } break; case 1:
                    switch (row) {
                        //top mid
                        case 0:
                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1));

                            adjacentPos.add(new Position(0, 0, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1));

                            adjacentPos.add(new Position(2, 0, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1)); break;
                        //mid mid
                        case 1:
                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 0, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 2));

                            adjacentPos.add(new Position(0, 0, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(0, 0, 2)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 2, 2));

                            adjacentPos.add(new Position(2, 0, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1)); adjacentPos.add(new Position(2, 0, 2)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 2, 2)); break;
                        //btm mid
                        case 2:
                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 0, 2)); adjacentPos.add(new Position(1, 2, 2));

                            adjacentPos.add(new Position(0, 0, 1)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(0, 0, 2)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 2, 2));

                            adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1)); adjacentPos.add(new Position(2, 0, 2)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 2, 2));

                            break;
                    } break; case 2:
                    switch (row) {
                        //top right
                        case 0:
                            adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1));

                            adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 1));

                            adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1)); break;
                        //mid right
                        case 1:
                            adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 2)); adjacentPos.add(new Position(1, 1, 2));

                            adjacentPos.add(new Position(0, 2, 0)); adjacentPos.add(new Position(0, 1, 0)); adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 2, 2)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 2)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 2, 1)); break;
                        //btm right
                        case 2:
                            adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 1));

                            adjacentPos.add(new Position(0, 1, 1)); adjacentPos.add(new Position(0, 1, 2)); adjacentPos.add(new Position(0, 2, 1)); adjacentPos.add(new Position(0, 2, 2));

                            adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 2, 1)); adjacentPos.add(new Position(2, 2, 2)); break;
                    } break;
            } break;
        //bottom plane - same as top plane
        case 2:
            switch (col) {
                case 0:
                    switch (row) {
                        //top left
                        case 0:
                            //highlight
                            //top plane
                            adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 1));
                            //mid plane
                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1)); break;
                        //mid left
                        //highlight
                        case 1:
                            adjacentPos.add(new Position(2, 0, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 0, 2));

                            //mid cube

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 0, 2)); break;
                        //btm left
                        case 2:
                            adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 1, 2));

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 0, 2)); break;
                    } break; case 1:
                    switch (row) {
                        //top mid
                        case 0:
                            adjacentPos.add(new Position(2, 0, 0)); adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1));

                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); break;
                        //mid mid
                        case 1:
                            adjacentPos.add(new Position(2, 0, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 2, 1)); adjacentPos.add(new Position(2, 0, 2)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 2, 2));

                            adjacentPos.add(new Position(1, 0, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 0, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 2)); break;
                        //btm mid
                        case 2:
                            adjacentPos.add(new Position(2, 0, 1)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1)); adjacentPos.add(new Position(2, 0, 2)); adjacentPos.add(new Position(2, 2, 2));

                            adjacentPos.add(new Position(1, 0, 1)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 0, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 2));

                            break;
                    } break; case 2:
                    switch (row) {
                        //top right
                        case 0:
                            adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 1));

                            adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 1)); break;
                        //mid right
                        case 1:
                            adjacentPos.add(new Position(2, 2, 0)); adjacentPos.add(new Position(2, 1, 0)); adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 2, 2)); adjacentPos.add(new Position(2, 1, 2));

                            adjacentPos.add(new Position(1, 2, 0)); adjacentPos.add(new Position(1, 1, 0)); adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 2, 2)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 1)); break;
                        //btm right
                        case 2:
                            adjacentPos.add(new Position(2, 1, 1)); adjacentPos.add(new Position(2, 1, 2)); adjacentPos.add(new Position(2, 2, 1));

                            adjacentPos.add(new Position(1, 1, 1)); adjacentPos.add(new Position(1, 1, 2)); adjacentPos.add(new Position(1, 2, 1)); adjacentPos.add(new Position(1, 2, 2)); break;
                    } break;
            } break;
    }

    //highlight
    for (Position posit : adjacentPos) {
        Rectangle r = rect[posit.x][posit.y][posit.z].getRect(); if (r.getFill() == Color.WHEAT) {
            r.setFill(Color.PINK);
        }
    }
}

@Override
public void run() {
    try {
        Thread.sleep(500L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

public int GRID_SIZE() {
    return 3;
}
}

