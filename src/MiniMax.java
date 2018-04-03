import java.awt.*;

/**
 * Uses MiniMax and Alpha-Beta Pruning to play Abalone.
 */
public class MiniMax {
    /**
     *
     */
    private static Transposition transpositionTable;

    /**
     * The maximum depth
     */
    private static double maxDepth;

    private static Action bestMove;

    /**
     * MiniMax cannot be instantiated.
     */
    public MiniMax () {}

    /**
     * Execute the MiniMax and Alpha-Beta Pruning.
     * @param player        the player that the AI will identify as
     * @param game          the Abalone game
     * @param depth         the maximum depth
     */
    public Action run (Agent player, Game game, double depth, Transposition table) {
        transpositionTable = table;
        if (depth < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }
        bestMove = null;
        maxDepth = depth;
        StateSpace state = new StateSpace(game.getBoard(), game.isBlackTurn() ? Color.BLACK : Color.WHITE);

        miniMax(player, state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

        return bestMove;
    }

    /**
     * The meat of the algorithm.
     * @param player        the player that the AI will identify as
     * @param state         the Abalone game state
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentDepth  the current depth
     * @return              the heuristic score of the board
     */
    private int miniMax (Agent player, StateSpace state, double alpha, double beta, int currentDepth) {
        int whiteCount = state.getWhiteCount(state.getBoard());
        int blackCount = state.getBlackCount(state.getBoard());

        if (currentDepth++ == maxDepth || whiteCount < 9 || blackCount < 9) {
            // Check transposition table for previously calculated player/state heuristic values
            // if the score is -1000, then there is no matching key in the table
            int score = transpositionTable.getTranspositionTableValue(player, state);
            if (score == -1000) {
                score = heuristic(player, state);
                transpositionTable.addToTranspositionTable(player, state, currentDepth, score);
            }
            return score;
        }

        if (state.getColor() == player.getColor()) {
            return getMax(player, state, alpha, beta, currentDepth);
        } else {
            return getMin(player, state, alpha, beta, currentDepth);
        }
    }

    /**
     * Play the move with the highest score.
     * @param player        the player that the AI will identify as
     * @param state         the Abalone game state
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentDepth  the current depth
     * @return              the heuristic value of the resulting board state
     */
    private int getMax (Agent player, StateSpace state, double alpha, double beta, int currentDepth) {
        state.standardProcedure();

        // ITERATE THROUGH POSSIBLE MOVES
        for (Action m : state.getMoveList()) {

            StateSpace modifiedState = new StateSpace(state.getNextBoard(m, state.getBoard()), (state.getColor().equals(Color.BLACK)) ? 3 : 2);

            // DO MINIMAX ON THE NEW STATE
            int hVal = miniMax(player, modifiedState, alpha, beta, currentDepth);

            if (hVal > alpha) {
                alpha = hVal;
                if (currentDepth == 1) {
                    bestMove = m;
                }
            }

            // Pruning
            if (alpha >= beta) {
                break;
            }
        }

        return (int)alpha;
    }

    /**
     * Play the move with the lowest score.
     * @param player        the player that the AI will identify as
     * @param state         the Abalone game state
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentDepth  the current depth
     * @return              the heuristic value of the resulting board state
     */
    private int getMin (Agent player, StateSpace state, double alpha, double beta, int currentDepth) {
        state.standardProcedure();

        // ITERATE THROUGH POSSIBLE MOVES
        for (Action m : state.getMoveList()) {

            // CREATE NEW STATE BASED ON ACTION
            StateSpace modifiedState = new StateSpace(state.getNextBoard(m, state.getBoard()), (state.getColor().equals(Color.BLACK)) ? 3 : 2);

            // DO MINIMAX ON THE NEW STATE
            int hVal = miniMax(player, modifiedState, alpha, beta, currentDepth);

            if (hVal < beta) {
                beta = hVal;
                if (currentDepth == 1) {
                    bestMove = m;
                }
            }

            // Pruning.
            if (alpha >= beta) {
                break;
            }
        }

        return (int)beta;
    }

    /**
     * Get the score of the board.
     * @param player        the play that the AI will identify as
     * @param state         the Abalone game state
     * @return              the score of the board
     */
    private int heuristic (Agent player, StateSpace state) {
        return Heuristic.getHeuristics(player, state);
    }

}