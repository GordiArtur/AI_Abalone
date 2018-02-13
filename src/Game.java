import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {

	public static int turn = 0; // 0 = black piece turn; 1 = white piece turn;
	public static int turnCount = 0; // History of turns

	private Board board;
	private Human human;
	private Controls controls;

	public Game() {
		setLayout(new BorderLayout());
		board = new Board();
		controls = new Controls(board);
		human = new Human(board);
		add(controls, BorderLayout.NORTH);
		add(board, BorderLayout.CENTER);
		add(human, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		pack();
		setBounds(0, 0, 1000, 1000); // Set window size
		setVisible(true);

		while (board.getBlackCount() > 8 && board.getWhiteCount() > 8) {
			human.play(board);
			if (turn == 1) {
				turn = 0;
				System.out.println("RESET");
			}
		}
	}

	public static void main(String[] args) {
		Game Game = new Game();

	}
}
