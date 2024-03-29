import java.awt.Color;
import java.awt.Dimension;
import java.util.TreeSet;
import javax.swing.JPanel;

public class Board extends JPanel {

    /**
     * Size of Hex display size for GUI
     */
    private static final int HEX_SIZE = 90;

    /**
     * Size of board game.
     */
    private static final int BOARD_SIZE = 9;

    /**
     * Array of Hex.
     */
    private Hex[][] hexes;

    /**
     * Integer half the size of BOARD_SIZE;
     */
    private static final int HALF_SIZE = 4;

    /**
     * Reference to Game object from Game
     */
    private Game game;

    Board(Game game) {
        hexes = new Hex[BOARD_SIZE][BOARD_SIZE]; // Check for nulls
        drawBoard();
        this.game = game;

        setLayout(null); // Don't use BorderLayout, else 8,8 disappears
        setPreferredSize(new Dimension(900, 900));
        setVisible(true);
    }

    /**
     * Copy Constructor. Recreates a new board in memory.
     * @param b Original board to copy from
     */
    Board(Board b) {
        hexes = new Hex[BOARD_SIZE][BOARD_SIZE]; // Check for nulls
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                if (b.hexes[y][x] != null) {
                    hexes[y][x] = new Hex(x, y);
                    if (b.hexes[y][x].getPiece() != null) {
                        hexes[y][x].setPiece(b.hexes[y][x].getPiece().getColor());
                    }
                }
            }
        }
    }

    /**
     * Empty constructor to use with the state space generator
     */
    Board() {
        hexes = new Hex[BOARD_SIZE][BOARD_SIZE]; // Check for nulls
        drawBoard();
    }

    /**
     * Generates the board hexes that stores an array of 9 x 9 hex.
     */
    private void drawBoard() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            int dx = (int) (Math.abs(HALF_SIZE - y) * ((double) HEX_SIZE / 2));
            if (y > HALF_SIZE) {
                for (int x = y - HALF_SIZE; x < BOARD_SIZE; x++) {
                    hexes[y][x] = new Hex(x, y);
                    hexes[y][x].setBounds(x * HEX_SIZE - dx, y * HEX_SIZE, HEX_SIZE, HEX_SIZE);
                    add(hexes[y][x]);
                }
            } else {
                for (int x = 0; x < BOARD_SIZE; x++) {
                    if (x < (HALF_SIZE) + 1 + y) {
                        hexes[y][x] = new Hex(x, y);
                        hexes[y][x].setBounds(x * HEX_SIZE + dx, y * HEX_SIZE, HEX_SIZE, HEX_SIZE);
                        add(hexes[y][x]);
                    }
                }
            }
        }
    }

    /**
     * Return hex from board given x and y coordinates. Returns null if out-of-bounds.
     * @param x X position
     * @param y Y position
     * @return Hex of specific X and Y position
     */
    public Hex getHex(int x, int y) {
        try {
            if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE || hexes[y][x] == null) {
                return null;
            }
            return hexes[y][x];
        } catch (Exception e) {
            System.err.print("ERROR: getHex out-of-bounds, " + x + y);
        }
        return null;
    }

    /**
     * Move piece from source hex to destination hex. If null space then throw away source hex.
     * Redraws board.
     * @param sx Source x position
     * @param sy Source y position
     * @param dx Destination x position
     * @param dy Destination y position
     */
    public void movePiece(int sx, int sy, int dx, int dy) {
        if (sx < 0 || sx >= BOARD_SIZE || sy < 0 || sy >= BOARD_SIZE || hexes[sy][sx] == null) { // Bad-Source
            return;
        } else if (dx < 0 || dx >= BOARD_SIZE || dy < 0 || dy >= BOARD_SIZE || hexes[dy][dx] == null) { // Move piece
            // off board
            if (hexes[sy][sx].getPiece().getColor().equals(Color.WHITE)) {
                game.decrementWhiteScore();
            } else {
                game.decrementBlackScore();
            }
            hexes[sy][sx].setPiece(null);
            hexes[sy][sx].redraw();
        } else if (hexes[sy][sx] != null && hexes[dy][dx] != null) {
                hexes[dy][dx].setPiece(hexes[sy][sx].getPiece().getColor());
                hexes[dy][dx].redraw();
                hexes[sy][sx].setPiece(null);
                hexes[sy][sx].redraw();
        }
    }

    /**
     * FOR STATESPACEGENERATOR ONLY.
     * Move piece from source hex to destination hex. If null space then throw away source hex.
     * Redraws board.
     * @param sx Source x position
     * @param sy Source y position
     * @param dx Destination x position
     * @param dy Destination y position
     * @param b Board with pieces to move
     * @return True if a piece was moved off the board. return false if bad inputs
     */
    public static boolean movePiece(int sx, int sy, int dx, int dy, Board b) {
        if (sx < 0 || sx >= BOARD_SIZE || sy < 0 || sy >= BOARD_SIZE || b.hexes[sy][sx] == null) { // Bad-Source
            return false;
        } else if (dx < 0 || dx >= BOARD_SIZE || dy < 0 || dy >= BOARD_SIZE || b.hexes[dy][dx] == null) { // Move piece
            // off board
            if (b.hexes[sy][sx].getPiece().getColor().equals(Color.WHITE)) {
                b.hexes[sy][sx].setPiece(null);
                return true;
            } else {
                b.hexes[sy][sx].setPiece(null);
                return true;
            }
        } else if (b.hexes[sy][sx] != null && b.hexes[dy][dx] != null) {
                b.hexes[dy][dx].setPiece(b.hexes[sy][sx].getPiece().getColor());
                b.hexes[sy][sx].setPiece(null);
        }
        return false;
    }

    /**
     * Update board to display default layout.
     */
    private void standardLayout() {
        for (int y = 0; y < 9; ++y) {
            if (Math.abs(HALF_SIZE - y) > 2) {
                for (int x = 0; x < 9; ++x) {
                    if (hexes[y][x] != null)
                        hexes[y][x].setPiece((y < HALF_SIZE) ? Color.WHITE : Color.BLACK);
                }
            }
        }
        hexes[2][2].setPiece(Color.WHITE);
        hexes[2][3].setPiece(Color.WHITE);
        hexes[2][4].setPiece(Color.WHITE);
        hexes[6][4].setPiece(Color.BLACK);
        hexes[6][5].setPiece(Color.BLACK);
        hexes[6][6].setPiece(Color.BLACK);
    }

    /**
     * Update board to display german daisy layout.
     */
    private void germanDaisy() {
        hexes[2][1].setPiece(Color.WHITE);
        hexes[2][5].setPiece(Color.BLACK);
        hexes[6][3].setPiece(Color.BLACK);
        hexes[6][7].setPiece(Color.WHITE);
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                if (Math.abs(x - 1) <= 1 && Math.abs(y - 2) <= 1 && (x + y) != 3)
                    hexes[y][x].setPiece(Color.WHITE);
                if (Math.abs(x - 5) <= 1 && Math.abs(y - 2) <= 1 && (x + y) != 7)
                    hexes[y][x].setPiece(Color.BLACK);
                if (Math.abs(x - 3) <= 1 && Math.abs(y - 6) <= 1 && (x + y) != 9)
                    hexes[y][x].setPiece(Color.BLACK);
                if (Math.abs(x - 7) <= 1 && Math.abs(y - 6) <= 1 && (x + y) != 13)
                    hexes[y][x].setPiece(Color.WHITE);
            }
        }
    }

    /**
     * Update board to display belgian daisy layout.
     */
    private void belgianDaisy() {
        hexes[1][1].setPiece(Color.WHITE);
        hexes[1][4].setPiece(Color.BLACK);
        hexes[7][4].setPiece(Color.BLACK);
        hexes[7][7].setPiece(Color.WHITE);
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                if (Math.abs(x - 1) <= 1 && Math.abs(y - 1) <= 1 && (x + y) != 2)
                    hexes[y][x].setPiece(Color.WHITE);
                if (Math.abs(x - 4) <= 1 && Math.abs(y - 1) <= 1 && (x + y) != 5)
                    hexes[y][x].setPiece(Color.BLACK);
                if (Math.abs(x - 4) <= 1 && Math.abs(y - 7) <= 1 && (x + y) != 11)
                    hexes[y][x].setPiece(Color.BLACK);
                if (Math.abs(x - 7) <= 1 && Math.abs(y - 7) <= 1 && (x + y) != 14)
                    hexes[y][x].setPiece(Color.WHITE);
            }
        }
    }

    /**
     * All pieces on board is cleaned off and set to 0 marbles.
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (hexes[i][j] != null && hexes[i][j].getPiece() != null) {
                    hexes[i][j].setPiece(null);
                    hexes[i][j].redraw();
                }
            }
        }
    }

    /**
     * Resets the board to one of 3 starting layouts.
     * 1 = Standard
     * 2 = Belgian Daisy
     * 3 = German Daisy
     * @param layout Integer value of board layout type
     */
    public void selectLayout(int layout) {
        clearBoard();
        switch (layout) {
            case 1:
                standardLayout();
                break;
            case 2:
                belgianDaisy();
                break;
            case 3:
                germanDaisy();
                break;
            default:
                System.err.println("Invalid param At Board.selectLayout(int layout)");
                break;
        }
    }

    /**
     * Returns the size of the board;
     * @return Integer size of board
     */
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Prints to System.out.print a text layout of the board
     */
    public static void printBoard(final Board b) {
        System.out.println();
        for (int y = 0; y < BOARD_SIZE; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (b.getHex(x,y) == null) {
                    line.insert(0, "  ");
                } else if (b.getHex(x, y).getPiece() == null) {
                    line.append(" ").append(b.getHex(x, y).getID()).append(" ");
                } else {
                    line.append((b.getHex(x, y).getPiece().getColor().equals(Color.BLACK)) ? " BB " : " WW ");
                }
            }
            System.out.println(line);
        }
    }

    /**
     * Generates the string representation of the pieces on the board.
     */
    @Override
    public String toString() {
        TreeSet<String> blackset = new TreeSet<>();
        TreeSet<String> whiteset = new TreeSet<>();
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                if (hexes[y][x] != null && hexes[y][x].getPiece() != null) {
                    StringBuilder line = new StringBuilder(Character.toString((char) (73 - y))); // Adds letter
                    if (y > HALF_SIZE) {
                        line.append(x - (y - HALF_SIZE) + 1);
                    } else {
                        line.append(x + (5 - y));
                    }
                    if (hexes[y][x].getPiece().getColor().equals(Color.BLACK)) {
                        line.append("b,");
                        blackset.add(line.toString());
                    } else {
                        line.append("w,");
                        whiteset.add(line.toString());
                    }
                }
            }
        }
        StringBuilder lineout = new StringBuilder();
        for (String s : blackset) {
            lineout.append(s);
        }
        for (String s : whiteset) {
            lineout.append(s);
        }
        return lineout.toString().substring(0, lineout.toString().length() - 1);
    }

    public Hex[][] getHexes() {
        return hexes;
    }
}