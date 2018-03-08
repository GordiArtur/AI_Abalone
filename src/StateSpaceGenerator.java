import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class StateSpaceGenerator {
    private Color playerColor;
    private Board originBoard;
    private int numPieceSets;
    private List<List<Hex>> pieceCombinations;
    private List<String> possibleMoves;
    private List<Board> boardStateSpaces;

    /**
     * Temporary main class for testing
     */
    public static void main(String[] args) {
        System.out.print("Please enter the full file path: ");
        //Scanner sc = new Scanner(System.in);
        //String filename = sc.nextLine();
        String filename = "test1.input";
        new StateSpaceGenerator(filename);
    }

    /**
     * Constructor for StateSpaceGenerator
     */
    private StateSpaceGenerator(String file){
        originBoard = new Board();
        numPieceSets = 0;
        pieceCombinations = new ArrayList<>();

        readConvert(file);
        generatePossiblePieceCombinations();
        validateMoves(originBoard);
        outputPieceSets();
        outputMove();
        outputBoard();
    }

    /**
     * 1. Read input file
     * 2. Convert board layout to native notation
     *   a. set current player color
     *   b. set row and column
     *   c. add piece to originBoard
     * TODO simplify switch statement
     */
    private void readConvert(String filename){
        String gameState = null;

        // open and each line of the file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            playerColor = br.readLine().equals("b") ? Color.BLACK : Color.WHITE;
            gameState = br.readLine();
        } catch (Exception e) {
            System.out.println("File read error");
        }

        if(gameState != null) {
            // convert each piece location to the native notation
            for (String piece : gameState.split(",")) {
                int row;
                int col;
                Color color;

                // set piece color
                color = piece.charAt(2) == 'b' ? Color.BLACK : Color.WHITE;

                // convert row and column notation
                int colValue = Character.getNumericValue(piece.charAt(1));
                switch (piece.charAt(0)) {
                    case 'A': //ex. A3 = 68
                        col = colValue + 3;
                        row = 8;
                        break;
                    case 'B':
                        col = colValue + 2;
                        row = 7;
                        break;
                    case 'C':
                        col = colValue + 1;
                        row = 6;
                        break;
                    case 'D':
                        col = colValue;
                        row = 5;
                        break;
                    case 'E':
                        col = colValue - 1;
                        row = 4;
                        break;
                    case 'F':
                        col = colValue - 2;
                        row = 3;
                        break;
                    case 'G':
                        col = colValue - 3;
                        row = 2;
                        break;
                    case 'H':
                        col = colValue - 4;
                        row = 1;
                        break;
                    case 'I':
                        col = colValue - 5;
                        row = 0;
                        break;
                    default:
                        col = 0;
                        row = 0;
                        break;
                }

                // assign piece to the originBoard
                originBoard.getHex(row, col).setPiece(color);
            }
        }
    }

    /**
     * Finds all the sets of pieces that can be moved and adds to pieceCombinations
     * TODO simplify if possible
     */
    private void generatePossiblePieceCombinations(){
        List<Hex> tempHexSelection;

        // use the double for loop to iterate over the entire board
        for(int i = 0; i < 9 ; i++) {
            for(int j = 0; j < 9 ; j++) {
                // check for a non-null hex, a non-null piece, and the correctly colored origin piece
                if((originBoard.getHex(i, j) != null)&&(originBoard.getHex(i, j).getPiece() != null)&&
                        (originBoard.getHex(i, j).getPiece().getColor() == playerColor)) {
                    // add the origin piece
                    tempHexSelection = new ArrayList<>();
                    tempHexSelection.add(originBoard.getHex(i, j));
                    pieceCombinations.add(numPieceSets++, tempHexSelection);

                    // check for a non-null hex, a non-null piece, and a correctly colored adjacent piece to the north west
                    if((originBoard.getHex(i-1, j-1) != null)&&(originBoard.getHex(i-1, j-1).getPiece() != null)&&
                            (originBoard.getHex(i-1, j-1).getPiece().getColor() == playerColor)){
                        // add a two piece set
                        tempHexSelection = new ArrayList<>();
                        tempHexSelection.add(originBoard.getHex(i, j));
                        tempHexSelection.add(originBoard.getHex(i-1, j-1));
                        pieceCombinations.add(numPieceSets++, tempHexSelection);

                        // check for a non-null hex, a non-null piece, and a correctly colored piece two spaces to the north west
                        if((originBoard.getHex(i-2, j-2) != null)&&(originBoard.getHex(i-2, j-2).getPiece() != null)&&
                                (originBoard.getHex(i-2, j-2).getPiece().getColor() == playerColor)){
                            // add a three piece set
                            tempHexSelection = new ArrayList<>();
                            tempHexSelection.add(originBoard.getHex(i, j));
                            tempHexSelection.add(originBoard.getHex(i-1, j-1));
                            tempHexSelection.add(originBoard.getHex(i-2, j-2));
                            pieceCombinations.add(numPieceSets++, tempHexSelection);
                        }
                    }

                    // check for a non-null hex, a non-null piece, and a correctly colored adjacent piece to the north east
                    if((originBoard.getHex(i, j-1) != null)&&(originBoard.getHex(i, j-1).getPiece() != null)&&
                            (originBoard.getHex(i, j-1).getPiece().getColor() == playerColor)){
                        // add a two piece set
                        tempHexSelection = new ArrayList<>();
                        tempHexSelection.add(originBoard.getHex(i, j));
                        tempHexSelection.add(originBoard.getHex(i, j-1));
                        pieceCombinations.add(numPieceSets++, tempHexSelection);

                        // check for a non-null hex, a non-null piece, and a correctly colored piece two spaces to the north east
                        if((originBoard.getHex(i, j-2) != null)&&(originBoard.getHex(i, j-2).getPiece() != null)&&
                                (originBoard.getHex(i, j-2).getPiece().getColor() == playerColor)){
                            //  add a three piece set
                            tempHexSelection = new ArrayList<>();
                            tempHexSelection.add(originBoard.getHex(i, j));
                            tempHexSelection.add(originBoard.getHex(i, j-1));
                            tempHexSelection.add(originBoard.getHex(i, j-2));
                            pieceCombinations.add(numPieceSets++, tempHexSelection);
                        }
                    }

                    // check for a non-null hex, a non-null piece, and a correctly colored adjacent piece to the east
                    if((originBoard.getHex(i+1, j) != null)&&(originBoard.getHex(i+1, j).getPiece() != null)&&
                            (originBoard.getHex(i+1, j).getPiece().getColor() == playerColor)){
                        //  add a two piece set
                        tempHexSelection = new ArrayList<>();
                        tempHexSelection.add(originBoard.getHex(i, j));
                        tempHexSelection.add(originBoard.getHex(i+1, j));
                        pieceCombinations.add(numPieceSets++, tempHexSelection);

                        // check for a non-null hex, a non-null piece, and a correctly colored piece two spaces to the east
                        if((originBoard.getHex(i+2, j) != null)&&(originBoard.getHex(i+2, j).getPiece() != null)&&
                                (originBoard.getHex(i+2, j).getPiece().getColor() == playerColor)){
                            // add a three piece set
                            tempHexSelection = new ArrayList<>();
                            tempHexSelection.add(originBoard.getHex(i, j));
                            tempHexSelection.add(originBoard.getHex(i+1, j));
                            tempHexSelection.add(originBoard.getHex(i+2, j));
                            pieceCombinations.add(numPieceSets++, tempHexSelection);
                        }
                    }
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
        // iterates through every set of pieces
        // retrieves a List<Hex> which contains 1-3 pieces
        // pieceCombinations.get([index of the specific List<Hex>])
        for(int i = numPieceSets - 1; i > 0; i--) {
            if(pieceCombinations.get(i).size() == 1) {
                //System.out.println((pieceCombinations.get(i).get(0)).getXpos() + "," + (pieceCombinations.get(i).get(0)).getYpos());
            } else if(pieceCombinations.get(i).size() == 2) {
                //System.out.print((pieceCombinations.get(i).get(0)).getXpos() + "," + (pieceCombinations.get(i).get(0)).getYpos() + " : ");
                //System.out.println((pieceCombinations.get(i).get(1)).getXpos() + "," + (pieceCombinations.get(i).get(1)).getYpos());
            } else if(pieceCombinations.get(i).size() == 3) {
                //System.out.print((pieceCombinations.get(i).get(0)).getXpos() + "," + (pieceCombinations.get(i).get(0)).getYpos() + " : ");
                //System.out.print((pieceCombinations.get(i).get(1)).getXpos() + "," + (pieceCombinations.get(i).get(1)).getYpos() + " : ");
                //System.out.println((pieceCombinations.get(i).get(2)).getXpos() + "," + (pieceCombinations.get(i).get(2)).getYpos());
            }
            //boardStateSpaces.add(tempBoard);
        }
    }

    /**
     * Outputs all sets of pieces that can move
     * ONLY FOR TESTING
     */
    private void outputPieceSets(){
        System.out.println("\n" + numPieceSets + " Piece Combinations:");
        // go through the list of piece sets
        for(int i = 0; i < numPieceSets; i++) {
            System.out.print("Set " + i+1 + " = ");
            if(pieceCombinations.get(i).size() == 1) {
                System.out.println((pieceCombinations.get(i).get(0)).getXpos() + "," + (pieceCombinations.get(i).get(0)).getYpos());
            } else if(pieceCombinations.get(i).size() == 2) {
                System.out.print((pieceCombinations.get(i).get(0)).getXpos() + "," + (pieceCombinations.get(i).get(0)).getYpos() + " : ");
                System.out.println((pieceCombinations.get(i).get(1)).getXpos() + "," + (pieceCombinations.get(i).get(1)).getYpos());
            } else if(pieceCombinations.get(i).size() == 3) {
                System.out.print((pieceCombinations.get(i).get(0)).getXpos() + "," + (pieceCombinations.get(i).get(0)).getYpos() + " : ");
                System.out.print((pieceCombinations.get(i).get(1)).getXpos() + "," + (pieceCombinations.get(i).get(1)).getYpos() + " : ");
                System.out.println((pieceCombinations.get(i).get(2)).getXpos() + "," + (pieceCombinations.get(i).get(2)).getYpos());
            }
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