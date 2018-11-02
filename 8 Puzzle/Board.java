import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.MinPQ;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

// immutable data type Board

public final class Board {

    private final int n; // board dimension n
    private final int[][] blocks;
    private final int totalBlocks;
    private final int[] spaceBlock = new int[2]; // [0] - row, [1] - col

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public Board(int[][] blocks) {
        if (blocks == null || blocks.length < 1) throw new IllegalArgumentException();

        n = blocks.length;
        this.blocks = new int[n][n];
        totalBlocks = this.n * this.n;

        // Keep track of the number of spaces, there should be no more than 1
        int spaceCount = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int block = blocks[row][col];

                if (block == 0) {
                    spaceCount++;

                    spaceBlock[0] = row;
                    spaceBlock[1] = col;

                    if (spaceCount > 1) throw new IllegalArgumentException("There is more than one space");
                }

                this.blocks[row][col] = block;
            }
        }

        if (spaceCount == 0) throw new IllegalArgumentException("There are no spaces");

    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // returns the value inside the block given a consecutive block count
    private int getBlockValue(int count) {

        int row = getBlockRow(count);
        int col = getBlockCol(count);

        return blocks[row - 1][col - 1];
    }

    // returns block's row, based on the input value (assuming rows start with 1)
    private int getBlockRow(int val) {
        if (val < 0 || val > totalBlocks) throw new IllegalArgumentException();

        // 0 is a special case, always the last block
        if (val == 0) return totalBlocks;

        // Determine the row, by rounding up
        return (int) (Math.ceil(((double) val / this.n)));
    }

    // returns block's column, based on the input value (assuming columns start with 1)
    private int getBlockCol(int val) {
        if (val < 0 || val > totalBlocks) throw new IllegalArgumentException();

        // 0 is a special case, always the last block
        if (val == 0) return totalBlocks;

        int column = val % this.n;
        column = (column == 0) ? this.n : column;

        return column;
    }

    // number of blocks out of place

    /**
     * Hamming priority function.
     * The number of blocks in the wrong position, plus the number of moves made so far to get to the search node.
     *
     * @return integer
     */
    public int hamming() {

        int numOfBlocksWPos = 0;

        // Determine the number of blocks in the wrong position
        for (int i = 1; i <= totalBlocks; i++) {
            if (i == totalBlocks && getBlockValue(i) == 0) continue; // We found a space where it supposed to be
            if (getBlockValue(i) != i) numOfBlocksWPos++;
        }

        return numOfBlocksWPos;
    }

    // sum of Manhattan distances between blocks and goal

    /**
     * Manhattan priority function.
     * The sum of the Manhattan distances (sum of the vertical and horizontal distance) from the blocks to their goal positions,
     * plus the number of moves made so far to get to the search node.
     *
     * @return integer
     */
    public int manhattan() {

        int manSum = 0;

        // Go through each block and if it's out of place calculate the distance to put it back
        for (int i = 1; i <= totalBlocks; i++) {

            int row = getBlockRow(i);
            int col = getBlockCol(i);

            int blockValue = blocks[row - 1][col - 1];

            // if the block is out of place find it's correct rows and colls
            if (blockValue != i) {
                int correctRow = getBlockRow(blockValue);
                int correctCol = getBlockCol(blockValue);

                // number of rows required to move to the right spot
                int sumRows = (row < correctRow) ? correctRow - row : row - correctRow;
                int sumCols = (col < correctCol) ? correctCol - col : col - correctCol;

                manSum += (sumRows + sumCols);
            }
        }

        return manSum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }


    // a board that is obtained by exchanging any pair of blocks
    // the blank square is not a block
    public Board twin() {

        // Holds a copy of the blocks array
        int[][] duplicateBlocks = new int[n][n];

        // Make a copy of the blocks array
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                duplicateBlocks[row][col] = this.blocks[row][col];
            }
        }

        // Holds coordinates of blocks to be exchanged
        int[][] randBlock = new int[2][2];

        // Generate random coordinates for a pair of blocks
        // Use cached row and col to prevent duplicate randoms

        int cacheRow = spaceBlock[0];
        int cacheCol = spaceBlock[1];

        for (int i = 0; i < 2; i++) {
            int randomRow;
            int randomCol;

            do {
                randomRow = StdRandom.uniform(this.n);
                randomCol = StdRandom.uniform(this.n);
            } while (randomRow == spaceBlock[0] && randomCol == spaceBlock[1]
                    && randomRow == cacheRow && randomCol == cacheCol);

            randBlock[i][0] = randomRow;
            randBlock[i][1] = randomCol;

            cacheRow = randomRow;
            cacheCol = randomCol;
        }

        swapBlockValues(duplicateBlocks,
                randBlock[0][0], randBlock[0][1],
                randBlock[1][0], randBlock[1][1]);

        return new Board(duplicateBlocks);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return this.manhattan() == ((Board) y).manhattan();
