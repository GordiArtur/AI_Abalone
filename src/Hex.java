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
    private static final int HEX_SIZE = 90;
    private static final Color HEX_COLOR = new Color(165, 125, 90);
    private static final Color SELECTED_COLOR = Color.CYAN;

    private Piece piece;
    private int x;
    private int y;
    private JLabel position;
    private Color color;

    public Hex(int x, int y) {
        color = HEX_COLOR;
        setLayout(new BorderLayout());
        setVisible(true);
        this.x = x;
        this.y = y;
        this.position = new JLabel("" + x + "" + y, SwingConstants.CENTER);
        add(position);
    }

    public Piece getPiece() {
        return piece;
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

    public void setXpos(int x) {
        this.x = x;
    }

    public int getYpos() {
        return y;
    }

    public void setYpos(int y) {
        this.y = y;
    }

    public JLabel getPosition() {
        return position;
    }

    public void setPosition(JLabel position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public void setSelectedColor() {
        this.color = SELECTED_COLOR;
        repaint();
    }

    public void setDefaultColor() {
        this.color = HEX_COLOR;
        repaint();
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
            position.setVisible(false);
            piece.setVisible(true);
        } else {
            position.repaint();
            position.setVisible(true);
        }
        repaint();
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        Graphics2D g2d = (Graphics2D) paramGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillOval(0, 0, HEX_SIZE, HEX_SIZE);
        add(position);
    }
}
