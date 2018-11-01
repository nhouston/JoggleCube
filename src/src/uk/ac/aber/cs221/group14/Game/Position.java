/*
 * @(#) Position.java 1.2 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.Game;

/**
 * Class that creates the Positions for the grid
 *
 * @author Nelson Almedia - nea2
 *
 * @version 1.1 Initial development
 * @version 1.2 Completion of class
 */
public class Position {

public final int x;
public final int y;
public final int z;

/**
 * Inner class that assigns x,y & z values
 *
 * @param side what side your on
 * @param col the y column
 * @param row the z row
 */
public Position(int side, int col, int row) {
    this.x = side; this.y = col; this.z = row;
}

}
