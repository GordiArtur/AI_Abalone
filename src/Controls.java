import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;


public class Controls extends JPanel {

    private final int width = 200;
    private final int height= 100;
    private final int timer_precision = 10000000; // 0.1 second precision
    private final int time_per_turn = 30; // Max 30 seconds per turn

    private long start_time = 0;
    private long pause_time = 0;
    private long end_time;

    private JLabel timer_label;
    private Timer timer;


    public Controls() {
        setLayout(new GridLayout(3,3));

        // Board Layout Controls
        JPanel layout_control_panel = new JPanel();

        JButton standard_layout_button = new JButton("Standard");
        JButton belgian_daisy_button = new JButton("Belgian Daisy");
        JButton german_daisy_button = new JButton("German Daisy");

        layout_control_panel.add(standard_layout_button);
        layout_control_panel.add(belgian_daisy_button);
        layout_control_panel.add(german_daisy_button);

        add(layout_control_panel);


        // Timer Label
        JPanel timer_label_panel = new JPanel();
        timer_label = new JLabel("0.0");
        timer_label_panel.add(timer_label);

        add(timer_label_panel);


        // Timer Controls
        JPanel timer_control_panel = new JPanel();

        JButton start_button = new JButton("Start");
        JButton pause_button = new JButton("Pause");
        JButton reset_button = new JButton("Reset");

        timer_control_panel.add(start_button);
        timer_control_panel.add(pause_button);
        timer_control_panel.add(reset_button);

        add(timer_control_panel);

        // Timer button listeners
        start_button.addActionListener(new StartListener());
        pause_button.addActionListener(new PauseListener());
        reset_button.addActionListener(new ResetListener());


        setPreferredSize(new Dimension(width, height));
        setVisible(true);

        timer = new Timer(100, new StopWatchListener());
    }

    public class StopWatchListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            long time_value = (System.nanoTime() / timer_precision) - (start_time / timer_precision);
            double output_time = time_value / 100.0;
            timer_label.setText("" + decimalFormat.format(output_time));

            if (time_value >= (time_per_turn * 100)) { // From centiseconds to seconds
                timer.stop();
                System.out.println("Out of time");
                //@TODO add game handlers here
            }
        }
    }

    public class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (start_time == 0) { // If timer is 0 (not started)
                start_time = System.nanoTime();
                timer.start();
            } else if (pause_time != 0) { // If timer is paused
                start_time = System.nanoTime() + (start_time - pause_time);
                pause_time = 0;
                timer.start();
            }
        }
    }

    public class PauseListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            timer.stop();
            pause_time = System.nanoTime();
        }
    }

    public class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (!timer.isRunning()) {
                start_time = 0;
                pause_time = 0;
                timer_label.setText("0.0");
            }
        }
    }
}