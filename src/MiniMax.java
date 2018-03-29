/**
 * Uses MiniMax and Alpha-Beta Pruning to play Abalone.
 */
public class MiniMax {

    /**
     * The maximum depth
     */
    private static double maxDepth;

    /**
     * MiniMax cannot be instantiated.
     */
    private MiniMax () {}

    /**
     * Execute the MiniMax and Alpha-Beta Pruning.
     * @param player        the player that the AI will identify as
     * @param game          the Abalone game
     * @param depth         the maximum depth
     */
    static void run (Agent player, Game game, double depth) {
        if (depth < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }

        maxDepth = depth;
        miniMax(player, game, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    /**
     * The meat of the algorithm.
     * @param player        the player that the AI will identify as
     * @param game          the Abalone game
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentDepth    the current depth
     * @return              the heuristic score of the board
     */
    private static int miniMax (Agent player, Game game, double alpha, double beta, int currentDepth) {
        if (currentDepth++ == maxDepth /*|| CHECK IF GAME IS OVER*/) {
            return heuristic(player, game);
        }

        if (game.getCurrentPlayer() == player) {
            return getMax(player, game, alpha, beta, currentDepth);
        } else {
            return getMin(player, game, alpha, beta, currentDepth);
        }
    }

    /**
     * Play the move with the highest score.
     * @param player        the player that the AI will identify as
     * @param game          the Abalone game
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentDepth  the current depth
     * @return              the heuristic value of the resulting board state
     */
    private static int getMax (Agent player, Game game, double alpha, double beta, int currentDepth) {
        int bestMove = -1;

        // ITERATE THROUGH POSSIBLE MOVES
        /*for (Integer m : game.getMoves()) {

            // COPY THE CURRENT STATE
            Game modifiedGame = game.copy();
            // MAKE THE MOVE ON THE COPY
            modifiedGame.move(m);
            // DO MINIMAX ON THE COPY
            int hVal = miniMax(player, modifiedGame, alpha, beta, currentDepth);

            if (hVal > alpha) {
                alpha = hVal;
                bestMove = m;
            }

            // Pruning
            if (alpha >= beta) {
                break;
            }
        }

        // MAKE THE BEST MOVE
        game.move(bestMove);*/

        return (int)alpha;
    }

    /**
     * Play the move with the lowest score.
     * @param player        the player that the AI will identify as
     * @param game          the Abalone game
     * @param alpha         the alpha value
     * @param beta          the beta value
     * @param currentDepth  the current depth
     * @return              the heuristic value of the resulting board state
     */
    private static int getMin (Agent player, Game game, double alpha, double beta, int currentDepth) {
        int bestMove = -1;

        // ITERATE THROUGH POSSIBLE MOVES
        /*for (Integer m : game.getMoves()) {

            // COPY THE CURRENT STATE
            Game modifiedGame = game.copy();
            // MAKE THE MOVE ON THE COPY
            modifiedGame.move(m);
            // DO MINIMAX ON THE COPY
            int hVal = miniMax(player, modifiedGame, alpha, beta, currentDepth);

            if (hVal < beta) {
                beta = hVal;
                bestMove = m;
            }

            // Pruning.
            if (alpha >= beta) {
                break;
            }
        }

        // MAKE THE BEST MOVE
        game.move(bestMove);*/

        return (int)beta;
    }

    /**
     * Get the score of the board.
     * @param player        the play that the AI will identify as
     * @param game          the Abalone game
     * @return              the score of the board
     */
    private static int heuristic (Agent player, Game game) {
        /* GET HEURISTIC SCORE AND RETURN IT */
        return 0;
    }

}