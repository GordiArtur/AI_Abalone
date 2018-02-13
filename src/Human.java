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
}
