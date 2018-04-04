import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a move on a Abalone board. Holds up to three marbles of a move, and 2 values
 * for direction. Also holds a special list for if move is inline.
 */
public class Action {

    private List<Marble> selectedHex;
    private int dy;
    private int dx;
    private List<Marble> inlineHex = null;

    public List<Marble> getSelectedHex() {
        return selectedHex;
    }

    public void setSelectedHex(List<Marble> selectedHex) {
        this.selectedHex = selectedHex;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public List<Marble> getInlineHex() {
        return inlineHex;
    }

    public void setInlineHex(List<Marble> inlineHex) {
        this.inlineHex = inlineHex;
    }

    /**
     * Returns a List of Hex objects instead of Marble objects
     * @return List of Marbles as Hex objects
     */
    public List<Hex> getSelectedHexList(Board board) {
        List<Hex> hexList = new ArrayList<>();
        for (Marble marble : selectedHex) {
            hexList.add(board.getHex(marble.getX(), marble.getY()));
        }
        return hexList;
    }

    /**
     * Default constructor of Action.
     *
     * @param dx X direction of Action
     * @param dy Y direction of Action
     * @param selectedHex List of IntHex marbles
     */
    Action(int dx, int dy, List<Marble> selectedHex) {
        this.dx = dx;
        this.dy = dy;
        this.selectedHex = selectedHex;
    }

    /**
     * Constructor of Action. Easier to input a single Marble.
     *
     * @param dx X direction of Action
     * @param dy Y direction of Action
     * @param hex IntHex marbles
     */
    Action(int dx, int dy, Marble... hex) {
        this.dx = dx;
        this.dy = dy;
        selectedHex = new ArrayList<>();
        selectedHex.addAll(Arrays.asList(hex));
    }

    /**
     * Outputs a string representation of an Action. Useful for console logging.
     *
     * @return String representation of Action.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("A:").append(dy).append(dx).append(" | ");
        for (Marble h : selectedHex) {
            s.append(h.getY()).append(h.getX()).append(" ");
        }
        return s.toString();
    }
}