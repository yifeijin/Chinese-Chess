package xiangqi.studentyjin2.common;

import xiangqi.common.XiangqiColor;
import xiangqi.common.XiangqiCoordinate;
import xiangqi.common.XiangqiPiece;
import xiangqi.common.XiangqiPieceType;

import java.util.ArrayList;

import static xiangqi.common.XiangqiColor.BLACK;
import static xiangqi.common.XiangqiColor.RED;
import static xiangqi.common.XiangqiPieceType.NONE;
import static xiangqi.studentyjin2.common.XiangqiCoordinateImpt.makeCoordinate;

/**
 * validator for all the a move
 */
public class XiangqiValidator {

    private XiangqiCoordinateImpt source;       // coordinate where the piece is from
    private XiangqiCoordinateImpt destination;  // coordinate where the piece goes to
    private XiangqiPiece[][] board;             // this turn's board
    private boolean isRed;                      // is this turn's color red
    private boolean useForCoveredBy;            // whether allows piece to eat its own piece. It should be true when check isCoveredBy.

    private int sx;                             // source rank
    private int sy;                             // source file
    private int dx;                             // destination rank
    private int dy;                             // destination file
    private int rank;                           // height of the board
    private int file;                           // width of the board
    private boolean followBasicRules;           // whether the move follows basic rules

    // use for check flying general, true if the move is valid flying general
    private boolean isFlyingGeneral;
    // use for check flying general, true if the move is trying flying general
    private boolean tryFlyingGeneral;

    /**
     * constructor
     */
    private XiangqiValidator(XiangqiCoordinate source, XiangqiCoordinate destination, XiangqiPiece[][] board, boolean isRed, boolean useForCoveredBy) {
        this.source = makeCoordinate(source);
        this.destination = makeCoordinate(destination);
        this.board = board;
        this.isRed = isRed;
        this.useForCoveredBy = useForCoveredBy;

        this.sx = source.getRank();
        this.sy = source.getFile();
        this.dx = destination.getRank();
        this.dy = destination.getFile();

        this.rank = board.length - 1;
        this.file = board[1].length - 1;
    }

    /**
     * factory method
     */
    public static XiangqiValidator makeValidator(XiangqiCoordinate source, XiangqiCoordinate destination, XiangqiPiece[][] board, boolean isRed, boolean useForCoveredBy) {
        return new XiangqiValidator(source, destination, board, isRed, useForCoveredBy);
    }

    /**
     * validator for some basic rules.
     */
    public void basicRulesValidator() {
        followBasicRules = true;
        XiangqiColor thisSideColor = isRed ? RED : BLACK;
        XiangqiColor opponentColor = isRed ? BLACK : RED;

        // if source is out of bound, return false
        if (sx < 1 || sy < 1 || sx > rank || sy > file) {
            XiangqiGameImpt.moveMessage = "source is out of bound";
            followBasicRules = false;
            return;
        }

        // if destination is out of bound, return false
        if (dx < 1 || dy < 1 || dx > rank || dy > file) {
            XiangqiGameImpt.moveMessage = "destination is out of bound";
            followBasicRules = false;
            return;
        }

        XiangqiPieceType sourcePieceType = board[sx][sy].getPieceType();

        // if the source is empty, return false
        if (sourcePieceType == XiangqiPieceType.NONE) {
            XiangqiGameImpt.moveMessage = "source is empty";
            followBasicRules = false;
            return;
        }

        // if the source is opposite color, return false
        if (board[sx][sy].getColor() == opponentColor) {
            XiangqiGameImpt.moveMessage = "source is opposite's color";
            followBasicRules = false;
            return;
        }

        // if the source is the same as destination, return false
        if (sx == dx && sy == dy) {
            XiangqiGameImpt.moveMessage = "source is the same as destination";
            followBasicRules = false;
            return;
        }

        // if the destination is its own color, return false
        if (!useForCoveredBy) {
            if (board[dx][dy].getColor() == thisSideColor) {
                XiangqiGameImpt.moveMessage = "can't eat your own piece";
                followBasicRules = false;
            }
        }
    }

