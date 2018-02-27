import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;


import com.google.common.base.Stopwatch; // https://github.com/google/guava

/**
 * Create and control main UI buttons
 */
public class Controls extends JPanel {
    private final int width = 200;
    private final int height = 100;
    private final int timePerTurn = 30; // Max 30 seconds per turn
    private final int timerRefreshRate = 100; // 100ms or 0.1s

    private int turnCount = 0;

    private Board board; // Board passed from Game.java
    private Timer timer; // Used to refresh the JPanel
    private Stopwatch stopwatch; // Used to calculate time per turn

    private JLabel stopwatchLabel;
    private JLabel colorTurnLabel;
    private JLabel turnCounterLabel;

    /**
     * Create and add all UI buttons to Controls JPanel
     * @param board current Board
     */
    public Controls(Board board) {
        timer = new Timer(timerRefreshRate, new TimerListener());
        stopwatch = stopwatch.createUnstarted();
        this.board = board;

        setLayout(new GridLayout(3, 3));
        setPreferredSize(new Dimension(width, height));
        setVisible(true);

        createBoardLayoutControls();
        createTimerControls();
    }

    /**
     * Create and add Board Layout controls to Controls JPanel
     *
     * JButtons: Standard, Belgian Daisy, German Daisy
     */
    private void createBoardLayoutControls() {
        // Board Layout Controls
        JPanel layoutControlPanel = new JPanel();

        JButton standardLayoutButton = new JButton("Standard");
        JButton belgianDaisyButton = new JButton("Belgian Daisy");
        JButton germanDaisyButton = new JButton("German Daisy");

        layoutControlPanel.add(standardLayoutButton);
        layoutControlPanel.add(belgianDaisyButton);
        layoutControlPanel.add(germanDaisyButton);

        add(layoutControlPanel);

        // Board Layout button listeners
        standardLayoutButton.addActionListener(new StandardLayoutListener());
        belgianDaisyButton.addActionListener(new BelgianDaisyListener());
        germanDaisyButton.addActionListener(new GermanDaisyListener());
    }

    /**
     * Create and add Timer controls to Controls JPanel
     *
     * JLabels: colorTurnLabel, stopwatchLabel, turnCounterLabel
     * JButtons: Start, Pause, Reset
     */
    private void createTimerControls() {
        // Timer Label
        JPanel stopwatchLabelPanel = new JPanel();

        colorTurnLabel = new JLabel("Black goes first: ");
        stopwatchLabel = new JLabel();
        turnCounterLabel = new JLabel("Number of moves: " + turnCount);

        stopwatchLabelPanel.add(colorTurnLabel);
        stopwatchLabelPanel.add(stopwatchLabel);
        stopwatchLabelPanel.add(turnCounterLabel);

        add(stopwatchLabelPanel);

        // Timer Controls
        JPanel timerControlPanel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton resetButton = new JButton("Reset");

        timerControlPanel.add(startButton);
        timerControlPanel.add(pauseButton);
        timerControlPanel.add(resetButton);

        add(timerControlPanel);

        // Timer button listeners
        startButton.addActionListener(new StartListener());
        pauseButton.addActionListener(new PauseListener());
        resetButton.addActionListener(new ResetListener());
    }

    /**
     * Start Timer AND Stopwatch
     *
     * Timer will refresh the JPanel at timerRefreshRate
     * Stopwatch will represent time for each player's turn
     *
     * NOTE: Timer must be stopped to trigger startTimer
     */
    public void startTimer() {
        if (!timer.isRunning()) {
            stopwatch.start();
            timer.start();
        }
    }

    /**
     * Stop Timer AND Stopwatch
     *
     * Timer will stop refreshing the JPanel
     * Stopwatch will stop adding time for current player's turn
     *
     * NOTE: Timer must be running to trigger stopTimer
     */
    public void stopTimer() {
        if (timer.isRunning()) {
            stopwatch.stop();
            timer.stop();
        }
    }

    /**
     * Reset Stopwatch value to 0
     *
     * NOTE: Timer must be stopped to trigger resetTimer
     */
    public void resetTimer() {
        if (!timer.isRunning()) {
            stopwatch.reset();
            timer.stop();
            stopwatchLabel.setText("0");
        }
    }

    /**
     * Return true if timer is running
     *
     * @return timer.isRunning
     */
    public boolean isTimerRunning() {
        return timer.isRunning();
    }

    /**
     * @TODO Document Tony's function
     */
    public void incrementTurn() {
        turnCounterLabel.setText("Number of moves: " + (++turnCount));
        repaint();
    }

    /**
     * Update stopwatchLabel every timerRefreshRate milliseconds with current stopWatch value
     */
    public class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            stopwatchLabel.setText("" + stopwatch.elapsed(TimeUnit.SECONDS));
        }
    }

    /**
     * Call startTimer action
     * Start Timer AND Stopwatch
     */
    private class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            startTimer();
        }
    }

    /**
     * Call pauseTimer action
     * Stop Timer AND Stopwatch
     */
    private class PauseListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            stopTimer();
        }
    }

    /**
     * Call resetListener
     * Reset Stopwatch
     */
    private class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            resetTimer();
        }
    }

    /**
     * Select board layout from Board class
     */
    private class StandardLayoutListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(1);
        }
    }

    /**
     * Select board layout from Board class
     */
    private class BelgianDaisyListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(2);
        }
    }

    /**
     * Select board layout from Board class
     */
    private class GermanDaisyListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(3);
        }
    }
}