/*
 * Pawn.java
 * 
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
 * @author Team Failboat
 * @version 0.3.0
 */
public class Pawn extends Piece {
	private static final long serialVersionUID = 6056720416976755691L;
	private static final int[][] movesWithFirstMove = {{0,2},{0,1},{1,1},{-1,1}};
	private static final int[][] moves = {{0,1},{1,1},{-1,1}};
	
	public Pawn(int uNr, int x, int y, ChessColor color) throws IllegalArgumentException {
		super(uNr, x, y, color, 1, true);
	}
	
	public Pawn(int uNr, int x, int y, ChessColor color, boolean initialPosition) throws IllegalArgumentException {
		super(uNr, x, y, color, 1, initialPosition);
	}



	/* (non-Javadoc)
	 * @see pieces.Piece#getMoves()
	 */
	@Override
	public int[][] getMoves() {
		
		/*
		 * this way the function will enter the if if the pawn has moved
		 * (which it most likely will have), thus reducing the amount of time checking things since
		 * it will only check the y-coordinate and then jump out.
		 */
		if (!initialPosition){
			return moves;
		}return movesWithFirstMove;
	}

	@Override
	public int[][] getMovableDirections() {
		return null;
	}
	
	@Override
	public void setYcoord(int ycoord) throws IllegalArgumentException {
		super.setYcoord(ycoord);
		initialPosition = false;
	}
}
