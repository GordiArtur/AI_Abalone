import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;

/**
 * Creates and stores the transposition table.
 */
public class Transposition {
    /**
     * Store already calculated heuristics.
     */
    private static Hashtable<Integer, Hashentry> transpositionTable;

    /**
     * Contains randomized values that each represent the location
     * and type of a single marble on the board.
     */
    private static long zobristTable[][][];

    /**
     * Contains the size of the board.
     */
    private static final int BOARD_SIZE = 9;

    /**
     * Contains the size of the hashtable.
     */
    private static final int HASH_SIZE = 10000;

    /**
     * Represents the current state of the board.
     */
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
        long result;

        // Create Zobrist table
        for (int h = 0; h < BOARD_SIZE; h++) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < states; j++) {
                    // only allows positive long values to be assigned
                    do{
                        result = rnd.nextLong() >>> 1;
                    }while(result == 0);
                    zobristTable[h][i][j] = result;
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
     * Creates a Zobrist key for a given Board.
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
     * Looks for a matching state in the transposition table. Returns -1000
     * if the state is not found.
     * @param player Agent - may be used to store turn later
     * @param state StateSpace - the layout of the board
     * @return int - score of stored layout else -1000
     */
    public int getTranspositionTableValue(Agent player, StateSpace state) {
        long tempKey = createHashKey(state.getBoard());

        if(transpositionTable.containsKey(hashFunction(tempKey))) {
            return transpositionTable.get(hashFunction(tempKey)).score;
        }
        return -1000;
    }

    /**
     * Adds a key-value pair to the transposition table.
     * Current replacement scheme is to always replace found
     * values. Can update later if necessary.
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