import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Controls extends JPanel {

    private final int width = 200;
    private final int height = 100;
    private final int timer_precision = 10000000; // 0.1 second precision
    private final int time_per_turn = 30; // Max 30 seconds per turn

    private Board board;

    private long start_time = 0;
    private long pause_time = 0;
    private int turnCount;

    private JLabel timer_label;
    private JLabel color_turn_label;
    private Timer timer;
    private JLabel lastMove;
    private JLabel turnCounter;

    public Controls(Board boards) {
        this.board = boards;
        setLayout(new GridLayout(3, 3));
        turnCount = 0;
        // Board Layout Controls
        JPanel layout_control_panel = new JPanel();

        JButton standard_layout_button = new JButton("Standard");
        JButton belgian_daisy_button = new JButton("Belgian Daisy");
        JButton german_daisy_button = new JButton("German Daisy");

        layout_control_panel.add(standard_layout_button);
        layout_control_panel.add(belgian_daisy_button);
        layout_control_panel.add(german_daisy_button);

        add(layout_control_panel);

        // Board Layout button listeners
        standard_layout_button.addActionListener(new StandardLayoutListener());
        belgian_daisy_button.addActionListener(new BelgianDaisyListener());
        german_daisy_button.addActionListener(new GermanDaisyListener());

        // Timer Label
        JPanel timer_label_panel = new JPanel();
        color_turn_label = new JLabel("Black goes first: ");
        timer_label = new JLabel("0.0");
        timer_label_panel.add(color_turn_label);
        timer_label_panel.add(timer_label);
        turnCounter = new JLabel("Number of moves: " + turnCount);
        add(timer_label_panel);
        timer_label_panel.add(turnCounter);
        // last Move label
        
        
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

    public Timer getTimer() {
        return timer;
    }
    
    public void startTimer() {
        if (start_time == 0) { // If timer is 0 (not started)
            start_time = System.nanoTime();
            timer.start();
        } else if (pause_time != 0) { // If timer is paused
            start_time = System.nanoTime() + (start_time - pause_time);
            pause_time = 0;
            timer.start();
        }
    }

    public void stopTimer() {
        timer.stop();
        pause_time = System.nanoTime();
    }

    public void resetTimer() {
        if (!timer.isRunning()) {
            start_time = 0;
            pause_time = 0;
            timer_label.setText("0.0");
        }
    }
    
    public class StopWatchListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            long time_value = (System.nanoTime() / timer_precision) - (start_time / timer_precision);
            double output_time = time_value / 100.0;
            timer_label.setText("" + decimalFormat.format(output_time));

            if (time_value >= (time_per_turn * 100)) { // From centiseconds to seconds
                timer.stop();
                start_time = 0;
                pause_time = 0;
                System.out.println("Out of time");
                if (Game.turn == 0) {
                    Game.turn = 1;
                    color_turn_label.setText("White piece turn: ");
                } else {
                    Game.turn = 0;
                    color_turn_label.setText("Black piece turn: ");
                }
                // @TODO add game handlers here
            }
        }
    }

    private class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	startTimer();
        }
    }

    private class PauseListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            timer.stop();
            pause_time = System.nanoTime();
        }
    }

    private class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (!timer.isRunning()) {
                start_time = 0;
                pause_time = 0;
                timer_label.setText("0.0");
            }
        }
    }

    private class StandardLayoutListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(1);
        }
    }

    private class BelgianDaisyListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(2);
        }
    }

    private class GermanDaisyListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(3);
        }
    }
    public void incrementTurn() {
        turnCounter.setText("Number of moves: " + (++turnCount));
        repaint();
    }
}