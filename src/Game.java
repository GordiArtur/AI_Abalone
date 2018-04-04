import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Game extends JFrame {

    /**
     * Set logging levels on or off
     */
    public static boolean LOG = false;

    /**
     * The maximum marbles of one color on a board
     */
    public static final int MAX_MARBLES = 14;

    /**
     * The minimum number of marbles of one color on a board
     */
    public static final int MIN_MARBLES = 8;

    /**
     * The maximum tree depth of a minimax algorithm
     */
    public static final int MINIMAX_TREE_DEPTH = 5;

    /**
     * The board to play on
     */
    private Board board;


    /**
     * The controls to use
     */
    private Controls controls;

    /**
     * The movement controls to use
     */
    private MovementControls movementControls;

    /**
     * The black player
     */
    private Agent playerBlack;

    /**
     * The white player
     */
    private Agent playerWhite;

    /**
     * The player who's turn it currently is
     */
    private Agent currentPlayer;

    /**
     * Whether or not it is black's turn
     */
    private boolean blackTurn;

    /**
     * Black's score
     */
    private int blackScore;

    /**
     * White's score
     */
    private int whiteScore;

    /**
     * The turn number
     */
    private int turnCount;

    /**
     * Current game status
     */
    private boolean isRunning;

    /**
     * JTextPane of black played history
     */
    private JTextPane blackHistoryPane;

    /**
     * JTextPane of white played history
     */
    private JTextPane whiteHistoryPane;

    /**
     * JLabel of last move that was played
     */
    private JLabel lastMoveLabel;

    /**
     * String of black played history, used by blackHistoryPane
     */
    private StringBuilder blackHistory;

    /**
     * String of white played history, used by whiteHistoryPane
     */
    private StringBuilder whiteHistory;

    private Game() {
        System.out.println("Start called");
        setLayout(new BorderLayout());
        board = new Board(this);
        controls = new Controls(board, this);
        movementControls = new MovementControls(this, board, controls);
        playerBlack = new Human(this, board, controls, Color.BLACK);
        playerWhite = new AI(this, board, controls, Color.WHITE);
        JPanel history = createHistoryPanel();
        currentPlayer = playerWhite;
        blackTurn = false;
        blackScore = MAX_MARBLES;
        whiteScore = MAX_MARBLES;
        turnCount = 0;
        isRunning = false;
        add(controls, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(movementControls, BorderLayout.SOUTH);
        add(history, BorderLayout.EAST);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setBounds(0, 0, 1000, 1000); // Set window size
        setVisible(true);
        controls.setTurnColor();
        controls.setTurnCount();
    }

    /**
     * Current turn of the player, True if it's the player, False if it's the AI.
     */
    public void switchTurn() {
        if (LOG) System.out.println("Successful Play\n");
        controls.stopTimer();
        controls.resetTimer();
        controls.startTimer();
        board.repaint();
        if (currentPlayer == playerWhite) {
            incrementTurn();
        }
        controls.setTurnCount();
        if (turnCount > controls.getTurnLimit() || blackScore < 9 || whiteScore < 9) { // Victory condition 1, out of turns
            controls.stopTimer();
            System.err.println("Maximum turn limit reached!");
            if (blackScore == whiteScore) {
                System.err.println(("It's a tie!"));
            } else {
                System.err.println("Winner is " + ((blackScore < whiteScore) ? "White" : "Black"));
            }
        } else { // Switching players
            if (currentPlayer == playerBlack) {
                currentPlayer = playerWhite;
                blackTurn = false;
            } else {
                currentPlayer = playerBlack;
                blackTurn = true;
            }
            controls.setTurnColor();
            currentPlayer.move();
        }
    }

    /**
     * Increments the turn count.
     */
    public void incrementTurn() {
        turnCount++;
    }

    /**
     * @return True if it is black's turn, otherwise false
     */
    public boolean isBlackTurn() {
        return blackTurn;
    }

    /**
     * @return The color who's turn it is as a String
     */
    public String getTurnColor() {
        if (blackTurn) {
            return "Black";
        } else {
            return "White";
        }
    }

    /**
     * @return The current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return The current player
     */
    public Agent getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return The turn count
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * Decrements black's score by 1
     */
    public void decrementBlackScore() {
        blackScore--;
    }

    /**
     * Decrements white's score by 1
     */
    public void decrementWhiteScore() {
        whiteScore--;
    }

    /**
     * Returns blackScore
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Returns whiteScore
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * Sets isRunning value
     */
    public void setIsRunning(boolean isGameRunning) {
        isRunning = isGameRunning;
    }

    /**
     * @return isRunning value
     */
    public boolean getIsRunning() {
        return isRunning;
    }

    /**
     * Creates new black agents based on parameter input.
     *
     * @param agent 0 = Human; 1 = AI;
     */
    public void selectBlackAgent(int agent) {
        if (agent != 0 && agent != 1) {
            throw new InvalidParameterException("Invalid Game.selectAgent parameter. Must be 0 or 1");
        }
        switch (agent) {
            case 0:
                playerBlack = new Human(this, board, controls, Color.BLACK);
                break;
            case 1:
                playerBlack = new AI(this, board, controls, Color.BLACK);
                break;
        }
        System.out.println("black agent selected: " + (agent == 0 ? "Human" : "AI"));
    }

    /**
     * Creates new white agents based on parameter input.
     *
     * @param agent 0 = Human; 1 = AI;
     */
    public void selectWhiteAgent(int agent) {
        if (agent != 0 && agent != 1) {
            throw new InvalidParameterException("Invalid Game.selectAgent parameter. Must be 0 or 1");
        }
        switch (agent) {
            case 0:
                playerWhite = new Human(this, board, controls, Color.WHITE);
                break;
            case 1:
                playerWhite = new AI(this, board, controls, Color.WHITE);
                break;
        }
        System.out.println("white agent selected: " + (agent == 0 ? "Human" : "AI"));
    }

    /**
     * Restarts the game: - Sets the turn count back to 1 - Sets the black and white scores to 0 - Sets the black to go
     * first - Resets the timer - Clears any selected tiles - Resets the move history - Resets the timer history -
     * Resets isRunning to true
     */
    public void restartGame() {
        turnCount = 0;
        blackScore = MAX_MARBLES;
        whiteScore = MAX_MARBLES;
        blackTurn = false;
        controls.setTurnColor();
        controls.setTurnCount();
        controls.stopTimer();
        controls.resetTimer();
        currentPlayer = playerWhite;
        movementControls.clearSelected();
    }

    /**
     * Do something about updating history 1. Create the message 2. Add to lastMoveLabel 3. Add to history 4. Update
     * History pane
     *
     * @param dx Positive or Negative x direction
     * @param dy Positive or Negative y direction
     * @param selectedHex an Array of hexes selected with pieces
     */
    public void updateHistory(int dx, int dy, ArrayList<Hex> selectedHex) {
        StringBuilder lastMove = new StringBuilder((selectedHex.get(0).getPiece().getColor().equals(
            Color.BLACK)) ? "BLACK" : "WHITE");
        lastMove.append(" ").append(Controls.getDirectionText(dx, dy)).append(" (");
        for (Hex h : selectedHex) {
            lastMove.append(" ").append(h.getID());
        }
        lastMove.append(" ) ").append(controls.getStopWatchTime()).append("s");
        lastMoveLabel.setText(lastMove.toString());

        HTMLEditorKit editor = new HTMLEditorKit();
        HTMLDocument HTMLdoc = new HTMLDocument();
        StringBuilder HTMLhistory = new StringBuilder("<html>\n");
        if (selectedHex.get(0).getPiece().getColor().equals(Color.BLACK)) {
            blackHistory.append("<p>").append(lastMove).append("</p>\n");
            HTMLhistory.append(blackHistory).append("</html>");
            blackHistoryPane.setEditorKit(editor);
            blackHistoryPane.setDocument(HTMLdoc);
        } else {
            whiteHistory.append("<p>").append(lastMove).append("</p>\n");
            HTMLhistory.append(whiteHistory).append("</html>");
            whiteHistoryPane.setEditorKit(editor);
            whiteHistoryPane.setDocument(HTMLdoc);
        }
        try {
            editor.insertHTML(HTMLdoc, 0, HTMLhistory.toString(), 0, 0, Tag.HTML);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes history JPanel and rebuilds it clean
     */
    public void clearHistory() {
        blackHistoryPane.setDocument(new HTMLDocument());
        whiteHistoryPane.setDocument(new HTMLDocument());
        blackHistory = new StringBuilder();
        whiteHistory = new StringBuilder();
        lastMoveLabel.setText("");
    }

    /**
     * Create JPanel with 2 JTextpane displaying black and white history, and a last played move JLabel.
     *
     * @return JPanel with history components
     */
    private JPanel createHistoryPanel() {
        JPanel createHistoryPanel = new JPanel(new BorderLayout());

        lastMoveLabel = new JLabel();
        lastMoveLabel.setHorizontalAlignment(SwingConstants.CENTER);

        blackHistory = new StringBuilder();
        whiteHistory = new StringBuilder();

        blackHistoryPane = new JTextPane();
        whiteHistoryPane = new JTextPane();

        JScrollPane blackScrollPane = new JScrollPane(blackHistoryPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        blackScrollPane.setPreferredSize(new Dimension(170, 425));
        JScrollPane whiteScrollPane = new JScrollPane(whiteHistoryPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        whiteScrollPane.setPreferredSize(new Dimension(170, 425));
        createHistoryPanel.add(blackScrollPane, BorderLayout.NORTH);
        createHistoryPanel.add(lastMoveLabel, BorderLayout.CENTER);
        createHistoryPanel.add(whiteScrollPane, BorderLayout.SOUTH);

        return createHistoryPanel;
    }

    /**
     * The main method, starts the game
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            if (args[0].equals("-L")) {
                Game.LOG = false;
                new Game();
            } else {
                StateSpaceGenerator.main(args);
            }
        } else {
            new Game();
        }
    }
}
