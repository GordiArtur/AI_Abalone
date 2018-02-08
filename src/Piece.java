import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Piece extends JPanel {
    private Color color;
    
    public Piece(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(100, 100));
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        
        paramGraphics.setColor(color);
        paramGraphics.fillOval(25, 20, 60, 60);
    }
}
