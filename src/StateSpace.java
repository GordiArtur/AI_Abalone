
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StateSpace is a utility object based off the StateSpaceGenerator. StateSpace acts as a node with
 * a copy of a board state, lists of all the valid Actions that can be played with the given board
 * state, and color for which marbles can be moved.
 *
 * @author Tony Lin
 * @version 1.0
 */
public class StateSpace {

    /**
     * Maximum size of the board
     */
    private static final int BOARD_SIZE = 9;

    /**
     * list of all the directions possible in a Abalone board game
     */
    private static final int direction[] = {-11, -10, -1, 1, 10, 11};

    /**
     * 2D int array of a Abalone board game.
     * 0 represents null/off-board space.
     * 1 represents empty space on board.
     * 2 represents black piece on a space
     * 3 represents white piece on a space.
     */
    private int[][] board;

    /**
     * Color of marbles that can be legally moved on the StateSpace board. Also means the who's
     * turn to move marbles on the board.
     * 2 represents black piece.
     * 3 represents white piece.
     */
    private int color;

    /**
     * Subset of moveList storing all the groups of 3-marbles in a Action.
     */
    private List<Action> triples;

    /**
     * Subset of moveList storing all the groups of 2-marbles in a Action.
     */
    private List<Action> doubles;

    /**
     * Subset of moveList storing all the groups of 1-marble in a Action.
     */
    private List<Action> singles;

    /**
     * The combined list of triples, doubles, and singles that have been tested by validMove()
     * and createValidMoves().
     */
    private List<Action> moveList;

    /**
     * Constructor. Creates a StateSpace that takes in a int[][] array Board and color representing the marbles that can
     * move.
     *
     * @param b int array of a Abalone board
     * @param c color of the marbles that can move
     */
    public StateSpace(int[][] b, int c) {
        this.board = b;
        this.color = c;
    }

    /**
     * Special Constructor. Converts Abalone Board object to an int Board for StateSpace to use.
     *
     * @param b int array of a Abalone board
     * @param c color of the marbles that can move
     */
    public StateSpace(Board b, Color c) {
        this.board = getIntBoard(b.getHexes());
        this.color = (c.equals(Color.BLACK)) ? 2 : 3;
    }

    /**
     * Tester that creates a standard Layout Board and tests StateSpace generation.
     * @param args
     */
    public static void Main(String args) {

    }

    /**
     * A method that simplifies the creation of the StateSpace. and it's validMoves
     */
    public void standardProcedure() {
        this.setMoveList(this.createValidMoves(this.getPieceCombination(this.board)));
    }

