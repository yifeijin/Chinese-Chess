package xiangqi.studentyjin2.version.alphaXiangqi;

import org.junit.Before;
import org.junit.Test;
import xiangqi.XiangqiGameFactory;
import xiangqi.common.*;
import xiangqi.studentyjin2.common.XiangqiCoordinateImpt;

import static org.junit.Assert.*;

public class AlphaXiangqiTestCases {

    private XiangqiGame game;

    @Before
    public void setup() {
        game = XiangqiGameFactory.makeXiangqiGame(XiangqiGameVersion.ALPHA_XQ);
    }

    @Test
    public void factoryProducesAlphaXiangqiGame() {
        assertNotNull(game);
    }

    @Test
    public void redMakesFirstValidMove() {
        assertEquals(MoveResult.OK, game.makeMove(XiangqiCoordinateImpt.makeCoordinate(1, 1), XiangqiCoordinateImpt.makeCoordinate(1, 2)));
    }

    @Test
    public void blackMakesValidSecondMove() {
        game.makeMove(XiangqiCoordinateImpt.makeCoordinate(1, 1), XiangqiCoordinateImpt.makeCoordinate(1, 2));
        assertEquals(MoveResult.RED_WINS, game.makeMove(XiangqiCoordinateImpt.makeCoordinate(1, 1),
                XiangqiCoordinateImpt.makeCoordinate(1, 2)));
    }

    @Test
    public void tryToMoveInvalidLocation() {
        assertEquals(MoveResult.ILLEGAL, game.makeMove(XiangqiCoordinateImpt.makeCoordinate(1, 1), XiangqiCoordinateImpt.makeCoordinate(2, 1)));
        assertTrue(game.getMoveMessage().length() >= 1);
    }

    @Test
    public void tryToMoveFromInvalidLocation() {
        assertEquals(MoveResult.ILLEGAL, game.makeMove(XiangqiCoordinateImpt.makeCoordinate(2, 1), XiangqiCoordinateImpt.makeCoordinate(1, 2)));
        assertTrue(game.getMoveMessage().length() >= 1);
    }

    @Test
    public void getPieceAtReturnsNoneNone() {
        final XiangqiPiece p = game.getPieceAt(XiangqiCoordinateImpt.makeCoordinate(1, 1), XiangqiColor.RED);
        assertEquals(XiangqiPieceType.NONE, p.getPieceType());
        assertEquals(XiangqiColor.NONE, p.getColor());
    }
}