    /**
     * validator for chariot
     *
     * @return true if the move is valid
     */
    public boolean chariotValidator() {

        // first checks orthogonal
        if (!source.isOrthogonal(destination)) {
            XiangqiGameImpt.moveMessage = "Destination is not orthogonal to source";
            return false;
        }

        // goes horizontally
        if (sx == dx) {
            if (Math.abs(dy - sy) == 1) {
                return true;
            } else {
                for (int i = smallerNumber(sy, dy) + 1; i < greaterNumber(sy, dy); i++) {
                    if (board[sx][i].getPieceType() != XiangqiPieceType.NONE) {
                        XiangqiGameImpt.moveMessage = "horizontally there is a piece in between";
                        return false;
                    }
                }
            }
        }

        // goes vertically
        if (sy == dy) {
            if (Math.abs(dx - sx) == 1) {
                return true;
            } else {
                for (int i = smallerNumber(sx, dx) + 1; i < greaterNumber(sx, dx); i++) {
                    if (board[i][sy].getPieceType() != XiangqiPieceType.NONE) {
                        XiangqiGameImpt.moveMessage = "vertically there is a piece in between";
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * validator for horse
     *
     * @return true if the move is valid
     */
    public boolean horseValidator() {

        int xDiff = Math.abs(dx - sx);
        int yDiff = Math.abs(dy - sy);

        if (Math.pow(xDiff, 2) + Math.pow(yDiff, 2) != 5) {
            XiangqiGameImpt.moveMessage = "horse move is invalid";
            return false;
        }

        if (xDiff == 2) {
            if (board[(sx + dx) / 2][sy].getPieceType() != NONE) {
                XiangqiGameImpt.moveMessage = "horse is blocked";
                return false;
            }
        }

        if (yDiff == 2) {
            if (board[sx][(sy + dy) / 2].getPieceType() != NONE) {
                XiangqiGameImpt.moveMessage = "horse is blocked";
                return false;
            }
        }

        return true;

    }

    /**
     * validator for elephant
     *
     * @return true if the move is valid
     */
    public boolean elephantValidator() {
        int midX = (sx + dx) / 2;
        int midY = (sy + dy) / 2;

        // first checks diagonal
        if (!source.isDiagonal(destination)) {
            XiangqiGameImpt.moveMessage = "destination is not diagonal to source";
            return false;
        }

        // if goes across river, return false
        if (dx > 5) {
            XiangqiGameImpt.moveMessage = "elephant can't go across river";
            return false;
        }

        // if there is a piece in the middle, return false
        if (board[midX][midY].getPieceType() != XiangqiPieceType.NONE) {
            XiangqiGameImpt.moveMessage = "there is a piece in the middle";
            return false;
        }

        // checks distance is 4
        if (source.distanceTo(destination) != 4) {
            XiangqiGameImpt.moveMessage = "elephant can only go two squares diagonally";
            return false;
        }

        return true;
    }

    /**
     * validator for advisor
     *
     * @return true if the move is valid
     */
    public boolean advisorValidator() {

        // if goes out of palace, return false
        if (dy < 4 || dy > 6 || dx > 3) {
            XiangqiGameImpt.moveMessage = "advisor can't go out of palace";
            return false;
        }

        // return true if two coordinates are orthogonal and distance is 2
        if (source.isDiagonal(destination) &&
                (source.distanceTo(destination) == 2)) {
            return true;
        }

        XiangqiGameImpt.moveMessage = "advisor can't move from" + source + " to " + destination;
        return false;
    }

    /**
     * validator for Beta advisor
     *
     * @return true if the move is valid
     */
    public boolean advisorValidatorForBeta() {
        XiangqiCoordinateImpt sImpt = makeCoordinate(source);
        XiangqiCoordinateImpt dImpt = makeCoordinate(destination);

        // return true if two coordinates are orthogonal and distance is 2
        if (sImpt.isDiagonal(dImpt) && (sImpt.distanceTo(dImpt) == 2)) {
            return true;
        }

        XiangqiGameImpt.moveMessage = "advisor can't move from" + source + " to " + destination;
        return false;
    }

    /**
     * validator for general
     *
     * @return true if the move is valid
     */
    public boolean generalValidator(XiangqiCoordinate opponentGeneral) {

        isFlyingGeneral(opponentGeneral);
        if (tryFlyingGeneral && isFlyingGeneral) {
            return true;
        }

        if ((dy > 3 && dy < 7 && dx < 4) && source.distanceTo(destination) == 1) {
            return true;
        }

        XiangqiGameImpt.moveMessage = "general can't move from" + source + " to " + destination;
        return false;
    }

    /**
     * validator for beta general.
     *
     * @return true if the move is valid
     */
    public boolean generalValidatorForBeta(XiangqiCoordinate opponentGeneral) {

        isFlyingGeneral(opponentGeneral);
        if (tryFlyingGeneral && isFlyingGeneral) {
            return true;
        }

        if ((dy == 2 || dy == 3 || dy == 4) && source.distanceTo(destination) == 1) {
            return true;
        }

        XiangqiGameImpt.moveMessage = "general can't move from" + source + " to " + destination;
        return false;
    }

    /**
     * helper function to update isFlyingGeneral and tryFlyingGeneral
     *
     * @param opponentGeneral coordinate of opponent general
     */
    private void isFlyingGeneral(XiangqiCoordinate opponentGeneral) {
        int opponentGeneralX = opponentGeneral.getRank();
        int opponentGeneralY = opponentGeneral.getFile();
        isFlyingGeneral = true;
        tryFlyingGeneral = false;

        if (sy == opponentGeneralY && dx == opponentGeneralX && dy == opponentGeneralY) {
            tryFlyingGeneral = true;
            for (int i = sx + 1; i < dx; i++) {
                if (board[i][sy].getPieceType() != XiangqiPieceType.NONE) {
                    isFlyingGeneral = false;
                }
            }
        }
    }

    /**
     * validator for soldier
     *
     * @return true if the move is valid
     */
    public boolean soldierValidator() {

        // before the river
        if (sx < 6) {
            if (dx - sx == 1 && dy - sy == 0) {
                return true;
            }
        }

        // across the river
        if (dx >= 6) {
            if ((dx - sx == 1 && dy - sy == 0) || (dx - sx == 0 && Math.abs(dy - sy) == 1)) {
                return true;
            }
        }

        XiangqiGameImpt.moveMessage = "soldier can't move from" + source + " to " + destination;
        return false;
    }

    /**
     * validator for Beta soldier
     *
     * @return true if the move is valid
     */
    public boolean soldierValidatorForBeta() {
        if (destination.getRank() - source.getRank() == 1 && destination.getFile() - source.getFile() == 0) {
            return true;
        }

        XiangqiGameImpt.moveMessage = "soldier can't move from" + source + " to " + destination;
        return false;
    }

    /**
     * validator for cannon
     *
     * @return true if the move is valid
     */
    public boolean cannonValidator() {
        // first of all, it goes orthogonally
        if (!source.isOrthogonal(destination)) {
            XiangqiGameImpt.moveMessage = "cannon can only move orthogonally";
            return false;
        }

        // find number of pieces between source and destination
        ArrayList<XiangqiPiece> PiecesInBetween = new ArrayList<>();
        if (sx == dx) {
            for (int i = smallerNumber(sy, dy) + 1; i < greaterNumber(sy, dy); i++) {
                if (board[sx][i].getPieceType() != XiangqiPieceType.NONE) {
                    PiecesInBetween.add(board[sx][i]);
                }
            }
        }

        if (sy == dy) {
            for (int i = smallerNumber(sx, dx) + 1; i < greaterNumber(sx, dx); i++) {
                if (board[i][sy].getPieceType() != XiangqiPieceType.NONE) {
                    PiecesInBetween.add(board[i][sy]);
                }
            }
        }

        if (PiecesInBetween.size() > 1) {
            XiangqiGameImpt.moveMessage = "cannon can not jump more than one piece";
            return false;
        }

        if (PiecesInBetween.size() == 0) {
            if (board[dx][dy].getPieceType() != XiangqiPieceType.NONE) {
                XiangqiGameImpt.moveMessage = "cannon must jump a piece to eat";
                return false;
            }
        }

        if (PiecesInBetween.size() == 1) {
            if (board[dx][dy].getPieceType() == XiangqiPieceType.NONE) {
                XiangqiGameImpt.moveMessage = "";
                return false;
            }
        }

        return true;
    }

    /**
     * getter for followBasicRules
     */
    public boolean getFollowBasicRules() {
        return followBasicRules;
    }

    /**
     * takes two numbers and returns the greater one.
     *
     * @param i first number
     * @param j second number
     * @return greater one
     */
    private int greaterNumber(int i, int j) {
        return i > j ? i : j;
    }

    /**
     * takes two numbers and returns the smaller one
     *
     * @param i first number
     * @param j second number
     * @return smaller one
     */
    private int smallerNumber(int i, int j) {
        return i > j ? j : i;
    }

}
