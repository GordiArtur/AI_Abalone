import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

// Represents one space in the game
public class Hex extends JPanel {
    private boolean visible;
    private Piece piece;
    private Coordinates coordinates;
    
    public Hex(boolean visible) { 
        this.visible = visible;
        this.coordinates = new Coordinates(-1, -1);
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public void setCoordinates(int column, int row) {
        coordinates.setColumn(column);
        coordinates.setRow(row);
    }
    
    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public void setPiece(Color color) {
        this.piece = new Piece(color);
        this.add(piece);
    }
    
    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        
        if (visible) {
            paramGraphics.setColor(new Color(165, 125, 90));
            paramGraphics.fillOval(10, 10, 90, 90);
        }
    }
}
