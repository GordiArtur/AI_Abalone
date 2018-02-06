import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class Hex extends JPanel {

    private Color color;
    
    public Hex(Color color) {
        this.color = color;
        Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int dim = (int)(1.0 / Board.BOARD_SIZE * Math.min(localDimension.width * 0.75, 
                localDimension.height * 0.75));
        
        Ellipse2D.Float localCircle = new Ellipse2D.Float(((localDimension.width - dim) / 2), 
        ((localDimension.height - dim) / 2), dim, dim);       
    }
    
    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
    }
}