//        return Arrays.equals(this.blocks, that.blocks);
    }

    private void swapBlockValues(int[][] targetArray, int blockARow, int blockACol, int blockBRow, int blockBCol) {

        int tempSwap = targetArray[blockARow][blockACol];
        targetArray[blockARow][blockACol] = targetArray[blockBRow][blockBCol];
        targetArray[blockBRow][blockBCol] = tempSwap;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        MinPQ<Board> pq = new MinPQ<Board>();

        // Create a blueprint array that will be used to fill out the baords
        int[][] boardBlueprint = new int[n][n];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                boardBlueprint[row][col] = this.blocks[row][col];
            }
        }

        /**
         * Continue to create neighboring boards
         *
         * Inspect space locations for block movement opportunities.
         */

        if (spaceBlock[0] > 0) {
            // Space block is NOT in the top ROW - swap it with a block on the bottom

            int bBlockRow = spaceBlock[0] + 1; // Block is one row down
            int bBlockCol = spaceBlock[1]; // Block is in the same column

            swapBlockValues(boardBlueprint, bBlockRow, bBlockCol,
                    spaceBlock[0], spaceBlock[1]);

            // Create a new board and add it to pq.
            pq.insert(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    spaceBlock[0], spaceBlock[1],
                    bBlockRow, bBlockCol);
        }

        if (spaceBlock[0] < n) {
            // Space block is NOT in the bottom ROW - swap it with a block from the top

            int tBlockRow = spaceBlock[0] - 1; // Block is one row up.
            int tBlockCol = spaceBlock[1]; // Block is on the same column.

            swapBlockValues(boardBlueprint, tBlockRow, tBlockCol,
                    spaceBlock[0], spaceBlock[1]);

            // Create a new board and add it to pq.
            pq.insert(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    spaceBlock[0], spaceBlock[1],
                    tBlockRow, tBlockCol);

        }

        if (spaceBlock[1] > 0) {
            // space is NOT in the first COLUMN - swap it with a block on the left.

            int lBlockRow = spaceBlock[0]; // Block is on the same row.
            int lBlockCol = spaceBlock[1] - 1; // Block is one column back.

            swapBlockValues(boardBlueprint,
                    lBlockRow, lBlockCol,
                    spaceBlock[0], spaceBlock[1]);

            // Create a new board and add it to pq.
            pq.insert(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    spaceBlock[0], spaceBlock[1],
                    lBlockRow, lBlockCol);
        }

        if (spaceBlock[1] < n) {
            //space is NOT in the last COLUMN - swap it with a block on the right.

            int rBlockRow = spaceBlock[0]; // Block is on the same row.
            int rBlockCol = spaceBlock[1] + 1; // Block is one column to the right.

            swapBlockValues(boardBlueprint, rBlockRow, rBlockCol,
                    spaceBlock[0], spaceBlock[1]);

            // Create a new board and add it to pq.
            pq.insert(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint, spaceBlock[0], spaceBlock[1],
                    rBlockRow, rBlockCol);
        }


        return pq;
    }

    // string representation of this board
    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

        int n = 3;
        int count = 0;

        int[][] testA = new int[n][n];


        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                //testA[i - 1][j - 1] = (i == 1 && j == 1) ? 0 : i;
                testA[i - 1][j - 1] = count++;
            }
        }

        Board tb = new Board(testA);
        Board tb1 = new Board(testA);

        //@Test
        StdOut.println(tb.equals(tb1));

        //@Test
        StdOut.println(tb.toString());

        //@Test
        StdOut.println("Manhattan:" + tb.manhattan());

        //@Test
        StdOut.println("Hamming: " + tb.hamming());

        //@Test
        //Board twin1 = ;
        StdOut.println(tb.twin().toString());
    }

}