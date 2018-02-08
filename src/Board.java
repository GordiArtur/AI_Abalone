import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Board extends JFrame {

    public static final int BOARD_SIZE = 9;
    private int size;
    private int[][] grid;
    private int half;
    private int whiteCount;
    private int blackCount;
    
    public Board(final int size, String gameName) {
        super(gameName);
        setLayout(new BorderLayout()); // Other elements can be added to the BorderLayout
        HexLayout layout = new HexLayout();
        add(layout, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setBounds(50, 50, 1500, 1100);
        setVisible(true);   
        
        // TONY'S CODE
        /*this.size = size;
        this.half = (int) (((double) size) / 2);
        
        this.grid = new int[size][size];
        
        for (int y = 0; y < size; y++) {
            if (y > half) {
                for (int x = 0; x < (y - half); x++) {
                        grid[y][x] = 3;
                }
            } else {
                for (int x = half + 1 + y; x < size; x++) {
                    grid[y][x] = 3;
                }
            }
        }*/        
    }

    /**
     * @return the size
     */
    public int getSize2() {  // JPanel already has a method called getSize
        return size;
    }

    /**
     * @return the grid
     */
    public int[][] getBoard() {
        return grid;
    }

    /**
     * @param x coordinate x
     * @param y coordinate y
     * @param dir direction of move
     */
    public void update(int x, int y, int direction) {
        int value = grid[y][x];
        grid[y][x] = 0;
        switch (direction) {
            case 0: grid[--y][x] = value;
                    break;
            case 1: grid[y][++x] = value;
                    break;
            case 2: grid[++y][++x] = value;
                    break;
            case 3: grid[++y][x] = value;
                    break;
            case 4: grid[--y][x] = value;
                    break;
            case 5: grid[--y][--x] = value;
        }
    }
    
    public void setMarble(int x, int y, boolean isWhite) {
        grid[y][x] = isWhite ? 1 : 2;
    }
    
    public void addMarble(int x, int y, boolean isWhite) {
        setMarble(x, y, isWhite);
        if (isWhite) {
            whiteCount++;
        } else {
            blackCount++;
        }
    }
    
    /**
     * @return the whiteCount
     */
    public int getWhiteCount() {
        return whiteCount;
    }

    /**
     * @return the blackCount
     */
    public int getBlackCount() {
        return blackCount;
    }

    public void printBoard() {
        for (int y = 0; y < size; y++) {
            for (int pad = 0; pad < (Math.abs(y - half)); pad++) {
                System.out.print("    ");
            }
            for (int x = 0; x < size; x++) {
                if (grid[y][x] != 3)
                    System.out.print("[" + x + "," + y + "," + grid[y][x] + "] ");
            }
            System.out.println();
            System.out.println();
        }
    }
}