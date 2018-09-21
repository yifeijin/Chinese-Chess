package xiangqi.studentyjin2.version.deltaXiangqi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xiangqi.XiangqiGameFactory;
import xiangqi.common.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static xiangqi.common.MoveResult.*;
import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.*;
import static xiangqi.studentyjin2.common.XiangqiCoordinateImpt.makeCoordinate;
import static xiangqi.studentyjin2.common.XiangqiPieceImpt.makePiece;

public class DeltaXiangqiTestCases {
    private static XiangqiCoordinate[][] c = new XiangqiCoordinate[11][10];
    private static XiangqiPiece
            noPiece = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE),
            redChariot = makePiece(CHARIOT, RED),
            redElephant = makePiece(ELEPHANT, RED),
            redAdvisor = makePiece(ADVISOR, RED),
            redGeneral = makePiece(GENERAL, RED),
            redSoldier = makePiece(SOLDIER, RED),
            blackChariot = makePiece(CHARIOT, BLACK),
            blackElephant = makePiece(ELEPHANT, BLACK),
            blackAdvisor = makePiece(ADVISOR, BLACK),
            blackGeneral = makePiece(GENERAL, BLACK),
            blackSoldier = makePiece(SOLDIER, BLACK);
    private XiangqiGame game;
    private XiangqiGame game2;

    @Before
    public void setup() {
        game = XiangqiGameFactory.makeXiangqiGame(XiangqiGameVersion.DELTA_XQ);
        game2 = DeltaXiangqiGame.makeTestDeltaXiangqi();

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 10; j++) {
                c[i][j] = makeCoordinate(i, j);
            }
        }
    }


    // I reuse most of the tests from Gamma
    @Test
    public void factoryProducesBetaXiangqiGame() {
        assertNotNull(game);
    }

    @Test
    public void queryAnEmptyLocation() {
        XiangqiPiece piece = game.getPieceAt(c[2][2], BLACK);
        assertEquals(noPiece, piece);
    }

    @Test
    public void makeMoveWithInvalidCoordinates() {
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(0, 3), c[1][4]));
        assertEquals(ILLEGAL, game.makeMove(c[1][1], makeCoordinate(1, 20)));
        assertEquals(ILLEGAL, game.makeMove(makeCoordinate(0, 100), c[2][4]));
    }

    @Test
    public void ensureMessageOnIllegalMove() {
        MoveResult result = game.makeMove(c[1][1], c[1][3]);
        assertEquals(ILLEGAL, result);
        assertTrue(game.getMoveMessage().length() > 5);
    }

    @Test
    public void attemptToMoveOpponentPiece() {
        assertEquals(ILLEGAL, game.makeMove(c[10][1], c[9][1]));
    }

    @Test
    public void attemptToMoveEmpty() {
        assertEquals(ILLEGAL, game.makeMove(c[2][1], c[2][2]));
    }

    @Test
    public void testChariotInvalidMove() {
        assertEquals(ILLEGAL, game.makeMove(c[1][1], c[5][1]));
        assertEquals(ILLEGAL, game.makeMove(c[1][1], c[2][2]));
        assertEquals(OK, game.makeMove(c[1][4], c[2][5]));
        assertEquals(OK, game.makeMove(c[1][4], c[2][5]));
        assertEquals(ILLEGAL, game.makeMove(c[1][1], c[1][4]));
    }

    @Test
    public void testAdvisor() {
        assertEquals(OK, game.makeMove(c[1][4], c[2][5]));
        assertEquals(OK, game.makeMove(c[1][6], c[2][5]));
        assertEquals(OK, game.makeMove(c[2][5], c[3][4]));
        assertEquals(OK, game.makeMove(c[2][5], c[3][6]));
        assertEquals(ILLEGAL, game.makeMove(c[3][4], c[3][5]));
        assertEquals(ILLEGAL, game.makeMove(c[3][5], c[3][7]));
    }

    @Test
    public void testGeneral() {
        assertEquals(ILLEGAL, game.makeMove(c[1][5], c[10][5]));
        assertEquals(ILLEGAL, game.makeMove(c[1][5], c[3][5]));
        assertEquals(OK, game.makeMove(c[1][5], c[2][5]));
        assertEquals(OK, game.makeMove(c[1][5], c[2][5]));
        assertEquals(OK, game.makeMove(c[2][5], c[2][4]));
        assertEquals(ILLEGAL, game.makeMove(c[2][5], c[2][8]));
        assertEquals(ILLEGAL, game.makeMove(c[2][4], c[2][4]));
    }

    @Test
    public void testSoldier() {
        assertEquals(OK, game.makeMove(c[4][1], c[5][1]));
        assertEquals(OK, game.makeMove(c[4][1], c[5][1]));
        assertEquals(ILLEGAL, game.makeMove(c[5][1], c[5][2]));
        assertEquals(ILLEGAL, game.makeMove(c[5][1], c[4][1]));
        assertEquals(OK, game.makeMove(c[5][1], c[6][1]));
        assertEquals(OK, game.makeMove(c[5][1], c[6][1]));
        assertEquals(OK, game.makeMove(c[6][1], c[6][2]));
        assertEquals(OK, game.makeMove(c[6][1], c[6][2]));
        assertEquals(OK, game.makeMove(c[6][2], c[6][1]));
        XiangqiPiece piece61 = game.getPieceAt(c[6][1], RED);
        XiangqiPiece piece61Black = game.getPieceAt(c[5][9], BLACK);
        assertEquals(redSoldier, piece61);
        assertEquals(redSoldier, piece61Black);
    }

    @Test
    public void testIsCoveredBy() {
        // from red board
        assertEquals(false, ((DeltaXiangqiGame) game).isCoveredBy(c[1][5], BLACK));
        assertEquals(false, ((DeltaXiangqiGame) game).isCoveredBy(c[3][2], RED));
        assertEquals(true, ((DeltaXiangqiGame) game).isCoveredBy(c[5][1], RED));
        assertEquals(false, ((DeltaXiangqiGame) game).isCoveredBy(c[5][1], BLACK));
        assertEquals(false, ((DeltaXiangqiGame) game).isCoveredBy(c[10][1], BLACK));
        assertEquals(true, ((DeltaXiangqiGame) game).isCoveredBy(c[10][2], BLACK));
        assertEquals(false, ((DeltaXiangqiGame) game).isCoveredBy(c[1][3], RED));
        assertEquals(false, ((DeltaXiangqiGame) game).isCoveredBy(c[10][3], BLACK));
    }

    @Test
    public void testIsCoveredBy2() {
        assertEquals(false, ((DeltaXiangqiGame) game2).isCoveredBy(c[9][3], RED));
        assertEquals(true, ((DeltaXiangqiGame) game2).isCoveredBy(c[6][6], RED));
        assertEquals(true, ((DeltaXiangqiGame) game2).isCoveredBy(c[1][1], BLACK));
        game2.makeMove(c[6][6], c[10][6]);
    }

    @Test
    public void testInvalidMoveBecauseOfGeneralUnderAttack() {
        // red pushes middle soldier
        game.makeMove(c[4][5], c[5][5]);
        // black pushes too
        game.makeMove(c[4][5], c[5][5]);
        // red pushes again to eat black's soldier
        game.makeMove(c[5][5], c[6][5]);
        // black does trivial move
        game.makeMove(c[1][1], c[1][2]);
        // red tries to move away the solider
        assertEquals(ILLEGAL, game.makeMove(c[6][5], c[6][6]));
        XiangqiPiece piece65 = game.getPieceAt(c[6][5], RED);
        XiangqiPiece piece65Black = game.getPieceAt(c[5][5], BLACK);
        XiangqiPiece piece66 = game.getPieceAt(c[6][6], RED);
        XiangqiPiece piece66Black = game.getPieceAt(c[5][4], BLACK);
        assertEquals(redSoldier, piece65);
        assertEquals(redSoldier, piece65Black);
        assertEquals(noPiece, piece66);
        assertEquals(noPiece, piece66Black);
    }

    @Test
    public void testUnableToMoveBecauseOfFlyingGeneral() {
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[1][6], c[1][5]));
    }

    @Test
    public void testCheckmate() {
        Assert.assertEquals(OK, game2.makeMove(c[6][6], c[9][6]));
        Assert.assertEquals(OK, game2.makeMove(c[5][9], c[6][9]));
        Assert.assertEquals(RED_WINS, game2.makeMove(c[6][4], c[10][4]));
    }

    @Test
    public void testCheckmate2() {
        Assert.assertEquals(OK, game2.makeMove(c[10][3], c[10][2]));
        Assert.assertEquals(OK, game2.makeMove(c[5][1], c[6][1]));
        Assert.assertEquals(RED_WINS, game2.makeMove(c[6][6], c[6][5]));
    }

    @Test
    public void testCheckmate3() {
        Assert.assertEquals(OK, game2.makeMove(c[9][7], c[10][7]));
        Assert.assertEquals(OK, game2.makeMove(c[5][9], c[5][6]));
        Assert.assertEquals(OK, game2.makeMove(c[10][7], c[10][8]));
        Assert.assertEquals(BLACK_WINS, game2.makeMove(c[5][1], c[5][4]));
    }

    @Test
    public void testCheckmate4() {
        Assert.assertEquals(OK, game2.makeMove(c[6][4], c[6][1]));
        Assert.assertEquals(OK, game2.makeMove(c[5][1], c[5][2]));

        Assert.assertEquals(OK, game2.makeMove(c[6][6], c[6][8]));
        Assert.assertEquals(OK, game2.makeMove(c[1][5], c[2][5]));

        Assert.assertEquals(OK, game2.makeMove(c[6][1], c[9][1]));
        Assert.assertEquals(OK, game2.makeMove(c[2][5], c[1][5]));

        Assert.assertEquals(RED_WINS, game2.makeMove(c[9][1], c[9][2]));
    }

    @Test
    public void testGame2Moves() {
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[1][6], c[1][5]));
        Assert.assertEquals(true, ((DeltaXiangqiGame) game2).isCoveredBy(c[6][6], BLACK));
        Assert.assertEquals(blackChariot, game2.getPieceAt(c[5][1], BLACK));
        Assert.assertEquals(blackChariot, game2.getPieceAt(c[6][9], RED));
        Assert.assertEquals(OK, game2.makeMove(c[6][6], c[6][5]));
        Assert.assertEquals(true, ((DeltaXiangqiGame) game2).isCoveredBy(c[5][5], BLACK));
        Assert.assertEquals(true, ((DeltaXiangqiGame) game2).isCoveredBy(c[5][5], BLACK));
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[1][5], c[1][4]));
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[1][5], c[1][6]));
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[1][5], c[2][5]));
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[5][9], c[5][6]));
        Assert.assertEquals(true, ((DeltaXiangqiGame) game2).isValidMove(c[5][1], c[5][5], false));
        Assert.assertEquals(OK, game2.makeMove(c[5][1], c[5][5]));
    }

    @Test
    public void testGame2Moves2() {
        Assert.assertEquals(false, ((DeltaXiangqiGame) game2).isValidMove(c[10][3], c[9][4], false));
        Assert.assertEquals(OK, game2.makeMove(c[10][3], c[10][4]));
        Assert.assertEquals(noPiece, game2.getPieceAt(c[9][5], RED));
        Assert.assertEquals(noPiece, game2.getPieceAt(c[2][5], BLACK));
        Assert.assertEquals(OK, game2.makeMove(c[1][5], c[2][5]));
        Assert.assertEquals(ILLEGAL, game2.makeMove(c[5][1], c[6][1]));
    }

    /* -----------------------------------------------------------------------------------
       -----------------------------------------------------------------------------------
       -----------------------------------------------------------------------------------
       ------------------------- new test for delta --------------------------------------
       -----------------------------------------------------------------------------------
       -----------------------------------------------------------------------------------
       -----------------------------------------------------------------------------------
     */
    @Test
    public void testHorseValidMove() {
        assertEquals(ILLEGAL, game.makeMove(c[1][2], c[2][4]));
        assertEquals(OK, game.makeMove(c[1][2], c[3][1]));
        assertEquals(ILLEGAL, game.makeMove(c[1][2], c[2][5]));
        assertEquals(OK, game.makeMove(c[1][2], c[3][3]));

        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));
        assertEquals(OK, game.makeMove(c[4][1], c[5][1]));

        assertEquals(ILLEGAL, game.makeMove(c[3][1], c[1][2]));
        assertEquals(OK, game.makeMove(c[3][2], c[10][2]));
        assertEquals(ILLEGAL, game.makeMove(c[3][3], c[4][1]));

    }

    @Test
    public void testCannonValidMove() {
        assertEquals(OK, game.makeMove(c[3][2], c[4][2]));
        assertEquals(OK, game.makeMove(c[3][2], c[10][2]));

        assertEquals(OK, game.makeMove(c[4][2], c[10][2]));
        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));

        assertEquals(ILLEGAL, game.makeMove(c[10][2], c[10][3]));
        assertEquals(ILLEGAL, game.makeMove(c[10][2], c[10][5]));
        assertEquals(OK, game.makeMove(c[10][2], c[10][4]));
    }

    @Test
    public void testRedPerpetual() {
        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));
        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));

        assertEquals(OK, game.makeMove(c[2][1], c[1][1]));
        assertEquals(OK, game.makeMove(c[2][1], c[1][1]));

        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));
        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));

        assertEquals(OK, game.makeMove(c[2][1], c[1][1]));
        assertEquals(OK, game.makeMove(c[2][1], c[1][1]));

        assertEquals(BLACK_WINS, game.makeMove(c[1][1], c[2][1]));
    }

    @Test
    public void testBlackPerpetual() {
        assertEquals(OK, game.makeMove(c[4][1], c[5][1]));
        assertEquals(OK, game.makeMove(c[4][1], c[5][1]));

        assertEquals(OK, game.makeMove(c[5][1], c[6][1]));
        assertEquals(OK, game.makeMove(c[1][9], c[2][9]));

        assertEquals(OK, game.makeMove(c[1][5], c[2][5]));
        assertEquals(OK, game.makeMove(c[1][2], c[3][1]));

        assertEquals(OK, game.makeMove(c[2][5], c[1][5]));
        assertEquals(OK, game.makeMove(c[3][1], c[1][2]));

        assertEquals(OK, game.makeMove(c[1][5], c[2][5]));
        assertEquals(OK, game.makeMove(c[1][2], c[3][1]));

        assertEquals(OK, game.makeMove(c[2][5], c[1][5]));
        assertEquals(RED_WINS, game.makeMove(c[3][1], c[1][2]));
    }

    @Test
    public void testRedPerpetual2() {
        assertEquals(OK, game.makeMove(c[1][1], c[2][1]));
        assertEquals(OK, game.makeMove(c[1][3], c[3][1]));

        assertEquals(OK, game.makeMove(c[2][1], c[2][5]));
        assertEquals(OK, game.makeMove(c[3][1], c[1][3]));

        assertEquals(OK, game.makeMove(c[2][5], c[2][1]));
        assertEquals(OK, game.makeMove(c[1][3], c[3][1]));

        assertEquals(OK, game.makeMove(c[2][1], c[2][5]));
        assertEquals(OK, game.makeMove(c[3][1], c[1][3]));

        assertEquals(BLACK_WINS, game.makeMove(c[2][5], c[2][1]));
    }

    @Test
    public void testCannon() {
        assertEquals(ILLEGAL, game.makeMove(c[3][2], c[3][9]));
    }
}
