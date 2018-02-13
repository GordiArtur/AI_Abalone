import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Represents one space in the game
public class Hex extends JPanel {
    private boolean visible;
    private Piece piece;
    private int x;
    private int y;
    private JLabel label;
    
    public Hex(boolean visible, int x, int y) {
    	setLayout(new BorderLayout());
        this.visible = visible;
        this.x = x;
        this.y = y;
        this.label = new JLabel("" + x + "" + y, SwingConstants.CENTER);
        add(label);
    }
    
    public String getID() {
    	return "" + x + "" + y;
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public void setPiece(Color color) {
    	if (color == null) {
    		this.piece = null;
    		return;
    	}
        this.piece = new Piece(color, x, y);
        this.add(piece);
    }
    
    public Piece getPiece() {
    	return piece;
    }
    
    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        Graphics2D g2d = (Graphics2D) paramGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (visible) {
            g2d.setColor(new Color(165, 125, 90));
            g2d.fillOval(0, 0, 100, 100);
        }
    }
}
