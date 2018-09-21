package xiangqi.studentyjin2.version.betaXiangqi;

import org.junit.Before;
import org.junit.Test;
import xiangqi.XiangqiGameFactory;
import xiangqi.common.*;

import static org.junit.Assert.*;
import static xiangqi.common.MoveResult.DRAW;
import static xiangqi.common.MoveResult.ILLEGAL;
import static xiangqi.common.MoveResult.OK;
import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.CHARIOT;
import static xiangqi.studentyjin2.common.XiangqiCoordinateImpt.makeCoordinate;

public class BetaXiangqiTestCases {
    private XiangqiGame game;

    @Before
    public void setup() {
        game = XiangqiGameFactory.makeXiangqiGame(XiangqiGameVersion.BETA_XQ);
    }

    @Test
    public void factoryProducesBetaXiangqiGame() {
        assertNotNull(game);
    }

    @Test
    public void checksGetPiece() {
        XiangqiPiece p11 = game.getPieceAt(makeCoordinate(1, 1), RED);
        assertEquals(RED, p11.getColor());
        assertEquals(CHARIOT, p11.getPieceType());
        assertEquals("color = " + RED + " and pieceType = " + CHARIOT ,p11.toString());
    }

    @Test
    public void ensureMessageOnIllegalMove() {
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        assertTrue(game.getMoveMessage().length() > 5);
    }

