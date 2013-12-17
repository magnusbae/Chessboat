/*
 * ChessBoard.java
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

package core;

import java.io.Serializable;
import java.util.ArrayList;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Piece.ChessColor;

/**
 * @author Team Failboat
 * @version 1.0 beta 1
 */
public class ChessBoard implements Serializable{
	private static final long serialVersionUID = -2424162021533017060L;

	private int startNr = 31;
	private final ChessColor BLACK = ChessColor.BLACK;
	private final ChessColor WHITE = ChessColor.WHITE;
	private ChessColor lastTurnWas = BLACK;
	private ChessColor listingMovesForKing = null;	
	
	//           y x
	private Piece[][] board = new Piece[8][8];
	private Piece[][] shadowBoard = new Piece[8][8];
	
	private ArrayList<Piece> whiteGone = new ArrayList<Piece>();
	private ArrayList<Piece> blackGone = new ArrayList<Piece>();
	
	private boolean rules = false;
	private boolean passant = false; //Variable to handle the én passant move.
	
	public class Memento{
		final ChessColor lastTurnWas;
		final ChessColor listingMovesForKing;
		
		final Piece[][] board;
		final Piece[][] shadowBoard;
		
		final ArrayList<Piece> whiteGone;
		final ArrayList<Piece> blackGone;
		
		final boolean rules;
		final boolean passant;
		
		
		/**
		 * @param lastTurnWas
		 * @param listingMovesForKing
		 * @param board
		 * @param shadowBoard
		 * @param whiteGone
		 * @param blackGone
		 * @param rules
		 * @param passant
		 */
		public Memento(ChessColor lastTurnWas, ChessColor listingMovesForKing,
				Piece[][] board, Piece[][] shadowBoard,
				ArrayList<Piece> whiteGone, ArrayList<Piece> blackGone,
				boolean rules, boolean passant) {
			this.lastTurnWas = lastTurnWas;
			this.listingMovesForKing = listingMovesForKing;
			this.board = new Piece[8][8];
			for (Piece pieces[] : board) {
				for (Piece piece : pieces) {
					if (piece != null){
						this.board[piece.getYcoord()][piece.getXcoord()] 
						     = copyPiece(piece);
					}
				}
			}
			this.shadowBoard = new Piece[8][8];
			for (Piece pieces[] : shadowBoard) {
				for (Piece piece : pieces) {
					if (piece != null){
						this.shadowBoard[piece.getYcoord()][piece.getXcoord()] 
						                                    = copyPiece(piece);
					}
				}
			}
			this.whiteGone = new ArrayList<Piece>();
			for (Piece piece : whiteGone) {
				this.whiteGone.add(copyPiece(piece));
			}
			this.blackGone = new ArrayList<Piece>(); 
			for (Piece piece : blackGone) {
				this.blackGone.add(copyPiece(piece));
			}				
			this.rules = rules;
			this.passant = passant;
		}
	}
	
	

	/**
	 * Creates a non-standard game of chess.
	 * @param gameMode 
	 * 0: Default mode
	 * 1: "Locked in": pawns switch side <br /> 
	 * 2: No pawns <br />
	 * 3: "Haxx0r-mode": turns off some rules.
	 */
	public ChessBoard(int gameMode){
		switch(gameMode){
		

		/*
		 * Creates a standard set of chess.
		 */
		case 0:
			
			/*
			 * Creating white pieces.
			 * NOTE: The white are on top due to compatibility with GUI.
			 * Should not cause problems for AI.
			 */
			board[7] = createOfficers(7, ChessColor.WHITE);
			board[6] = createPawns(6, ChessColor.WHITE);
			
			//creating black pieces
			board[1] = createPawns(1, ChessColor.BLACK);

			
			board[0] = createOfficers(0, ChessColor.BLACK);
			startNr = 31;
//			movePiece(getPiece(3, 7), 4, 4);
//			movePiece(getPiece(2, 7), 3, 4);
//			movePiece(getPiece(1, 7), 2, 4);
//			movePiece(getPiece(2, 1), 3, 6);
			rules = true;
//			lastTurnWas = WHITE;
//			board[1] = new Piece[8];
//			board[6] = new Piece[8];
		
			/*
			 * "Locked in"
			 */
			break;
			
		case 1: 
			board[7] = createOfficers(7, ChessColor.WHITE);
			
			/* Creating white pawns in front of black officers. */
			board[1] = createPawns(1, ChessColor.WHITE);
			
			/* Creating black pieces. Black pawns in front of white officers */
			board[6] = createPawns(6, ChessColor.BLACK);
			board[0] = createOfficers(0, ChessColor.BLACK);
			rules = true;
			break;
			
		/*
		 * "No pawns allowed"
		 */
		case 2:
			board[7] = createOfficers(7, ChessColor.WHITE);
			board[0] = createOfficers(0, ChessColor.BLACK);
			rules = true;
			break;
		
		/*
		 * "Haxx0r-mode" - Turns of some of the rules. Cheat if you can.
		 */
		case 3: 
			
			/* Creating a standard set of chess */
			board[7] = createOfficers(7, ChessColor.WHITE);
			board[6] = createPawns(6, ChessColor.WHITE);
			board[1] = createPawns(1, ChessColor.BLACK);
			board[0] = createOfficers(0, ChessColor.BLACK);
			rules = false; //but the rules are turned off.
			break;
		default: 
			break;
		}
		startNr = 31;
	}
	
	
	/**
	 * Saves the state of the board to a memento object
	 * @return Memento object for caretaker.
	 */
	public Memento saveToMemento(){
		return new Memento(lastTurnWas, listingMovesForKing, board, 
							shadowBoard, whiteGone, blackGone, rules, passant);
	}
	
