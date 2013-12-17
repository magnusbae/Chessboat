/*
 * TestKnight.java
 */
package pieces.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import pieces.Knight;
import pieces.Piece.ChessColor;

public class TestKnight {
	private int[][] moves = {{-1,2},{1,2},{2,1},{2,-1},{1,-2},{1,-2},{-2,1},{-2,-1}};
	Knight test = new Knight(15, 5, 5, ChessColor.BLACK);
	
	@Test
	public void testGetMoves() {
		assertArrayEquals(moves, test.getMoves());
	}

	@Test
	public void testGetMovableDirections() {
		assertNull(test.getMovableDirections());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKnight() {
		Knight test2 = new Knight(-1, 3, 3, ChessColor.BLACK);
	}

	@Test
	public void testGetPiecenr() {
		assertEquals(15, test.getPiecenr());
	}

	@Test
	public void testGetXcoord() {
		assertEquals(5, test.getXcoord());
	}

	@Test
	public void testGetYcoord() {
		assertEquals(5, test.getYcoord());
	}

	@Test
	public void testSetXcoord() {
		test.setXcoord(2);
		assertEquals(2, test.getXcoord());
	}

	@Test
	public void testSetYcoord() {
		test.setYcoord(3);
		assertEquals(3, test.getYcoord());
	}

}
