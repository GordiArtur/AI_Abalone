
public class AI implements Agent {

    private Board board;
    private Controls control;
    private Game aiGame;
    
    public AI(Game game,Board board, Controls control) {
        aiGame = game;
        this.board = board;
        this.control = control;
    }

    public Board play(Board board) {
        System.out.println("Ai is playing");
        aiGame.setTurn(true);
        aiGame.switchTurn();
        control.incrementTurn();
        return board;
    }
}
