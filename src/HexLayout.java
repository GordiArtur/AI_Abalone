import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

// Represents the layout of game spaces in a hexagon
public class HexLayout extends JPanel {

    private Hex[][] hexes = new Hex[Board.BOARD_SIZE][Board.BOARD_SIZE];
    
    public HexLayout() {
        setLayout(null);
        drawBoard();
        assignHexCoordinates();
        setStandard(); // This will need to be changed depending on layout selection
    }
    
    // Draws the board in a hexagon shape
    private void drawBoard() {
        int x = 0;
        int y = 0;

        for(int row = 0; row < Board.BOARD_SIZE; row++) {
            for(int col = 0; col < Board.BOARD_SIZE; col++) {
                if ((row == 0 || row == 8) && (col < 2 || col > 6)) {
                    hexes[col][row] = new Hex(false);
                } else if ((row == 1 || row == 7) && (col == 0 || col > 6))  {
                    hexes[col][row] = new Hex(false);
                } else if ((row == 2 || row == 6) && (col == 0 || col == 8))  {
                    hexes[col][row] = new Hex(false);
                } else if ((row == 3 || row == 5) && col == 8)  {
                    hexes[col][row] = new Hex(false);
                } else {
                    hexes[col][row] = new Hex(true);
                }
                add(hexes[col][row]);
                hexes[col][row].setBounds(y, x, 100, 100);
                y += 110;
            }
            if(row%2 == 0) {
                y = 55;
            } else {
                y = 0;
            }
            x += 90;
        }
    }
    
    // Assigns proper coordinates to the hexes
    private void assignHexCoordinates() {
        int column = 0;
        
        for(int row = 0; row <= 4; row++) {
            for(int col = 0; col < Board.BOARD_SIZE; col++) {
                if (hexes[col][row].getVisible()) {
                    hexes[col][row].setCoordinates(column, row);
                    column++;
                }
            }
            column = 0;
        }
        
        column = 8;

        for(int row = 5; row < Board.BOARD_SIZE; row++) {
            for(int col = 8; col >= 0; col--) {
                if (hexes[col][row].getVisible()) {
                    hexes[col][row].setCoordinates(column, row);
                    column--;
                }
            }
            column = 8; 
        }
    }
    
    // Sets standard board configuration
    public void setStandard() {
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < Board.BOARD_SIZE; col++) {
                if (hexes[col][row].getVisible()) {
                    if (row != 2) {
                        hexes[col][row].setPiece(Color.WHITE);
                    } else if (hexes[col][row].getCoordinates().getColumn() > 1 
                            && hexes[col][row].getCoordinates().getColumn() < 5) {
                        hexes[col][row].setPiece(Color.WHITE);
                    }
                }
            }
        }
        
        for(int row = 6; row < Board.BOARD_SIZE; row++) {
            for(int col = 0; col < Board.BOARD_SIZE; col++) {
                if (hexes[col][row].getVisible()) {
                    if (row != 6) {
                        hexes[col][row].setPiece(Color.BLACK);
                    } else if (hexes[col][row].getCoordinates().getColumn() > 3 
                            && hexes[col][row].getCoordinates().getColumn() < 7) {
                        hexes[col][row].setPiece(Color.BLACK);
                    }
                }
            }
        }
    }
    
    public void setBelgianDaisy() {
        
    }
    
    public void setGermanDaisy() {
        
    }
    
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
    }

}
