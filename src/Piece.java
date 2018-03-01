import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Piece extends JPanel {

    /**
     * The size of a piece on a hex
     */
    private static final int PIECE_SIZE = 45;

    /**
     * The color of the piece
     */
    private Color color;

    /**
     * The position of the piece
     */
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

    /**
     * Color accessor
     * @return the color of the piece
     */
    public Color getColor() {
        return color;
    }

    @Override
    public void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);

        paramGraphics.setColor(color);
        paramGraphics.fillOval(PIECE_SIZE / 2, PIECE_SIZE / 2, PIECE_SIZE, PIECE_SIZE);
    }
}
