import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {

    private Board board;
    private Controls controls;
    private MovementControls movementControls;
    private Agent playerBlack;
    private Agent playerWhite;
    private Agent currentPlayer;
    private boolean blackTurn;
    private int blackScore;
    private int whiteScore;
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
        controls.startTimer();
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
     * @param player
     */
    public void setPlayer(Agent player) {
        currentPlayer = player;
    }

    public boolean isBlackTurn() {
        return blackTurn;
    }

    public String getTurnColor() {
        if (blackTurn) {
            return "Black";
        } else {
            return "White";
        }
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void incrementBlackScore() {
        blackScore++;
    }

    public void incrementWhiteScore() {
        whiteScore++;
    }

    public void resetGame() {
        turnCount = 1;
        blackScore = 0;
        whiteScore = 0;
        controls.setTurnColor();
        controls.setTurnCount();
        controls.stopTimer();
        controls.resetTimer();
        controls.startTimer();
        board.selectLayout(1);
        movementControls.clearSelected();
        // Reset move history
        // Reset timer history
    }

    public static void main(String[] args) {
        Game Game = new Game();
    }
}
