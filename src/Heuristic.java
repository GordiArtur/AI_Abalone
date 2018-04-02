import java.awt.*;

/**
 * Created by Artur Gordiyenko on 2018-04-01.
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

    private Heuristic() {}

    public static int getHeuristics(Agent agent, StateSpace stateSpace) {
        int heuristic = 0;
        heuristic += closestToCenter(agent, stateSpace);
        heuristic += marbleKill(agent, stateSpace);
        heuristic += winCondition(agent, stateSpace);
        return heuristic;
    }

    private static int closestToCenter(Agent agent, StateSpace stateSpace) {
        int[][] board = stateSpace.getBoard();
        return 0;
    }

    private static int marbleKill(Agent agent, StateSpace stateSpace) {
        int ownMarbleCount;
        int enemyMarbleCount;
        if (agent.getColor() == Color.black) {
            ownMarbleCount = stateSpace.getBlackCount(stateSpace.getBoard());
            enemyMarbleCount = stateSpace.getWhiteCount(stateSpace.getBoard());
        } else {
            ownMarbleCount = stateSpace.getWhiteCount(stateSpace.getBoard());
            enemyMarbleCount = stateSpace.getBlackCount(stateSpace.getBoard());
        }
        return (ownMarbleCount - enemyMarbleCount) * KILL;
    }

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
