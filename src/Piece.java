import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Piece extends JPanel {
	public static final int PIECE_SIZE = 45;
	private Color color;
	private JLabel label;

	public Piece(Color color, int x, int y) {
		this.color = color;
		setPreferredSize(new Dimension(PIECE_SIZE * 2, PIECE_SIZE * 2));
		setLayout(new BorderLayout());
		setOpaque(false);
		this.label = new JLabel("" + x + "" + y, SwingConstants.CENTER);
		if (color == Color.WHITE) {
			label.setForeground(Color.BLACK);
		} else {
			label.setForeground(Color.WHITE);
		}
		add(label);
	}

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
