/*
 * RepetitionChecker.java
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

import core.ChessBoard.Memento;

/**
 * @author Darknight
 *
 */
public class RepetitionChecker {
	ArrayList<Memento> boards = new ArrayList<Memento>();

	
	/**
	 * Method that checks for repeating patterns in the gameplay.
	 * @param lastBoard A reference to the board in play.
	 * @return true if board seems to be repetive
	 */
	public boolean checkForRepetition(ChessBoard lastBoard){
		int boardsToCompare = 8;
		boards.add(lastBoard.saveToMemento());
		if (boards.size() < boardsToCompare){ return false; }
		int index = boards.size() - 1;
		
		ArrayList<ChessBoard> compareList = new ArrayList<ChessBoard>();
		for (int i=0; i < boardsToCompare; i++){
			compareList.add(new ChessBoard(0));
			compareList.get(i).restoreFromMemento(boards.get(index--));
		}
		
		/* Time to compare the boards. */
		if (boardsToCompare == 8){
			if ((compareList.get(7).toString().equals(compareList.get(3).toString())) 
				&& (compareList.get(6).toString().equals(compareList.get(2).toString()))
				&& (compareList.get(5).toString().equals(compareList.get(1).toString()))
				&& (compareList.get(4).toString().equals(compareList.get(0).toString()))){
					return true;
				}
		}
		return false;
	}
	
	
	/**
	 * Help method for GUI-undo functionality
	 */
	public void removeLastEntry(){
		boards.remove(boards.size() -1 );
	}
}
