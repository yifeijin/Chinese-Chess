package xiangqi.studentyjin2.common;

import xiangqi.common.XiangqiColor;
import xiangqi.common.XiangqiPiece;
import xiangqi.common.XiangqiPieceType;

/**
 * xiangqi piece implement class
 */
public class XiangqiPieceImpt implements XiangqiPiece {

    private final XiangqiColor color;           // color of the piece
    private final XiangqiPieceType pieceType;   // piece type of the piece

    /**
     * constructor
     */
    private XiangqiPieceImpt(XiangqiPieceType pieceType, XiangqiColor color) {
        this.pieceType = pieceType;
        this.color = color;
    }

    /**
     * factory method
     *
     * @param color     color of the piece
     * @param pieceType piece type of the piece
     * @return a xiangqi coordinate
     */
    public static XiangqiPiece makePiece(XiangqiPieceType pieceType, XiangqiColor color) {
        return new XiangqiPieceImpt(pieceType, color);
    }

    /**
     * @see XiangqiPiece#getColor()
     */
    @Override
    public XiangqiColor getColor() {
        return color;
    }

    /**
     * @see XiangqiPiece#getPieceType()
     */
    @Override
    public XiangqiPieceType getPieceType() {
        return pieceType;
    }

    @Override
    public String toString() {
        return "color = " + getColor() + " and pieceType = " + getPieceType();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj.getClass() == this.getClass()) {
            XiangqiPiece other = (XiangqiPiece) obj;
            return color == other.getColor() && pieceType == other.getPieceType();
        }

        return false;
    }
}
