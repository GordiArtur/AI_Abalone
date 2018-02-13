
public class AI implements Agent {

	private Board board;
	private Controls control;
	
	public AI(Board board, Controls control) {
		this.board = board;
		this.control = control;
	}

	public Board play(Board board) {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
