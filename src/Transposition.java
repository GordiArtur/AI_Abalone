import java.awt.*;
import java.util.Hashtable;
import java.util.Random;

/**
 * 1) Create Zobrist table filled with random long values
 * 2) Create Zobrist key which represents the current board layout
 * 3)
 */
public class Transposition {
    private static Hashtable<Integer, Hashentry> transpositionTable;
    private static long zobristTable[][][];
    private static final int BOARD_SIZE = 9;
    private static long zobristKey;
    private class Hashentry
    {
        private long zobrist;
        private int depth;
        private int flag;
        private int score;
        private int ancient;

        Hashentry(int depth, int flag, int score, int ancient)
        {
            this.zobrist = zobristKey;
            this.depth = depth;
            this.flag = flag;
            this.score = score;
            this.ancient = ancient;
        }
    }

    /**
     * Creates the Zobrist table containing the randomized
     * long value for each piece. Does not have a turn value.
     * Table: [size][size][state][turn]
     * For the piece state:
     * 0 represents null/off-board space.
     * 1 represents empty space on board.
     * 2 represents black piece on a space
     * 3 represents white piece on a space.
     */
    Transposition(Board board) {
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
     * Creates a zobristKey for a board layout
     * @param board - a board layout
     */
    private long createHashKey(int[][] board){
        long key = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 2) {   // position contains black marble
                    zobristKey ^= zobristTable[i][j][2];
                } else if (board[i][j] == 3) {   // position contains white marble
                    zobristKey ^= zobristTable[i][j][3];
                }
            }
        }
        return key;
    }

    /**
     * Updates the hashkey with the movement
     * of a single marble.
     * @param originX - original x coord of marble
     * @param originY - original y coord of marble
     * @param destX - destination x coord of marble
     * @param destY - destination y coord of marble
     * @param color - color of marble
     */
    public void movePiece(int originX, int originY, int destX, int destY, Color color) {
        int colorValue = 0;
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
     *
     * @param key
     * @return
     */
    private int hashFunction(long key) {
        return (int)(key %transpositionTable.size());
    }

    /**
     *
     * @param state
     * @return
     */
    public int getTranspositionTableValue(Agent player, StateSpace state) {
        long tempKey = createHashKey(state.getBoard());
        return transpositionTable.get(hashFunction(tempKey)).score;
    }

    public void addToTranspositionTable(Agent player, StateSpace state, int score) {
        transpositionTable.put(hashFunction(createHashKey(
                state.getBoard())), new Hashentry(0, 0, score, 0 ));
    }
}