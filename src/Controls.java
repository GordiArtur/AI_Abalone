import com.google.common.base.Stopwatch;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Create and control main UI buttons
 */
public class Controls extends JPanel {
    private static final int TIME_PER_TURN = 30; // Max 30 seconds per turn
    private static final int TIMER_REFRESH_RATE = 100; // 100ms or 0.1s
    private static final String TIMER_FORMAT = "00.0"; // display timer format to 0.1 seconds precision
    private int timePerTurn = 30; // Max 30 seconds per turn
    private int movesPerPlayer = 30; // Max 30 moves per player
    private int lastUsedLayout = 1; // stores last used layout
    private boolean gameRunning;


    private Board board; // Board passed from Game.java
    private Timer timer; // Used to refresh the JPanel
    private Game game; // Game passed from Game.java
    private Stopwatch stopwatch; // Used to calculate time per turn

    private JPanel layoutControlPanel;
    private JPanel timerControlPanel;

    private JLabel stopwatchLabel; // Stop watch display
    private JLabel colorTurnLabel; // Turn order display
    private JLabel turnCounterLabel; // Turn counter display
    private JLabel scoreLabel; // Black score display

    private JButton timerStartPauseButton; // Time start pause button

    private JSpinner timeLimitPerPlayerSpinner; // Time limit input spinner
    private JSpinner turnLimitPerPlayerSpinner; // Turn limit input spinner

    /**
     * Create and add all UI buttons to Controls JPanel
     * @param board current Board
     */
    Controls(Board board, Game game) {
        timer = new Timer(TIMER_REFRESH_RATE, new TimerListener());
        stopwatch = Stopwatch.createUnstarted();
        //stopwatch = stopwatch.createUnstarted();
        this.board = board;
        this.game = game;
        gameRunning = false;
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

        timerStartPauseButton = new JButton("Start Timer");
        JButton timerResetButton = new JButton("Reset Timer");
        JButton gameResetButton = new JButton("Reset Game");

        colorTurnLabel = new JLabel();
        stopwatchLabel = new JLabel(TIMER_FORMAT);
        turnCounterLabel = new JLabel();
        scoreLabel = new JLabel();


        // Add timer start/pause reset controls
        timerControlPanel.add(timerStartPauseButton);
        timerControlPanel.add(timerResetButton);

        // Add timer and move counter displays
        timerControlPanel.add(colorTurnLabel);
        timerControlPanel.add(stopwatchLabel);
        timerControlPanel.add(turnCounterLabel);

        // Add game reset controls
        timerControlPanel.add(gameResetButton);

        // Add score label
        timerControlPanel.add(scoreLabel);

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

        // Create time and turn limit labels
        JPanel timeTurnSelectionLabelPanel = new JPanel(new GridLayout(2,1));
        JLabel timeLimitLabel = new JLabel("Time Limit (seconds)");
        JLabel turnLimitLabel = new JLabel("Turn Limit (per player)");
        timeTurnSelectionLabelPanel.add(timeLimitLabel);
        timeTurnSelectionLabelPanel.add(turnLimitLabel);


        // Create time and turn limit spinners
        JPanel timeTurnSelectionInputPanel = new JPanel(new GridLayout(2,1));
        timeLimitPerPlayerSpinner = new JSpinner(new SpinnerNumberModel(timePerTurn, 0, Integer.MAX_VALUE,5));
        turnLimitPerPlayerSpinner = new JSpinner(new SpinnerNumberModel(movesPerPlayer, 0, Integer.MAX_VALUE, 5));

        timeTurnSelectionInputPanel.add(timeLimitPerPlayerSpinner);
        timeTurnSelectionInputPanel.add(turnLimitPerPlayerSpinner);


        // Create layout radio buttons
        JPanel layoutSelectionPanel = new JPanel(new GridLayout(3,1));
        ButtonGroup layoutButtonGroup = new ButtonGroup(); // Ensures only one button can be selected at a time
        JRadioButton standardLayoutRadioButton = new JRadioButton("Standard", true);
        JRadioButton belgianDaisyRadioButton = new JRadioButton("Belgian", false);
        JRadioButton germanDaisyRadioButton = new JRadioButton("German", false);

        layoutButtonGroup.add(standardLayoutRadioButton);
        layoutButtonGroup.add(belgianDaisyRadioButton);
        layoutButtonGroup.add(germanDaisyRadioButton);
        layoutSelectionPanel.add(standardLayoutRadioButton);
        layoutSelectionPanel.add(belgianDaisyRadioButton);
        layoutSelectionPanel.add(germanDaisyRadioButton);


        // Create black agent selection radio buttons
        JPanel blackAgentSelectionPanel = new JPanel(new GridLayout(3,1));
        ButtonGroup blackAgentButtonGroup = new ButtonGroup(); // Ensures only one button can be selected at a time
        JLabel blackPlayerLabel = new JLabel("Black");
        JRadioButton blackHumanRadioButton = new JRadioButton("Human", true);
        JRadioButton blackAIRadioButton = new JRadioButton("AI", false);

        blackAgentButtonGroup.add(blackHumanRadioButton);
        blackAgentButtonGroup.add(blackAIRadioButton);
        blackAgentSelectionPanel.add(blackPlayerLabel);
        blackAgentSelectionPanel.add(blackHumanRadioButton);
        blackAgentSelectionPanel.add(blackAIRadioButton);


        // Create white agent selection radio buttons
        JPanel whiteAgentSelectionPanel = new JPanel(new GridLayout(3,1));
        ButtonGroup whiteAgentButtonGroup = new ButtonGroup(); // Ensures only one button can be selected at a time
        JLabel whitePlayerLabel = new JLabel("White");
        JRadioButton whiteHumanRadioButton = new JRadioButton("Human", false);
        JRadioButton whiteAIRadioButton = new JRadioButton("AI", true);

        whiteAgentButtonGroup.add(whiteHumanRadioButton);
        whiteAgentButtonGroup.add(whiteAIRadioButton);
        whiteAgentSelectionPanel.add(whitePlayerLabel);
        whiteAgentSelectionPanel.add(whiteHumanRadioButton);
        whiteAgentSelectionPanel.add(whiteAIRadioButton);


        // Create Start Game button
        JButton gameStartButton = new JButton("Start Game");


        // Add all controls to layoutControlPanel
        layoutControlPanel.add(timeTurnSelectionLabelPanel);
        layoutControlPanel.add(timeTurnSelectionInputPanel);
        layoutControlPanel.add(layoutSelectionPanel);
        layoutControlPanel.add(gameStartButton);
        layoutControlPanel.add(blackAgentSelectionPanel);
        layoutControlPanel.add(whiteAgentSelectionPanel);


        // Time and turn limit spinner listeners
        timeLimitPerPlayerSpinner.addChangeListener(new TimeLimitPerPlayerListener());
        turnLimitPerPlayerSpinner.addChangeListener(new TurnLimitPerPlayerListener());

        // Board Layout button listeners
        standardLayoutRadioButton.addActionListener(new StandardLayoutListener());
        belgianDaisyRadioButton.addActionListener(new BelgianDaisyListener());
        germanDaisyRadioButton.addActionListener(new GermanDaisyListener());

        // Agent button listeners
        blackHumanRadioButton.addActionListener(new BlackHumanAgentListener());
        blackAIRadioButton.addActionListener(new BlackAIAgentListener());
        whiteHumanRadioButton.addActionListener(new WhiteHumanAgentListener());
        whiteAIRadioButton.addActionListener(new WhiteAIAgentListener());

        // Start game button listener
        gameStartButton.addActionListener(new GameStartListener());

        // Add HistoryPanel
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
            gameRunning = true;
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
            gameRunning = false;
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
        gameRunning = false;
    }