	/**
	 * Restores the board to a previous mementostate
	 * @param mementoObject The state to restore
	 */
	public void restoreFromMemento(Object mementoObject){
		lastTurnWas = ((Memento)mementoObject).lastTurnWas;
		listingMovesForKing = ((Memento)mementoObject).listingMovesForKing;
		board = ((Memento)mementoObject).board;
		shadowBoard = ((Memento)mementoObject).shadowBoard;
		whiteGone = ((Memento)mementoObject).whiteGone;
		blackGone = ((Memento)mementoObject).blackGone;
		rules = ((Memento)mementoObject).rules;
		passant = ((Memento)mementoObject).passant;
	}
	
	
	/**
	 * Finds a piece given the color and type. Returns the first it finds. Useful for finding kings.
	 * @param color
	 * @param name String defining the type of piece. Returns the first found piece of this type.
	 * @return Piece found 
	 */
	public Piece findPiece(ChessColor color, String name){
		for (Piece pieces[] : board) {
			for (Piece piece : pieces) { //Searches through the entire board.
				if (piece != null && piece.getClass().getSimpleName().equalsIgnoreCase(name.trim())
						&& (piece.getColor() == color)){
					return piece;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Method that shortens the length of a list of moves and returns it
	 * @param movesTable
	 * @param movesFound
	 * @return A shortened list of moves.
	 */
	private int[][] shortenList(int[][] movesTable, int movesFound){
		int[][] temp = new int[movesFound][2];
		for(int i = 0; i < temp.length; i++){
			temp[i][0] = movesTable[i][0];
			temp[i][1] = movesTable[i][1];
		}
		return temp;
	}
	
	
	/**
	 * As requested by Kristian. Don't use it in vain!
	 * @return a reference to the board.
	 */
	@Deprecated
	public Piece[][] getBoard() {
		return board;
	}
	
	
	/**
	 * Get the piece at the given coordinate
	 * @param x
	 * @param y
	 * @return Piece
	 */
	public Piece getPiece(int x, int y){
		return board[y][x];
	}
	
	
	/**
	 * Sets the piece at the coordinates. Overwrites any existing pieces.
	 * WARNING: Don't use this if you don't mean it!!!!!!!!
	 * @param newPiece Piece you want to set at the x and y coordinate of the board
	 * @param x
	 * @param y
	 */
	public void setPiece(Piece newPiece, int x, int y){
		board[y][x] = newPiece;
	}
	
	
	/**
	 * Returns an enum of type ChessColor
	 * @return The color whose turn it is. 
	 */
	public ChessColor getTurn(){
		if(lastTurnWas == ChessColor.BLACK){
			return ChessColor.WHITE;
		}
		return ChessColor.BLACK;
	}
	
	
	/**
	 * Method that makes a copy of a piece. Returns a piece of the corresponding type.
	 * @param piece
	 * @return A deep copy of the given piece.
	 */
	private Piece copyPiece(Piece piece){
		Piece ret = null;
		if (piece instanceof pieces.Pawn){
			ret = new Pawn(piece.getValue(), piece.getXcoord(), piece.getYcoord(),
			    		   piece.getColor(), piece.isInitialPosition()); 
		}else if (piece instanceof pieces.Rook){
			ret = new Rook(piece.getValue(), piece.getXcoord(), piece.getYcoord(),
			    		   piece.getColor(), piece.isInitialPosition()); 
		}else if (piece instanceof pieces.Knight){
			ret = new Knight(piece.getValue(), piece.getXcoord(), piece.getYcoord(),
	 						 piece.getColor(), piece.isInitialPosition()); 
		}else if (piece instanceof pieces.Bishop){
			ret = new Bishop(piece.getValue(), piece.getXcoord(), piece.getYcoord(),
							piece.getColor(), piece.isInitialPosition()); 
		}else if (piece instanceof pieces.Queen){
			ret = new Queen(piece.getValue(), piece.getXcoord(), piece.getYcoord(),
							piece.getColor(), piece.isInitialPosition()); 
		}else {
			ret = new King(piece.getValue(), piece.getXcoord(), piece.getYcoord(),
			    		   piece.getColor(), piece.isInitialPosition()); 
		}
		return ret;
	}
	
	
	/**
	 * Moves the given piece.
	 * @param toMove The piece to be moved. Use the getPiece(x, y) method to get the piece. 
	 * @param toX
	 * @param toY
	 * @return true if the piece is moved. False if it's an illegal move, or the piece doesn't exist.
	 */
	public synchronized boolean movePiece(Piece toMove, int toX, int toY){
		boolean moveOK = false; //For moving king out of checked position.
		boolean passantSetNow = false;
		Piece shadow = null; //Shadow-pawn
		
		if(rules){
			if ((toMove == null) || (toMove.getColor().equals(lastTurnWas))
				 || !(checkIfMoveIsLegal(toMove, toX, toY))){ return false; } //Returns false if calling a move that is not possible.
		}
		
		/* Adds pieces that are to be striked to a table for graphical representation */
		int oldWhiteSize = whiteGone.size();
		int oldBlackSize = blackGone.size();
		
		/*
		 *  Checks if it's an "én passant" move
		 * If so a pawn can strike a pawn that has moved two steps like if it had
		 * just moved one step.
		 */
		if (passant && toMove instanceof pieces.Pawn){
			if ((shadowBoard[toY][toX] != null) && (shadowBoard[toY][toX].getColor() != toMove.getColor())){
				shadow = shadowBoard[toY][toX];			//Allocates the shadow-pawn.
//				shadowBoard[toY][toX] = null; 			//Blanks the passantboard.		Seems to be unessesary.
				
				/* 
				 * Adds 1 for black pieces, subtracts one for white
				 * This makes sure the pieces are removed from the board. 
				 */
				if(shadow.getColor() == BLACK){
					toY += 1;
				}else{
					toY -= 1; 
				}
			}
		}
		
		/* 
		 * The normal procedure of moving pieces off the board and 
		 * into an arraylist containing them
		 */ 
		if(getPiece(toX, toY) != null){
			if(toMove.getColor().equals(BLACK)){
				whiteGone.add(getPiece(toX, toY));
			}else {
				blackGone.add(getPiece(toX, toY));
			}
		}
		
		/* If a shadow-pawn has been striken, returns the move coordinates back to normal */
		if (passant && (shadow != null)){
			if(shadow.getColor() == BLACK){
				toY -= 1;
			}else{
				toY += 1;
			} 
		}
		
		/* If passant (clears the shadowBoard) */
		if (passant){
			shadowBoard = new Piece[8][8];
		}
		
		/* 
		 * If king (and castling move) 
		 * Moves only the rook. The move of the king is done afterwards (as usual (the kings coordinates was parameters.)
		 */
		if ((toMove instanceof pieces.King) && (Math.abs(toMove.getXcoord() - toX) == 2)){ //If the king is moving two x-units.
			if (toMove.getXcoord() - toX > 0){ //For positive X
				board[toY][toX + 1] = getPiece(0, toY);
				board[toY][0] = null;
				
				/* Sets only X coord, y is the same */
				getPiece(toX + 1, toY).setXcoord(toX + 1);
			}else{ // for negative X
				board[toY][toX - 1] = getPiece(7, toY);
				board[toY][7] = null;
				getPiece(toX - 1, toY).setXcoord(toX - 1);
			}
		}
		
		/* If passant move. Makes a duplicate pawn on a shadow board and sets appropriate flags */
		else if((toMove instanceof pieces.Pawn) && (Math.abs(toMove.getYcoord() - toY) > 1)){
			
			/* Copies the new pawn to the correct place on the shadowBoard.  Decrements Y for black, increments for white */
			if (toMove.getColor() == BLACK){
				shadowBoard[toY-1][toX] = new Pawn(toMove.getPiecenr(), toX, (toY-1), toMove.getColor());
			}else {
				shadowBoard[toY+1][toX] = new Pawn(toMove.getPiecenr(), toX, (toY+1), toMove.getColor());
			}
			passant = true;
			passantSetNow = true;
		}
		
		/* Performs the actual move of the piece */
		board[toY][toX] = toMove;
		board[toMove.getYcoord()][toMove.getXcoord()] = null; //Blanks the old location.
		
		/* 
		 * If something somehow goes wrong. Corrects the "mistake" and returns false.
		 * For instance, when the attempted move does not result in the king being out of check
		 * ***********Legacy code*************
		 */
		if (!getPiece(toX, toY).equals(toMove) //If the piece hasn't moved somehow
			|| (((toMove instanceof pieces.King) && (isCheck(toX, toY, toMove.getColor()))) //If the piece is in check after the move and it's a king 
				|| (isCheck(findPiece(toMove.getColor(), "king"))))){ //Or the king is in check after the move
			
			/* If king. Does a better control of wether or not the king is in check */
			if (toMove instanceof pieces.King){
				int oldX = toMove.getXcoord();
				int oldY = toMove.getYcoord();
				toMove.setXcoord(toX); //Tells the piece it has moved
				toMove.setYcoord(toY); //
				if (isCheck(toMove)){ //If in check
					setPiece(toMove, oldX, oldY); //Moves the piece back to it's old coordinates
					toMove.setXcoord(oldX); //Restores coordinates
					toMove.setYcoord(oldY); //
				}else { //if not, the move was OK.
					moveOK = true; 
				}
			}
			
			/* If the king moved from a checked position and it went wrong, or a general error. */
			if (!moveOK){ 
				setPiece(toMove, toMove.getXcoord(), toMove.getYcoord()); //Moves the piece back to it's former location.
				
				/* Removes the striken piece from the list of striken pieces (if a piece was striken) */
				if((toMove.getColor().equals(BLACK))){ //If white pieces striken
					if(whiteGone.size() > oldWhiteSize){
						setPiece((whiteGone.get(whiteGone.size() - 1)), toX, toY);
						whiteGone.remove(whiteGone.size() - 1);
					}else{ 											//If originally a blank field
						setPiece(null, toX, toY); //Nulls the field.
					}
				}else { //if black pieces striken
					if (blackGone.size() > oldBlackSize) {
						setPiece((blackGone.get(blackGone.size() - 1)), toX, toY);
						blackGone.remove(blackGone.size() - 1);
					}else{ 											//If originally a blank field
						setPiece(null, toX, toY);
					}
				}
				return false; //On error we return a false. No explonation. Haven't bothered to give more info. This is not supposed to happen.
			}
		}
		
		/* 
		 * Tells the piece that it has moved.
		 * If a king has been moved out of check, this has already been done, and this step is thus
		 * skipped.
		 */
		if (!moveOK){
			toMove.setXcoord(toX);
			toMove.setYcoord(toY);
		}
		
		/* Removes the passant-pawn striken (if striken), then clears the passant-flag. */
		if (passant && !passantSetNow){
			if(shadow != null){ //Clears if striken
				if (shadow.getColor() == BLACK){ //if black
					board[toY+1][toX] = null;
				}else{ //if white
					board[toY-1][toX] = null;
				}
			}
			passant = false; //clears the passant-flag.
		}
		
		/* changes turn */
		if(toMove.getColor().equals(BLACK)){
			lastTurnWas = BLACK;
		}else {
			lastTurnWas = WHITE;
		}

		/* Clears the isCheck-blocking flag: */
		listingMovesForKing = null;
		return true;
	}
	
	
	/**
	 * Method that checks if a move to the given coordinates for a piece is a legal move.
	 * @param p The piece to be moved
	 * @param x The new X-location
	 * @param y The new Y-location
	 * @return True if the piece can move here.
	 */
	private boolean checkIfMoveIsLegal(Piece p, int x, int y){
		int[][] movesList = listMoves(p);
		for (int[] move : movesList) {
			if(move[0] == x && move[1] == y){ return true; }
		}
		return false;
	}
	
	
	/**
	 * General method for checking if a possible move is legal. Checks if the move results in a check.
	 * This method is called by the {@link core.ChessBoard#listMoves(Piece) listMoves} function.
	 * @param p
	 * @param ret
	 * @param xCoord
	 * @param yCoord
	 * @param moveX
	 * @param moveY
	 * @param movesSoFar
	 * @return null if not, or the ret[][] array with added coordinates if OK.
	 */
	private int[][] checkMoveIsPossible(Piece p, int[][] ret, int xCoord, int yCoord, 
										int moveX, int moveY, int movesSoFar){
		int legalMovesFound = movesSoFar;					//setting original state
		Piece[][] tempBoard = new Piece[8][8];				//Creating a temporary board for keeping pieces.
		tempBoard[yCoord][xCoord] = getPiece(xCoord, yCoord); //Grabs the piece to be moved.
		setPiece(null, xCoord, yCoord);						//Nulls the original position
		
		/* Checks if there's a piece at coordinates testing for */
		if (getPiece(moveX, moveY) != null){				
			tempBoard[moveY][moveX] = getPiece(moveX, moveY); //moves this away from the board as well.
		}
		
		/* Sets the piece to move to it's new coordinates */
		setPiece(tempBoard[yCoord][xCoord], moveX, moveY);
		
		/* Checks if the king is in check after this move */
		if(!(isCheck(findPiece(p.getColor(), "king")))){
			ret[legalMovesFound][0] = moveX;		//If not in check this is a good time to add the moves to the list of good moves
			ret[legalMovesFound++][1] = moveY;		//Post-incrementing the counter. 
		}
		
		/* If there was a piece where we tried to see if the new piece would fit we should move it back */
		if (tempBoard[moveY][moveX] != null){
			setPiece(tempBoard[moveY][moveX], moveX, moveY);
			tempBoard[moveY][moveX] = null;
		}else{
			setPiece(null, moveX, moveY); // If not, we should null the field.
		}
		
		/* Then we should move the piece back where it was :) */
		setPiece(tempBoard[yCoord][xCoord], xCoord, yCoord);
		tempBoard[yCoord][xCoord] = null; //Blank it from the tempboard just in case.
		if (legalMovesFound == movesSoFar){ //If the move wasn't a good one we just return null
			return null;
		}
		return ret; //If the move was OK.
	}
	
	
	/**
	 * General method for checking if a possible move is legal. Checks if the move results in a check.
	 * This method is called by the {@link core.ChessBoard#listDirections(Piece) listDirections}  function.
	 * @param p
	 * @param xCoord
	 * @param yCoord
	 * @param moveX
	 * @param moveY
	 * @param movesSoFar
	 * @return An Integer array with length two, or null.
	 */
	private Integer[] checkMoveIsPossible(Piece p, int xCoord, int yCoord, 
			int moveX, int moveY, int movesSoFar){
		/*
		 * See the int[][] version above for comments (int[][] checkMoveIsPossible())
		 */
		
		int legalMovesFound = movesSoFar;
		Integer[] ret = new Integer[2];
		Piece[][] tempBoard = new Piece[8][8]; //For containing pieces not on the board.
		
		tempBoard[yCoord][xCoord] = getPiece(xCoord, yCoord);
		setPiece(null, xCoord, yCoord);
		if (getPiece(moveX, moveY) != null){
			tempBoard[moveY][moveX] = getPiece(moveX, moveY);
		}
		setPiece(tempBoard[yCoord][xCoord], moveX, moveY);
		if(!(isCheck(findPiece(p.getColor(), "king")))){
			ret[0] = moveX;
			ret[1] = moveY;
			legalMovesFound++;
		}
		if (tempBoard[moveY][moveX] != null){
			setPiece(tempBoard[moveY][moveX], moveX, moveY);
			tempBoard[moveY][moveX] = null;
		}else{
			setPiece(null, moveX, moveY);
		}
		setPiece(tempBoard[yCoord][xCoord], xCoord, yCoord);
		tempBoard[yCoord][xCoord] = null;
		if (legalMovesFound == movesSoFar){
			return null;
		}
		return ret;
		}

	
	/**
	 * This function finds the positions a piece can move to according to the piece's position on the board.
	 * Illegal and out-of-bounds moves are not returned. Works with all types of pieces.
	 * @param p The piece you want to move.
	 * @return A two-dimensional integer array containing the moves the brick can move to.
	 */
	public int[][] listMoves(Piece p){
		/*
		 * This method only works with some of the pieces, but automaticly calls the correct method if.
		 */
		
		if(p == null){ return null; }
		if(p.getMoves() == null) { return listDirections(p); } //Depends on the type of piece. 
		int xCoord = p.getXcoord();
		int yCoord = p.getYcoord();
		int legalMovesFound = 0;
		final int[][] movesGotten = p.getMoves(); //Get the relative moves from the piece.
		final int[][] moves = new int[movesGotten.length][2]; //Creates a table for copying the moves
		int[][] ret = new int[moves.length][2]; 			  //Creates a table for returning the legal moves found
		
		/* Copies the moves to a new array. This is to make sure that we avoid bugs. We're not using arraycopy due to the
		 * nature of the two-dimensional array. This way we know that we have control.
		 */
		for(int i = 0; i < moves.length; i++){
			moves[i][0] = movesGotten[i][0];
			moves[i][1] = movesGotten[i][1];
		}
		
		/* Now we start running through the moves in table */
		for(int move[] : moves){

			/* Inverts moves to work with white pieces (the board is upside down coordinate-wise) */
			if(p.getColor().equals(ChessColor.WHITE)){
				move[0] *= -1;
				move[1] *= -1;
			}
			
			/* Sets the absolute coordinates */
			int moveX = xCoord + move[0];
			int moveY = yCoord + move[1];
			
			/* For all other than pawns. Due the to the pawn-nature of going diagonally when striking we'll handle those afterwards */
			if (!(p instanceof pieces.Pawn)){
				if ((moveX >= 0) && (moveX <= 7) && (moveY >= 0) && (moveY <= 7)){ //No point in bothering to pick up pieces outside of the board...
					if (getPiece(moveX, moveY) == null || !(p.getColor().equals(getPiece(moveX, moveY).getColor()))){ //We can't stack pieces of the same color, and we can't strike them.
						if (p instanceof pieces.King){ //I found myself a king. I want to threat him specially! :)
							if (!(isCheck(moveX, moveY, p.getColor()))){ 	//Is he in check?
								ret[legalMovesFound][0] = moveX;			//No? That's good!
								ret[legalMovesFound++][1] = moveY;			//Post-incrementing the counter.
							}
						}else { //Not a pawn, not a king
							
							/* Calls the general controlmethod. */
							int[][] temp = checkMoveIsPossible(p, ret, xCoord, yCoord, moveX, moveY, legalMovesFound);
							if(temp != null){ // If something came back.
								ret = temp; //switch the arrays.
								legalMovesFound++; //increment the counter. New round!
							}
						}
					}
				}
			} else { // If pawn **************************** Pawns are exceptional! 
				if ((moveX >= 0) && (moveX <= 7) && (moveY >= 0) && (moveY <= 7)){ //Keepin' it real
					if(moveX != xCoord){ //If he want's to move diagonally.
						
						/* Checking if there's a piece of opposing color where the pawn wants to go */
						if(getPiece(moveX, moveY) != null && !(getPiece(moveX, moveY).getColor().equals(p.getColor()))){ 
							
							/* Calls the general check */
							int[][] temp = checkMoveIsPossible(p, ret, xCoord, yCoord, moveX, moveY, legalMovesFound);
							if(temp != null){ 	//If something came back
								ret = temp; 	//switch the arrays
								legalMovesFound++;
							}
						}
						
						/* If there wasn't something there, but the passant flag is set */
						else if ((passant) && (shadowBoard[moveY][moveX] != null) // checks the shadowboard.
								  && !(shadowBoard[moveY][moveX].getColor().equals(p.getColor()))){ //in case that the shadowboard hasn't been cleared properly, checks that it is of opposing color
							
							/* Calling the general check method */
							int[][] temp = checkMoveIsPossible(p, ret, xCoord, yCoord, moveX, moveY, legalMovesFound);
							if(temp != null){ 	//If something came back
								ret = temp; 	//switch the arrays
								legalMovesFound++;
							}
						}
					}
					
					/* If the pawn wants to go forward */
					else if(getPiece(moveX, moveY) == null){ //The space is not occupied.
						
						/* Complicated if:
						 * If the move is one step - OK. If it's two steps and the one-step is okay.
						 */
						if(((Math.abs(moveY - yCoord) == 1)) || ((Math.abs(moveY - yCoord) == 2) 
								&& (((p.getColor() == BLACK) && (getPiece(xCoord, yCoord + 1) == null)) 
									|| ((p.getColor() == WHITE) && (getPiece(xCoord, yCoord - 1) == null))))){
							
							/* Calling the general check method */ 
							int[][] temp = checkMoveIsPossible(p, ret, xCoord, yCoord, moveX, moveY, legalMovesFound);
							if(temp != null){ 	//If something came back
								ret = temp; 	//switch the arrays
								legalMovesFound++;
							}
						}
					}
				}
			}
		}
		
		/* If it's a king and it thinks it can castle we have to check if that's possible */
		if ((p instanceof pieces.King) && (p.canCastle())) { 
			return checkCastle(p, ret, legalMovesFound); 
		}
		
		/* If no possible moves found we return null */
		if(legalMovesFound == 0) return null;
		
		/* Else we create a table of exact length and returns that instead */
		return shortenList(ret, legalMovesFound);
	}
	
	/**
	 * Private function that does the other pieces.
	 * @param p The piece you want to move.
	 * @return A two-dimensional integer array containing the moves the brick can move to.
	 */
	private int[][] listDirections(Piece p){
		int xCoord = p.getXcoord();
		int yCoord = p.getYcoord();
		final int[][] moves = p.getMovableDirections(); //grabs the directions. 
		ArrayList<Integer[]> ret = new ArrayList<Integer[]>(); //More practical with an arraylist than an array here.
		
		/* Goes through the possible axes. */
		for(int move[] : moves){
			
			/* sets up for the first run */
			int moveX = xCoord + move[0]; 
			int moveY = yCoord + move[1];
			while((moveX <= 7) && (moveX >= 0) && (moveY <= 7) && (moveY >= 0)){ //Keepin' it inside the board.
				
				/* If a blank field, or a field of opposing color */
				if((getPiece(moveX, moveY) == null) || !(getPiece(moveX, moveY).getColor().equals(p.getColor()))){ 
					
					/* Calls the general method for checking */
					Integer[] temp = checkMoveIsPossible(p,xCoord, yCoord, moveX, moveY, ret.size());
					if(temp != null){ 	//If something came back
						ret.add(temp);	//we add it to the arraylist
					}
				}
				if(getPiece(moveX, moveY) != null){ break; } //If we met a piece along the way we can't jump it, hence we call break;
				
				/* But if we didn't break, we should set up for the next round. */
				moveX += move[0];
				moveY += move[1];
			}
			
			/* 
			 * Now that we hit the end of the board or another piece in one direction it's time to switch.
			 * Setting moveX and Y for moving in the other direction 
			 */
			moveX = xCoord - move[0];
			moveY = yCoord - move[1];
			while((moveX <= 7) && (moveX >= 0) && (moveY <= 7) && (moveY >= 0)){ //Setting bounds
				
				/* If a blank field, or a field of opposing color */
				if((getPiece(moveX, moveY) == null) || !(getPiece(moveX, moveY).getColor().equals(p.getColor()))){
					
					/* Calls the general method for checking */
					Integer[] temp = checkMoveIsPossible(p,xCoord, yCoord, moveX, moveY, ret.size());
					if(temp != null){ 	//If something came back
						ret.add(temp);	//we add it to the arraylist
					}
				}
				if(getPiece(moveX, moveY) != null){ break; } //Break if met a piece. 
				
				/* Or else we continue along the same path */
				moveX -= move[0]; 
				moveY -= move[1];
			}
		}
		
		/* All done. Now we check the size. */
		if(ret.size() == 0){ return null; } //null if 0.
		
		/* Else we create a two-dimensional integer-array and return that instead of the arraylist */
		int[][] temp = new int[ret.size()][2];
		for(int i = 0; i < ret.size(); i++){
			temp[i][0] = ret.get(i)[0].intValue();
			temp[i][1] = ret.get(i)[1].intValue();
		}
		return temp; //The end
	}
		
	
	/**
	 * Create pawns for the given row. Must be used in conjunction with the createOfficers() method.
	 * Numbering relies on the createOfficers() method being called first, then two calls to createPawns() have to be made for correct numbering.
	 * @param row the y coordinate.
	 * @return Table of pawns.
	 */
	private Piece[] createPawns(int row, ChessColor color){
		Piece[] pawns = new Piece[8];
	
		/* Assigning from left to right */
		for (int i = 0; i < pawns.length; i++){
			pawns[i] = new Pawn(startNr--, i, row, color);
		}
		return pawns;
	}
	
	
	/**
	 * Creates officers. Must be used in conjunction with the createPawn() method.
	 * Numbering relies on the createPawn() method being called twice between the two calls to createOfficers
	 * @param row
	 * @param color
	 * @return Table of officers.
	 */
	private Piece[] createOfficers(int row, ChessColor color){
		int x = 0;
		Piece[] officers = new Piece[8];

		/* Assigning from left to right */
		officers[0] = new Rook	(startNr--, x++, row, color);
		officers[1] = new Knight(startNr--, x++, row, color);
		officers[2] = new Bishop(startNr--, x++, row, color);
		officers[3] = new Queen	(startNr--, x++, row, color);
		officers[4] = new King	(startNr--, x++, row, color);
		officers[5] = new Bishop(startNr--, x++, row, color);
		officers[6] = new Knight(startNr--, x++, row, color);		
		officers[7] = new Rook	(startNr--, x++, row, color);
		
		return officers;
	}
	
	/**
	 * Method that returns an ArrayList<Pieces> of white pieces that are eliminated.
	 * @return ArrayList<Pieces> containing white pieces that has been eliminated.
	 */
	public ArrayList<Piece> getWhiteGone(){
		return whiteGone;
	}
	
	/**
	 * Method that returns an ArrayList<Pieces> of black pieces that are eliminated.
	 * @return ArrayList<Pieces> containing black pieces that has been eliminated.
	 */
	public ArrayList<Piece> getBlackGone(){
		return blackGone;
	}
	
	/**
	 * Checks if a king that thinks it can castle actually can. Checks the rooks and looks for a clear path (King can not
	 * be in check during the castling move).
	 * @param king The king in question
	 * @param legalMoves
	 * @param legalMovesFound
	 * @return A two-dimensional integer array containing the moves the brick can move to.
	 */
	private int[][] checkCastle(Piece king, int[][] legalMoves, int legalMovesFound){
		/*
		 * The checkCastle method assumes that the legalMoves list has enough space to store two additional moves.
		 * This is assured because the list in it's first dimension is of size 8, but the number of legal moves found for
		 * a king that can castle is a maximum of 5 due to the king being unable to walk of the board.
		 */

		/*
		 * Checks if the King will be put in check during the castling move
		 * by manually calling ischeck for the kings path across both directions. Here in positive X-direction.
		 */
		if ((!(isCheck(king.getXcoord()+1, king.getYcoord(), king.getColor()))
			 && !(isCheck(king.getXcoord()+2, king.getYcoord(), king.getColor())))) {   
				
			/* Checks positive x-path. If a clear path to the Rook the king can perform the castling... */
			int counter = king.getXcoord() + 1;
			while ((counter >= 0) && (counter <=7)){
				Piece found = null;
				if((found = getPiece(counter, king.getYcoord())) != null){
					if (found.canCastle()){ 			//..., but only if the rook hasn't been moved. (other pieces return false anyways.
						legalMoves[legalMovesFound][0] = king.getXcoord() + 2; 	//Adds the kings castling move to the list of legal moves.
						legalMoves[legalMovesFound++][1] = king.getYcoord();	//Further handling is done within the the movePiece() method.
					}
					break; //No need to continue the while when a piece is found.
				}
				counter++; //Increments the counter.
			}
		}

		/* Checks that the king will not be put in check during the castling move for negative x */
		if ((!(isCheck(king.getXcoord()-1, king.getYcoord(), king.getColor()))
		     && !(isCheck(king.getXcoord()-2, king.getYcoord(), king.getColor())))) {   
			
			/* Checks negative X-path. Same as above */ 
			int counter = king.getXcoord() - 1;
			while ((counter >= 0) && (counter <=7)){
				Piece found = null;
				if((found = getPiece(counter, king.getYcoord())) != null){
					if (found.canCastle()){
						legalMoves[legalMovesFound][0] = king.getXcoord() - 2;
						legalMoves[legalMovesFound++][1] = king.getYcoord();
					}
					break;
				}
				counter--; //Decrements the counter.
			}
		}
		
		/* If no moves found we return null so that we don't get errors when checking for mate */
		if (legalMovesFound == 0){ return null; }
		return shortenList(legalMoves, legalMovesFound); //Shortens the list and returns it.
	}
	
	/**
	 * Checks whether or not the king is in check at it's current position.
	 * @param king
	 * @return True if the king is in check
	 */
	public boolean isCheck(Piece king){
		if (!(king instanceof pieces.King)){ return false; }
		return isCheck(king.getXcoord(), king.getYcoord(), king.getColor()); //Calls the other isCheck method.
	}
	
	/**
	 * Checks whether or not a king at the given coordinate, with the given color, is in check
	 * @param xCoord
	 * @param yCoord
	 * @param color
	 * @return True if the king is in check
	 */
	public boolean isCheck(int xCoord, int yCoord, ChessColor color){
		
		/* isCheck is onlye run for the color whose turn it is. Returns false for other colors. */
		if (listingMovesForKing != null && listingMovesForKing != color){ return false; }
		listingMovesForKing = getTurn();
		
		/* Sets local variables */
		final int kingX = xCoord;
		final int kingY = yCoord;
		int kingMovedFromX = -1; //This makes sure any error will throw an out of bounds for the array.
		int kingMovedFromY = -1; //And makes sure theres consistency between segments (if's and else's)
		int[][] list = null;
		
		/* Moves king or piece at coordinate from board to shadowboard to properly check all positions */
		if ((board[kingY][kingX] != null) && (board[kingY][kingX] instanceof pieces.King)){ //If King at coordinates
			if (board[kingY][kingX].getColor() != color){
				return false; // If another king is at the selected position. Must be allowed to strike. Should not be able to get in such a position
			}
			else{
				shadowBoard[kingY][kingX] = board[kingY][kingX]; 	//copies reference
				board[kingY][kingX] = null;							//deletes original
				kingMovedFromX = kingX;								//stores coordinates for safety.
				kingMovedFromY = kingY;
			}
		}else if ((board[kingY][kingX] != null) && (board[kingY][kingX].getColor() != color)){ //if opposing color at coordinates
			shadowBoard[kingY][kingX] = board[kingY][kingX];
			board[kingY][kingX] = null;
			boolean isCheckReturned = isCheck(kingX, kingY, color);		//Calls isCheck for the new coordinates and stores them
			
			/* Restores pieces */
			board[kingY][kingX] = shadowBoard[kingY][kingX];
			shadowBoard[kingY][kingX] = null;
			
			/* finished */
			return isCheckReturned;
		}else { //If king not given, removes the king from the board to make sure it does not interfere with the isCheck() process.
			Piece found = findPiece(color, "king");
			kingMovedFromX = found.getXcoord();
			kingMovedFromY = found.getYcoord();
			shadowBoard[kingMovedFromY][kingMovedFromX] = board[kingMovedFromY][kingMovedFromX];
			board[kingMovedFromY][kingMovedFromX] = null;
		}
		
		/* Runs through all the pieces */
		try{ 
			for (Piece p[] : board) {
				for (Piece piece : p) {
					if((piece != null) && (piece.getColor() != color)){ //Checks only the opposing color
						
						/*
						 * First: If a king tries to strike a piece secured by a pawn, this piece will have 
						 * been removed in the first iteration. Then will isCheck be called again, and then this 
						 * bit does the trick of getting the pawn to tell if it's willing to strike or not.
						 */
						if ((piece instanceof pieces.Pawn) && (getPiece(kingX, kingY) == null)){
							setPiece((new Pawn(0 ,kingX, kingY, color)), kingX, kingY);
							list = listMoves(piece);
							setPiece(null, kingX, kingY);
						}else { //For all other pieces than pawns. If a piece is striken, only the second iteration will get here.
								list = listMoves(piece);
						}
						
						/* Checks if the list of moves returned is corresponding with the position of the king (real or theoretical) */ 
						if (list != null){
							for (int[] moves : list) {
								if ((moves[0] == kingX) && (moves[1] == kingY)){ //If piece is able to move to the kings position.
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}finally {
			
			/* Cleaning up. Moves any moved/hidden pieces back to where they where */
			board[kingMovedFromY][kingMovedFromX] = shadowBoard[kingMovedFromY][kingMovedFromX];
			shadowBoard[kingMovedFromY][kingMovedFromX] = null;
		}
	}
	
	
	/**
	 * Method that checks if a given color is stale mate or not. If a color has no legal moves left it's stale mate, if it's also in check
	 * it's check mate.
	 * @param color The ChessColor to check if is check mate
	 * @return True if no legal moves left.
	 */
	public boolean isStaleMate(ChessColor color){
		for(Piece pieces[] : board){
			for (Piece piece : pieces) {
				if ((piece != null) && (piece.getColor() == color) && (listMoves(piece) != null)){ return false; }
			}
		}
		return true;
	}
	
	
	/**
	 * Method that checks if the given player has sufficient mating materials (more or less)
	 * @param color
	 * @return True if usable piece count over or equals 1
	 */
	public boolean hasSufficientMatingMaterials(ChessColor color){
		double usablePiecesFound = 0;
		for (Piece pieces[]: board) {
			for (Piece piece : pieces) {
				if ((piece != null) && (piece.getColor() == color)){
					
					/* We don't count kings for this */
					if (!(piece instanceof pieces.King)){
						if((piece instanceof pieces.Pawn) || (piece instanceof pieces.Bishop)){
							
							/* Pawns which can move (is not blocked) is always nice) */
							if((piece instanceof pieces.Pawn) && (listMoves(piece) != null)){
								usablePiecesFound += 0.34;
							
							/* A bishop is nicer though, but one alone is not enough */
							}else if (piece instanceof pieces.Bishop){
								usablePiecesFound += 0.5;
								
							/* A knight isn't any good alone either (but we may have to change this value at a later point) */
							}else if (piece instanceof pieces.Knight){
								usablePiecesFound += 0.5;
							}
						}else{
							usablePiecesFound++;
						}
					}
				}
			}
		}
		if (usablePiecesFound >= 1){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Sets the rules to false.
	 */
	public void setRulesFalse(){
		rules = false;
	}
	
	
	/**
	 * toString(). Returns a huge string stating where all the pieces are.
	 */
	public String toString(){
		String ret = "";
		for (Piece pieces[] : board){
			if (pieces != null){
				for(Piece p : pieces){
					if(p != null){
						ret += p.getColor() + " " + p.getClass().getSimpleName() + " in position (x,y): (" 
							   + p.getXcoord() + ", " + p.getYcoord() + ").\n";
					}
				}
			}
		}
		return ret;
	}
}
