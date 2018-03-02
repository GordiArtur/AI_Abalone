import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovementControls extends JPanel {

    /**
     * Array of hexes (3) that player has selected.
     */
    private List<Hex> selectedHex;

    /**
     * Reference to Board object from Game.
     */
    private Board board;

    /**
     * Movement buttons.
     */
    private JButton NE, E, SE, SW, W, NW;

    /**
     * Reference to Controls object from Game.
     */
    private Controls controls;

    /**
     * Reference to Game object from Game
     */
    private Game game;

    public MovementControls(Game game, Board board, Controls controls) {
        this.board = board;
        this.game = game;
        this.controls = controls;
        selectedHex = new ArrayList<Hex>();
        setLayout(new GridLayout(1, 6));
        setPreferredSize(new Dimension(900, 30));
        setVisible(true);
        createMovementControls();
        createMouseListener();

    }

    /**
     * Apply MouseListener to every hex cell in Board.
     */
    private void createMouseListener() {
        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                if (board.getHex(i, j) != null)
                    board.getHex(i, j).addMouseListener(new MouseListener());
            }
        }
    }

    /**
     * Create GUI button panel for inputting directions. Apply ButtonListener
     * MovementListener to all JButton.
     */
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

    /**
     * Sorts the List<Hex> for selectedHex to arrange Hexes from origin point
     * (top-left corner) in ascending order. Not generic code.
     */
    private void sortSelected() {
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

    /**
     * Outputs an integer that represents the axial direction of the elements in selectedHex.
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

    class MovementListener implements ActionListener {

        /**
         * Checks which button is clicked, follow by executing validMove. Upon validMove
         * is successful, updates Game's turn to AI.
         *
         * @param e ActionEvent from button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!game.getIsRunning()) {
                return;
            }
            boolean played = false;
            if (!selectedHex.isEmpty()) {
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
            if (played) { // Updates turn if successful play
                clearSelected();
                System.out.println("Successful Play");
            }
        }

        /**
         * Checks if the selected hexes and called action is a legal move. If so, apply Board.playedMove().
         * 1. validMove sorts selectedHex array and reverses if moving away from origin (Origin is Top-Left)
         * 2. Generate special identity of direction
         * 3. Generate identity of selectedHex array
         * 4. First check identity to direction identity, Inline
         * 4.1. If adjacent hex is empty space or off-board area, playedMove and return true
         * 4.2. Else create empty temp list. Checks for gaps, same color pieces, and last piece.
         * 4.3. Reverse temp, add selectedHex to temp,
         * 5 Second check for Broadside and single piece
         * 5.1. Check for available space, move then return true
         * 6. movePiece, clear selected, switchTurn() for 4.1, 4.2, and 5 move scenarios
         * return false for nullptr and blocked moves
         * return true for validMove (assumes Board.playedMove() is successful)
         *
         * @param dx X value of horizontal movement
         * @param dy Y value of vertical movement
         */
        private boolean validMove(final int dx, final int dy) { // Don't read it. It's very long
            System.out.println("Checking valid move");
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
                    movePieces(selectedHex, dx, dy);
                    controls.playedMove(selectedHex, dx, dy);
                    clearSelected();
                    game.switchTurn();
                    return true;
                } else {
                    ArrayList<Hex> temp = new ArrayList<Hex>();
                    for (int i = 1; i <= selectedHex.size(); i++) {
                        if (board.getHex(sx + (dx * i), sy + (dy * i)) == null || board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() == null) { // Gap space sumito
                            break;
                        } else if (board.getHex(sx + (dx * i), sy + (dy * i)).getPiece().getColor().equals(selectedHex.get(0).getPiece().getColor())) { // Same color blocker
                            return false;
                        } else if (i == selectedHex.size() && board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() != null) { // Last piece blocker
                            return false;
                        } else { // Add this piece to temp; piece to be moved.
                            temp.add(board.getHex(sx + (dx * i), sy + (dy * i)));
                        }
                    }
                    Collections.reverse(temp);
                    temp.addAll(selectedHex);
                    movePieces(temp, dx, dy);
                    controls.playedMove(selectedHex, dx, dy);
                    clearSelected();
                    game.switchTurn();
                    return true;
                }
            } else { // Broadside and singular
                for (Hex hex : selectedHex) {
                    sx = hex.getXpos();
                    sy = hex.getYpos();
                    if (board.getHex(sx + dx, sy + dy) != null && board.getHex(sx + dx, sy + dy).getPiece() != null) {
                        return false;
                    }
                }
                movePieces(selectedHex, dx, dy);
                controls.playedMove(selectedHex, dx, dy);
                clearSelected();
                game.switchTurn();
                return true;
            }
        }

    }

    /**
     * Calls board.movePiece() to move pieces in hexes.
     *
     * @param hexes Array of hexes with pieces to move
     * @param dx    X coordinate move (-1 to 1)
     * @param dy    Y coordinate move (-1 to 1)
     */
    private void movePieces(final List<Hex> hexes, final int dx, final int dy) {
        for (Hex hex : hexes) {
            int sx = hex.getXpos();
            int sy = hex.getYpos();
            board.movePiece(sx, sy, sx + dx, sy + dy);
        }
    }

    /**
     * Sets background of selectedHex Hexes to default.
     */
    public void clearSelected() {
        for (Hex h : selectedHex) {
            h.setDefaultColor();
        }
        selectedHex.clear();
    }

    /**
     * Checks if hex is admissible to selectedHex array.
     * 1. If empty, add
     * 2. else if duplicate, remove
     * 3. else if not full
     * 3.1. checks if hex's marble is the same as current player
     * 3.2. checks if marbles are in a line
     *
     * @param hex Hex cell
     */
    private void addToSelection(Hex hex) {
        if (selectedHex.size() == 0) {
            selectedHex.add(hex);
            hex.setSelectedColor();
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
                    hex.setSelectedColor();
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
                    hex.setSelectedColor();
                }
            }
        }
    }

    class MouseListener extends MouseAdapter {

        /**
         * Checks if it's player's active turn before finding clicked Hex and adding to selectedHex.
         *
         * @param e Mouse Click event
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!game.getIsRunning()) {
                return;
            }
            try {
                Hex selectHex = (Hex) e.getSource();
                System.out.println(selectHex.getID());
                if (selectHex.getPiece() != null) {
                    if (game.isBlackTurn() && selectHex.getPiece().getColor().equals(Color.BLACK)) {
                        addToSelection(selectHex);
                    } else if (!game.isBlackTurn() && selectHex.getPiece().getColor().equals(Color.WHITE)) {
                        addToSelection(selectHex);
                    }
                }
            } catch (NullPointerException npe) {
            }
        }
    }
}