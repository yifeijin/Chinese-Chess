package xiangqi.studentyjin2.common;

import xiangqi.common.*;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.*;
import static xiangqi.studentyjin2.common.XiangqiCoordinateImpt.makeCoordinate;
import static xiangqi.studentyjin2.common.XiangqiPieceImpt.makePiece;

/**
 * Xiangqi game implement class
 */
public abstract class XiangqiGameImpt implements XiangqiGame {
    protected static String moveMessage;                // move message

    protected int rank;                                 // height of the board
    protected int file;                                 // width of the board

    protected XiangqiPiece[][] redBoard;                // board from red's perspective
    protected XiangqiPiece[][] blackBoard;              // board from black's perspective

    protected int moveCount;                            // number of moves
    protected boolean isRed;                            // is this turn red
    protected XiangqiColor thisSideColor;               // this turn's color
    protected XiangqiColor opponentColor;               // opponent's color
    protected XiangqiPiece[][] board;                   // this turn's board
    protected XiangqiPiece[][] otherBoard;              // opponent's board
    protected ArrayList<XiangqiPiece[][]> boardState;   // array list of board state

    /**
     * constructor.
     *
     * @param rank height of the board
     * @param file width of the board
     */
    protected XiangqiGameImpt(int rank, int file) {
        redBoard = new XiangqiPiece[rank][file];
        blackBoard = new XiangqiPiece[rank][file];
        boardState = new ArrayList<>();
        moveCount = 1;
        updateInfo();
        this.rank = rank;
        this.file = file;

        for (int i = 1; i < rank; i++) {
            for (int j = 1; j < file; j++) {
                redBoard[i][j] = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);
                blackBoard[i][j] = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);
            }
        }
    }

    /**
     * make a move and returns move result. Different versions will implement the method according
     * to their rules.
     *
     * @param source      the coordinate where the piece moving starts
     * @param destination the coordinate where the piece moving ends
     * @return move result
     */
    @Override
    public abstract MoveResult makeMove(XiangqiCoordinate source, XiangqiCoordinate destination);

    /**
     * Initialize a game
     *
     * @param args an array of objects that are needed for initialization
     */
    @Override
    public abstract void initialize(Object... args);

    /**
     * Gives illegal move message.
     *
     * @return moveMessage
     */
    @Override
    public String getMoveMessage() {
        return moveMessage;
    }

    /**
     * Gets the piece's coordinate from aspect's point of view.
     *
     * @param where  the coordinate to access. Different by red and black.
     * @param aspect the player from whom the request is made. This is needed
     *               in order to determine which location the <code>where</code> parameter
     *               references
     * @return coordinate from aspect's perspective.
     */
    @Override
    public XiangqiPiece getPieceAt(XiangqiCoordinate where, XiangqiColor aspect) throws CompletionException {
        int x = where.getRank();
        int y = where.getFile();

        if (makeCoordinate(where).isOutOfBound()) {
            throw new CompletionException(new Throwable());
        }

        return aspect == RED ? redBoard[x][y] : blackBoard[x][y];
    }

    /**
     * Checks if a move is valid for a standard game.
     *
     * @param source          coordinate where the piece is from
     * @param destination     coordinate where the piece goes to
     * @param useForCoveredBy whether allows piece to eat its own piece. It should be allowed when check isCoveredBy.
     * @return true if the move is valid
     */
    public boolean isValidMove(XiangqiCoordinate source, XiangqiCoordinate destination, boolean useForCoveredBy) {

        XiangqiValidator validator = XiangqiValidator.makeValidator(source, destination, board, isRed, useForCoveredBy);

        validator.basicRulesValidator();
        if (!validator.getFollowBasicRules()) {
            return false;
        }

        switch (board[source.getRank()][source.getFile()].getPieceType()) {
            case CHARIOT:
                return validator.chariotValidator();
            case HORSE:
                return validator.horseValidator();
            case ELEPHANT:
                return validator.elephantValidator();
            case ADVISOR:
                return validator.advisorValidator();
            case GENERAL:
                XiangqiCoordinate opponentGeneral = findGeneral(opponentColor);
                return validator.generalValidator(opponentGeneral);
            case SOLDIER:
                return validator.soldierValidator();
            case CANNON:
                return validator.cannonValidator();
            default:
                return false;
        }
    }

    /**
     * Checks whether this side general would be under attack after the move.
     *
     * @param source      coordinate where the piece is from
     * @param destination coordinate where the piece goes to
     * @return true if the general is under attack
     */
    public boolean isGeneralUnderAttackAfterMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        boolean ret;
        XiangqiPiece[][] otherBoard = isRed ? blackBoard : redBoard;

        int sx = source.getRank();
        int sy = source.getFile();
        int dx = destination.getRank();
        int dy = destination.getFile();

        XiangqiPiece sPiece = board[sx][sy];
        XiangqiPiece dPiece = board[dx][dy];

        // move the pieces
        board[dx][dy] = sPiece;
        board[sx][sy] = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);
        otherBoard[rank - dx][file - dy] = sPiece;
        otherBoard[rank - sx][file - sy] = makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);

        ret = isCoveredBy(findGeneral(thisSideColor), opponentColor);

        // move pieces back
        board[dx][dy] = dPiece;
        board[sx][sy] = sPiece;
        otherBoard[rank - dx][file - dy] = dPiece;
        otherBoard[rank - sx][file - sy] = sPiece;

        return ret;
    }

    /**
     * Checks if the coordinate is covered by the given color. The coordinate is from current
     * turn's perspective.
     *
     * @param where coordinate to check
     * @param color which color
     * @return true if the spot is covered
     */
    public boolean isCoveredBy(XiangqiCoordinate where, XiangqiColor color) {
        boolean infoChanged = color != thisSideColor;

        // change to the other side in order to use isValidMove Function
        if (infoChanged) {
            moveCount++;
            updateInfo();
            where = makeCoordinate(rank - where.getRank(), file - where.getFile());
        }

        for (int i = 1; i < rank; i++) {
            for (int j = 1; j < file; j++) {
                if (board[i][j].getColor() == color) {
                    if (infoChanged && isValidMove(makeCoordinate(i, j), where, true)) {
                        moveCount--;
                        updateInfo();
                        return true;
                    } else if (!infoChanged && isValidMove(makeCoordinate(i, j), where, true)) {
                        return true;
                    }
                }
            }
        }

        if (infoChanged) {
            moveCount--;
            updateInfo();
        }

        return false;
    }

    /**
     * Checks if a set is checkmate. The function should be called after the move and before update info.
     *
     * @return true if the move is checkmate
     */
    public boolean isCheckmate() {

        moveCount++;
        updateInfo();
        boolean ret = true;

        // make a list of all coordinates
        ArrayList<XiangqiCoordinate> allC = new ArrayList<>();
        for (int i = 1; i < rank; i++) {
            for (int j = 1; j < file; j++) {
                allC.add(makeCoordinate(i, j));
            }
        }

        /*
         * First find all this side color pieces. For each piece, check whether there is a valid move and
         * after the move general will not be under attack. If yes, then the move just made is not checkmate.
         */
        for (XiangqiCoordinate source : allC) {
            if (board[source.getRank()][source.getFile()].getColor() == thisSideColor) {
                for (XiangqiCoordinate destination : allC) {
                    if (isValidMove(source, destination, false) && !isGeneralUnderAttackAfterMove(source, destination)) {
                        ret = false;
                        break;
                    }
                }
            }
        }

        moveCount--;
        updateInfo();
        return ret;
    }

    /**
     * checks whether the moves are perpetual.
     *
     * @return true if it is perpetual, otherwise false
     */
    public boolean isPerpetual() {
        int numberStates = boardState.size();

        // need at least 9 states to be perpetual
        if (numberStates < 9) {
            return false;
        }

        XiangqiPiece[][] state1 = boardState.get(numberStates - 9);
        XiangqiPiece[][] state2 = boardState.get(numberStates - 5);
        XiangqiPiece[][] state3 = boardState.get(numberStates - 1);

        return stateEquals(state1, state2) && stateEquals(state2, state3);
    }

    /**
     * Checks whether the two xiangqi boards are the same.
     *
     * @param s1 one board
     * @param s2 another board
     * @return true if they are the same
     */
    private boolean stateEquals(XiangqiPiece[][] s1, XiangqiPiece[][] s2) {
        int s1x = s1.length;
        int s1y = s1[1].length;
        int s2x = s2.length;
        int s2y = s2[1].length;

        if (s1x != s2x || s1y != s2y) {
            return false;
        }

        for (int i = 1; i < s1x; i++) {
            for (int j = 1; j < s1y; j++) {
                if (s1[i][j].getColor() != s2[i][j].getColor() || s1[i][j].getPieceType() != s2[i][j].getPieceType()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Find color's general's coordinate
     *
     * @param color which color's general to find
     * @return coordinate from this turn's perspective
     */
    public XiangqiCoordinate findGeneral(XiangqiColor color) {

        for (int i = 1; i < rank; i++) {
            for (int j = 1; j < file; j++) {
                if (board[i][j].getPieceType() == GENERAL && board[i][j].getColor() == color) {
                    return makeCoordinate(i, j);
                }
            }
        }

        return null;
    }

    /**
     * initialize a simple game for test checkmate and stalemate on standard board
     */
    public void initTestGame() {
        redBoard[6][1] = makePiece(CHARIOT, BLACK);
        redBoard[6][9] = makePiece(CHARIOT, BLACK);
        redBoard[10][5] = makePiece(GENERAL, BLACK);
        redBoard[1][6] = makePiece(GENERAL, RED);
        redBoard[6][4] = makePiece(CHARIOT, RED);
        redBoard[6][6] = makePiece(CHARIOT, RED);
        redBoard[9][7] = makePiece(SOLDIER, RED);
        redBoard[10][3] = makePiece(SOLDIER, RED);

        blackBoard[5][9] = makePiece(CHARIOT, BLACK);
        blackBoard[5][1] = makePiece(CHARIOT, BLACK);
        blackBoard[1][5] = makePiece(GENERAL, BLACK);
        blackBoard[10][4] = makePiece(GENERAL, RED);
        blackBoard[5][6] = makePiece(CHARIOT, RED);
        blackBoard[5][4] = makePiece(CHARIOT, RED);
        blackBoard[2][3] = makePiece(SOLDIER, RED);
        blackBoard[1][7] = makePiece(SOLDIER, RED);
    }

    /**
     * updates all info about the game
     */
    public void updateInfo() {
        isRed = moveCount % 2 != 0;
        thisSideColor = isRed ? RED : XiangqiColor.BLACK;
        opponentColor = isRed ? XiangqiColor.BLACK : RED;
        board = isRed ? redBoard : blackBoard;
        otherBoard = isRed ? blackBoard : redBoard;
    }
}
