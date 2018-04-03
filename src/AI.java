import java.awt.Color;
import java.util.List;

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
     * Transposition table used to store heuristic calculations
     */
    private static Transposition transpositionTable;

    /**
     * The color the AI is playing (black or white)
     */
    private Color color;

    public AI(Game game, Board board, Controls control, Color color) {
        this.game = game;
        this.board = board;
        this.control = control;
        this.color = color;
        transpositionTable = new Transposition(board);
    }

    /**
     * Moves marbles based on Action input.
     * @param action contains List of selected Hex pieces, and x and y direction of the move
     * @return true if the move was made.
     */
    private boolean makeMove(Action action) {
        boolean moveMade;

        List<Hex> hexList = action.getSelectedHexList(board);
        moveMade = control.validMove(action.getDx(), action.getDy(), hexList);

        if(moveMade) {
            for (Hex hex : hexList) {
                int sx = hex.getXpos();
                int sy = hex.getYpos();
                if(hex.getPiece() != null) {
                    transpositionTable.movePiece(sx, sy, sx + action.getDx(), sy + action.getDy(), hex.getPiece().getColor());
                }
            }
        }

        return moveMade;
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
        System.out.println("Ai is playing " + ((color.equals(Color.BLACK)) ? "Black" : "White"));
        MiniMax algo = new MiniMax();
        Action action = algo.run(this, game, Game.MINIMAX_TREE_DEPTH, transpositionTable);
        makeMove(action);
    }

}