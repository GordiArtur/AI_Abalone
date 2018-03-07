import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class StateSpaceGenerator {
    /**
     * Reference to Game object from Game
     */
    private Game game;
    /**
     * The board created by the initial state space
     */
    private Board originBoard;
    /**
     * A temporary board used to record the resulting state space from validMove()
     */
    private Board tempBoard;
    /**
     * Array of hexes (3) - a possible move
     */
    private List<Hex> selectedHex;
    /**
     * Array of selectedHex - list of pieces to be moved
     */
    private List<List<Hex>> possibleMoves;
    /**
     * Array of two-d arrays - the list of resulting possible state spaces
     */
    private List<Hex[][]> possibleStateSpaces;

    /*
     * Constructor for StateSpaceGenerator
     */
    public StateSpaceGenerator(){
        readConvert();
        generatePossibleMoves();
        validateStateSpace();
        showStateSpace();
    }


    /*
     * Reads input files and sets the initial Board and Game based on
     * the file data
     */
    public void readConvert(){
        String playerTurn = "";
        String gameState = "";

        // open and read the file
        try (BufferedReader br = new BufferedReader(new FileReader("Test1.input"))) {
            playerTurn = br.readLine();
            gameState = br.readLine();
        } catch (Exception e) {
            System.out.println("File read error");
        }

        // convert each piece location to the native notation
        for(String piece: gameState.split(",")) {
            int row;
            int col;
            Color color;

            // convert row and column notation
            switch(piece.charAt(0)){
                case 'A':
                    row = 8;
                    col = (int)piece.charAt(1) - 5;
                    break;
                case 'B':
                    row = 7;
                    col = (int)piece.charAt(1) - 4;
                    break;
                case 'C':
                    row = 6;
                    col = (int)piece.charAt(1) - 3;
                    break;
                case 'D':
                    row = 5;
                    col = (int)piece.charAt(1) - 2;
                    break;
                case 'E':
                    row = 4;
                    col = (int)piece.charAt(1) - 1;
                    break;
                case 'F':
                    row = 3;
                    col = (int)piece.charAt(1);
                    break;
                case 'G':
                    row = 2;
                    col = (int)piece.charAt(1) + 1;
                    break;
                case 'H':
                    row = 1;
                    col = (int)piece.charAt(1) + 2;
                    break;
                case 'I':
                    row = 0;
                    col = (int)piece.charAt(1) + 3;
                    break;
                default:
                    row = 0;
                    col = 0;
                    break;
            }

            // if the piece belongs to black
            if(piece.charAt(2) == 'b') {
                color = Color.BLACK;
            } else {
                color = Color.WHITE;
            }

            // assign piece to board
            Piece tempPiece = new Piece(color, row, col);
        }
    }

    /*
     * Iterates through all possible moves (selectedHex) from the game and board
     */
    private void generatePossibleMoves(){
        // go through the board array
        // only select pieces that match the current player turn
        // save to possibleMoves list
    }

    /*
     * Goes through possibleMoves and checks if a move is valid
     * Copy and modify MovementControls.validMove
     * OR modify MovementControls to take a selectedHex as a parameter
     * If the move is valid, call showStateSpace
     */
    private void validateStateSpace(){}

    /*
     * Outputs the resulting state from a valid move
     */
    private void showStateSpace(){}
}