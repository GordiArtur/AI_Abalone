import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

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
     * Reference to Game object from Game
     */
    private Game game;

    /**
     * Reference to Controls object from Game
     */
    private Controls controls;

    public MovementControls(Game game, Board board, Controls controls) {
        this.board = board;
        this.game = game;
        this.controls = controls;
        selectedHex = new ArrayList<>();
        board.selectLayout(1);
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
                if (board.getHex(i, j) != null) {
                    board.getHex(i, j).addMouseListener(new MouseListener());
                }
            }
        }
    }

    /**
     * Create GUI button panel for inputting directions. Apply ButtonListener MovementListener to all JButton.
     *
     * Add keyboard shortcut listener: Windows: alt + [q; a; z; c; d; e] Mac: ctrl + option + [q; a; z; c; d; e]
     */
    private void createMovementControls() {
        /*
      Movement buttons.
     */
        JButton NE = new JButton("North-East");
        JButton e = new JButton("East");
        JButton SE = new JButton("South-East");
        JButton SW = new JButton("South-West");
        JButton w = new JButton("West");
        JButton NW = new JButton("North-West");

        NW.setMnemonic(KeyEvent.VK_Q);
        NW.addActionListener(new MovementListener());
        w.setMnemonic(KeyEvent.VK_A);
        w.addActionListener(new MovementListener());
        SW.setMnemonic(KeyEvent.VK_Z);
        SW.addActionListener(new MovementListener());
        SE.setMnemonic(KeyEvent.VK_C);
        SE.addActionListener(new MovementListener());
        e.setMnemonic(KeyEvent.VK_D);
        e.addActionListener(new MovementListener());
        NE.setMnemonic(KeyEvent.VK_E);
        NE.addActionListener(new MovementListener());

        add(NW);
        add(w);
        add(SW);
        add(SE);
        add(e);
        add(NE);
    }

    class MovementListener implements ActionListener {

        /**
         * Checks which button is clicked, follow by executing validMove. Upon validMove is successful, updates Game's
         * turn to AI.
         *
         * @param e ActionEvent from button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (controls.isGameRunning()) {
                boolean played = false;
                if (!selectedHex.isEmpty()) {
                    for (Hex h : selectedHex) {
                        System.out.print(h.getID() + " ");
                    }
                    System.out.println("" + e.getActionCommand());
                    switch (e.getActionCommand()) {
                        case ("North-East"):
                            played = controls.validMove(0, -1, selectedHex);
                            break;
                        case ("East"):
                            played = controls.validMove(1, 0, selectedHex);
                            break;
                        case ("South-East"):
                            played = controls.validMove(1, 1, selectedHex);
                            break;
                        case ("South-West"):
                            played = controls.validMove(0, 1, selectedHex);
                            break;
                        case ("West"):
                            played = controls.validMove(-1, 0, selectedHex);
                            break;
                        case ("North-West"):
                            played = controls.validMove(-1, -1, selectedHex);
                            break;
                        default:
                            break;
                    }
                }
                if (played) { // Updates turn if successful play
                    clearSelected();
                }
            }
        }
    }

    /**
     * Checks if hex is admissible to selectedHex array. 1. If empty, add 2. else if duplicate, remove 3. else if not
     * full 3.1. checks if hex's marble is the same as current player 3.2. checks if marbles are in a line
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
                if (controls.identity(sx, sy, dx, dy) > 0) {
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
                int ds = controls.identity(dx, dy, sx, sy);
                int di = controls.identity(dx, dy, ix, iy);
                int is = controls.identity(ix, iy, sx, sy);
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
            if (controls.isGameRunning()) {
                try {
                    Hex selectHex = (Hex) e.getSource();
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

    /**
     * Sets background of selectedHex Hexes to default.
     */
    public void clearSelected() {
        for (Hex h : selectedHex) {
            h.setDefaultColor();
        }
        selectedHex.clear();
    }
}