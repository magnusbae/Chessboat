package core.tests;

import static org.junit.Assert.*;
import org.junit.Test;

import core.ChessBoard;

import pieces.*;
import pieces.Piece.ChessColor;

public class ChessBoardTest {
	ChessBoard test = new ChessBoard(0);

	@Test
	public final void testGetPiece() {
		assertEquals(new Rook(7,0,0, ChessColor.BLACK), test.getPiece(0, 0));
	}

	@Test
	public final void testMovePiece() {
		assertNull(test.getPiece(3, 2));
		test.movePiece(test.getPiece(2, 0), 3, 2);
		assertNull(test.getPiece(2, 0));
		assertNotNull(test.getPiece(3, 2));
	}

}
