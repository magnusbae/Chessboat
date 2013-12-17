/**
 * PieceTest.java
 */
package pieces.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import pieces.*;
import pieces.Piece.ChessColor;

/**
 * JUnit test for all the different pieces. Does not test the getmoves/directions methods
 * since they are well proven, and final.
 * @author Darknight
 *
 */
public class PieceTest {
	King	king 	= new King	(0, 0, 0, ChessColor.WHITE);
	Queen 	queen 	= new Queen	(0, 0, 0, ChessColor.WHITE);
	Bishop 	bishop 	= new Bishop(0, 0, 0, ChessColor.WHITE);
	Knight 	knight 	= new Knight(0, 0, 0, ChessColor.WHITE);
	Rook 	rook	= new Rook	(0, 0, 0, ChessColor.WHITE);
	Pawn 	pawn 	= new Pawn	(0, 0, 0, ChessColor.WHITE);
	
	/**
	 * Test method for {@link pieces.Piece#Piece(int, int, int, pieces.Piece.ChessColor, int)}.
	 */
	@Test //(expected=IllegalArgumentException.class)
	public final void testPiece() {
		King	king 	= new King	(0, 0, 0, ChessColor.WHITE);
		Queen 	queen 	= new Queen	(0, 0, 0, ChessColor.WHITE);
		Bishop 	bishop 	= new Bishop(0, 0, 0, ChessColor.WHITE);
		Knight 	knight 	= new Knight(0, 0, 0, ChessColor.WHITE);
		Rook 	rook	= new Rook	(0, 0, 0, ChessColor.WHITE);
		Pawn 	pawn 	= new Pawn	(0, 0, 0, ChessColor.WHITE);
	}

	/**
	 * Test method for {@link pieces.Piece#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		Pawn 	pawn2 	= new Pawn	(0, 0, 0, ChessColor.WHITE);
		assertTrue(pawn.equals(pawn2));
		assertFalse(rook.equals(pawn));
		assertFalse(knight.equals(bishop));
		assertFalse(king.equals(rook));
		assertFalse(pawn.equals(queen));
	}

	/**
	 * Test method for {@link pieces.Piece#getPiecenr()}.
	 */
	@Test
	public final void testGetPiecenr() {
		assertEquals(0, pawn.getPiecenr());
		assertEquals(0, rook.getPiecenr());
		assertEquals(0, bishop.getPiecenr());
		assertEquals(0, king.getPiecenr());
		assertEquals(0, queen.getPiecenr());
		assertEquals(0, knight.getPiecenr());
	}

	/**
	 * Test method for {@link pieces.Piece#getXcoord()}.
	 */
	@Test
	public final void testGetXcoord() {
		assertEquals(0, queen.getXcoord());
		assertEquals(0, king.getXcoord());
		assertEquals(0, knight.getXcoord());
		assertEquals(0, pawn.getXcoord());
		assertEquals(0, bishop.getXcoord());
		assertEquals(0, rook.getXcoord());
	}

	/**
	 * Test method for {@link pieces.Piece#getYcoord()}.
	 */
	@Test
	public final void testGetYcoord() {
		assertEquals(0, queen.getYcoord());
		assertEquals(0, king.getYcoord());
		assertEquals(0, knight.getYcoord());
		assertEquals(0, pawn.getYcoord());
		assertEquals(0, bishop.getYcoord());
		assertEquals(0, rook.getYcoord());
	}

	/**
	 * Test method for {@link pieces.Piece#getColor()}.
	 */
	@Test
	public final void testGetColor() {
		assertEquals(ChessColor.WHITE, queen.getColor());
		assertEquals(ChessColor.WHITE, king.getColor());
		assertEquals(ChessColor.WHITE, knight.getColor());
		assertEquals(ChessColor.WHITE, pawn.getColor());
		assertEquals(ChessColor.WHITE, bishop.getColor());
		assertEquals(ChessColor.WHITE, rook.getColor());
	}

	/**
	 * Test method for {@link pieces.Piece#getValue()}.
	 */
	@Test
	public final void testGetValue() {
		assertEquals(9, queen.getValue());
		assertEquals(4, king.getValue());
		assertEquals(3, knight.getValue());
		assertEquals(1, pawn.getValue());
		assertEquals(3, bishop.getValue());
		assertEquals(5, rook.getValue());
	}

	/**
	 * Test method for {@link pieces.Piece#setXcoord(int)}.
	 */
	@Test
	public final void testSetXcoord() {
		pawn.setXcoord(5);
		queen.setXcoord(5);
		king.setXcoord(5);
		bishop.setXcoord(5);
		knight.setXcoord(5);
		rook.setXcoord(5);
		
		assertEquals(5, queen.getXcoord());
		assertEquals(5, king.getXcoord());
		assertEquals(5, knight.getXcoord());
		assertEquals(5, pawn.getXcoord());
		assertEquals(5, bishop.getXcoord());
		assertEquals(5, rook.getXcoord());
	}

	/**
	 * Test method for {@link pieces.Piece#setYcoord(int)}.
	 */
	@Test
	public final void testSetYcoord() {
		pawn.setYcoord(5);
		queen.setYcoord(5);
		king.setYcoord(5);
		bishop.setYcoord(5);
		knight.setYcoord(5);
		rook.setYcoord(5);
		
		assertEquals(5, queen.getYcoord());
		assertEquals(5, king.getYcoord());
		assertEquals(5, knight.getYcoord());
		assertEquals(5, pawn.getYcoord());
		assertEquals(5, bishop.getYcoord());
		assertEquals(5, rook.getYcoord());
	}

}
