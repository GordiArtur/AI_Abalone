import java.awt.*;
import java.util.Hashtable;
import java.util.Random;

/**
 * 1) Create Zobrist table filled with random long values
 * 2) Create Zobrist key which represents the current board layout
 * 3) Add new heuristic values to the hashtable
 * 4) Retrieve heuristic values from the hashtable
 *
 */
public class Transposition {
    private static Hashtable<Integer, Hashentry> transpositionTable;
    private static long zobristTable[][][];
    private static final int BOARD_SIZE = 9;
    private static final int HASH_SIZE = 10000;
    private static long zobristKey;

    /**
     * Stores the values of each board position.
     */
    private class Hashentry
    {
        //private long zobrist;
        private int depth;
        private int flag;
        private int score;
        private int ancient;

        Hashentry(int depth, int flag, int score, int ancient)
        {
            //this.zobrist = zobristKey;
            this.depth = depth;
            this.flag = flag;
            this.score = score;
            this.ancient = ancient;
        }
    }

    /**
     * @TODO random function returns - values, should only be +
     * Creates the Zobrist table containing the randomized
     * long value for each piece. Does not have a turn value.
     * Also creates the Zobrist key containing the current layout.
     * Table: [size][size][state][turn]
     * For the piece state:
     * 0 represents null/off-board space.
     * 1 represents empty space on board.
     * 2 represents black piece on a space
     * 3 represents white piece on a space.
     */
    Transposition(Board board) {
        transpositionTable = new Hashtable<>();
        Random rnd = new Random();
        int states = 4;
        zobristTable = new long[BOARD_SIZE][BOARD_SIZE][states];
        zobristKey = 0;

        // Create Zobrist table
        for (int h = 0; h < BOARD_SIZE; h++) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < states; j++) {
                    zobristTable[h][i][j] = rnd.nextLong();
                }
            }
        }

        // Create Zobrist key
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.getHex(i, j) != null && board.getHex(i, j).getPiece() != null) {
                    if (board.getHex(i, j).getColor() == Color.BLACK) {
                        zobristKey ^= zobristTable[i][j][2];
                    } else if (board.getHex(i, j).getColor() == Color.WHITE) {
                        zobristKey ^= zobristTable[i][j][3];
                    }
                }
            }
        }
    }

    /**
     * Creates a Zobrist key for a given Board
     * @param board - a board layout
     */
    private long createHashKey(int[][] board){
        long key = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 2) {   // position contains black marble
                    key ^= zobristTable[i][j][2];
                } else if (board[i][j] == 3) {   // position contains white marble
                    key ^= zobristTable[i][j][3];
                }
            }
        }
        return key;
    }

    /**
     * Updates the Zobrist hashkey with the movement
     * of a single marble.
     * @param originX int - original x coord of marble
     * @param originY int - original y coord of marble
     * @param destX int - destination x coord of marble
     * @param destY int - destination y coord of marble
     * @param color Color - color of marble
     */
    public void movePiece(int originX, int originY, int destX, int destY, Color color) {
        int colorValue;
        if(color == Color.BLACK) {
            colorValue = 2;
        } else {
            colorValue = 3;
        }
        // remove piece from hashkey
        zobristKey ^= zobristTable[originX][originY][colorValue];

        // add piece to hashkey
        zobristKey ^= zobristTable[destX][destY][colorValue];
    }

    /**
     * Creates the hash key for the key-value pair
     * stored in the hashtable.
     * @param key long - Zobrist key representing a layout
     * @return int - hash key
     */
    private int hashFunction(long key) {
        return (int)(key %HASH_SIZE);
    }

    /**
     * Returns -1000 if not already in table
     * @param player Agent - may be used to store turn later
     * @param state StateSpace - the layout of the board
     * @return int - score of stored layout else -1000 if
     * not found in the table
     */
    public int getTranspositionTableValue(Agent player, StateSpace state) {
        long tempKey = createHashKey(state.getBoard());

        if(transpositionTable.containsKey(hashFunction(tempKey))) {
            return transpositionTable.get(hashFunction(tempKey)).score;
        }
        return -1000;
    }

    /**
     * @TODO update with replacement scheme
     * Adds a key-value pair to the transposition table
     * @param player Agent - may be used to store turn later
     * @param state StateSpace - the layout of the board
     * @param depth int - current depth of the search tree
     * @param score int - heuristic value of the current node
     */
    public void addToTranspositionTable(Agent player, StateSpace state, int depth, int score) {
        int tempHashedKey = hashFunction(createHashKey(state.getBoard()));

        transpositionTable.put(tempHashedKey, new Hashentry(depth, 0, score, 0 ));
    }
}