import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Game extends JFrame {

    private Board board;
    private Human human;
    
    public Game() {
    	setLayout(new BorderLayout()); // Other elements can be added to the BorderLayout
        board = new Board();
        add(board);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setBounds(0, 0, 1500, 1000); // Set window size
        setVisible(true);   
        
        human = new Human();
        while (board.getBlackCount() > 8 && board.getWhiteCount() > 8) {
        	
        }
    }
    
    public static void main(String[] args) {
        Game Game = new Game();
        
    }
}
