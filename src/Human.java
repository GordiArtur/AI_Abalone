import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Created by Artur Gordiyenko on 2018-02-06.
 */
public class Human extends JPanel implements Agent{

	private List<Hex> selectedHex;
	private int activeTurn;
	private Board board;
	private JButton NE, E, SE, SW, W, NW;

	public Human(Board board) {
		this.board = board;
		activeTurn = 0;
		selectedHex = new ArrayList<Hex>();
		setLayout(new GridLayout(1, 6));
		setPreferredSize(new Dimension(900, 30));
		setVisible(true);
		createMovementControls();
		createMouseListener();

	}

	/**
		* Creates the mouse listener for the board.
		*/
	private void createMouseListener() {
		for (int i = 0; i < board.getBoardSize(); ++i) {
			for (int j = 0; j < board.getBoardSize(); ++j) {
				if (board.getHex(i, j) != null)
					board.getHex(i, j).addMouseListener(new MouseListener());
			}
		}
	}

	private void createMovementControls() {
		NE = new JButton("North-East");
		E = new JButton("East");
		SE = new JButton("South-East");
		SW = new JButton("South-West");
		W = new JButton("West");
		NW = new JButton("North-West");

		NW.addActionListener(new MovementListener());
		W.addActionListener(new MovementListener());
		SW.addActionListener(new MovementListener());
		SE.addActionListener(new MovementListener());
		E.addActionListener(new MovementListener());
		NE.addActionListener(new MovementListener());

		add(NW);
		add(W);
		add(SW);
		add(SE);
		add(E);
		add(NE);
	}

	public void setActiveTurn(int i) {
		activeTurn = i;
	}

	public Board play(Board board) {
		Game.turn = activeTurn;
		this.board = board;
		while (Game.turn != activeTurn) {
			
		}
		return this.board;
	}

