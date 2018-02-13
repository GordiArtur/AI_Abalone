import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Artur Gordiyenko on 2018-02-06.
 */
public class Human implements Agent {
	private boolean isActive = false;

	public void play() {
		isActive = true;
	}

	class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			Hex selectHex = null;
			try {
				selectHex = (Hex) e.getSource();
			} catch (NullPointerException npe) {

			}
			if (selectHex != null) {
				System.out.println(selectHex.getID());
			}
		}
	}
}
