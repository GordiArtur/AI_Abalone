import java.util.Scanner;

public class game {

    private board Board;
    private ai AI;
    
    private static Scanner scan;
    
    public game() {
        this.Board = new board(9);
        this.AI = new ai(false);
        
        Pieces p = Pieces.BLACK;
        Board.addMarble(1,0,true);
        Board.addMarble(3,4,false);
        
        int count = 0;
        
        while (Board.getWhiteCount() != 0 || Board.getBlackCount() != 0) {
            System.out.print("*****************Turn " + count++ + "*****************\n");
            Board.printBoard();
            
            if ((double) count % 2 != 0) {
                makeMove(Board);
            } else { 
                makeMoveAI(Board);
            }
        }
        
        System.out.print("Game over");
    }
    
    public static void main(String[] args) {
        scan = new Scanner(System.in);
        game Game = new game();
        
    }

    private void makeMoveAI(board board) {
        ai.move(board);
        
    }

    private void makeMove(board board) {
        System.out.print("Enter coordinates direction: ");
        int x = scan.nextInt();
        int y = scan.nextInt();
        int dir = scan.nextInt();
        System.out.print("You entered coordinates direction: " + x + " " + y + " " + dir);
        System.out.println();
        board.update(x, y, dir);
    }
    
}
