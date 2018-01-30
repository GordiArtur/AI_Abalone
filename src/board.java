
public class board {

    private int size;
    private int[][] grid;
    private int half;
    private int whiteCount;
    private int blackCount;
    
    public board(final int size) {
        this.size = size;
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
        }
    }

    /**
     * @return the size
     */
    public int getSize() {
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