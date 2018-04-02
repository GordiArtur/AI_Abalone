import java.awt.*;

/**
 * Static class used to calculate a heuristic value. No instances of this class should be created
 */
public class Heuristic {

    /**
     * Weight value of a win condition
     */
    private static final int WIN = 1000;

    /**
     * Weight value of a piece in center
     */
    private static final int CENTER = 10;

    /**
     * Weight value of a kill move
     */
    private static final int KILL = 20;

    /**
     * Center of the 2d board. i = 4; j = 4
     */
    private static final int CENTER_OF_BOARD_ARRAY = 4;

    private Heuristic() {}

    /**
     * Returns total heuristic value for current StateSpace
     * @param agent current agent
     * @param state current state space
     * @return total heuristic value
     */
    public static int getHeuristics(Agent agent, StateSpace state) {
        int heuristic = 0;
        heuristic += closestToCenter(agent, state);
        heuristic += marbleKill(agent, state);
        heuristic += winCondition(agent, state);
        return heuristic;
    }

    /**
     * Returns manhattan distance of every ally marble from the center.
     * Returns a negative value, as the closer to 0, the better
     * @param agent current agent
     * @param state current state space
     * @return manhattan distance from center for all ally marbles
     */
    private static int closestToCenter(Agent agent, StateSpace state) {
        int color;
        if (agent.getColor() == Color.black) {
            color = 2; // Black color representation in StateSpace 2d array
        } else {
            color = 3; // White color representation in StateSpace 2d array
        }
        int distance = 0;
        int[][] board = state.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == color) {
                    distance += CENTER_OF_BOARD_ARRAY - i;
                    distance += CENTER_OF_BOARD_ARRAY - j;
                }
            }
        }
        return -distance; // Always negative, the lower, the worse
    }


    /**
     * Returns a heuristic based on ally and enemy marble count.
     * If value is positive -> ally marbles > enemy marbles
     * If value is negative -> ally marbles < enemy marbles
     * If value is 0 -> ally marbles == enemy marbles
     * @param agent current agent
     * @param state current state space
     * @return marble kill heuristic value
     */
    private static int marbleKill(Agent agent, StateSpace state) {
        int ownMarbleCount;
        int enemyMarbleCount;
        if (agent.getColor() == Color.black) {
            ownMarbleCount = state.getBlackCount(state.getBoard());
            enemyMarbleCount = state.getWhiteCount(state.getBoard());
        } else {
            ownMarbleCount = state.getWhiteCount(state.getBoard());
            enemyMarbleCount = state.getBlackCount(state.getBoard());
        }
        return (ownMarbleCount - enemyMarbleCount) * KILL;
    }

    /**
     * Returns a heuristic based on ally and enemy winning condition.
     * If value is positive -> ally won
     * If value is negative -> enemy won
     * If value is 0 -> no winning condition for given StateSpace
     * @param agent current agent
     * @param stateSpace current state space
     * @return win condition heuristic value
     */
    private static int winCondition(Agent agent, StateSpace stateSpace) {
        int ownMarbleCount;
        int enemyMarbleCount;
        if (agent.getColor() == Color.black) {
            ownMarbleCount = stateSpace.getBlackCount(stateSpace.getBoard());
            enemyMarbleCount = stateSpace.getWhiteCount(stateSpace.getBoard());
        } else {
            ownMarbleCount = stateSpace.getWhiteCount(stateSpace.getBoard());
            enemyMarbleCount = stateSpace.getBlackCount(stateSpace.getBoard());
        }
        if (ownMarbleCount < 9) {
            return -WIN;
        }
        if (enemyMarbleCount < 9) {
            return WIN;
        }
        return 0;
    }
}
