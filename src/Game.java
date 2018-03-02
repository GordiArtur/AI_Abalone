import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {

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

    public Game() {
        System.out.println("Start called");
        setLayout(new BorderLayout());
        board = new Board(this);
        controls = new Controls(board, this);
        movementControls = new MovementControls(this, board, controls);
        playerBlack = new Human(this, board, controls, Color.BLACK);
        playerWhite = new AI(this, board, controls, Color.WHITE);
        currentPlayer = playerBlack;
        blackTurn = true;
        blackScore = 0;
        whiteScore = 0;
        turnCount = 1;
        add(controls, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(movementControls, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        System.out.println("Switch called");
        controls.stopTimer();
        controls.resetTimer();
        controls.startTimer();
        currentPlayer.move(board);
        if (currentPlayer == playerBlack) {
            currentPlayer = playerWhite;
            blackTurn = false;
        } else {
            turnCount++;
            controls.setTurnCount();
            currentPlayer = playerBlack;
            blackTurn = true;
        }
        controls.setTurnColor();
    }

    /**
     * Returns the current player.
     * @return true if it's the player, false if it's the AI.
     */
    public Agent getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player, input from Human and AI
     * @param player the current player
     */
    public void setCurrentPlayer(Agent player) {
        currentPlayer = player;
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
     * @return The turn count
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * Increments black's score by 1
     */
    public void incrementBlackScore() {
        blackScore++;
    }

    /**
     * Increments white's score by 1
     */
    public void incrementWhiteScore() {
        whiteScore++;
    }

    /**
     * Restarts the game:
     * - Sets the turn count back to 1
     * - Sets the black and white scores to 0
     * - Sets the black to go first
     * - Resets the timer
     * - Resets the board layout to standard
     * - Clears any selected tiles
     * - Resets the move history
     * - Resets the timer history
     */
    public void restartGame() {
        turnCount = 1;
        blackScore = 0;
        whiteScore = 0;
        blackTurn = true;
        controls.setTurnColor();
        controls.setTurnCount();
        controls.stopTimer();
        controls.resetTimer();
        board.selectLayout(1);
        movementControls.clearSelected();
        // Reset move history
        // Reset timer history
    }

    /**
     * The main method, starts the game
     * @param args arguments
     */
    public static void main(String[] args) {
        Game Game = new Game();
    }
}
