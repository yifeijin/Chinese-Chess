package xiangqi.studentyjin2.version.gammaXiangqi;

import org.junit.Before;
import org.junit.Test;
import xiangqi.XiangqiGameFactory;
import xiangqi.common.*;

import static org.junit.Assert.*;
import static xiangqi.common.MoveResult.*;
import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.*;
import static xiangqi.studentyjin2.common.XiangqiCoordinateImpt.makeCoordinate;
import static xiangqi.studentyjin2.common.XiangqiPieceImpt.makePiece;

public class GammaXiangqiTestCases {

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
        game = XiangqiGameFactory.makeXiangqiGame(XiangqiGameVersion.GAMMA_XQ);
        game2 = GammaXiangqiGame.makeTestGammaXiangqi();

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 10; j++) {
                c[i][j] = makeCoordinate(i, j);
            }
        }
    }

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
    public void testPieceEquals() {
        XiangqiPiece piece11 = game.getPieceAt(c[1][1], RED);
        assertEquals(redChariot, piece11);
        assertFalse(piece11.equals(game));
    }

    @Test
    public void testChariotValidMove() {
        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));
        XiangqiPiece piece108 = game.getPieceAt(c[10][8], BLACK);
        XiangqiPiece piece109 = game.getPieceAt(c[10][9], BLACK);
        assertEquals(noPiece, piece109);
        assertEquals(redChariot, piece108);
        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));
        XiangqiPiece piece12 = game.getPieceAt(c[1][2], RED);
        XiangqiPiece piece11 = game.getPieceAt(c[1][1], RED);
        assertEquals(redChariot, piece12);
        assertEquals(noPiece, piece11);
        assertEquals(OK, game.makeMove(c[1][2], c[10][2]));
        assertEquals(OK, game.makeMove(c[1][9], c[1][8]));
        XiangqiPiece piece102 = game.getPieceAt(c[10][2], RED);
        XiangqiPiece piece101 = game.getPieceAt(c[10][1], RED);
        assertEquals(blackChariot, piece102);
        assertEquals(noPiece, piece101);
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
    public void testElephantMove() {
        assertEquals(ILLEGAL, game.makeMove(c[1][3], c[4][6]));
        assertEquals(OK, game.makeMove(c[1][3], c[3][1]));
        assertEquals(OK, game.makeMove(c[1][3], c[3][1]));
        assertEquals(OK, game.makeMove(c[3][1], c[5][3]));
        assertEquals(OK, game.makeMove(c[3][1], c[5][3]));
        assertEquals(ILLEGAL, game.makeMove(c[5][3], c[7][1]));
        assertEquals(ILLEGAL, game.makeMove(c[5][3], c[5][4]));
        assertEquals(ILLEGAL, game.makeMove(c[5][3], c[4][4]));
        // move rock to block elephant
        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));
        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));
        assertEquals(OK, game.makeMove(c[1][2], c[4][2]));
        assertEquals(OK, game.makeMove(c[1][2], c[4][2]));
        assertEquals(ILLEGAL, game.makeMove(c[5][3], c[3][1]));
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
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[1][5], BLACK));
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[3][2], RED));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[5][1], RED));
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[5][1], BLACK));
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[10][1], BLACK));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[10][2], BLACK));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[1][3], RED));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[10][3], BLACK));

        // from black board
        assertEquals(OK, game.makeMove(c[1][9], c[1][8]));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[1][2], RED));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[1][2], BLACK));
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[2][1], RED));

        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[10][8], RED));
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[1][8], RED));
        assertEquals(true, ((GammaXiangqiGame) game).isCoveredBy(c[1][8], BLACK));
    }

    @Test
    public void testIsCoveredBy2() {
        assertEquals(false, ((GammaXiangqiGame) game2).isCoveredBy(c[9][3], RED));
        assertEquals(true, ((GammaXiangqiGame) game2).isCoveredBy(c[6][6], RED));
        assertEquals(true, ((GammaXiangqiGame) game2).isCoveredBy(c[1][1], BLACK));
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
        assertEquals(ILLEGAL, game2.makeMove(c[1][6], c[1][5]));
    }

    @Test
    public void testInvalidMoveBecauseOfGeneralUnderAttack2() {
        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));
        assertEquals(OK, game.makeMove(c[1][4], c[2][5]));

        assertEquals(OK, game.makeMove(c[1][2], c[7][2]));
        assertEquals(OK, game.makeMove(c[1][1], c[1][2]));

        assertEquals(OK, game.makeMove(c[7][2], c[7][3]));
        assertEquals(OK, game.makeMove(c[1][2], c[1][1]));

        assertEquals(OK, game.makeMove(c[7][3], c[7][5]));
        assertEquals(redChariot, game.getPieceAt(c[7][5], RED));
        assertEquals(noPiece, game.getPieceAt(c[8][5], RED));
        assertEquals(blackAdvisor, game.getPieceAt(c[9][5], RED));
        assertEquals(false, ((GammaXiangqiGame) game).isCoveredBy(c[1][5], RED));
        assertEquals(ILLEGAL, game.makeMove(c[2][5], c[1][4]));
    }

    @Test
    public void testDraw() {
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][1], c[1][2]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][2], c[1][1]);
        game.makeMove(c[1][1], c[1][2]);
        assertEquals(DRAW, game.makeMove(c[1][1], c[1][2]));
    }

    @Test
    public void testCheckmate() {
        assertEquals(OK, game2.makeMove(c[6][6], c[9][6]));
        assertEquals(OK, game2.makeMove(c[5][9], c[6][9]));
        assertEquals(RED_WINS, game2.makeMove(c[6][4], c[10][4]));
    }

    @Test
    public void testCheckmate2() {
        assertEquals(OK, game2.makeMove(c[10][3], c[10][2]));
        assertEquals(OK, game2.makeMove(c[5][1], c[6][1]));
        assertEquals(RED_WINS, game2.makeMove(c[6][6], c[6][5]));
    }

    @Test
    public void testCheckmate3() {
        assertEquals(OK, game2.makeMove(c[9][7], c[10][7]));
        assertEquals(OK, game2.makeMove(c[5][9], c[5][6]));
        assertEquals(OK, game2.makeMove(c[10][7], c[10][8]));
        assertEquals(BLACK_WINS, game2.makeMove(c[5][1], c[5][4]));
    }

    @Test
    public void testCheckmate4() {
        assertEquals(OK, game2.makeMove(c[6][4], c[6][1]));
        assertEquals(OK, game2.makeMove(c[5][1], c[5][2]));

        assertEquals(OK, game2.makeMove(c[6][6], c[6][8]));
        assertEquals(OK, game2.makeMove(c[1][5], c[2][5]));

        assertEquals(OK, game2.makeMove(c[6][1], c[9][1]));
        assertEquals(OK, game2.makeMove(c[2][5], c[1][5]));

        assertEquals(RED_WINS, game2.makeMove(c[9][1], c[9][2]));
    }

    @Test
    public void testGame2Moves() {
        assertEquals(ILLEGAL, game2.makeMove(c[1][6], c[1][5]));
        assertEquals(true, ((GammaXiangqiGame) game2).isCoveredBy(c[6][6], BLACK));
        assertEquals(blackChariot, game2.getPieceAt(c[5][1], BLACK));
        assertEquals(blackChariot, game2.getPieceAt(c[6][9], RED));
        assertEquals(OK, game2.makeMove(c[6][6], c[6][5]));
        assertEquals(true, ((GammaXiangqiGame) game2).isCoveredBy(c[5][5], BLACK));
        assertEquals(true, ((GammaXiangqiGame) game2).isCoveredBy(c[5][5], BLACK));
        assertEquals(ILLEGAL, game2.makeMove(c[1][5], c[1][4]));
        assertEquals(ILLEGAL, game2.makeMove(c[1][5], c[1][6]));
        assertEquals(ILLEGAL, game2.makeMove(c[1][5], c[2][5]));
        assertEquals(ILLEGAL, game2.makeMove(c[5][9], c[5][6]));
        assertEquals(true, ((GammaXiangqiGame) game2).isValidMove(c[5][1], c[5][5], false));
        assertEquals(OK, game2.makeMove(c[5][1], c[5][5]));
    }

    @Test
    public void testGame2Moves2() {
        assertEquals(false, ((GammaXiangqiGame) game2).isValidMove(c[10][3], c[9][4], false));
        assertEquals(OK, game2.makeMove(c[10][3], c[10][4]));
        assertEquals(noPiece, game2.getPieceAt(c[9][5], RED));
        assertEquals(noPiece, game2.getPieceAt(c[2][5], BLACK));
        assertEquals(OK, game2.makeMove(c[1][5], c[2][5]));
        assertEquals(ILLEGAL, game2.makeMove(c[5][1], c[6][1]));
    }

}
