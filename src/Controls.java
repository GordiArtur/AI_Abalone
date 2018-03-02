import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
    private static final String TIMER_FORMAT = "00.0"; // display timer format to 0.1 seconds precision

    private Board board; // Board passed from Game.java
    private Timer timer; // Used to refresh the JPanel
    private Game game; // Game passed from Game.java
    private Stopwatch stopwatch; // Used to calculate time per turn

    private JPanel layoutControlPanel;
    private JPanel timerControlPanel;

    private JLabel stopwatchLabel; // Stop watch display
    private JLabel colorTurnLabel; // Turn order display
    private JLabel turnCounterLabel; // Turn counter display

    private JButton timerStartPauseButton; // Time start pause button
    private JButton timerResetButton; // Timer reset button
    private JButton gameStartButton; // Game start button
    private JButton gameResetButton; // Game reset button

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
        layoutControlPanel = new JPanel();

        JButton standardLayoutButton = new JButton("Standard");
        JButton belgianDaisyButton = new JButton("Belgian Daisy");
        JButton germanDaisyButton = new JButton("German Daisy");

        layoutControlPanel.add(standardLayoutButton);
        layoutControlPanel.add(belgianDaisyButton);
        layoutControlPanel.add(germanDaisyButton);

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
        // Timer Controls
        timerControlPanel = new JPanel();

        timerStartPauseButton = new JButton("Start Timer");
        timerResetButton = new JButton("Reset Timer");
        gameStartButton = new JButton("Start Game");
        gameResetButton = new JButton("Reset Game");

        colorTurnLabel = new JLabel();
        stopwatchLabel = new JLabel(TIMER_FORMAT);
        turnCounterLabel = new JLabel();

        // Add timer start/pause reset controls
        timerControlPanel.add(timerStartPauseButton);
        timerControlPanel.add(timerResetButton);

        // Add timer and move counter displays
        timerControlPanel.add(colorTurnLabel);
        timerControlPanel.add(stopwatchLabel);
        timerControlPanel.add(turnCounterLabel);

        // Add game start and reset controls
        timerControlPanel.add(gameStartButton);
        timerControlPanel.add(gameResetButton);

        add(timerControlPanel);

        // Timer button listeners
        timerStartPauseButton.addActionListener(new TimerStartListener()); // Start/Stop timer
        timerResetButton.addActionListener(new TimerResetListener()); // Reset timer
        gameStartButton.addActionListener(new GameStartListener()); // Start game
        gameResetButton.addActionListener(new GameResetListener()); // Reset game

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
            timerStartPauseButton.setText("Pause Timer");
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
            timerStartPauseButton.setText("Start Timer");
        }
    }

    /**
     * Reset Stopwatch value to 0
     */
    public void resetTimer() {
        stopwatch.reset();
        timer.stop();
        timerStartPauseButton.setText("Start Timer");
        stopwatchLabel.setText(TIMER_FORMAT);
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
            double timeElapsed = (double)stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000;
            DecimalFormat format = new DecimalFormat(TIMER_FORMAT);
            stopwatchLabel.setText("" + format.format(timeElapsed));
        }
    }

    /**
     * Call startTimer action
     * Start Timer AND Stopwatch
     */
    private class TimerStartListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (!timer.isRunning()) {
                startTimer();
            } else {
                stopTimer();
            }
        }
    }

    /**
     * Call resetTimer action
     * Reset Stopwatch
     */
    private class TimerResetListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            resetTimer();
        }
    }

    /**
     * Call GameResetListener
     * Begin a new game
     */
    private class GameStartListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (!game.getIsRunning()) {
                game.restartGame();
                startTimer();
            }
        }
    }

    /**
     * Call GameResetListener
     * Restart game
     */
    private class GameResetListener implements ActionListener {
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

    /**
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