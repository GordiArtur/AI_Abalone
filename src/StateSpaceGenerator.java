import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StateSpaceGenerator {

    private Color playerColor;
    private int numPieceSets;
    private List<List<Hex>> pieceCombinations;

    /**
     * Constructor for StateSpaceGenerator
     */
    private StateSpaceGenerator(String file) {
        numPieceSets = 0;
        pieceCombinations = new ArrayList<>();

        Board originBoard = readConvert(file);
        System.out.println("Input: " + originBoard);
        System.out.println();
        generatePossiblePieceCombinations(originBoard);
        validateMoves(originBoard);
        //outputPieceSets();
        outputMove();
        //outputBoard();
    }

    /**
     * Temporary main class for testing
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String filename = "test1.input";
        System.out.print("Please enter the full file path: ");
        //filename = scan.nextLine();
        new StateSpaceGenerator(filename);
    }

    /**
     * 1. Read input file 2. Convert board layout to native notation a. set current player color b. set row and column
     * c. add piece to originBoard TODO simplify switch statement
     */
    private Board readConvert(String filename) {
        String gameState = null;
        Board board = new Board();

        // open and each line of the file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            playerColor = br.readLine().equals("b") ? Color.BLACK : Color.WHITE;
            gameState = br.readLine();
        } catch (Exception e) {
            System.out.println("File read error");
        }

        if (gameState != null) {
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
                board.getHex(col, row).setPiece(color);
            }
        }
        Board.printBoard(board);
        return board;
    }

    /**
     * Finds all the sets of pieces that can be moved and adds to pieceCombinations TODO simplify if possible
     */
    private void generatePossiblePieceCombinations(Board originBoard) {
        List<Hex> tempHexSelection;

        // use the double for loop to iterate over the entire board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // check for a non-null hex, a non-null piece, and the correctly colored origin piece
                if ((originBoard.getHex(i, j) != null) && (originBoard.getHex(i, j).getPiece() != null) &&
                    (originBoard.getHex(i, j).getPiece().getColor() == playerColor)) {
                    // add the origin piece
                    tempHexSelection = new ArrayList<>();
                    tempHexSelection.add(originBoard.getHex(i, j));
                    pieceCombinations.add(numPieceSets++, tempHexSelection);

                    // check for a non-null hex, a non-null piece, and a correctly colored adjacent piece to the north west
                    if ((originBoard.getHex(i - 1, j - 1) != null) && (originBoard.getHex(i - 1, j - 1).getPiece()
                        != null) &&
                        (originBoard.getHex(i - 1, j - 1).getPiece().getColor() == playerColor)) {
                        // add a two piece set
                        tempHexSelection = new ArrayList<>();
                        tempHexSelection.add(originBoard.getHex(i, j));
                        tempHexSelection.add(originBoard.getHex(i - 1, j - 1));
                        pieceCombinations.add(numPieceSets++, tempHexSelection);

                        // check for a non-null hex, a non-null piece, and a correctly colored piece two spaces to the north west
                        if ((originBoard.getHex(i - 2, j - 2) != null) && (originBoard.getHex(i - 2, j - 2).getPiece()
                            != null) &&
                            (originBoard.getHex(i - 2, j - 2).getPiece().getColor() == playerColor)) {
                            // add a three piece set
                            tempHexSelection = new ArrayList<>();
                            tempHexSelection.add(originBoard.getHex(i, j));
                            tempHexSelection.add(originBoard.getHex(i - 1, j - 1));
                            tempHexSelection.add(originBoard.getHex(i - 2, j - 2));
                            pieceCombinations.add(numPieceSets++, tempHexSelection);
                        }
                    }

                    // check for a non-null hex, a non-null piece, and a correctly colored adjacent piece to the north east
                    if ((originBoard.getHex(i, j - 1) != null) && (originBoard.getHex(i, j - 1).getPiece() != null) &&
                        (originBoard.getHex(i, j - 1).getPiece().getColor() == playerColor)) {
                        // add a two piece set
                        tempHexSelection = new ArrayList<>();
                        tempHexSelection.add(originBoard.getHex(i, j));
                        tempHexSelection.add(originBoard.getHex(i, j - 1));
                        pieceCombinations.add(numPieceSets++, tempHexSelection);

                        // check for a non-null hex, a non-null piece, and a correctly colored piece two spaces to the north east
                        if ((originBoard.getHex(i, j - 2) != null) && (originBoard.getHex(i, j - 2).getPiece() != null)
                            &&
                            (originBoard.getHex(i, j - 2).getPiece().getColor() == playerColor)) {
                            //  add a three piece set
                            tempHexSelection = new ArrayList<>();
                            tempHexSelection.add(originBoard.getHex(i, j));
                            tempHexSelection.add(originBoard.getHex(i, j - 1));
                            tempHexSelection.add(originBoard.getHex(i, j - 2));
                            pieceCombinations.add(numPieceSets++, tempHexSelection);
                        }
                    }

                    // check for a non-null hex, a non-null piece, and a correctly colored adjacent piece to the east
                    if ((originBoard.getHex(i + 1, j) != null) && (originBoard.getHex(i + 1, j).getPiece() != null) &&
                        (originBoard.getHex(i + 1, j).getPiece().getColor() == playerColor)) {
                        //  add a two piece set
                        tempHexSelection = new ArrayList<>();
                        tempHexSelection.add(originBoard.getHex(i, j));
                        tempHexSelection.add(originBoard.getHex(i + 1, j));
                        pieceCombinations.add(numPieceSets++, tempHexSelection);

                        // check for a non-null hex, a non-null piece, and a correctly colored piece two spaces to the east
                        if ((originBoard.getHex(i + 2, j) != null) && (originBoard.getHex(i + 2, j).getPiece() != null)
                            &&
                            (originBoard.getHex(i + 2, j).getPiece().getColor() == playerColor)) {
                            // add a three piece set
                            tempHexSelection = new ArrayList<>();
                            tempHexSelection.add(originBoard.getHex(i, j));
                            tempHexSelection.add(originBoard.getHex(i + 1, j));
                            tempHexSelection.add(originBoard.getHex(i + 2, j));
                            pieceCombinations.add(numPieceSets++, tempHexSelection);
                        }
                    }
                }
            }
        }
    }

    /**
     * Goes through each piece in pieceCombinations and calls validMove() to validate If valid:
     * 1. the pieces are moved on the tempBoard
     * 2. add the move to the Move.output file
     * 3. add the resulting board configuration to the Board.output file
     */
    private void validateMoves(final Board originBoard) {
        // iterates through every set of pieces
        // retrieves a List<Hex> which contains 1-3 pieces
        // pieceCombinations.get([index of the specific List<Hex>])
        List<String> boardList = new ArrayList<>();
        List<String> moveList = new ArrayList<>();
        //TreeSet<String> moveList = new TreeSet<>();
        for (List<Hex> list : pieceCombinations) {
            Board testMove = new Board(originBoard);
            //System.out.println("Check for Direction: NW");
            if (validMove(-1, -1, list, testMove)) {
                boardList.add(testMove.toString());
                moveList.add(moveToString(-1, -1, list));
                testMove = new Board(originBoard);
            }
            //System.out.println("Check for Direction: W");
            if (validMove(-1, 0, list, testMove)) {
                boardList.add(testMove.toString());
                moveList.add(moveToString(-1, 0, list));
                testMove = new Board(originBoard);
            }
            //System.out.println("Check for Direction: SW");
            if (validMove(0, 1, list, testMove)) {
                boardList.add(testMove.toString());
                moveList.add(moveToString(0, 1, list));
                testMove = new Board(originBoard);
            }
            //System.out.println("Check for Direction: NE");
            if (validMove(0, -1, list, testMove)) {
                boardList.add(testMove.toString());
                moveList.add(moveToString(0, -1, list));
                testMove = new Board(originBoard);
            }
            //System.out.println("Check for Direction: E");
            if (validMove(1, 0, list, testMove)) {
                boardList.add(testMove.toString());
                moveList.add(moveToString(1, 0, list));
                testMove = new Board(originBoard);
            }
            //System.out.println("Check for Direction: SE");
            if (validMove(1, 1, list, testMove)) {
                boardList.add(testMove.toString());
                moveList.add(moveToString(1, 1, list));
            }
        }
        // Debugging output
        for (String s : boardList) {
            System.out.println(s);
        }
    }

    /**
     * Outputs all sets of pieces that can move ONLY FOR TESTING
     */
    private void outputPieceSets() {
        System.out.println("\n" + numPieceSets + " Piece Combinations:");
        // go through the list of piece sets
        int i = 0;
        for (List<Hex> l : pieceCombinations) {
            System.out.print("Set " + (i + 1) + " = ");
            for (Hex h : l) {
                System.out.print(h.getID() + " ");
            }
            System.out.println();
            i++;
        }
    }

    /**
     * Generate player move in string representation, similar to updateHistory in Game.java
     * @param dx X direction value (-1, 0, 1)
     * @param dy Y direction value (-1, 0, 1)
     * @param selectedHex List of Hex with pieces
     * @return String representation of player move
     */
    private String moveToString(int dx, int dy, List<Hex> selectedHex) {
        StringBuilder lastMove = new StringBuilder((selectedHex.get(0).getPiece().getColor().equals(
            Color.BLACK)) ? "BLACK" : "WHITE");
        lastMove.append(" ").append(Controls.getDirectionText(dx, dy));
        for (Hex h : selectedHex) {
            lastMove.append(" ").append(h.getID());
        }
        return lastMove.toString();
    }

    /**
     * Outputs the possible move to an output file
     */
    private void outputMove() {
    }

    /**
     * Outputs the resulting state from a valid move to an output file
     */
    private String outputBoard(Board b) {
        return null;
    }

    /**
     * Checks if the selected hexes and called action is a legal move. If so, apply Board.playedMove(). 1. validMove
     * sorts selectedHex array and reverses if moving away from origin (Origin is Top-Left) 2. Generate special identity
     * of direction 3. Generate identity of selectedHex array 4. First check identity to direction identity, Inline 4.1.
     * If adjacent hex is empty space or off-board area, playedMove and return true 4.2. Else create empty temp list.
     * Checks for gaps, same color pieces, and last piece. 4.3. Reverse temp, add selectedHex to temp, 5 Second check
     * for Broadside and single piece 5.1. Check for available space, move then return true 6. movePiece, clear
     * selected, switchTurn() for 4.1, 4.2, and 5 move scenarios return false for nullptr and blocked moves return true
     * for validMove (assumes Board.playedMove() is successful)
     *
     * @param dx X value of horizontal movement
     * @param dy Y value of vertical movement
     */
    private boolean validMove(final int dx, final int dy, final List<Hex> list, Board board) { // Don't read it. It's very long
        // System.out.println("Checking valid move");
        List<Hex> selectedHex = sortSelected(list);
        if (dx > 0 || dy > 0) {
            Collections.reverse(selectedHex);
        }
        // System.out.println("Origin Point " + selectedHex.get(0).getID());
        int identity = 0;
        int didentity = Math.abs(dx) * 10 + Math.abs(dy);
        int sx, sy;
        if (selectedHex.size() > 1) {
            identity = identity(selectedHex.get(0).getXpos(), selectedHex.get(0).getYpos(),
                selectedHex.get(1).getXpos(), selectedHex.get(1).getYpos());
        }
        if (identity > 0 && identity == didentity) { // Inline
            sx = selectedHex.get(0).getXpos();
            sy = selectedHex.get(0).getYpos();
            // Empty space or Off-board
            if (board.getHex(sx + dx, sy + dy) == null) {
                return false;
            } else if (board.getHex(sx + dx, sy + dy).getPiece() == null) {
                movePieces(selectedHex, dx, dy, board);
                return true;
            } else { // Inline Sumito checks
                ArrayList<Hex> temp = new ArrayList<>();
                for (int i = 1; i <= selectedHex.size(); i++) {
                    if (board.getHex(sx + (dx * i), sy + (dy * i)) == null
                        || board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() == null) { // Gap space sumito
                        break;
                    } else if (board.getHex(sx + (dx * i), sy + (dy * i)).getPiece().getColor()
                        .equals(selectedHex.get(0).getPiece().getColor())) { // Same color blocker
                        return false;
                    } else if (i == selectedHex.size()
                        && board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() != null) { // Last piece blocker
                        return false;
                    } else { // Add this piece to temp; piece to be moved.
                        temp.add(board.getHex(sx + (dx * i), sy + (dy * i)));
                    }
                }
                Collections.reverse(temp);
                temp.addAll(selectedHex);
                movePieces(temp, dx, dy, board);
                return true;
            }
        } else { // Broadside and singular
            for (Hex hex : selectedHex) {
                sx = hex.getXpos();
                sy = hex.getYpos();
                if (board.getHex(sx + dx, sy + dy) == null) {
                    return false;
                } else if (board.getHex(sx + dx, sy + dy).getPiece() != null) {
                    return false;
                }
            }
            movePieces(selectedHex, dx, dy, board);
            return true;
        }
    }

    /**
     * Sorts the List<Hex> for selectedHex to arrange Hexes from origin point (top-left corner) in ascending order. Not
     * generic code.
     */
    private List<Hex> sortSelected(List<Hex> selectedHex) {
        List<Hex> temp = new ArrayList<>();
        if (selectedHex.size() == 3) {
            List<Hex> unsorted = new ArrayList<>(selectedHex);
            Hex a = unsorted.get(0);
            Hex b = unsorted.get(1);
            Hex c = unsorted.get(2);
            temp.add((a.getXY() < b.getXY()) ? (a.getXY() < c.getXY()) ? a : c : (b.getXY() < c.getXY()) ? b : c);
            unsorted.remove(temp.get(0));
            temp.add((unsorted.get(0).getXY() < unsorted.get(1).getXY()) ? unsorted.get(0) : unsorted.get(1));
            unsorted.remove(temp.get(1));
            temp.add(unsorted.get(0));
            return new ArrayList<>(temp);
        } else if (selectedHex.size() == 2) {
            Hex small = (selectedHex.get(0).getXY() < selectedHex.get(1).getXY()) ? selectedHex.get(0)
                : selectedHex.get(1);
            Hex large = (selectedHex.get(0).getXY() > selectedHex.get(1).getXY()) ? selectedHex.get(0)
                : selectedHex.get(1);
            temp.add(small);
            temp.add(large);
            return new ArrayList<>(temp);
        }
        return new ArrayList<>(selectedHex);
    }

    /**
     * Outputs an integer that represents the axial direction of the elements in selectedHex. 1 is vertical 10 is
     * horizontal 11 is diagonal
     *
     * @param sx First hex x coordinate
     * @param sy First hex y coordinate
     * @param dx Last hex x coordinate
     * @param dy Last hex y coordinate
     * @return Integer Identity of axial direction
     */
    private int identity(int sx, int sy, int dx, int dy) {
        int out = 0;
        if ((dx + dy) == (sx + sy)) {
            return 0;
        }
        out += (Math.abs(dx - sx) == 1) ? 10 : 0;
        out += (Math.abs(dy - sy) == 1) ? 1 : 0;
        return (Math.abs(dx - sx) > 1 || Math.abs(dy - sy) > 1) ? 0 : out;
    }

    /**
     * Calls board.movePiece() to move pieces in hexes.
     *
     * @param hexes Array of hexes with pieces to move
     * @param dx X coordinate move (-1 to 1)
     * @param dy Y coordinate move (-1 to 1)
     */
    private void movePieces(final List<Hex> hexes, final int dx, final int dy, final Board b) {
        for (Hex hex : hexes) {
            int sx = hex.getXpos();
            int sy = hex.getYpos();
            Board.movePiece(sx, sy, sx + dx, sy + dy, b);
        }
    }
}