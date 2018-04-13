import java.awt.*;

/**
 * Static class used to calculate a heuristic value. No instances of this class should be created
 */
public class Heuristic {

    /**
     * Weight value of a win condition
     */
    private static final int WIN = 10000;

    /**
     * Weight value of a piece in center
     * Calculated by getting half of (best center value + worst center value)
     * [(18 + 56) / 2]
     */
    private static final int CENTER_WEIGHT = 37;

    /**
     * Weight value of a kill move
     */
    private static final int KILL = 200;

    /**
     * Weight value of grouping marbles
     */
    private static final int GROUPING_WEIGHT = 100;

    /**
     * Weight value used to calculate enemy manhattan distance from center of the board
     */
    private static final int ENEMY_DISTANCE_FROM_CENTER_WEIGHT = 2;

    private Heuristic() {}

    /**
     * Returns total heuristic value for current StateSpace
     * @param agent current agent
     * @param state current state space
     * @return total heuristic value
     */
    public static int getHeuristics(Agent agent, StateSpace state) {
        int heuristic = 0;
        int ownMarbleCount;
        int enemyMarbleCount;
        if (agent.getColor() == Color.black) {
            ownMarbleCount = state.getBlackCount(state.getBoard());
            enemyMarbleCount = state.getWhiteCount(state.getBoard());
        } else {
            ownMarbleCount = state.getWhiteCount(state.getBoard());
            enemyMarbleCount = state.getBlackCount(state.getBoard());
        }

        heuristic += closestToCenter(agent, state);
        heuristic += enemyFurtherFromCenter(agent, state);
        heuristic += marbleKill(ownMarbleCount, enemyMarbleCount);
        heuristic += winCondition(agent, state);
        heuristic += marbleIsolation(agent, state);
      
        return heuristic;
    }

    /**
     * Returns manhattan distance of every ally marble from the center.
     * Best possible value before subtraction: 18 (if all present)
     * Worst possible value before subtraction: 56
     *
     * Since best value < worst value, we need to subtract the final value by a weight
     * so that [weight - best value] > [weight - worst value]
     *
     * @param agent current agent
     * @param state current state space
     * @return manhattan distance from center for all ally marbles
     */
    private static int closestToCenter(Agent agent, StateSpace state) {
        int color;
        int[][] board = state.getBoard();
        if (agent.getColor() == Color.black) {
            color = 2; // Black color representation in StateSpace 2d array
        } else {
            color = 3; // White color representation in StateSpace 2d array
        }
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == color) {
                    if (i == 0 || j == 0 || i == 8 || j == 8) {
                        distance += 4;
                    } else if (i == 1 || j == 1 || i == 7 || j == 7) {
                        distance += 3;
                    } else if (i == 2 || j == 2 || i == 6 || j == 6) {
                        distance += 2;
                    } else if (i == 3 || j == 3 || i == 5 || j == 5) {
                        distance += 1;
                    } // else distance += 0
                }
            }
        }
        return CENTER_WEIGHT - distance;
    }

    /**
     * Returns manhattan distance of every enemy marble from the center.
     * Worst possible value before subtraction: 18 (if all present)
     * Best possible value before subtraction: 56
     *
     * @param agent current agent
     * @param state current state space
     * @return manhattan distance from center for all enemy marbles
     */
    private static int enemyFurtherFromCenter(Agent agent, StateSpace state) {
        int color;
        int[][] board = state.getBoard();
        if (agent.getColor() == Color.black) {
            color = 3; // White color representation in StateSpace 2d array
        } else {
            color = 2; // Black color representation in StateSpace 2d array
        }
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == color) {
                    if (i == 0 || j == 0 || i == 8 || j == 8) {
                        distance += 4;
                    } else if (i == 1 || j == 1 || i == 7 || j == 7) {
                        distance += 3;
                    } else if (i == 2 || j == 2 || i == 6 || j == 6) {
                        distance += 2;
                    } else if (i == 3 || j == 3 || i == 5 || j == 5) {
                        distance += 1;
                    } // else distance += 0
                }
            }
        }
        return distance * ENEMY_DISTANCE_FROM_CENTER_WEIGHT;
    }

    /**
     * Returns a heuristic based on ally and enemy marble count.
     * If value is positive -> ally marbles > enemy marbles
     * If value is negative -> ally marbles < enemy marbles
     * If value is 0 -> ally marbles == enemy marbles
     * @param ownMarbleCount agent's marbles
     * @param enemyMarbleCount enemy's marbles
     * @return marble kill heuristic value
     */
    private static int marbleKill(int ownMarbleCount, int enemyMarbleCount) {
        return (ownMarbleCount - enemyMarbleCount) * KILL;
    }

    /**
     * Returns a heuristic based on ally and enemy winning condition.
     * If value is positive -> ally won
     * If value is negative -> enemy won
     * If value is 0 -> no winning condition for given StateSpace
     * @param agent current agent
     * @param state current state space
     * @return win condition heuristic value
     */
    private static int winCondition(Agent agent, StateSpace state) {
        int ownMarbleCount;
        int enemyMarbleCount;
        if (agent.getColor() == Color.black) {
            ownMarbleCount = state.getBlackCount(state.getBoard());
            enemyMarbleCount = state.getWhiteCount(state.getBoard());
        } else {
            ownMarbleCount = state.getWhiteCount(state.getBoard());
            enemyMarbleCount = state.getBlackCount(state.getBoard());
        }
        if (ownMarbleCount < 9) {
            return -WIN;
        }
        if (enemyMarbleCount < 9) {
            return WIN;
        }
        return 0;
    }

    /**
     * Returns a value of all enemy marbles surrounded by other enemy marbles.
     *
     * Since best value < worst value, we need to subtract the final value by a weight
     * so that [weight - best value] > [weight - worst value]
     *
     * @param agent current agent
     * @param state current state space
     * @return manhattan distance from center for all enemy marbles
     */
    private static int marbleIsolation(Agent agent, StateSpace state) {
        int color;
        int[][] board = state.getBoard();
        if (agent.getColor() == Color.black) {
            color = 3; // White color representation in StateSpace 2d array
        } else {
            color = 2; // Black color representation in StateSpace 2d array
        }
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == color) {
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1 ; l++) {
                            if (i+k < 0 || i+k >= board.length || j+l < 0 || j+l >= board.length) {
                                break;
                            }
                            if (board[i+k][j+l] == color) {
                                score++;
                            }
                        }
                    }
                }
            }
        }
        return GROUPING_WEIGHT-score;
    }
}