	public void sortSelected() {
		if (selectedHex.size() == 3) {
			List<Hex> unsorted = new ArrayList<Hex>(selectedHex);
			List<Hex> temp = new ArrayList<Hex>();
			Hex a = unsorted.get(0);
			Hex b = unsorted.get(1);
			Hex c = unsorted.get(2);
			temp.add((a.getXY() < b.getXY()) ? (a.getXY() < c.getXY()) ? a : c : (b.getXY() < c.getXY()) ? b : c);
			unsorted.remove(temp.get(0));
			temp.add((unsorted.get(0).getXY() < unsorted.get(1).getXY()) ? unsorted.get(0) : unsorted.get(1));
			unsorted.remove(temp.get(1));
			temp.add(unsorted.get(0));
			selectedHex = new ArrayList<Hex>(temp);
		} else if (selectedHex.size() == 2) {
			Hex small = (selectedHex.get(0).getXY() < selectedHex.get(1).getXY()) ? selectedHex.get(0)
					: selectedHex.get(1);
			Hex large = (selectedHex.get(0).getXY() > selectedHex.get(1).getXY()) ? selectedHex.get(0)
					: selectedHex.get(1);
			selectedHex.set(0, small);
			selectedHex.set(1, large);
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

	class MovementListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean played = false;
			if (activeTurn == Game.turn && !selectedHex.isEmpty()) {
				System.out.println("" + e.getActionCommand());
				switch (e.getActionCommand()) {
				case ("North-East"):
					played = validMove(0, -1);
					break;
				case ("East"):
					played = validMove(1, 0);
					break;
				case ("South-East"):
					played = validMove(1, 1);
					break;
				case ("South-West"):
					played = validMove(0, 1);
					break;
				case ("West"):
					played = validMove(-1, 0);
					break;
				case ("North-West"):
					played = validMove(-1, -1);
					break;
				default:
					break;
				}
			}
			if (played) {
				Game.turn = (Game.turn == 0) ? 1 : 0;
			}
		}

		private boolean validMove(int dx, int dy) { // Don't read it. It's very long
			sortSelected();
			if (dx > 0 || dy > 0) {
				Collections.reverse(selectedHex);
			}
			System.out.println("Origin Point " + selectedHex.get(0).getID());
			int identity = 0;
			int didentity = Math.abs(dx) * 10 + Math.abs(dy);
			int sx, sy;
			if (selectedHex.size() > 1) {
				identity = identity(selectedHex.get(0).getXpos(), selectedHex.get(0).getYpos(),
						selectedHex.get(1).getXpos(), selectedHex.get(1).getYpos());
			}
			if (identity > 0 && identity == didentity) { // Inline
				sx = selectedHex.get(0).getXpos();
				sy = selectedHex.get(0).getYpos();
				// Empty space or Off-board
				if (board.getHex(sx + dx, sy + dy) == null || board.getHex(sx + dx, sy + dy).getPiece() == null) {
					for (int i = 0; i < selectedHex.size(); i++) {
						sx = selectedHex.get(i).getXpos();
						sy = selectedHex.get(i).getYpos();
						board.movePiece(sx, sy, sx + dx, sy + dy);
					}
					clearSelected();
					return true;
				} else {
					ArrayList<Hex> temp = new ArrayList<Hex>(selectedHex);
					Collections.reverse(temp);
					for (int i = 1; i <= selectedHex.size(); i++) {
						if (board.getHex(sx + (dx * i), sy + (dy * i)).getPiece().getColor()
								.equals(selectedHex.get(0).getPiece().getColor())) { // Same color blocker
							return false;
						} else if (board.getHex(sx + (dx * i), sy + (dy * i)) == null
								|| board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() == null) { // Gap space sumito
							break;
							// Last piece blocker
						} else if (i == selectedHex.size()) {
							if (board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() != null) {
								return false;
							}
						} else {
							temp.add(board.getHex(sx + (dx * i), sy + (dy * i)));
						}
					}
					for (int i = 0; i < temp.size(); i++) {
						sx = temp.get(i).getXpos();
						sy = temp.get(i).getYpos();
						board.movePiece(sx, sy, sx + dx, sy + dy);
					}
					clearSelected();
					return true;
				}
			} else { // Broadside and singular
				for (int i = 0; i < selectedHex.size(); i++) {
					sx = selectedHex.get(i).getXpos();
					sy = selectedHex.get(i).getYpos();
					try {
						if (board.getHex(sx + dx, sy + dy) == null) {

						} else if (board.getHex(sx + dx, sy + dy) != null
								&& board.getHex(sx + dx, sy + dy).getPiece() == null) {

						} else {
							return false;
						}
					} catch (Exception e) {

					}
				}
				for (int i = 0; i < selectedHex.size(); i++) {
					sx = selectedHex.get(i).getXpos();
					sy = selectedHex.get(i).getYpos();
					board.movePiece(sx, sy, sx + dx, sy + dy);
				}
				clearSelected();
				return true;
			}
		}

	}

	private void clearSelected() {
		for (Hex h : selectedHex) {
			h.setDefaultColor();
		}
		selectedHex.clear();
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
				int ds = identity(dx, dy, sx, sy);
				int di = identity(dx, dy, ix, iy);
				int is = identity(ix, iy, sx, sy);
				if ((ds + di + is == 2 || ds + di + is == 20 || ds + di + is == 22)
						&& (ds == di || di == is || ds == is)) {
					selectedHex.add(hex);
					hex.setColor(Color.CYAN);
				}
			}
		}
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
				System.out.println(selectHex.getID());
				if (selectHex != null && selectHex.getPiece() != null) {
					if (Game.turn == 0 && selectHex.getPiece().getColor().equals(Color.BLACK)) {
						addToSelection(selectHex);
					} else if (Game.turn == 1 && selectHex.getPiece().getColor().equals(Color.WHITE)) {
						addToSelection(selectHex);
					}
				}
			}
		}

	}

}
