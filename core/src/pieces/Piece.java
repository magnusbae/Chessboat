/*
 * Piece.java
 */

/* Copyright information:
 * 
 *  Copyright 2010 Failboat Productions. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY FAILBOAT PRODUCTIONS ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL FAILBOAT PRODUCTIONS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Failboat Productions.
 */

package pieces;

import java.io.Serializable;

/**
 * @author Team Failboat
 * @version 0.2.0
 *
 * Type of piece is determined by the subclass. Ie. Pawn, Knight, Bishop, etc.
 * Each piece have to have a unique identifier (int) between 0 and 31. 
 */
public abstract class Piece implements Serializable, Castleable {
	private static final long serialVersionUID = -5791875789669890106L;
	
	private final int piecenr; 		// 0 til 31, om vi har behov for unik identifikasjon. Hvitt lag: 0-15.
								// Google translate says: 0 to 31, if we have a need for unique identification. White team: 0-15.
	private int xcoord, ycoord; // location is everything!
	private ChessColor color;
	private final int value;
	protected boolean initialPosition;
	
	public enum ChessColor{
		BLACK, WHITE;
		
		public String toString(){
			return super.toString().charAt(0) + super.toString().substring(1).toLowerCase();
		}
	}
	

	/**
	 * Super constructor
	 * @param uNr - Integer between 0 and 31
	 * @param x - Integer between 0 and 8
	 * @param y - Integer between 0 and 8
	 * @throws IllegalArgumentException If coordinates or identifier is out of bounds.
	 */
	public Piece(int uNr, int x, int y, ChessColor color, int value) throws IllegalArgumentException{
		String uNrIn = "Unique identifier must be between 0 and 31. Was " + uNr;
		if(uNr < 0 || uNr > 31) throw new IllegalArgumentException(uNrIn);
		if(x < 0 || y < 0 || x > 8 || y > 8) throw new IllegalArgumentException("Coordinates out of bounds.");
		piecenr = uNr;
		xcoord = x;
		ycoord = y;
		this.color = color;
		this.value = value;
		
	}
	
	/**
	 * Super constructor
	 * @param uNr - Integer between 0 and 31
	 * @param x - Integer between 0 and 8
	 * @param y - Integer between 0 and 8
	 * @throws IllegalArgumentException If coordinates or identifier is out of bounds.
	 */
	public Piece(int uNr, int x, int y, ChessColor color, int value, boolean initialPosition) throws IllegalArgumentException{
		String uNrIn = "Unique identifier must be between 0 and 31. Was " + uNr;
		if(uNr < 0 || uNr > 31) throw new IllegalArgumentException(uNrIn);
		if(x < 0 || y < 0 || x > 8 || y > 8) throw new IllegalArgumentException("Coordinates out of bounds.");
		piecenr = uNr;
		xcoord = x;
		ycoord = y;
		this.color = color;
		this.value = value;
		this.initialPosition = initialPosition;
	}
	
	public void setInitialPosition(boolean state){
		initialPosition = state;
	}
	
	public boolean isInitialPosition(){
		return initialPosition;
	}
	
	/**
	 * 
	 * @param o Piece
	 * @return True if class, color and piecenr are the same.
	 */
	public boolean equals(Object obj){
		if (obj instanceof Piece){
			Piece o = (Piece) obj;
			if(o.getClass().equals(getClass()) && o.getColor().equals(getColor()) && o.getPiecenr() == piecenr){ 
				return true; 
			}
		}
		return false;
	}

	/**
	 * @return The unique integer identifying the piece. 
	 */
	public int getPiecenr() {
		return piecenr;
	}
	
	/**
	 * @return The x coordinate for the piece (Integer)
	 */
	public int getXcoord() {
		return xcoord;
	}
	
	/**
	 * @return The y coordinate for the piece (Integer)
	 */
	public int getYcoord() {
		return ycoord;
	}
	
	/**
	 * 
	 * @return The color of the given piece. (Black or white)
	 */
	public ChessColor getColor(){
		return color;
	}
	
	
	/**
	 * 
	 * @return The integer value of the piece
	 */
	public int getValue(){
		return value;
	}
	
	public boolean canCastle(){
		return false;
	}
	
	/**
	 * Call to get a list of moves a piece can perform
	 * This function should only be used for pieces that can not travel unlimited lengt along a path, ie. pawns, kings, knights.
	 * Other functions should return null.
	 * 
	 * @return A two dimensional int table with all the legal moves for the piece
	 * First dimension is the move (dynamic length depending on type of piece), the second should be of length two, and represent the x- and y-coordinate
	 * respectively.
	 */
	abstract public int[][] getMoves(); 
	/*
	 * Moves should be absolute. Game engine should deal with pawns, kings, and knights.
	 */
	
	/**
	 * Returns some kind of list that indicates the directions a piece can move in
	 * @return int[][]. Table indicating the directions the piece can move in.
	 * Format should be: int[direction no.][x or y]
	 * see comment below for examples. 
	 */
	abstract public int[][] getMovableDirections();
	/* 
	 * int[direction no.][x or y]
	 * usage: 	directions[0][0] = 0;
	 * 			directions[0][1] = 1;
	 * This piece can only move in the x direction for this move. 
	 * Simplified as private static int[][] directions = {{0,1},{1,0}};
	 * table is a class constant for all Towers.
	 */

	/**
	 * @param xcoord - The new X coordinate for the piece.
	 * @throws IllegalArgumentException - If coordinate out of bounds
	 */
	public void setXcoord(int xcoord) throws IllegalArgumentException {
		if (xcoord < 0 || xcoord > 8) throw new IllegalArgumentException("X coordinate out of bounds");
		this.xcoord = xcoord;
	}	

	/**
	 * 
	 * @param ycoord - The new Y coordinate for the piece.
	 * @throws IllegalArgumentException - If coordinate out of bounds
	 */
	public void setYcoord(int ycoord) throws IllegalArgumentException {
		if (ycoord < 0 || ycoord > 8) throw new IllegalArgumentException("Y coordinate out of bounds");
		this.ycoord = ycoord;
	}
}
