import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Piece extends JPanel {
    private static final int PIECE_SIZE = 45;

    private Color color;
    private JLabel position;

    public Piece(Color color, int x, int y) {
        this.color = color;
        setPreferredSize(new Dimension(PIECE_SIZE * 2, PIECE_SIZE * 2));
        setLayout(new BorderLayout());
        setOpaque(false);
        this.position = new JLabel("" + x + "" + y, SwingConstants.CENTER);
        if (color == Color.WHITE) {
            position.setForeground(Color.BLACK);
        } else {
            position.setForeground(Color.WHITE);
        }
        add(position);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public JLabel getPosition() {

        return position;
    }

    public void setPosition(JLabel position) {
        this.position = position;
    }

    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);

        paramGraphics.setColor(color);
        paramGraphics.fillOval(PIECE_SIZE / 2, PIECE_SIZE / 2, PIECE_SIZE, PIECE_SIZE);
    }
}
