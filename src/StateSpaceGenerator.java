import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 *
 */
public class StateSpaceGenerator {
    /**
     *
     */
    private Color playerColor;
    /**
     *
     */
    private Board originBoard;
    /**
     *
     */
    private List<Hex> selectedHex;
    /**
     * Array of selectedHex - list of possible piece combinations
     */
    private List<List<Hex>> pieceCombinations;
    /**
     * Array of two-d arrays - the list of resulting possible state spaces
     */
    private List<Hex[][]> possibleStateSpaces;

    /**
     * Constructor for StateSpaceGenerator
     */
    public StateSpaceGenerator(){
        originBoard = new Board();

        readConvert();
        generatePossiblePieceCombinations();
        validateMoves(originBoard);
    }

    /**
     * 1. Read input file
     * 2. Convert board layout to native notation
     *   a. set current player color
     *   b. set row and column
     *   c. add piece to originBoard
     */
    private void readConvert(){
        String gameState = "";

        // open and read the file
        try (BufferedReader br = new BufferedReader(new FileReader("Test1.input"))) {
            playerColor = br.readLine().equals("b") ? Color.BLACK : Color.WHITE;
            gameState = br.readLine();
        } catch (Exception e) {
            System.out.println("File read error");
        }

        // convert each piece location to the native notation
        for(String piece: gameState.split(",")) {
            int row;
            int col;
            Color color;

            // set piece color
            color = piece.charAt(2) == 'b' ? Color.BLACK : Color.WHITE;

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

            // assign piece to hex array
            originBoard.getHex(row, col).setPiece(color);
        }
    }

    /**
     * Finds all possible piece combinations and inserts them
     * into pieceCombinations
     */
    private void generatePossiblePieceCombinations(){
        // loop through the hex array
        for(int i = 0; i < 9 ; i++) {
            for(int j = 0; j < 9 ; j++) {
                // check for a null hex, a null piece, and the correctly colored origin piece
                if((originBoard.getHex(i, j) != null)&&(originBoard.getHex(i, j).getPiece() != null)&&
                        (originBoard.getHex(i, j).getPiece().getColor() == playerColor)) {
                    // add the origin piece by itself to the list of pieces
                    selectedHex.clear();
                    selectedHex.add(originBoard.getHex(i, j));
                    pieceCombinations.add(selectedHex);

                    // check for a null hex, a null piece, and a correctly colored adjacent piece to the north west
                    if((originBoard.getHex(i-1, j-1) != null)&&(originBoard.getHex(i-1, j-1).getPiece() != null)&&
                            (originBoard.getHex(i-1, j-1).getPiece().getColor() == playerColor)){
                        //  add a two piece set to the pieceCombinations
                        selectedHex.add(originBoard.getHex(i-1, j-1));
                        pieceCombinations.add(selectedHex);
                        // check for a null hex, a null piece, and a correctly colored piece in the next space over
                        if((originBoard.getHex(i-2, j-2) != null)&&(originBoard.getHex(i-2, j-2).getPiece() != null)&&
                                (originBoard.getHex(i-2, j-2).getPiece().getColor() == playerColor)){
                            //  add a three piece set to the pieceCombinations
                            selectedHex.add(originBoard.getHex(i-2, j-2));
                            pieceCombinations.add(selectedHex);
                        }
                    }
                    // no more pieces in that direction, so empty the hex, and re-add the origin piece
                    selectedHex.clear();
                    selectedHex.add(originBoard.getHex(i, j));
                    pieceCombinations.add(selectedHex);

                    // check for a null hex, a null piece, and a correctly colored adjacent piece to the north east
                    if((originBoard.getHex(i, j-1) != null)&&(originBoard.getHex(i, j-1).getPiece() != null)&&
                            (originBoard.getHex(i, j-1).getPiece().getColor() == playerColor)){
                        //  add a two piece set to the pieceCombinations
                        selectedHex.add(originBoard.getHex(i, j-1));
                        pieceCombinations.add(selectedHex);
                        // check for a null hex, a null piece, and a correctly colored piece in the next space over
                        if((originBoard.getHex(i, j-2) != null)&&(originBoard.getHex(i, j-2).getPiece() != null)&&
                                (originBoard.getHex(i, j-2).getPiece().getColor() == playerColor)){
                            //  add a three piece set to the pieceCombinations
                            selectedHex.add(originBoard.getHex(i, j-2));
                            pieceCombinations.add(selectedHex);
                        }
                    }
                    // no more pieces in that direction, so empty the hex, and re-add the origin piece
                    selectedHex.clear();
                    selectedHex.add(originBoard.getHex(i, j));
                    pieceCombinations.add(selectedHex);

                    // check for a null hex, a null piece, and a correctly colored adjacent piece to the east
                    if((originBoard.getHex(i+1, j) != null)&&(originBoard.getHex(i+1, j).getPiece() != null)&&
                            (originBoard.getHex(i+1, j).getPiece().getColor() == playerColor)){
                        //  add a two piece set to the pieceCombinations
                        selectedHex.add(originBoard.getHex(i+1, j));
                        pieceCombinations.add(selectedHex);
                        // check for a null hex, a null piece, and a correctly colored piece in the next space over
                        if((originBoard.getHex(i+2, j) != null)&&(originBoard.getHex(i+2, j).getPiece() != null)&&
                                (originBoard.getHex(i+2, j).getPiece().getColor() == playerColor)){
                            //  add a three piece set to the pieceCombinations
                            selectedHex.add(originBoard.getHex(i+2, j));
                            pieceCombinations.add(selectedHex);
                        }
                    }
                    selectedHex.clear();
                }
            }
        }
    }

    /**
     * Goes through each piece in pieceCombinations and calls
     * validMove() to validate
     * If valid:
     *   1. the pieces are moved on the tempBoard
     *   2. add the move to the Move.output file
     *   3. add the resulting board configuration to the Board.output file
     */
    private void validateMoves(Board tempBoard){
        // go through each possible combination of pieces
        for(List<Hex> pieceSet : pieceCombinations) {
            /*
            if(validMove(0, -1)) {outputMove(); outputBoard();};
            if(validMove(1, 0)) {outputMove(); outputBoard();};
            if(validMove(1, 1)) {outputMove(); outputBoard();};
            if(validMove(0, 1)) {outputMove(); outputBoard();};
            if(validMove(-1, 0)) {outputMove(); outputBoard();};
            if(validMove(-1, -1)) {outputMove(); outputBoard();};
            */
        }
    }

    /**
     * Outputs the possible move to an output file
     */
    private void outputMove(){}

    /**
     * Outputs the resulting state from a valid move to an output file
     */
    private void outputBoard(){}

}