import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.util.List;

import com.google.common.base.Stopwatch; // https://github.com/google/guava

/**
 * Create and control main UI buttons
 */
public class Controls extends JPanel {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;
    private static final int TIME_PER_TURN = 30; // Max 30 seconds per turn
    private static final int TIMER_REFRESH_RATE = 100; // 100ms or 0.1s

    private Board board; // Board passed from Game.java
    private Timer timer; // Used to refresh the JPanel
    private Game game; // Game passed from Game.java
    private Stopwatch stopwatch; // Used to calculate time per turn

    private JLabel stopwatchLabel;
    private JLabel colorTurnLabel;
    private JLabel turnCounterLabel;

    /**
     * Create and add all UI buttons to Controls JPanel
     * @param board current Board
     */
    public Controls(Board board, Game game) {
        timer = new Timer(TIMER_REFRESH_RATE, new TimerListener());
        stopwatch = stopwatch.createUnstarted();
        this.board = board;
        this.game = game;

        setLayout(new GridLayout(3, 3));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
     * JButtons: Start, Pause, Reset, Reset Game
     */
    private void createTimerControls() {
        // Timer Label
        JPanel stopwatchLabelPanel = new JPanel();

        colorTurnLabel = new JLabel();
        stopwatchLabel = new JLabel();
        turnCounterLabel = new JLabel();

        stopwatchLabelPanel.add(colorTurnLabel);
        stopwatchLabelPanel.add(stopwatchLabel);
        stopwatchLabelPanel.add(turnCounterLabel);

        add(stopwatchLabelPanel);

        // Timer Controls
        JPanel timerControlPanel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton resetButton = new JButton("Reset Timer");
        JButton restartGameButton = new JButton("Restart Game");

        timerControlPanel.add(startButton);
        timerControlPanel.add(pauseButton);
        timerControlPanel.add(resetButton);
        timerControlPanel.add(restartGameButton);

        add(timerControlPanel);

        // Timer button listeners
        startButton.addActionListener(new StartListener());
        pauseButton.addActionListener(new PauseListener());
        resetButton.addActionListener(new ResetListener());
        restartGameButton.addActionListener(new RestartGameListener());
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
     * Sets the turnCounterLabel text to the current turn count
     */
    public void setTurnCount() {
        turnCounterLabel.setText("Turn " + game.getTurnCount());
        repaint();
    }

    /**
     * Sets the colorTurnLabel text to the current turn color
     */
    public void setTurnColor() {
        colorTurnLabel.setText(game.getTurnColor());
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
     * Call restartGameListener
     * Restart game
     */
    private class RestartGameListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.restartGame();
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

    /*
     * Updates lastMove with the player's played move. Updates player's history.
     * hexArray : array of player's pieces moved.
     * dx : x direction.
     * dy : y direction.
     */
    public void playedMove(List<Hex> selectedHex, int dx, int dy) {
        String out = "Turn: " + game.getTurnCount() + " ";
        for (Hex h : selectedHex) {
            out += h.getID() + " ";
        }
        switch (dx * 10 + dy) {
            case 1:
                out += "SW";
                break;
            case 10:
                out += "E";
                break;
            case 11:
                out += "SE";
                break;
            case -1:
                out += "NE";
                break;
            case -10:
                out += "W";
                break;
            case -11:
                out += "NW";
                break;
        }
    }
}