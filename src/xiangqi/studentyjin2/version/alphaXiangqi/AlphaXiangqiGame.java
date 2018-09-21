package xiangqi.studentyjin2.version.alphaXiangqi;

import xiangqi.common.*;
import xiangqi.studentyjin2.common.XiangqiGameImpt;
import xiangqi.studentyjin2.common.XiangqiPieceImpt;

/**
 * Alpha xiangqi game
 */
public class AlphaXiangqiGame extends XiangqiGameImpt {

    public AlphaXiangqiGame() {
        super(4, 4);

        moveCount = 0;
    }

    @Override
    public MoveResult makeMove(XiangqiCoordinate source, XiangqiCoordinate destination) {
        if (source.getRank() != 1 || source.getFile() != 1 || destination.getRank() != 1 || destination.getFile() != 2) {
            System.out.println(getMoveMessage());
            return MoveResult.ILLEGAL;
        }

        return moveCount++ == 0 ? MoveResult.OK : MoveResult.RED_WINS;
    }

    @Override
    public String getMoveMessage() {
        return "Invalid Move";
    }

    @Override
    public XiangqiPiece getPieceAt(XiangqiCoordinate where, XiangqiColor aspect) {
        return XiangqiPieceImpt.makePiece(XiangqiPieceType.NONE, XiangqiColor.NONE);
    }

    @Override
    public void initialize(Object... args) {

    }

}
