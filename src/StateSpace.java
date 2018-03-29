import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class StateSpace {

    private static final int BOARD_SIZE = 9;
    private int[][] board;
    private int color;
    private List<List<intHex>> pieceCombination;
    private static final int direction[] = {-11, -10, -1, 1, 10, 11};

    public StateSpace(Board b, Color c) {
        this.board = getIntBoard(b.getHexes());
        this.color = (c.equals(Color.BLACK)) ? (int) 1 : (int) 0;
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

    public List<List<intHex>> getPieceCombination() {
        if (pieceCombination != null) { // Don't generate pieceCombination twice
            return pieceCombination;
        }
        List<List<intHex>> singles = new ArrayList<>();
        List<List<intHex>> doubles = new ArrayList<>();
        List<List<intHex>> triples = new ArrayList<>();
        List<intHex> temp;
        for (int y = BOARD_SIZE - 1; y >= 0; --y) {
            for (int x = BOARD_SIZE - 1; x >= 0; --x) {
                if (this.board[y][x] == color) {
                    temp = new ArrayList<>(1);
                    temp.add(new intHex(x, y, color));
                    singles.add(temp);
                    for (int i = 0; i < 3; ++i) {
                        int dx = (direction[i] % 10);
                        int dy = (direction[i] / 10);
                        if (board[y + dy][x + dx] == color) {
                            temp = new ArrayList<>(2);
                            temp.add(new intHex(x, y, color));
                            temp.add(new intHex((x + dx), (y + dy), color));
                            doubles.add(temp);
                            if (board[y + dy + dy][x + dx + dx] == color) {
                                temp = new ArrayList<>(3);
                                temp.add(new intHex(x, y, color));
                                temp.add(new intHex((x + dx), (y + dy), color));
                                temp.add(new intHex((x + dx + dx), (y + dy + dy), color));
                                triples.add(temp);
                            }
                        }
                    }
                }
            }
        }
        pieceCombination = new ArrayList<>();
        pieceCombination.addAll(triples);
        pieceCombination.addAll(doubles);
        pieceCombination.addAll(singles);
        return pieceCombination;
    }

    private class intHex {
        public int x;
        public int y;
        public int color;
        intHex(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}