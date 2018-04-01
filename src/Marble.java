/**
 * Represents a marble on an Abalone board. Holds the X and Y coordinates, and the color in
 * integer form; 2 for black and 3 for white.
 */
public class Marble {

    private int x;
    private int y;
    private int color;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Default constructor of Marble.
     *
     * @param x X position of marble
     * @param y Y position of marble
     * @param color Color of marble
     */
    Marble(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Returns the coordinate of the marble in a YX integer, Y * 10 + X.
     *
     * @return integer of marble location
     */
    public int getXY() {
        return y * 10 + x;
    }
}