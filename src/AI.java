import java.awt.Color;

public class AI implements Agent {

    /**
     * Reference to board from Game.java
     */
    private Board board;

    /**
     * Reference to controls from Game.java
     */
    private Controls control;

    /**
     * Reference to game from Game.java
     */
    private Game game;

    /**
     * The color the AI is playing (black or white)
     */
    private Color color;
    
    public AI(Game game, Board board, Controls control, Color color) {
        this.game = game;
        this.board = board;
        this.control = control;
        this.color = color;
    }

    /**
     * Color accessor
     * @return The color the AI is playing
     */
    public Color getColor() {
        return color;
    }

    /**
     * Color mutator
     * @param color The color the AI is playing
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Represents a move taking place by the AI
     * @param board The board to move on
     * @return the board
     */
    public Board move(Board board) {
        System.out.println("Ai is playing");
        return board;
    }

}