import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Board extends JPanel {

    private static final int HEX_SIZE = 90;
    private static final int BOARD_SIZE = 9;
    private Hex[][] hexes;
    private static final int HALF_SIZE = 4;;
    private static final int START_PIECE_COUNT = 14;
    private int whiteCount;
    private int blackCount;

    public Board() {

        whiteCount = START_PIECE_COUNT;
        blackCount = START_PIECE_COUNT;

        hexes = new Hex[BOARD_SIZE][BOARD_SIZE]; // Check for nulls
        drawBoard();

        setLayout(null); // Don't use BorderLayout, else 8,8 disappears
        selectLayout(1);
        setPreferredSize(new Dimension(900, 900));
        setVisible(true);

    }

    // Draws the board in a hexagon shape
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

    public Hex getHex(int x, int y) {
        try {
            if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE || hexes[y][x] == null) {
                return null;
            }
            if ((x >= 0 || x < BOARD_SIZE) && (y >= 0 && y < BOARD_SIZE)) {
                if (hexes[y][x] != null) {
                    return hexes[y][x];
                }
            }
        } catch (Exception e) {
            System.err.print("ERROR: getHex out-of-bounds, " + x + y);
        }
        return null;
    }

    /**
        * @return the whiteCount
        */
    public int getWhiteCount() {
        return whiteCount;
    }

    /**
        * @return the blackCount
        */
    public int getBlackCount() {
        return blackCount;
    }

    public void movePiece(int sx, int sy, int dx, int dy) {
        if (sx < 0 || sx >= BOARD_SIZE || sy < 0 || sy >= BOARD_SIZE || hexes[sy][sx] == null) { // Bad-Source
            return;
        } else if (dx < 0 || dx >= BOARD_SIZE || dy < 0 || dy >= BOARD_SIZE || hexes[dy][dx] == null) { // Move piece
                                                                                                        // off board
            if (hexes[sy][sx].getPiece().getColor().equals(Color.WHITE)) {
                whiteCount--;
            } else {
                blackCount--;
            }
            hexes[sy][sx].setPiece(null);
            hexes[sy][sx].redraw();
        } else if ((sx >= 0 || sx < BOARD_SIZE) && (sy >= 0 && sy < BOARD_SIZE) && hexes[sy][sx] != null) {
            if ((dx >= 0 || dx < BOARD_SIZE) && (dy >= 0 && dy < BOARD_SIZE) && hexes[dy][dx] != null) {
                hexes[dy][dx].setPiece(hexes[sy][sx].getPiece().getColor());
                hexes[dy][dx].redraw();
                hexes[sy][sx].setPiece(null);
                hexes[sy][sx].redraw();
            }
        }
    }

    // Sets standard board configuration
    public void standardLayout() {
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

    public void germanDaisy() {
        hexes[2][2].setPiece(Color.WHITE);
        hexes[2][5].setPiece(Color.BLACK);
        hexes[3][6].setPiece(Color.BLACK);
        hexes[7][6].setPiece(Color.WHITE);
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

    public void belgianDaisy() {
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

    public void clearBoard() {
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
     * 1 = Standard 2 = Belgian Daisy 3 = German Daisy
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
        */
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}

