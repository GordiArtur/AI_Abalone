import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Board extends JPanel {

	public static final int BOARD_SIZE = 9;
	private Hex[][] hexes = new Hex[BOARD_SIZE][BOARD_SIZE]; // Check for nulls
	private List<Hex> selectedHex;
	public static final int HALF_SIZE = 4;;
	private int whiteCount;
	private int blackCount;

	public Board() {
		selectedHex = new ArrayList<Hex>();
		setLayout(null); // Don't use BorderLayout, else 8,8 disappears
		drawBoard();
		standardLayout();
		//belgianDaisy();
		//germanDaisy();
		setBorder(new LineBorder(Color.RED, 2));
		setPreferredSize(new Dimension(900, 900));
		setVisible(true);
		whiteCount = 14;
		blackCount = 14;
	}

	// Draws the board in a hexagon shape
	private void drawBoard() {
		for (int y = 0; y < BOARD_SIZE; y++) {
			int dx = Math.abs(HALF_SIZE - y) * 50;
			if (y > HALF_SIZE) {
				for (int x = y - HALF_SIZE; x < BOARD_SIZE; x++) {
					hexes[y][x] = new Hex(true, x, y);
					hexes[y][x].setBounds(x * 100 - dx, y * 100, 100, 100);
					add(hexes[y][x]);
					hexes[y][x].addMouseListener(new MouseListener());
				}
			} else {
				for (int x = 0; x < BOARD_SIZE; x++) {
					if (x < (HALF_SIZE) + 1 + y) {
						hexes[y][x] = new Hex(true, x, y);
						hexes[y][x].setBounds(x * 100 + dx, y * 100, 100, 100);
						add(hexes[y][x]);
						hexes[y][x].addMouseListener(new MouseListener());
					}
				}
			}
		}
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

	public Hex getHex(int x, int y) {
		if (x >= 0 || x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && hexes[y][x] != null)
			return hexes[y][x];
		return null;
	}

	public void movePiece(int sx, int sy, int dx, int dy) {
		if (sx >= 0 || sx < BOARD_SIZE && sy >= 0 && sy < BOARD_SIZE && hexes[sy][sx] != null) {
			if (dx >= 0 || dx < BOARD_SIZE && dy >= 0 && dy < BOARD_SIZE && hexes[dy][dx] != null) {
				hexes[dy][dx].setPiece(hexes[sy][sx].getPiece().getColor());
				hexes[sy][sx].setPiece(null);
			} else { // Move piece off board
				hexes[sy][sx].setPiece(null);
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

	class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			Hex selectHex = null;
			try {
				selectHex = (Hex) e.getSource();
			} catch (NullPointerException npe) {

			}
			if (selectHex != null) {
				System.out.println(selectHex.getID());
				addToSelection(selectHex);
			}
		}

		private void addToSelection(Hex hex) {
			if (hex.getPiece() != null) {
				if (selectedHex.size() == 0) {
					selectedHex.add(hex);
					hex.setColor(Color.CYAN);
				} else if (selectedHex.contains(hex)) { // Removes existing hex
					hex.setDefaultColor();
					selectedHex.remove(hex);
				} else if (selectedHex.size() < 3
						&& hex.getPiece().getColor().equals(selectedHex.get(0).getPiece().getColor())) {
					if (selectedHex.size() == 1) { // groups of 2
						int dx = hex.getXpos();
						int dy = hex.getYpos();
						int sx = selectedHex.get(0).getXpos();
						int sy = selectedHex.get(0).getYpos();
						System.out.println("CHECK: " + dx + dy + "\n" + sx + sy);
						System.out.println("" + identity(sx, sy, dx, dy));
						if (identity(sx, sy, dx, dy) > 0) {
							selectedHex.add(hex);
							hex.setColor(Color.CYAN);
						}
					} else if (selectedHex.size() == 2) { // groups of 3
						int dx = hex.getXpos();
						int dy = hex.getYpos();
						int sx = selectedHex.get(0).getXpos();
						int sy = selectedHex.get(0).getYpos();
						int ix = selectedHex.get(1).getXpos();
						int iy = selectedHex.get(1).getYpos();
						System.out.println("CHECK: " + dx + dy + "\n" + sx + sy + "\n" + ix + iy);
						int ds = identity(dx, dy, sx, sy);
						int di = identity(dx, dy, ix, iy);
						int is = identity(ix, iy, sx, sy);
						System.out.println("" + ds + di + is);
						if ((ds + di + is == 2 || ds + di + is == 20 || ds + di + is == 22)
								&& (ds == di || di == is || ds == is)) {
							selectedHex.add(hex);
							hex.setColor(Color.CYAN);
						}
					}

				}
			}
		}

		// Outputs in 1, 10, 11 (1,1 | 1,0 | 0,1) for direction
		// Outputs 0 for error
		private int identity(int sx, int sy, int dx, int dy) {
			int out = 0;
			if ((dx + dy) == (sx + sy)) {
				return 0;
			}
			out += (Math.abs(dx - sx) == 1) ? 10 : 0;
			out += (Math.abs(dy - sy) == 1) ? 1 : 0;
			return (Math.abs(dx - sx) > 1 || Math.abs(dy - sy) > 1) ? 0 : out;
		}
	}

}