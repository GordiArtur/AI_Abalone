import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StateSpace {

    private static final int BOARD_SIZE = 9;
    private int[][] board;
    private int color;
    //private List<List<IntHex>> pieceCombination;
    private List<Action> triples;
    private List<Action> doubles;
    private List<Action> singles;
    private List<Action> moveList;
    private static final int direction[] = {-11, -10, -1, 1, 10, 11};

    /**
     * Provides a demostration of the StateSpace for 10 moves. Outputs to console a grid layout
     * of the board and move.
     * @param args
     */
    public static void main(String args[]) {
        Board b = new Board();
        b.selectLayout(1);

        StateSpace SS = new StateSpace(b, Color.BLACK);
        for (int k = 0; k < 10; k++) {
            SS.setMoveList(SS.createValidMoves(SS.getPieceCombination()));
            Random rand = new Random();
            Action play = SS.getMoveList().get(rand.nextInt(SS.getMoveList().size()));
            System.out.println(play);
            int[][] newBoard = SS.getNextBoard(play, SS.getBoard());
            StateSpace.printBoard(newBoard);
            if (SS.color == 2) {
                SS = new StateSpace(newBoard, Color.WHITE);
            } else if (SS.color == 3) {
                SS = new StateSpace(newBoard, Color.BLACK);
            }
        }
    }

    public StateSpace(Board b, Color c) {
        this.board = getIntBoard(b.getHexes());
        this.color = (c.equals(Color.BLACK)) ? (int) 2 : (int) 3;
    }

    public StateSpace(int[][] b, Color c) {
        this.board = b;
        this.color = (c.equals(Color.BLACK)) ? (int) 2 : (int) 3;
    }

    /**
     * Converts current board object to 2D array of ints. 0 = null, 1 = empty hex, 2 = black, 3 = white
     *
     * @return 2D array of ints representing this Board.
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

    public int[][] getBoard() {
        return board;
    }

    /**
     * YOU SHOULD ONLY USE THIS ONCE!
     */
    public List<Action> getPieceCombination() {
        if (singles != null) { // Don't generate pieceCombination twice
            List<Action> PieceCombination = new ArrayList<>();
            PieceCombination.addAll(this.triples);
            PieceCombination.addAll(this.doubles);
            PieceCombination.addAll(this.singles);
            return PieceCombination;
        }
        List<Action> singles = new ArrayList<>();
        List<Action> doubles = new ArrayList<>();
        List<Action> triples = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                IntHex first, second, third;
                if (getHex(board, x, y) == color) {
                    first = new IntHex(x, y, color);
                    singles.add(new Action(0, 0, first));
                    for (int i = 3; i < direction.length; ++i) {
                        int dx = (direction[i] % 10);
                        int dy = (direction[i] / 10);
                        if (getHex(board, x + dx, y + dy) == color) {
                            second = new IntHex(x + dx, y + dy, color);
                            doubles.add(new Action(0, 0, first, second));
                            if (getHex(board, x + dx + dx, y + dy + dy) == color) {
                                third = new IntHex(x + dx + dx, y + dy + dy, color);
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
     * Return hex from board given x and y coordinates. Returns null if out-of-bounds.
     * @param x X position
     * @param y Y position
     * @return int value at specific position on board
     */
    public int getHex(int[][] board, int x, int y) {
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
     * Processing function that tests all moveInputs in pieceCombination
     *
     * @param pieceCombination List of actions that contain 0 0 for direction
     */
    public List<Action> createValidMoves(List<Action> pieceCombination) {
        List<Action> validActions = new ArrayList<>();
        for (Action a : pieceCombination) {
            for (int Direction : direction) {
                int dx = (Direction % 10);
                int dy = (Direction / 10);
                Action test = new Action(dx, dy, a.selectedHex);
                if (validMove(test)) {
                    if (test.inlineHex != null) {
                        System.out.println(test.inlineHex);
                    }
                    validActions.add(test);
                }
            }
        }
        return validActions;
    }

    /**
     * Assuming action is a valid Action, apply action on a copy of a board and return the new board layout
     * @param a Valid action
     * @param board Integer layout of the game board
     * @return resulting board after action is applied.
     */
    public int[][] getNextBoard(final Action a, final int[][] board) {
        int[][] tempBoard = board;
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
            if (dx < 0 || dx >= BOARD_SIZE || dy < 0 || dy >= BOARD_SIZE || tempBoard[dy][dx] == 0) {
                // Pushing marble off the board
                tempBoard[sy][sx] = 1;
            } else if (tempBoard[dy][dx] > 0) {
                // Standard pushing marble on the board
                tempBoard[dy][dx] = tempBoard[sy][sx];
                tempBoard[sy][sx] = 1;
            }
        }
        return tempBoard;
    }

    public void setMoveList(List<Action> a) {
        moveList = a;
    }

    public List<Action> getMoveList() {
        return moveList;
    }

    /**
     * Holds the three marbles that will be moved, and 2 values for direction
     */
    private class Action {

        List<IntHex> selectedHex;
        int dy;
        int dx;
        List<IntHex> inlineHex = null;

        Action(int dx, int dy, List<IntHex> selectedHex) {
            this.dx = dx;
            this.dy = dy;
            this.selectedHex = selectedHex;
        }

        Action(int dx, int dy, IntHex... hex) {
            this.dx = dx;
            this.dy = dy;
            selectedHex = new ArrayList<>();
            selectedHex.addAll(Arrays.asList(hex));
        }

        @Override
        public String toString() {
            String s = "A:" + dy + dx + " | ";
            for (IntHex h : selectedHex) {
                s += h.getXY() + " ";
            }
            return s;
        }
    }

    private class IntHex {

        int x;
        int y;
        int color;

        IntHex(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public int getXY() {
            return y * 10 + x;
        }
    }

    /**
     * Checks if the selected hexes and called action is a legal move. If so, apply Board.playedMove(). 1. validMove
     * sorts selectedHex array and reverses if moving away from origin (Origin is Top-Left) 2. Generate special identity
     * of direction 3. Generate identity of selectedHex array 4. First check identity to direction identity, Inline 4.1.
     * If adjacent hex is empty space or off-board area, playedMove and return true 4.2. Else create empty temp list.
     * Checks for gaps, same color pieces, and last piece. 4.3. Reverse temp, add selectedHex to temp, 5 Second check
     * for Broadside and single piece 5.1. Check for available space, move then return true 6. movePiece, clear
     * selected, switchTurn() for 4.1, 4.2, and 5 move scenarios return false for nullptr and blocked moves return true
     * for validMove (assumes Board.playedMove() is successful)
     */
    private boolean validMove(Action a) { // Read it, it's less long
//        if (a.dx > 0 || a.dy > 0) {
//            Collections.reverse(a.selectedHex);
//        }
        int identity = 0;
        int didentity = Math.abs(a.dx) * 10 + Math.abs(a.dy);
        int sx, sy;
        if (a.selectedHex.size() > 1) {
            identity = identity(a.selectedHex.get(0).x, a.selectedHex.get(0).y, a.selectedHex.get(1).x,
                a.selectedHex.get(1).y);
        }
        if (identity > 0 && identity == didentity) { // Inline
            sx = a.selectedHex.get(0).x;
            sy = a.selectedHex.get(0).y;
            // Empty space or Off-board
            if (getHex(board, sx + a.dx, sy + a.dy) == 0) {
                return false;
            } else if (getHex(board, sx + a.dx, sy + a.dy) == 1) {
                return true;
            } else { // Inline Sumito checks
                a.inlineHex = new ArrayList<>(5);
                for (int i = 1; i <= a.selectedHex.size(); i++) {
                    int hex = getHex(board, sx + (a.dx * i), sy + (a.dy * i));
                    if (hex == 0) { // Off board (AI should not self-eliminate)
                        return false;
                    } else if (hex == 1) { // Empty space
                        break;
                    } else if (hex == color) { // Same color blocker
                        return false;
                    } else if (i == a.selectedHex.size() && hex > 1) { // Last piece blocker
                        return false;
                    } else { // Add this piece to temp; piece to be moved.
                        int oppositeColor = hex;
                        a.inlineHex.add(new IntHex(sx + (a.dx * i), sy + (a.dy * i), oppositeColor));
                    }
                }
                Collections.reverse(a.inlineHex);
                a.inlineHex.addAll(a.selectedHex);
                return true;
            }
        } else { // Broadside and singular
            for (IntHex hex : a.selectedHex) {
                sx = hex.x;
                sy = hex.y;
                if (getHex(board, sx + a.dx, sy + a.dy) == 0) { // Moving off the board, NO SELF-ELIMINATION
                    return false;
                } else if (getHex(board, sx + a.dx, sy + a.dy) > 1) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Outputs an integer that represents the axial direction of the elements in selectedHex. 1 is vertical 10 is
     * horizontal 11 is diagonal
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
     * Prints to System.out.print a text layout of the board
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
}