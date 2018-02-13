import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;


public class Controls extends JPanel {

    private final int width = 200;
    private final int height= 50;


    public Controls() {
        add(new JButton("Start"));
        add(new JButton("Pause"));
        add(new JButton("Reset"));
        add(new JButton("test4"));
        setPreferredSize(new Dimension(width, height));
        setVisible(true);
    }
}
