import java.util.Scanner;

public class Game {

    private Board board;
    private AI ai;
    
    private static Scanner scan;
    
    public Game() {
        this.board = new Board(9);
        this.ai = new AI(false);
        
        board.addMarble(1,0,true);
        board.addMarble(3,4,false);
        
        int count = 0;
        
        while (board.getWhiteCount() != 0 || board.getBlackCount() != 0) {
            System.out.print("*****************Turn " + count++ + "*****************\n");
            board.printBoard();
            
            if ((double) count % 2 != 0) {
                makeMove(board);
            } else { 
                makeMoveAI(board);
            }
        }
        
        System.out.print("Game over");
    }
    
    public static void main(String[] args) {
        scan = new Scanner(System.in);
        Game Game = new Game();
        
    }

    private void makeMoveAI(Board board) {
        ai.move(board);
    }

    private void makeMove(Board board) {
        System.out.print("Enter coordinates direction I.E. \"x, y, dir\" : ");
        int x = scan.nextInt();
        int y = scan.nextInt();
        int dir = scan.nextInt();
        System.out.print("You entered coordinates direction: " + x + " " + y + " " + dir);
        System.out.println();
        board.update(x, y, dir);
    }
    
}