    /**
     * Checks if the game is currently running.
     * @return true if the game is running
     */
    public boolean isGameRunning() {
        return gameRunning;
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
        turnCounterLabel.setText("Turn " + game.getTurnCount() + " ");
        turnCounterLabel.setFont(turnCounterLabel.getFont().deriveFont(28.0f));
        repaint();
    }

    /**
     * Sets the colorTurnLabel text to the current turn color
     */
    public void setTurnColor() {
        colorTurnLabel.setText(" " + game.getTurnColor() + " |");
        colorTurnLabel.setFont(colorTurnLabel.getFont().deriveFont(28.0f));
        repaint();
    }

    /**
     * Update stopwatchLabel every timerRefreshRate milliseconds with current stopWatch value
     */
    public class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            double timeElapsed = (double)stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000;
            DecimalFormat format = new DecimalFormat(TIMER_FORMAT);
            stopwatchLabel.setText("" + format.format(timeElapsed) + " |");
            stopwatchLabel.setFont(stopwatchLabel.getFont().deriveFont(28.0f));
            scoreLabel.setText(" Score: B: " + game.getBlackScore() + "; W: " + game.getWhiteScore());
            scoreLabel.setFont(scoreLabel.getFont().deriveFont(28.0f));
            if (timeElapsed > timePerTurn) {
                stopTimer();
                System.err.println("Maximum time reached!");
            }
            if (game.getTurnCount() > movesPerPlayer) {
                stopTimer();
                System.err.println("Maximum turn limit reached!");
            }
            if (Game.MIN_MARBLES > game.getBlackScore() || Game.MIN_MARBLES > game.getWhiteScore()) {
                stopTimer();
                System.err.println("Minimum number of marbles reached!");
                scoreLabel.setText((game.getBlackScore() > game.getWhiteScore() ? "Black" : "White") + " Won!");
            }
        }
    }

    public String getStopWatchTime() {
        double timeElapsed = (double)stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000;
        DecimalFormat format = new DecimalFormat(TIMER_FORMAT);
        return format.format(timeElapsed);
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
                game.switchTurn();
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
            game.clearHistory();
            remove(timerControlPanel);
            add(layoutControlPanel);
            board.selectLayout(lastUsedLayout);
        }
    }

    /**
     * Call TimeLimitPerPlayerListener
     * Change time limit (in seconds) per player
     */
    private class TimeLimitPerPlayerListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            timePerTurn = (int)timeLimitPerPlayerSpinner.getValue();
        }
    }

    /**
     * Call TurnLimitPerPlayerListener
     * Change maximum turn limit per player
     */
    private class TurnLimitPerPlayerListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            movesPerPlayer = (int)turnLimitPerPlayerSpinner.getValue();
        }
    }

    /**
     * Select board layout from Board class
     */
    private class StandardLayoutListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(1);
            lastUsedLayout = 1;
        }
    }

    /**
     * Select board layout from Board class
     */
    private class BelgianDaisyListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(2);
            lastUsedLayout = 2;
        }
    }

    /**
     * Select board layout from Board class
     */
    private class GermanDaisyListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            board.selectLayout(3);
            lastUsedLayout = 3;
        }
    }

    /**
     * Select black piece to be a human agent
     */
    private class BlackHumanAgentListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.selectBlackAgent(0);
        }
    }

    /**
     * Select black piece to be an AI agent
     */
    private class BlackAIAgentListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.selectBlackAgent(1);
        }
    }

    /**
     * Select white piece to be a human agent
     */
    private class WhiteHumanAgentListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.selectWhiteAgent(0);
        }
    }

    /**
     * Select white piece to be an AI agent
     */
    private class WhiteAIAgentListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            game.selectWhiteAgent(1);
        }
    }

    /**
     * Returns a text format of the direction (integer values of -1, 0, 1)
     * @param dx Positive or Negative x direction
     * @param dy Positive or Negative y direction
     * @return String of Direction [NW, W, SW, SE, E, NE]
     */
    public static String getDirectionText(final int dx, final int dy) {
        switch (dx * 10 + dy) {
            case 1:
                return "SW";
            case 10:
                return "E";
            case 11:
                return "SE";
            case -1:
                return "NE";
            case -10:
                return "W";
            case -11:
                return "NW";
            default :
                return "NULL";
        }
    }

    /**
     * Checks if the selected hexes and called action is a legal move. If so, apply Board.playedMove(). 1. validMove
     * sorts selectedHex array and reverses if moving away from origin (Origin is Top-Left) 2. Generate special
     * identity of direction 3. Generate identity of selectedHex array 4. First check identity to direction
     * identity, Inline 4.1. If adjacent hex is empty space or off-board area, playedMove and return true 4.2. Else
     * create empty temp list. Checks for gaps, same color pieces, and last piece. 4.3. Reverse temp, add
     * selectedHex to temp, 5 Second check for Broadside and single piece 5.1. Check for available space, move then
     * return true 6. movePiece, clear selected, switchTurn() for 4.1, 4.2, and 5 move scenarios return false for
     * nullptr and blocked moves return true for validMove (assumes Board.playedMove() is successful)
     *
     * @param dx X value of horizontal movement
     * @param dy Y value of vertical movement
     */
    boolean validMove(final int dx, final int dy, List<Hex> selectedHex) { // Don't read it. It's very long
        if (Game.LOG) System.out.println("Checking valid move");
        selectedHex = sortSelected(selectedHex);
        if (dx > 0 || dy > 0) {
            Collections.reverse(selectedHex);
        }
        if (Game.LOG) System.out.println("Origin Point " + selectedHex.get(0).getID());
        int identity = 0;
        int didentity = Math.abs(dx) * 10 + Math.abs(dy);
        int sx, sy;
        if (selectedHex.size() > 1) {
            identity = identity(selectedHex.get(0).getXpos(), selectedHex.get(0).getYpos(),
                selectedHex.get(1).getXpos(), selectedHex.get(1).getYpos());
        }
        if (identity > 0 && identity == didentity) { // Inline
            sx = selectedHex.get(0).getXpos();
            sy = selectedHex.get(0).getYpos();
            // Empty space or Off-board
            if (board.getHex(sx + dx, sy + dy) == null || board.getHex(sx + dx, sy + dy).getPiece() == null) {
                game.updateHistory(dx, dy, (ArrayList<Hex>) selectedHex);
                movePieces(selectedHex, dx, dy);
                return true;
            } else {
                ArrayList<Hex> temp = new ArrayList<>();
                for (int i = 1; i <= selectedHex.size(); i++) {
                    if (board.getHex(sx + (dx * i), sy + (dy * i)) == null
                        || board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() == null) { // Gap space sumito
                        break;
                    } else if (board.getHex(sx + (dx * i), sy + (dy * i)).getPiece().getColor()
                        .equals(selectedHex.get(0).getPiece().getColor())) { // Same color blocker
                        System.out.println("Invalid move - same colour marble blocking");
                        return false;
                    } else if (i == selectedHex.size()
                        && board.getHex(sx + (dx * i), sy + (dy * i)).getPiece() != null) { // Last piece blocker
                        System.out.println("Invalid move - too many pieces blocking");
                        return false;
                    } else { // Add this piece to temp; piece to be moved.
                        temp.add(board.getHex(sx + (dx * i), sy + (dy * i)));
                    }
                }
                Collections.reverse(temp);
                temp.addAll(selectedHex);
                game.updateHistory(dx, dy, (ArrayList<Hex>) selectedHex);
                movePieces(temp, dx, dy);
                return true;
            }
        } else { // Broadside and singular
            for (Hex hex : selectedHex) {
                sx = hex.getXpos();
                sy = hex.getYpos();
                if (board.getHex(sx + dx, sy + dy) != null && board.getHex(sx + dx, sy + dy).getPiece() != null) {
                    System.out.println("Invalid move - another marble blocking path");
                    return false;
                }
            }
            game.updateHistory(dx, dy, (ArrayList<Hex>) selectedHex);
            movePieces(selectedHex, dx, dy);
            return true;
        }
    }

    /**
     * Sorts the List<Hex> for selectedHex to arrange Hexes from origin point (top-left corner) in ascending order. Not
     * generic code.
     */
    private List<Hex> sortSelected(final List<Hex> selectedHex) {
        if (selectedHex.size() == 3) {
            List<Hex> unsorted = new ArrayList<>(selectedHex);
            List<Hex> temp = new ArrayList<>();
            Hex a = unsorted.get(0);
            Hex b = unsorted.get(1);
            Hex c = unsorted.get(2);
            temp.add((a.getXY() < b.getXY()) ? (a.getXY() < c.getXY()) ? a : c : (b.getXY() < c.getXY()) ? b : c);
            unsorted.remove(temp.get(0));
            temp.add((unsorted.get(0).getXY() < unsorted.get(1).getXY()) ? unsorted.get(0) : unsorted.get(1));
            unsorted.remove(temp.get(1));
            temp.add(unsorted.get(0));
            return new ArrayList<>(temp);
        } else if (selectedHex.size() == 2) {
            List<Hex> temp = new ArrayList<>(selectedHex);
            Hex small = (selectedHex.get(0).getXY() < selectedHex.get(1).getXY()) ? selectedHex.get(0)
                : selectedHex.get(1);
            Hex large = (selectedHex.get(0).getXY() > selectedHex.get(1).getXY()) ? selectedHex.get(0)
                : selectedHex.get(1);
            temp.set(0, small);
            temp.set(1, large);
            return new ArrayList<>(temp);
        }
        return selectedHex;
    }

    /**
     * Outputs an integer that represents the axial direction of the elements in selectedHex.
     * 1 is vertical
     * 10 is horizontal
     * 11 is diagonal
     *
     * @param sx First hex x coordinate
     * @param sy First hex y coordinate
     * @param dx Last hex x coordinate
     * @param dy Last hex y coordinate
     * @return Integer Identity of axial direction
     */
    public int identity(int sx, int sy, int dx, int dy) {
        int out = 0;
        if ((dx + dy) == (sx + sy)) {
            return 0;
        }
        out += (Math.abs(dx - sx) == 1) ? 10 : 0;
        out += (Math.abs(dy - sy) == 1) ? 1 : 0;
        return (Math.abs(dx - sx) > 1 || Math.abs(dy - sy) > 1) ? 0 : out;
    }

    /**
     * Calls board.movePiece() to move pieces in hexes.
     *
     * @param hexes Array of hexes with pieces to move
     * @param dx X coordinate move (-1 to 1)
     * @param dy Y coordinate move (-1 to 1)
     */
    private void movePieces(final List<Hex> hexes, final int dx, final int dy) {
        for (Hex hex : hexes) {
            int sx = hex.getXpos();
            int sy = hex.getYpos();
            board.movePiece(sx, sy, sx + dx, sy + dy);
        }
    }

    public int getTurnLimit() {
        return (int) turnLimitPerPlayerSpinner.getValue();
    }
}
