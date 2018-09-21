package xiangqi.studentyjin2.common;

import xiangqi.common.XiangqiCoordinate;

/**
 * Xiangqi coordinate implement class
 */
public class XiangqiCoordinateImpt implements XiangqiCoordinate {
    private final int x;    // rank
    private final int y;    // file

    /**
     * constructor
     */
    private XiangqiCoordinateImpt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * copy constructor
     */
    public static XiangqiCoordinateImpt makeCoordinate(XiangqiCoordinate c) {
        return new XiangqiCoordinateImpt(c.getRank(), c.getFile());
    }

    /**
     * factory method
     */
    public static XiangqiCoordinate makeCoordinate(int x, int y) {
        return new XiangqiCoordinateImpt(x, y);
    }

    /**
     * Checks if the coordinate is in a standard board.
     *
     * @return true if it is, otherwise false
     */
    public boolean isOutOfBound() {
        return x < 1 || x > 10 || y < 1 || y > 9;
    }

    /**
     * Checks if the two coordinates are orthogonal
     *
     * @param c coordinate to check
     * @return true if another Coordinate is on the same row or column as this Coordinate
     */
    public boolean isOrthogonal(XiangqiCoordinate c) {
        return x == c.getRank() || y == c.getFile();
    }

    /**
     * @param c another Coordinate
     * @return true if the another coordinate is on a diagonal from this Coordinate
     */
    public boolean isDiagonal(XiangqiCoordinate c) {
        return Math.abs(c.getRank() - x) == Math.abs(c.getFile() - y);
    }

    /**
     * The distance to another coordinate is the sum of the differences between
     * the two Coordinates' x and y coordinates.
     *
     * @param c coordinate to compare
     * @return return the distance to the other coordinate
     */
    public int distanceTo(XiangqiCoordinate c) {
        return Math.abs(x - c.getRank()) + Math.abs(y - c.getFile());
    }

    @Override
    public int getRank() {
        return x;
    }

    @Override
    public int getFile() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + this.getRank() + "," + this.getFile() + ")";
    }
}
