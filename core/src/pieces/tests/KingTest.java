/*
 * KingTest.java
 */
package pieces.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pieces.King;
import pieces.Piece.ChessColor;

/**
 * @author Darknight
 * @version 0.1
 */
public class KingTest {
	private int[][] moves = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
	private King k = new King(2,4,0, ChessColor.BLACK);
	/**
	 * @throws java.lang.Exception
	 */
	
	/**
	 * Test method for {@link pieces.King#King(int, int, int)}.
	 */
	@Test (expected=IllegalArgumentException.class)
	public void testKing() {
		King k2 = new King(32, 4, 4, ChessColor.BLACK);
	}

	/**
	 * Test method for {@link pieces.King#getMoves()}.
	 */
	@Test
	public void testGetMoves() {
		assertArrayEquals(moves, k.getMoves());
	}

	/**
	 * Test method for {@link pieces.King#getMovableDirections()}.
	 */
	@Test
	public void testGetMovableDirections() {
		assertNull(k.getMovableDirections());
	}

	/**
	 * Test method for {@link pieces.King#canCastle()}.
	 */
	@Test
	public void testCanCastle() {
		assertTrue(k.canCastle());
	}

	/**
	 * Test method for {@link pieces.Piece#getPiecenr()}.
	 */
	@Test
	public void testGetPiecenr() {
		assertTrue(k.getPiecenr() == 2);
	}

	/**
	 * Test method for {@link pieces.Piece#getXcoord()}.
	 */
	@Test
	public void testGetXcoord() {
		assertTrue(k.getXcoord() == 4);
	}

	/**
	 * Test method for {@link pieces.Piece#getYcoord()}.
	 */
	@Test
	public void testGetYcoord() {
		assertTrue(k.getYcoord() == 0);
	}

	/**
	 * Test method for {@link pieces.Piece#setXcoord(int)}.
	 */
	@Test
	public void testSetXcoord() {
		k.setXcoord(5);
		assertEquals(5, k.getXcoord());
	}

	/**
	 * Test method for {@link pieces.Piece#setYcoord(int)}.
	 */
	@Test
	public void testSetYcoord() {
		k.setYcoord(1);
		assertEquals(1, k.getYcoord());
	}
	
	/**
	 * Test method for {@link pieces.Piece#setXcoord(int)}.
	 */
	@Test (expected=(IllegalArgumentException.class))
	public void testSetXcoordIllegal() {
		k.setXcoord(-1);
	}

	/**
	 * Test method for {@link pieces.Piece#setYcoord(int)}.
	 */
	@Test (expected=(IllegalArgumentException.class))
	public void testSetYcoordIllegal() {
		k.setYcoord(10);
	}
	
	/**
	 * Test method for {@link pieces.King#canCastle()}.
	 */
	@Test
	public void testCanCastleAfterMove() {
		k.setXcoord(5);
		assertFalse(k.canCastle());
	}
	
	@Test
	public void testGetValue(){
		assertEquals(4, k.getValue());
	}

}
