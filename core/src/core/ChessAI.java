/*
 * ChessAI.java
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

import java.util.ArrayList;
import java.util.Random;

import pieces.Piece;
import pieces.Queen;
import pieces.Piece.ChessColor;
import GUI.ChessGUI;

public class ChessAI extends Thread{
	private ChessBoard board;
	private ChessColor color;
	private ChessGUI gui;
	private int moveCounter;
	
	public ChessAI(ChessBoard board ,ChessColor color, ChessGUI gui){
		this.board = board;
		this.color = color;
		this.gui = gui;
	}
	/**
	 * This method runs in a thread and will move a piece when it is notified and it is ai's turn.
	 * It will also convert an ai pawn that has made it across the board into a queen.
	 */
	@SuppressWarnings("deprecation")
	public void run(){
		while(true){
			if(board.getTurn() == color){
				int[] move = findBestMove();
				if(move != null){
					board.movePiece(board.getPiece(move[0], move[1]), move[2], move[3]);
					Piece piece = board.getPiece(move[2], move[3]);
					if(piece instanceof pieces.Pawn && (piece.getYcoord() == 7 || piece.getYcoord() == 0)){
						Piece[][] boardCopy = board.getBoard();
						boardCopy[piece.getYcoord()][piece.getXcoord()] = new Queen(piece.getPiecenr(),
								piece.getXcoord(), piece.getYcoord(), piece.getColor());
					}
					gui.fireBoardUpdate(this);
				}	
			}else{
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * This method sets the board.
	 * @param newBoard
	 */
	public void setBoard(ChessBoard newBoard){
		board = newBoard;
	}
	
	/**
	 * This method will calculate values of all the avaliable moves for a piece.
	 * @param piece
	 * @return the score list is parallel with .listMoves(piece)
	 */
	@SuppressWarnings("deprecation")
	private int[] calculateMoves(Piece piece){
		//creates a copy of the chessboard
		ChessBoard boardSim = new ChessBoard(0);
		boardSim.restoreFromMemento(board.saveToMemento());
		Piece[][] boardCopy = boardSim.getBoard();
		
		int x = piece.getXcoord();
		int y = piece.getYcoord();
		
		int[][] moves = boardSim.listMoves(piece);
		if(boardSim.listMoves(piece)!=null){
			int[] score = new int[moves.length];
			
			//searches through moves
			for(int i = 0; i < moves.length; i++){
				//resets the board.
				boardSim.restoreFromMemento(board.saveToMemento());
				piece = boardSim.getPiece(x, y);
				boardCopy = boardSim.getBoard();

				//gives the move an initial score
				if(piece instanceof pieces.Pawn){ // pawns are special.
					if(boardCopy[moves[i][1]][moves[i][0]] != null){
						score[i] = boardCopy[moves[i][1]][moves[i][0]].getValue()*10;
					}else if(moves[i][1] == 4 || moves[i][1] == 3 && moveCounter < 4){
						score[i] = 9;
					}
					if(moves[i][1] == 0 || moves[i][1] ==  7){ // can gain a queen
						score[i] = 90;
					}
				}else if(piece instanceof pieces.Rook && piece.canCastle()&& moveCounter < 10){
					score[i] = -2;
				}else if(boardCopy[moves[i][1]][moves[i][0]] != null){
					score[i] = boardCopy[moves[i][1]][moves[i][0]].getValue()*10; //gives score if it can kill a piece
				}else{
					score[i] = 0;
				}
				
				//simulates a move
				boardSim.movePiece(piece, moves[i][0], moves[i][1]);
				
				//searches through enemy pieces
				for(int j = 0; j < boardCopy.length; j++){
					for(int k = 0; k < boardCopy[0].length; k++){
						if(boardCopy[j][k] != null){
							if(boardCopy[j][k].getColor() != piece.getColor()){
								int[][] enemyMoves = boardSim.listMoves(boardCopy[j][k]);
								if(enemyMoves != null){
									//Looks if any of the enemyMoves conflicts with moves
									for(int l = 0; l < enemyMoves.length; l++){
										if(enemyMoves[l][0] == moves[i][0] && enemyMoves[l][1] == moves[i][1]){
											if(piece instanceof pieces.Pawn && score[i] == 10){
												// pawns will kamikaze other pawns
											}else{               
												score[i] -= (piece.getValue()*10)+1;
											}
										}
									}
								}
							}
						}
					}
				}
				// Second move.
				
				// don't bother searching through a bad or good move.
				if(score[i] == 0){
					// Creates a new ChessBoard
					ChessBoard boardSimTwo = new ChessBoard(3);
					Piece[][] boardCopyTwo;
					
					int[][] movesTwo = boardSim.listMoves(boardCopy[moves[i][1]][moves[i][0]]);
					
					if(movesTwo != null){
						int[] scoreTwo = new int[movesTwo.length];
						for(int m = 0; m < movesTwo.length; m++){
							
							// restore and set rules = false
							boardSimTwo.restoreFromMemento(boardSim.saveToMemento());
							boardCopyTwo = boardSimTwo.getBoard();
							piece = boardSimTwo.getPiece(moves[i][0], moves[i][1]);
							boardSimTwo.setRulesFalse();
							
							
							//score
							if(piece instanceof pieces.Pawn){
								
								if(boardCopyTwo[movesTwo[m][1]][movesTwo[m][0]] != null){
									scoreTwo[m] = boardCopyTwo[movesTwo[m][1]][movesTwo[m][0]].getValue();
								}if(movesTwo[m][1] == 0 || movesTwo[m][1] ==  7){ // can gain a queen
									scoreTwo[m] += 9;
								}
								
							}else if(boardCopyTwo[movesTwo[m][1]][movesTwo[m][0]] != null){
								scoreTwo[m] += boardCopyTwo[movesTwo[m][1]][movesTwo[m][0]].getValue(); //gives score if it can kill a piece
							}else{
								scoreTwo[m] = 0;
							}
							
							//moves the piece a second time.
							boardSimTwo.movePiece(piece, movesTwo[m][0],movesTwo[m][1]);
							
							//searches through the entire board and checks enemy pieces and moves.
							for(int j = 0; j < boardCopyTwo.length; j++){
								for(int k = 0; k < boardCopyTwo[0].length; k++){
									if(boardCopyTwo[j][k] != null){
										if(boardCopyTwo[j][k].getColor() != piece.getColor()){
											int[][] enemyMoves = boardSimTwo.listMoves(boardCopy[j][k]);
											if(enemyMoves != null){
												
												//Looks if any of the enemyMoves conflicts with moves
												for(int l = 0; l < enemyMoves.length; l++){
													if(enemyMoves[l][0] == movesTwo[m][0] && enemyMoves[l][1] == movesTwo[m][1]){																											
														if(piece instanceof pieces.Pawn && scoreTwo[m] == 1){
															// pawns will kamikaze other pawns
														}else{
															scoreTwo[m] -= piece.getValue()+1;
														}
													}
												}
											}
										}
									}
								}
							}
						}
						// finds best move from secondMoves and adds it to score[i]
						int big = -999;
						int indexNr = 0;
						for(int n = 0; n < scoreTwo.length; n++){
							if(big < scoreTwo[n]){
								big = scoreTwo[n];
								indexNr = n;
							}
						}
						score[i] += scoreTwo[indexNr];
					}
				}	
			}
			return score;
		}else{
			return null;
		}
	}
	/**
	 * This method goes through all the ai pieces and finds the best move for each piece and saves them in an ArrayList.
	 * It then searches through the ArrayList and finds the piece with the best score.
	 * It returns an int[] with the coords to the piece it wants to move, and the coords to the place it wants to move.
	 * @return moveFromTo[0] = from X
			   moveFromTo[1] = from Y
			   moveFromTo[2] = to X
			   moveFromTo[3] = to Y
	 */
	@SuppressWarnings("deprecation")
	private int[] findBestMove(){
		int[] moveFromTo = new int[4];
		ArrayList<Piece> piece = new ArrayList<Piece>();
		ArrayList<String> bestMove = new ArrayList<String>();
		ArrayList<Integer> bestScore = new ArrayList<Integer>();
		
		Piece[][] boardCopy = board.getBoard();
		
		//searches through the friendly pieces
		for(int i = 0; i < boardCopy.length; i++){
			for(int j = 0; j < boardCopy.length; j++){
				if(boardCopy[i][j] != null){
					if(boardCopy[i][j].getColor() == color){
						
						//checks if the enemy can kill the piece on its current pos, and tries to save it if possible.
						int[] move = evaluatePos(boardCopy[i][j]);
						if(move != null){
							moveFromTo[0] = boardCopy[i][j].getXcoord();
							moveFromTo[1] = boardCopy[i][j].getYcoord();
							moveFromTo[2] = move[0];
							moveFromTo[3] = move[1];
							moveCounter++;
							return  moveFromTo;
						}
						// gives the piece a score on all its moves and saves the best move.
						int[] score = calculateMoves(boardCopy[i][j]);
						int[][] moves = board.listMoves(boardCopy[i][j]);
						
						if(score != null){
							int big = -999;
							int indexNr = 0;
							ArrayList<Integer> index = new ArrayList<Integer>();
							for(int k = 0; k < score.length; k++){
								if(score[k] > big){
									big = score[k];
									indexNr = k;
									index = new ArrayList<Integer>();
									index.add(k);
								}else if(score[k] == big){
									index.add(k);
								}
							}
							if(index.size() > 0){
								indexNr = index.get(randomGen(index.size()));
							}
							
							if(moves.length == 0){
								
							}else{
								piece.add(boardCopy[i][j]);
								bestScore.add(score[indexNr]);
								bestMove.add(""+moves[indexNr][0]+moves[indexNr][1]);
							}
						}
					}
				}
			}
		}
		
		// searches through all the moves saved earlier and selects the best move.
		int big = -999;
		int best = 0;
		ArrayList<Integer> good = new ArrayList<Integer>();
		for(int k = 0; k < bestScore.size(); k++){
			if(bestScore.get(k) > big){
				big = bestScore.get(k);
				best = k;
				good = new ArrayList<Integer>();
				good.add(k);
			}else if(bestScore.get(k) == big){
				good.add(k);
			}
		}
		if(good.size() > 0){
			good.add(best);
			best = good.get(randomGen(good.size()));
		}
		if(bestScore.size() == 0){return null;}
		moveFromTo[0] = piece.get(best).getXcoord();
		moveFromTo[1] = piece.get(best).getYcoord();
		moveFromTo[2] = bestMove.get(best).charAt(0)-48;
		moveFromTo[3] = bestMove.get(best).charAt(1)-48;
		moveCounter++;
		return moveFromTo;
	}
	/**
	 * A method that returns a random number from 0-a
	 * @param a
	 * @return
	 */
	private int randomGen(int a){
		Random ran = new Random();
		int random = ran.nextInt(a);
		return random;
	}
	
	/**
	 * This method will check if an ai piece is in a position where it can be killed.
	 * @param piece
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private int[] evaluatePos(Piece piece){
		boolean defended = isDefended(piece);
		// pawns usually can't be saved.
		if(piece instanceof pieces.Pawn){
			return null;
		// if the piece is defended don't bother searching more.
		}if(defended){
			return null;
		}
		int x = piece.getXcoord();
		int y = piece.getYcoord();
		Piece[][] boardCopy = board.getBoard();
		//scans the entire enemy army and checks if they can kill any friendly pieces
		for(int i = 0; i < boardCopy.length; i++){
			for(int j = 0; j < boardCopy[0].length; j++){
				if(boardCopy[i][j] != null){
					if(boardCopy[i][j].getColor() != color){
						int[][] enemyMoves = board.listMoves(boardCopy[i][j]);
						if(enemyMoves != null){
							for(int n = 0; n < enemyMoves.length; n++){
								if((enemyMoves[n][0] == x && enemyMoves[n][1] == y && !(boardCopy[i][j] instanceof pieces.Pawn))
										|| (boardCopy[i][j] instanceof pieces.Pawn && boardCopy[i][j].getXcoord() != enemyMoves[n][0] 
										       && (enemyMoves[n][0] == x && enemyMoves[n][1] == y))){
									//the enemy can kill this piece in the next round, so try to move.
									int[] score = calculateMoves(piece);
									if(score != null){
										int big = -999;
										int best = 0;
										ArrayList<Integer> index = new ArrayList<Integer>();
										for(int k = 0; k < score.length; k++){
											if(score[k] > big){
												big = score[k];
												best = k;
												index = new ArrayList<Integer>();
												index.add(k);
											}else if(score[k] == big){
												index.add(k);
											}
										}
										if(index.size() > 0){
											best = index.get(randomGen(index.size()));
										}
										int[][] moves = board.listMoves(piece);
										int[] move = new int[2];
										move[0] = moves[best][0];
										move[1] = moves[best][1];
										return move;
									}
								}
							}
						}
					}
				}
			}
		}
	return null;
	}
	/**
	 * This method will check if a piece is defended by placing an enemy piece where the piece is
	 *  and see if any of the friendly pieces can kill it.
	 * @param piece
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean isDefended(Piece piece){
		// creates a new chessboard
		ChessBoard boardSim = new ChessBoard(0);
		boardSim.restoreFromMemento(board.saveToMemento());
		Piece[][] boardCopy = boardSim.getBoard();
		
		ChessColor enemy;
		if(color == ChessColor.BLACK){
			enemy = ChessColor.WHITE;
		}else{
			enemy = ChessColor.BLACK;
		}
		// puts an enemy piece where the ai piece is.
		boardCopy[piece.getYcoord()][piece.getXcoord()] = new Queen(piece.getPiecenr(), piece.getXcoord(), piece.getYcoord(), enemy);
		piece = boardCopy[piece.getYcoord()][piece.getXcoord()];
		
		//checks if any friendly piece can kill the enemy piece and returns true if it can.
		for(int i = 0; i < boardCopy.length; i++){
			for(int j = 0; j < boardCopy[0].length; j++){
				if(boardCopy[i][j] != null){
					if(boardCopy[i][j].getColor() == color){
						int[][] moves = boardSim.listMoves(boardCopy[i][j]);
						if(moves != null){
							for(int k = 0; k < moves.length; k++){
								if((moves[k][0] == piece.getXcoord() && moves[k][1] == piece.getYcoord() && !(boardCopy[i][j] instanceof pieces.Pawn))
										|| (boardCopy[i][j] instanceof pieces.Pawn && boardCopy[i][j].getXcoord() != moves[k][0] 
										        && (moves[k][0] == piece.getXcoord() && moves[k][1] == piece.getYcoord()))){
									return true;									
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
