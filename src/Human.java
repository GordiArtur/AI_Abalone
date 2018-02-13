import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Created by Artur Gordiyenko on 2018-02-06.
 */
public class Human extends JPanel implements Agent {

	private List<Hex> selectedHex;
	private int activeTurn;
	// private Board board;

	public Human(Board board) {
		// this.board = board;
		activeTurn = 0;
		selectedHex = new ArrayList<Hex>();
		setLayout(null);
		setPreferredSize(new Dimension(900, 100));
		setBackground(Color.GREEN);
		setVisible(true);

		for (int i = 0; i < Board.BOARD_SIZE; ++i) {
			for (int j = 0; j < Board.BOARD_SIZE; ++j) {
				if (board.getHex(i, j) != null)
					board.getHex(i, j).addMouseListener(new MouseListener());
			}
		}
	}

	public void setActiveTurn(int i) {
		activeTurn = i;
	}

	@Override
	public void play() {

	}

	class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (Game.turn == activeTurn) {
				Hex selectHex = null;
				try {
					selectHex = (Hex) e.getSource();
				} catch (NullPointerException npe) {

				}
				if (selectHex != null && selectHex.getPiece() != null) {
					// System.out.println(selectHex.getID());
					if (Game.turn == 0 && selectHex.getPiece().getColor().equals(Color.BLACK)) {
						addToSelection(selectHex);
					} else if (Game.turn == 1 && selectHex.getPiece().getColor().equals(Color.WHITE)) {
						addToSelection(selectHex);
					}
				}
			}
		}

		private void addToSelection(Hex hex) {
			if (selectedHex.size() == 0) {
				selectedHex.add(hex);
				hex.setColor(Color.CYAN);
			} else if (selectedHex.contains(hex)) { // Removes existing hex
				if (selectedHex.size() == 3 && selectedHex.get(2) != hex) {
					for (Hex h : selectedHex) {
						h.setDefaultColor();
					}
					selectedHex.clear();
				} else {
					hex.setDefaultColor();
					selectedHex.remove(hex);
				}
			} else if (selectedHex.size() < 3
					&& hex.getPiece().getColor().equals(selectedHex.get(0).getPiece().getColor())) {
				if (selectedHex.size() == 1) { // groups of 2
					int dx = hex.getXpos();
					int dy = hex.getYpos();
					int sx = selectedHex.get(0).getXpos();
					int sy = selectedHex.get(0).getYpos();
					// System.out.println("CHECK: " + dx + dy + "\n" + sx + sy);
					// System.out.println("" + identity(sx, sy, dx, dy));
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
					/// System.out.println("CHECK: " + dx + dy + "\n" + sx + sy + "\n" + ix + iy);
					int ds = identity(dx, dy, sx, sy);
					int di = identity(dx, dy, ix, iy);
					int is = identity(ix, iy, sx, sy);
					// System.out.println("" + ds + di + is);
					if ((ds + di + is == 2 || ds + di + is == 20 || ds + di + is == 22)
							&& (ds == di || di == is || ds == is)) {
						selectedHex.add(hex);
						hex.setColor(Color.CYAN);
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
