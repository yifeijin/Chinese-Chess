package xiangqi.studentyjin2.version.gammaXiangqi;

import xiangqi.common.*;
import xiangqi.studentyjin2.common.XiangqiGameImpt;

import static xiangqi.common.MoveResult.*;
import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.*;
import static xiangqi.studentyjin2.common.XiangqiPieceImpt.makePiece;

/**
 * Gamma version Xiangqi game
 */
public class GammaXiangqiGame extends XiangqiGameImpt {

    /**
     * Constructor
     */
    private GammaXiangqiGame(boolean forTest) {

        super(11, 10);

        if (forTest) {
            initTestGame();
        } else {
            initialize();
        }
    }

    /**
     * factory method
     *
     * @return Gamma xiangqi game
     */
    public static XiangqiGame makeGammaXiangqi() {
        return new GammaXiangqiGame(false);
    }


    /**
     * setup an easy board for test checkmate and stalemate
     *
     * @return gammaXiangqiGame
     */
    public static XiangqiGame makeTestGammaXiangqi() {
        return new GammaXiangqiGame(true);
    }

    /**
     * Make a move and update the board.
     *
     * @param source      the coordinate where the piece moving starts
     * @param destination the coordinate where the piece moving ends
     * @return move result
     */
    @Override
    public MoveResult makeMove(XiangqiCoordinate source, XiangqiCoordinate destination) {

        int sx = source.getRank();
        int sy = source.getFile();
        int dx = destination.getRank();
        int dy = destination.getFile();

        // if valid, move pieces and update info
        if (isValidMove(source, destination, false)) {

            XiangqiPiece sPiece = board[sx][sy];

            if (isGeneralUnderAttackAfterMove(source, destination)) {
                moveMessage = "after the move, general can't be under attack";
                System.out.println(getMoveMessage());
                return ILLEGAL;
            }

            // move the piece and set the original spot to none
            board[dx][dy] = sPiece;
            board[sx][sy] = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);
            otherBoard[rank - dx][file - dy] = sPiece;
            otherBoard[rank - sx][file - sy] = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);

            if (moveCount >= 50) {
                return DRAW;
            }

            if (isCheckmate()) {
                return isRed ? RED_WINS : BLACK_WINS;
            }

            moveCount++;
            updateInfo();
            return OK;
        }

        System.out.println(getMoveMessage());
        return ILLEGAL;
    }

    /**
     * Initialize the game
     */
    @Override
    public void initialize(Object... args) {
        // set up red board
        redBoard[1][1] = makePiece(CHARIOT, RED);
        redBoard[1][3] = makePiece(ELEPHANT, RED);
        redBoard[1][4] = makePiece(ADVISOR, RED);
        redBoard[1][5] = makePiece(GENERAL, RED);
        redBoard[1][6] = makePiece(ADVISOR, RED);
        redBoard[1][7] = makePiece(ELEPHANT, RED);
        redBoard[1][9] = makePiece(CHARIOT, RED);
        redBoard[4][1] = makePiece(SOLDIER, RED);
        redBoard[4][3] = makePiece(SOLDIER, RED);
        redBoard[4][5] = makePiece(SOLDIER, RED);
        redBoard[4][7] = makePiece(SOLDIER, RED);
        redBoard[4][9] = makePiece(SOLDIER, RED);

        redBoard[10][1] = makePiece(CHARIOT, BLACK);
        redBoard[10][3] = makePiece(ELEPHANT, BLACK);
        redBoard[10][4] = makePiece(ADVISOR, BLACK);
        redBoard[10][5] = makePiece(GENERAL, BLACK);
        redBoard[10][6] = makePiece(ADVISOR, BLACK);
        redBoard[10][7] = makePiece(ELEPHANT, BLACK);
        redBoard[10][9] = makePiece(CHARIOT, BLACK);
        redBoard[7][1] = makePiece(SOLDIER, BLACK);
        redBoard[7][3] = makePiece(SOLDIER, BLACK);
        redBoard[7][5] = makePiece(SOLDIER, BLACK);
        redBoard[7][7] = makePiece(SOLDIER, BLACK);
        redBoard[7][9] = makePiece(SOLDIER, BLACK);

        // set up red board
        blackBoard[1][1] = makePiece(CHARIOT, BLACK);
        blackBoard[1][3] = makePiece(ELEPHANT, BLACK);
        blackBoard[1][4] = makePiece(ADVISOR, BLACK);
        blackBoard[1][5] = makePiece(GENERAL, BLACK);
        blackBoard[1][6] = makePiece(ADVISOR, BLACK);
        blackBoard[1][7] = makePiece(ELEPHANT, BLACK);
        blackBoard[1][9] = makePiece(CHARIOT, BLACK);
        blackBoard[4][1] = makePiece(SOLDIER, BLACK);
        blackBoard[4][3] = makePiece(SOLDIER, BLACK);
        blackBoard[4][5] = makePiece(SOLDIER, BLACK);
        blackBoard[4][7] = makePiece(SOLDIER, BLACK);
        blackBoard[4][9] = makePiece(SOLDIER, BLACK);

        blackBoard[10][1] = makePiece(CHARIOT, RED);
        blackBoard[10][3] = makePiece(ELEPHANT, RED);
        blackBoard[10][4] = makePiece(ADVISOR, RED);
        blackBoard[10][5] = makePiece(GENERAL, RED);
        blackBoard[10][6] = makePiece(ADVISOR, RED);
        blackBoard[10][7] = makePiece(ELEPHANT, RED);
        blackBoard[10][9] = makePiece(CHARIOT, RED);
        blackBoard[7][1] = makePiece(SOLDIER, RED);
        blackBoard[7][3] = makePiece(SOLDIER, RED);
        blackBoard[7][5] = makePiece(SOLDIER, RED);
        blackBoard[7][7] = makePiece(SOLDIER, RED);
        blackBoard[7][9] = makePiece(SOLDIER, RED);
    }

}
