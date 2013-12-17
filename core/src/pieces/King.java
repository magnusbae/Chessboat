/*
 * King.java 
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

/**
 * @author Darknight
 *
 */
public class King extends Piece implements Castleable {
	private static final long serialVersionUID = -5687359656488433061L;
	private static final int[][] moves = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
	
	public King(int uNr, int x, int y, ChessColor color) throws IllegalArgumentException {
		super(uNr, x, y, color, 4, true);
	}
	
	public King(int uNr, int x, int y, ChessColor color, boolean initialPosition) throws IllegalArgumentException {
		super(uNr, x, y, color, 4, initialPosition);
	}



	/* (non-Javadoc)
	 * @see pieces.Piece#getMovableDirections()
	 */
	@Override
	public int[][] getMovableDirections() {
		return null;
	}

	/* (non-Javadoc)
	 * @see pieces.Piece#getMoves()
	 */
	@Override
	public int[][] getMoves() {
		return moves;
	}

	@Override
	public boolean canCastle() {
		return initialPosition;
	}
	
	@Override
	public void setXcoord(int xcoord) throws IllegalArgumentException {
		super.setXcoord(xcoord);
		super.setInitialPosition(false); //Both setY and setX is called when moving a piece, hence only overriding one.
	}

}
