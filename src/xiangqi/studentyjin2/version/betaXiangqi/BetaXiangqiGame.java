package xiangqi.studentyjin2.version.betaXiangqi;

import xiangqi.common.*;
import xiangqi.common.XiangqiPieceType;
import xiangqi.studentyjin2.common.XiangqiGameImpt;
import xiangqi.studentyjin2.common.XiangqiValidator;

import static xiangqi.common.MoveResult.*;
import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.*;
import static xiangqi.studentyjin2.common.XiangqiPieceImpt.makePiece;

/**
 * Beta Xiangqi game
 */
public class BetaXiangqiGame extends XiangqiGameImpt {

    /**
     * constructor
     */
    private BetaXiangqiGame() {
        super(6, 6);
        initialize();
    }

    /**
     * factory method
     *
     * @return Beta xiangqi game
     */
    public static XiangqiGame makeBetaXiangqi() {
        return new BetaXiangqiGame();
    }

    /**
     * Make a move method.
     *
     * @param source      the coordinate where the piece moving starts
     * @param destination the coordinate where the piece moving ends
     * @return the result of the move made
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

            if (isCheckmate()) {
                return isRed ? RED_WINS : BLACK_WINS;
            }

            if (moveCount >= 20) {
                return DRAW;
            }

            moveCount++;
            updateInfo();

            return OK;
        }

        System.out.println(getMoveMessage());
        return ILLEGAL;
    }

    /**
     * Initialize a game
     */
    @Override
    public void initialize(Object... args) {
        redBoard[1][1] = makePiece(CHARIOT, RED);
        redBoard[1][2] = makePiece(ADVISOR, RED);
        redBoard[1][3] = makePiece(GENERAL, RED);
        redBoard[1][4] = makePiece(ADVISOR, RED);
        redBoard[1][5] = makePiece(CHARIOT, RED);
        redBoard[2][3] = makePiece(SOLDIER, RED);

        redBoard[5][1] = makePiece(CHARIOT, BLACK);
        redBoard[5][2] = makePiece(ADVISOR, BLACK);
        redBoard[5][3] = makePiece(GENERAL, BLACK);
        redBoard[5][4] = makePiece(ADVISOR, BLACK);
        redBoard[5][5] = makePiece(CHARIOT, BLACK);
        redBoard[4][3] = makePiece(SOLDIER, BLACK);

        blackBoard[1][1] = makePiece(CHARIOT, BLACK);
        blackBoard[1][2] = makePiece(ADVISOR, BLACK);
        blackBoard[1][3] = makePiece(GENERAL, BLACK);
        blackBoard[1][4] = makePiece(ADVISOR, BLACK);
        blackBoard[1][5] = makePiece(CHARIOT, BLACK);
        blackBoard[2][3] = makePiece(SOLDIER, BLACK);

        blackBoard[5][1] = makePiece(CHARIOT, RED);
        blackBoard[5][2] = makePiece(ADVISOR, RED);
        blackBoard[5][3] = makePiece(GENERAL, RED);
        blackBoard[5][4] = makePiece(ADVISOR, RED);
        blackBoard[5][5] = makePiece(CHARIOT, RED);
        blackBoard[4][3] = makePiece(SOLDIER, RED);
    }

    /**
     * Since Beta has some special rules, isValidMove need to be overridden.
     *
     * @param source      the coordinate the piece is from
     * @param destination the coordinate the piece goes to
     * @return true if it is valid, otherwise false
     */
    @Override
    public boolean isValidMove(XiangqiCoordinate source, XiangqiCoordinate destination, boolean useForCoveredBy) {

        /*
         * beta version xiangqi has some special rules need to override
         */
        XiangqiValidator validator = XiangqiValidator.makeValidator(source, destination, board, isRed, useForCoveredBy);

        validator.basicRulesValidator();
        if (!validator.getFollowBasicRules()) {
            return false;
        }

        switch (board[source.getRank()][source.getFile()].getPieceType()) {
            case CHARIOT:
                return validator.chariotValidator();
            case ADVISOR:
                return validator.advisorValidatorForBeta();
            case GENERAL:
                XiangqiCoordinate opponentGeneral = findGeneral(opponentColor);
                return validator.generalValidatorForBeta(opponentGeneral);
            case SOLDIER:
                return validator.soldierValidatorForBeta();
            default:
                return false;
        }
    }
}