    @Test
    public void sourceIsEmpty() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(2, 1), makeCoordinate(3, 1)));
    }

    @Test
    public void sourceIsOutOfBound() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(0, 0), makeCoordinate(1, 1)));
    }

    @Test
    public void makeMoveTakesInvalidLocation() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(1, 1), makeCoordinate(1, 0)));
    }

    @Test
    public void moveOppositePiece() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(5, 1), makeCoordinate(4, 1)));
    }

    @Test
    public void toEatOwnPiece() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(1, 1), makeCoordinate(1, 2)));
    }

    @Test
    public void stayInSamePlace() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(1, 1), makeCoordinate(1, 1)));
    }

    @Test
    public void getValidRedPiece() {
        XiangqiPiece piece = game.getPieceAt(makeCoordinate(1, 1), RED);
        assertEquals(RED, piece.getColor());
        assertEquals(CHARIOT, piece.getPieceType());
    }

    @Test
    public void getValidBlackPiece() {
        XiangqiPiece piece = game.getPieceAt(makeCoordinate(1, 1), BLACK);
        assertEquals(BLACK, piece.getColor());
        assertEquals(CHARIOT, piece.getPieceType());
    }

    @Test
    public void getInvalidPiece() {
        XiangqiPiece piece = game.getPieceAt(makeCoordinate(2, 2), BLACK);
        assertEquals(XiangqiColor.NONE, piece.getColor());
        assertEquals(XiangqiPieceType.NONE, piece.getPieceType());
    }

    @Test
    public void makeIllegalMove() {
        MoveResult result = game.makeMove(makeCoordinate(1, 1), makeCoordinate(1, 0));
        assertEquals(ILLEGAL, result);
    }

    @Test
    public void chariotValidMove() {
        MoveResult move1 = game.makeMove(makeCoordinate(1, 1), makeCoordinate(5, 1));
        assertEquals(MoveResult.OK, move1);
        assertEquals(XiangqiPieceType.NONE, (game.getPieceAt(makeCoordinate(1, 1), RED)).getPieceType());
        assertEquals(CHARIOT, (game.getPieceAt(makeCoordinate(5, 1), RED)).getPieceType());
        assertEquals(RED, (game.getPieceAt(makeCoordinate(5, 1), RED)).getColor());
        MoveResult move2 = game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        MoveResult move3 = game.makeMove(makeCoordinate(1, 2), makeCoordinate(2, 1));
        MoveResult move4 = game.makeMove(makeCoordinate(2, 1), makeCoordinate(2, 2));
        MoveResult move5 = game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 2));
        MoveResult move6 = game.makeMove(makeCoordinate(2, 2), makeCoordinate(5, 2));
        assertEquals(MoveResult.OK, move2);
        assertEquals(MoveResult.OK, move3);
        assertEquals(MoveResult.OK, move4);
        assertEquals(MoveResult.OK, move5);
        assertEquals(MoveResult.OK, move6);
    }

    @Test
    public void chariotInvalidMove() {
        MoveResult result = game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 2));
        assertEquals(ILLEGAL, result);
    }

    @Test
    public void chariotInvalidMoveWithPieceInBetween() {
        assertEquals(OK, game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1)));
        assertEquals(OK, game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1)));
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(2, 1), makeCoordinate(2, 5)));
    }

    @Test
    public void chariotEatsAdvisor() {
        MoveResult move1 = game.makeMove(makeCoordinate(2, 3), makeCoordinate(3, 3));
        MoveResult move2 = game.makeMove(makeCoordinate(1, 4), makeCoordinate(2, 5));
        MoveResult move3 = game.makeMove(makeCoordinate(1, 1), makeCoordinate(4, 1));
        assertEquals(MoveResult.OK, move1);
        assertEquals(MoveResult.OK, move2);
        assertEquals(MoveResult.OK, move3);
        XiangqiPiece piece52Black = game.getPieceAt(makeCoordinate(2, 5), BLACK);
        XiangqiPiece piece41Black = game.getPieceAt(makeCoordinate(1, 4), BLACK);
        XiangqiPiece piece25Red = game.getPieceAt(makeCoordinate(5, 2), RED);
        assertEquals(RED, piece52Black.getColor());
        assertEquals(CHARIOT, piece52Black.getPieceType());
        assertEquals(XiangqiPieceType.NONE, piece41Black.getPieceType());
        assertEquals(XiangqiPieceType.NONE, piece25Red.getPieceType());
    }

    @Test
    public void advisorInvalidMove() {
        MoveResult move1 = game.makeMove(makeCoordinate(1, 2), makeCoordinate(2, 2));
        assertEquals(ILLEGAL, move1);
    }

    @Test
    public void generalInvalidMove() {
        MoveResult move1 = game.makeMove(makeCoordinate(1, 3), makeCoordinate(2, 2));
        assertEquals(ILLEGAL, move1);
    }

    @Test
    public void testSoldier() {
        // red pushes the soldier
        MoveResult move1 = game.makeMove(makeCoordinate(2, 3), makeCoordinate(3, 3));
        // black eats the soldier
        MoveResult move2 = game.makeMove(makeCoordinate(2, 3), makeCoordinate(3, 3));
        assertEquals(MoveResult.OK, move1);
        assertEquals(MoveResult.OK, move2);
        XiangqiPiece blackSoldier = game.getPieceAt(makeCoordinate(3, 3), BLACK);
        assertEquals(XiangqiPieceType.SOLDIER, blackSoldier.getPieceType());
        assertEquals(BLACK, blackSoldier.getColor());

        // red left chariot eats black chariot
        MoveResult move3 = game.makeMove(makeCoordinate(1, 1), makeCoordinate(5, 1));
        // black soldier tries to go back, which is illegal
        MoveResult move4 = game.makeMove(makeCoordinate(3, 3), makeCoordinate(2, 3));
        // black soldier tries to go horizontally, which is illegal
        MoveResult move5 = game.makeMove(makeCoordinate(3, 3), makeCoordinate(3, 2));
        // black soldier pushes
        MoveResult move6 = game.makeMove(makeCoordinate(3, 3), makeCoordinate(4, 3));
        // red advisor eats the soldier
        MoveResult move7 = game.makeMove(makeCoordinate(1, 2), makeCoordinate(2, 3));
        // black trivial move
        MoveResult move8 = game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        // red advisor moves away, which causes flying general
        MoveResult move9 = game.makeMove(makeCoordinate(2, 3), makeCoordinate(1, 2));
        assertEquals(OK, move3);
        assertEquals(ILLEGAL, move4);
        assertEquals(ILLEGAL, move5);
        assertEquals(OK, move6);
        assertEquals(OK, move7);
        assertEquals(OK, move8);
        assertEquals(ILLEGAL, move9);
    }

    @Test
    public void DrawAfterTenRound() {
        // 1
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        // 2
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        // 3
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        // 4
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        // 5
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        // 6
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        // 7
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        // 8
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        // 9
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        game.makeMove(makeCoordinate(1, 1), makeCoordinate(2, 1));
        // 10
        MoveResult move1 = game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        MoveResult move2 = game.makeMove(makeCoordinate(2, 1), makeCoordinate(1, 1));
        assertEquals(OK, move1);
        assertEquals(DRAW, move2);
    }

}




