    /**
     * Prints to System.out.print a text layout of the board
     *
     * @param b 2D int array of a board
     */
    public static void printBoard(final int[][] b) {
        System.out.println();
        for (int y = 0; y < BOARD_SIZE; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (b[y][x] == 0) {
                    line.insert(0, "  ");
                } else if (b[y][x] == 1) {
                    line.append(" ").append(y).append(x).append(" ");
                } else {
                    line.append((b[y][x] == 2) ? " BB " : " WW ");
                }
            }
            System.out.println(line);
        }
    }

    /**
     * Converts current board object to 2D array of ints. 0 = null, 1 = empty hex, 2 = black, 3 = white
     *
     * @param hexes a Hex array that represents an Abalone board.
     * @return 2D array of ints representing an Abalone board.
     */
    private int[][] getIntBoard(Hex[][] hexes) {
        int intBoard[][] = new int[9][9];
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                if (hexes[y][x] == null) {
                    intBoard[y][x] = 0;
                } else if (hexes[y][x].getPiece() == null) {
                    intBoard[y][x] = 1;
                } else if (hexes[y][x].getPiece().getColor().equals(Color.BLACK)) {
                    intBoard[y][x] = 2;
                } else {
                    intBoard[y][x] = 3;
                }
            }
        }
        return intBoard;
    }

    /**
     * Returns a copy of the StateSpace board.
     *
     * @return 2D int array
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Generates a list of Action that contains groups of pieces, from the given board.
     *
     * @param board 2D in array of a Abalone board
     * @return List of Actions
     */
    public List<Action> getPieceCombination(int[][] board) {
        if (singles != null) { // Don't generate pieceCombination if data already exists.
            List<Action> PieceCombination = new ArrayList<>();
            PieceCombination.addAll(this.triples);
            PieceCombination.addAll(this.doubles);
            PieceCombination.addAll(this.singles);
            return PieceCombination;
        }

        // Initialize the triples, doubles, and singles array
        List<Action> singles = new ArrayList<>();
        List<Action> doubles = new ArrayList<>();
        List<Action> triples = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                IntHex first, second, third; // Create temporary marbles
                if (getHex(board, x, y) == this.color) { // First marble test
                    first = new IntHex(x, y, this.color);
                    singles.add(new Action(0, 0, first));
                    for (int i = 3; i < direction.length; ++i) {
                        int dx = (direction[i] % 10);
                        int dy = (direction[i] / 10);
                        if (getHex(board, x + dx, y + dy) == this.color) { // Second marble test
                            second = new IntHex(x + dx, y + dy, this.color);
                            doubles.add(new Action(0, 0, first, second));
                            if (getHex(board, x + dx + dx, y + dy + dy) == this.color) { // Third marble test
                                third = new IntHex(x + dx + dx, y + dy + dy, this.color);
                                triples.add(new Action(0, 0, first, second, third));
                            }
                        }
                    }
                }
            }
        }
        this.singles = singles;
        this.doubles = doubles;
        this.triples = triples;
        List<Action> PieceCombination = new ArrayList<>();
        PieceCombination.addAll(this.triples);
        PieceCombination.addAll(this.doubles);
        PieceCombination.addAll(this.singles);
        return PieceCombination;
    }

    /**
     * Return hex from 2D int array given x and y coordinates. Returns null if out-of-bounds.
     *
     * @param x X position
     * @param y Y position
     * @return int value at specific position on board
     */
    private int getHex(int[][] board, int x, int y) {
        try {
            if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
                return 0;
            }
            return board[y][x];
        } catch (Exception e) {
            System.err.print("ERROR: getHex out-of-bounds, " + x + y);
        }
        return 0;
    }

    /**
     * Tests all actions in pieceCombination if they are valid moves on the given board.
     *
     * @param pieceCombination List of actions that contain 0 0 for direction
     * @return List of Actions with direction
     */
    public List<Action> createValidMoves(List<Action> pieceCombination) {
        List<Action> validActions = new ArrayList<>();
        for (Action a : pieceCombination) {
            for (int Direction : direction) {
                int dx = (Direction % 10);
                int dy = (Direction / 10);
                Action test = new Action(dx, dy, a.selectedHex);
                if (validMove(test, this.board)) {
                    validActions.add(test);
                }
            }
        }
        return validActions;
    }

    /**
     * Assuming action is a valid Action, apply action on a copy of a board and return the
     * new board layout
     *
     * @param a Valid action
     * @param board Integer layout of the game board
     * @return resulting board after action is applied.
     */
    public int[][] getNextBoard(final Action a, final int[][] board) {
        List<IntHex> pieceList;
        if (a.inlineHex != null) {
            pieceList = new ArrayList<>(a.inlineHex);
        } else {
            pieceList = new ArrayList<>(a.selectedHex);
        }
        for (IntHex h : pieceList) {
            int sx = h.x;
            int sy = h.y;
            int dx = h.x + a.dx;
            int dy = h.y + a.dy;
            if (dx < 0 || dx >= BOARD_SIZE || dy < 0 || dy >= BOARD_SIZE || board[dy][dx] == 0) {
                // Pushing marble off the board
                board[sy][sx] = 1;
            } else if (board[dy][dx] > 0) {
                // Standard pushing marble on the board
                board[dy][dx] = board[sy][sx];
                board[sy][sx] = 1;
            }
        }
        return board;
    }

    /**
     * Return a copy of the moveList
     * @return List of Actions
     */
    public List<Action> getMoveList() {
        return moveList;
    }

    /**
     * Sets the valid moveList of the StateSpace.
     * @param a List of valid Actions
     */
    public void setMoveList(List<Action> a) {
        moveList = a;
    }

    /**
     * Verifies if an Action is valid on a given board. It first tests if the move is a
     * broadside or inline move. Test if moves have blocking marbles in their path, or are
     * self-elimination. Else return True if move is valid.
     *
     * @param action Action with valid direction and list of IntHex > 0
     * @param board 2D array representing an Abalone board
     * @return True if action performed on board is valid
     */
    private boolean validMove(Action action, int[][] board) { // Read it, it's less long
        int identity = 0;
        int didentity = Math.abs(action.dx) * 10 + Math.abs(action.dy);
        int sx, sy;
        if (action.selectedHex.size() > 1) { // creates an identity for testing if move is inline
            identity = identity(action.selectedHex.get(0).x, action.selectedHex.get(0).y, action.selectedHex.get(1).x,
                action.selectedHex.get(1).y);
        }
        if (identity > 0 && identity == didentity) { // Inline
            sx = action.selectedHex.get(0).x;
            sy = action.selectedHex.get(0).y;
            if (getHex(board, sx + action.dx, sy + action.dy) == 0) { // Empty space or Off-board
                return false;
            } else if (getHex(board, sx + action.dx, sy + action.dy) == 1) {
                return true;
            } else { // Inline Sumito checks
                action.inlineHex = new ArrayList<>(5);
                for (int i = 1; i <= action.selectedHex.size(); i++) {
                    int hex = getHex(board, sx + (action.dx * i), sy + (action.dy * i));
                    if (hex == 0) { // Off board (AI should not self-eliminate)
                        return true; // TURN TO FALSE TO PREVENT SUICIDE
                    } else if (hex == 1) { // Empty space
                        break;
                    } else if (hex == color) { // Same color blocker
                        return false;
                    } else if (i == action.selectedHex.size() && hex > 1) { // Last piece blocker
                        return false;
                    } else { // Add this piece to temp; piece to be moved.
                        action.inlineHex.add(new IntHex(sx + (action.dx * i), sy + (action.dy * i), hex));
                    }
                }
                Collections.reverse(action.inlineHex);
                action.inlineHex.addAll(action.selectedHex);
                return true;
            }
        } else { // Broadside and singular
            for (IntHex hex : action.selectedHex) {
                sx = hex.x;
                sy = hex.y;
                if (getHex(board, sx + action.dx, sy + action.dy) == 0) { // Moving off the board, NO SELF-ELIMINATION
                    return false;
                } else if (getHex(board, sx + action.dx, sy + action.dy) > 1) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Outputs an integer that represents the axial direction on a Abalone board.
     * 1 is vertical
     * 10 is horizontal
     * 11 is diagonal
     *
     * @param sx First hex x coordinate
     * @param sy First hex y coordinate
     * @param dx Last hex x coordinate
     * @param dy Last hex y coordinate
     * @return Integer Identity of axial direction
     */
    private int identity(int sx, int sy, int dx, int dy) {
        int out = 0;
        if ((dx + dy) == (sx + sy)) {
            return 0;
        }
        out += (Math.abs(dx - sx) == 1) ? 10 : 0;
        out += (Math.abs(dy - sy) == 1) ? 1 : 0;
        return (Math.abs(dx - sx) > 1 || Math.abs(dy - sy) > 1) ? 0 : out;
    }

    /**
     * Returns a count of black marbles on a 2D Abalone board
     *
     * @param board 2D array representing an Abalone board
     * @return count of 2's on the board
     */
    private int getBlackCount(int[][] board) {
        int count = 0;
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                if (board[y][x] == 2) {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * Returns a count of white marbles on a 2D Abalone board
     *
     * @param board 2D array representing an Abalone board
     * @return count of 3's on the board
     */
    private int getWhiteCount(int[][] board) {
        int count = 0;
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                if (board[y][x] == 3) {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * Represents a move on a Abalone board. Holds up to three marbles of a move, and 2 values
     * for direction. Also holds a special list for if move is inline.
     */
    private class Action {

        List<IntHex> selectedHex;
        int dy;
        int dx;
        List<IntHex> inlineHex = null;

        /**
         * Default constructor of Action.
         *
         * @param dx X direction of Action
         * @param dy Y direction of Action
         * @param selectedHex List of IntHex marbles
         */
        Action(int dx, int dy, List<IntHex> selectedHex) {
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
        Action(int dx, int dy, IntHex... hex) {
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
            for (IntHex h : selectedHex) {
                s.append(h.getXY()).append(" ");
            }
            return s.toString();
        }
    }

    /**
     * Represents a marble on an Abalone board. Holds the X and Y coordinates, and the color in
     * integer form; 2 for black and 3 for white.
     */
    private class IntHex {

        int x;
        int y;
        int color;

        /**
         * Default constructor of IntHex.
         *
         * @param x X position of marble
         * @param y Y position of marble
         * @param color Color of marble
         */
        IntHex(int x, int y, int color) {
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
}