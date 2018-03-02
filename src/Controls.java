import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
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
    private static final int HEIGHT = 200;
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

    /**
     * Create and add all UI buttons to Controls JPanel
     * @param board current Board
     */
    public Controls(Board board, Game game) {
        timer = new Timer(TIMER_REFRESH_RATE, new TimerListener());
        stopwatch = stopwatch.createUnstarted();
        this.board = board;
        this.game = game;

        setVisible(true);

        createGameControls();
        createSetupControls();

        add(layoutControlPanel);
    }

    /**
     * Create and add Timer controls to Controls JPanel
     *
     * JLabels: colorTurnLabel, stopwatchLabel, turnCounterLabel
     * JButtons: Start, Pause, Reset, Reset Game
     */
    private void createGameControls() {
        // Timer Controls
        timerControlPanel = new JPanel();
        //timerControlPanel.setBorder(new LineBorder(Color.black));

        timerStartPauseButton = new JButton("Start Timer");
        JButton timerResetButton = new JButton("Reset Timer");
        JButton gameResetButton = new JButton("Reset Game");

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

        // Add game reset controls
        timerControlPanel.add(gameResetButton);

        // Timer button listeners
        timerStartPauseButton.addActionListener(new TimerStartListener()); // Start/Stop timer
        timerResetButton.addActionListener(new TimerResetListener()); // Reset timer
        gameResetButton.addActionListener(new GameResetListener()); // Reset game

    }

    /**
     * Create and add Board Layout controls to Controls JPanel
     *
     * JButtons: Standard, Belgian Daisy, German Daisy
     */
    private void createSetupControls() {
        // Board Layout Controls
        layoutControlPanel = new JPanel();
        layoutControlPanel.setBorder(new LineBorder(Color.black));

        // Create layout radio buttons
        JPanel layoutSelectionPanel = new JPanel(new GridLayout(3,1));
        JRadioButton standardLayoutRadioButton = new JRadioButton("Standard");
        JRadioButton belgianDaisyRadioButton = new JRadioButton("Belgian");
        JRadioButton germanDaisyRadioButton = new JRadioButton("German");

        layoutSelectionPanel.add(standardLayoutRadioButton);
        layoutSelectionPanel.add(belgianDaisyRadioButton);
        layoutSelectionPanel.add(germanDaisyRadioButton);


        // Create black agent selection radio buttons
        JPanel blackAgentSelectionPanel = new JPanel(new GridLayout(3,2));
        JLabel blackPlayerLabel = new JLabel("Black");
        JRadioButton blackHumanRadioButton = new JRadioButton("Human");
        JRadioButton blackAIRadioButton = new JRadioButton("AI");

        blackAgentSelectionPanel.add(blackPlayerLabel);
        blackAgentSelectionPanel.add(blackHumanRadioButton);
        blackAgentSelectionPanel.add(blackAIRadioButton);


        // Create white agent selection radio buttons
        JPanel whiteAgentSelectionPanel = new JPanel(new GridLayout(3,2));
        JLabel whitePlayerLabel = new JLabel("White");
        JRadioButton whiteHumanRadioButton = new JRadioButton("Human");
        JRadioButton whiteAIRadioButton = new JRadioButton("AI");

        whiteAgentSelectionPanel.add(whitePlayerLabel);
        whiteAgentSelectionPanel.add(whiteHumanRadioButton);
        whiteAgentSelectionPanel.add(whiteAIRadioButton);


        // Create Start Game button
        JButton gameStartButton = new JButton("Start Game");


        // Add all controls to layoutControlPanel
        layoutControlPanel.add(layoutSelectionPanel);
        layoutControlPanel.add(gameStartButton);
        layoutControlPanel.add(blackAgentSelectionPanel);
        layoutControlPanel.add(whiteAgentSelectionPanel);


        // Board Layout button listeners


        gameStartButton.addActionListener(new GameStartListener());
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
     * Begin a new game, removes layout controls, adds timer controls
     */
    private class GameStartListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (!game.getIsRunning()) {
                game.restartGame();
                game.setIsRunning(true);
                remove(layoutControlPanel);
                add(timerControlPanel);
                startTimer();
            }
        }
    }

    /**
     * Call GameResetListener
     * Reset game, remove timer controls, add layout controls
     */
    private class GameResetListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.restartGame();
            game.setIsRunning(false);
            remove(timerControlPanel);
            add(layoutControlPanel);
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