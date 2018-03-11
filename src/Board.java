import java.awt.Color;
import java.awt.Dimension;
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

    public Board(Game game) {
        hexes = new Hex[BOARD_SIZE][BOARD_SIZE]; // Check for nulls
        drawBoard();
        this.game = game;

        setLayout(null); // Don't use BorderLayout, else 8,8 disappears
        setPreferredSize(new Dimension(900, 900));
        setVisible(true);
    }

    public Board(Board b) {
        this.hexes = b.hexes;
    }

    /**
     * Empty constructor to use with the state space generator
     */
    public Board() {
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
                //game.decrementWhiteScore();
            } else {
                //game.decrementBlackScore();
            }
            hexes[sy][sx].setPiece(null);
            hexes[sy][sx].redraw();
        } else if (hexes[sy][sx] != null) {
            if (hexes[dy][dx] != null) {
                hexes[dy][dx].setPiece(hexes[sy][sx].getPiece().getColor());
                hexes[dy][dx].redraw();
                hexes[sy][sx].setPiece(null);
                hexes[sy][sx].redraw();
            }
        }
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
                    line.append(" " + b.getHex(x, y).getID() + " ");
                } else {
                    line.append((b.getHex(x, y).getPiece().getColor().equals(Color.BLACK)) ? " BB " : " WW ");
                }
            }
            System.out.println(line);
        }
    }
}