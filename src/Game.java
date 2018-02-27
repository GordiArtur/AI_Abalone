import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {

    public static int turn = 0; // 0 = black piece turn; 1 = white piece turn;

    private Board board;
    private Human human;
    private AI ai;
    private Controls controls;
    private static boolean currentPlayer;

    public Game() {
        setLayout(new BorderLayout());
        board = new Board();
        controls = new Controls(board);
        human = new Human(this, board, controls);
        ai = new AI(this, board, controls);
        currentPlayer = true;
        add(controls, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(human, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setBounds(0, 0, 1000, 1000); // Set window size
        setVisible(true);
        switchTurn();
/*
        while (board.getBlackCount() > 8 && board.getWhiteCount() > 8) {
            if (turn == 0) {
                human.play(board);
            } else {
                ai.play(board);
            }
            if (turn == 1) {
                turn = 0;
                System.out.println("RESET");
            }
        }
        */
    }

    /**
     * Current turn of the player, True if it's the player, False if it's the AI.
     */
    public void switchTurn() {
        if (currentPlayer) {
            controls.startTimer();
            System.out.println("Start called");
            human.play(board);
            currentPlayer = false;
        } else if (!currentPlayer) {
            ai.play(board);
            currentPlayer = true;
        } else {
            System.out.println("Switch called");
        }
    }

    /**
     * Returns the current player.
     * @return true if it's the player, false if it's the AI.
     */
    public boolean getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player, input from Human and AI
     * @param in
     */
    public void setTurn(boolean in) {

        currentPlayer = in;
    }

    public static void main(String[] args) {
        Game Game = new Game();

    }
}
