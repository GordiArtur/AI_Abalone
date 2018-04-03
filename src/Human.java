import java.awt.Color;

public class Human implements Agent {

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

    public Human(Game game, Board board, Controls control, Color color) {
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
     * Logs to console output a move taking place by the AI
     */
    public void move() {
        if (Game.LOG) System.out.println("Human is playing " + ((color.equals(Color.BLACK)) ? "Black" : "White"));
    }
}