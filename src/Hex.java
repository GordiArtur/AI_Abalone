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
    public static final int HEX_SIZE = 90;
    private Piece piece;
    private int x;
    private int y;
    private JLabel label;
    public static Color defaultColor;
    private Color color;

    public Hex(int x, int y) {
        defaultColor = new Color(165, 125, 90);
        color = defaultColor;
        setLayout(new BorderLayout());
        setVisible(true);
        this.x = x;
        this.y = y;
        this.label = new JLabel("" + x + "" + y, SwingConstants.CENTER);
        add(label);
    }

    public String getID() {
        return "" + x + "" + y;
    }

    public int getXY() {
        return x * 10 + y;
    }

    public void redraw() {
        setVisible(false);
        if (piece != null) {
            piece.repaint();
            label.setVisible(false);
            piece.setVisible(true);
        } else {
            label.repaint();
            label.setVisible(true);
        }
        repaint();
        setVisible(true);
    }

    public void setPiece(Color color) {
        if (color == null) {
            this.piece.setVisible(false);
            this.piece = null;
            redraw();
            return;
        }
        this.piece = new Piece(color, x, y);
        this.add(piece);
        redraw();
    }

    public int getXpos() {
        return x;
    }

    public int getYpos() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setColor(Color c) {
        color = c;
        repaint();
    }

    public void setDefaultColor() {
        color = defaultColor;
        repaint();
    }

    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        Graphics2D g2d = (Graphics2D) paramGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillOval(0, 0, HEX_SIZE, HEX_SIZE);
        add(label);
    }
}
