import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {

    public static int turn = 0; // 0 = black piece turn; 1 = white piece turn;

    private Controls controls;
    private Board board;

    public Game() {
    	setLayout(new BorderLayout());
        board = new Board();
        controls = new Controls(board);
        add(controls, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setBounds(0, 0, 900, 1000); // Set window size
        setVisible(true);   
        
    }
    
    public static void main(String[] args) {
        Game Game = new Game();

    }
}
