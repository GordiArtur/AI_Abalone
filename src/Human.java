import java.awt.Color;

public class Human implements Agent {

    private Board board;
    private Controls control;
    private Game game;
    private Color color;

    public Human(Game game, Board board, Controls control, Color color) {
        this.game = game;
        this.board = board;
        this.control = control;
        this.color = color;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Controls getControl() {
        return control;
    }

    public void setControl(Controls control) {
        this.control = control;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Board move(Board board) {
        System.out.println("Human is playing");
        return board;
    }
}