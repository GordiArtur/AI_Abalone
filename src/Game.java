import java.awt.*;
import java.security.InvalidParameterException;
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

    /**
     * Current game status
     */
    private boolean isRunning;

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
        blackScore = 14;
        whiteScore = 14;
        turnCount = 1;
        isRunning = false;
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
     * Restarts the game:
     * - Sets the turn count back to 1
     * - Sets the black and white scores to 0
     * - Sets the black to go first
     * - Resets the timer
     * - Clears any selected tiles
     * - Resets the move history
     * - Resets the timer history
     * - Resets isRunning to true
     */
    public void restartGame() {
        turnCount = 1;
        blackScore = 14;
        whiteScore = 14;
        blackTurn = true;
        controls.setTurnColor();
        controls.setTurnCount();
        controls.stopTimer();
        controls.resetTimer();
        currentPlayer = playerBlack;
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
