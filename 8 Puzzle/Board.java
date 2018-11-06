import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

// immutable data type Board

public final class Board {

    private final short n; // board dimension n
    private final short[][] blocks;
    private final int totalBlocks;
    private final short rBlock1Row, rBlock1Col, rBlock2Row, rBlock2Col; // Coordinates of blocks to be exchanged to generate a twin
    private short spaceBlockRow, spaceBlockCol;


    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public Board(int[][] blocks) {
        if (blocks == null || blocks.length < 1) throw new IllegalArgumentException();

        n = (short) blocks.length;
        this.blocks = new short[n][n];
        totalBlocks = this.n * this.n;


        // Keep track of the number of spaces, there should be no more than 1
        int spaceCount = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int block = blocks[row][col];

                if (block == 0) {
                    spaceCount++;
                    if (spaceCount > 1) throw new IllegalArgumentException("There is more than one space");
                    spaceBlockRow = (short) row;
                    spaceBlockCol = (short) col;
                }

                this.blocks[row][col] = (short) block;
            }
        }

        if (spaceCount == 0) throw new IllegalArgumentException("There are no spaces");

        // Generate random coordinates for a pair of blocks to generate a twin.
        // Use cached row and col to prevent duplicate randoms.

        int[][] randBlock = new int[2][2];

        int cacheRow = spaceBlockRow;
        int cacheCol = spaceBlockCol;

        for (int i = 0; i < 2; i++) {
            int randomRow;
            int randomCol;

            int intN = (int) n;

            do {
                randomRow = StdRandom.uniform(intN);
                randomCol = StdRandom.uniform(intN);
            }
            while ((randomRow == spaceBlockRow && randomCol == spaceBlockCol) || (randomRow == cacheRow && randomCol == cacheCol));

            randBlock[i][0] = randomRow;
            randBlock[i][1] = randomCol;

            cacheRow = randomRow;
            cacheCol = randomCol;
        }

        rBlock1Row = (short) randBlock[0][0];
        rBlock1Col = (short) randBlock[0][1];
        rBlock2Row = (short) randBlock[1][0];
        rBlock2Col = (short) randBlock[1][1];
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
        if (column == 0) {
            column = this.n;
        }

//        column = (column == 0) ? this.n : column;

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
            int blockValue = getBlockValue(i);
            if (blockValue != i) {
                if (blockValue != 0) {
                    numOfBlocksWPos++;
                }
            }
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
            if (blockValue != i && blockValue != 0) {
                int correctRow = getBlockRow(blockValue);
                int correctCol = getBlockCol(blockValue);

                // number of rows required to move to the right spot

                int sumRows;
                int sumCols;

                if (row < correctRow) {
                    sumRows = correctRow - row;
                }
                else {
                    sumRows = row - correctRow;
                }


                if (col < correctCol) {
                    sumCols = correctCol - col;
                }
                else {
                    sumCols = col - correctCol;
                }

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

        // Holds a copy of the blocks array.
        int[][] duplicateBlocks = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                duplicateBlocks[row][col] = blocks[row][col];
            }
        }

        swapBlockValues(duplicateBlocks, rBlock1Row, rBlock1Col, rBlock2Row, rBlock2Col);

        Board twin = new Board(duplicateBlocks);

        return twin;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        if (n != that.n) return false;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (blocks[row][col] != that.blocks[row][col]) return false;
            }
        }
        return true;
    }

    private void swapBlockValues(int[][] targetArray, int blockARow, int blockACol, int blockBRow, int blockBCol) {

        int tempSwap = targetArray[blockARow][blockACol];
        targetArray[blockARow][blockACol] = targetArray[blockBRow][blockBCol];
        targetArray[blockBRow][blockBCol] = tempSwap;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        Stack<Board> boardStack = new Stack<Board>();

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

        if (spaceBlockRow > 0) {
            // There are rows above this block - swap it with the one above

            int tBlockRow = spaceBlockRow - 1; // Block is one row down
            int tBlockCol = spaceBlockCol; // Block is in the same column

            swapBlockValues(boardBlueprint,
                    tBlockRow, tBlockCol,
                    spaceBlockRow, spaceBlockCol);

            // Create a new board and add it to pq.
            boardStack.push(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    tBlockRow, tBlockCol,
                    spaceBlockRow, spaceBlockCol);
        }

        if (spaceBlockRow < n - 1) {
            // There are rows below this block - swap it with a one below

            int bBlockRow = spaceBlockRow + 1; // Block is one row up.
            int bBlockCol = spaceBlockCol; // Block is on the same column.

            swapBlockValues(boardBlueprint,
                    bBlockRow, bBlockCol,
                    spaceBlockRow, spaceBlockCol);

            // Create a new board and add it to pq.
            boardStack.push(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    bBlockRow, bBlockCol,
                    spaceBlockRow, spaceBlockCol);

        }

        if (spaceBlockCol > 0) {
            // There are columns to the left of this block  - swap it with the one on the left.

            int lBlockRow = spaceBlockRow; // Block is on the same row.
            int lBlockCol = spaceBlockCol - 1; // Block is one column back.

            swapBlockValues(boardBlueprint,
                    lBlockRow, lBlockCol,
                    spaceBlockRow, spaceBlockCol);

            // Create a new board and add it to pq.
            boardStack.push(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    lBlockRow, lBlockCol,
                    spaceBlockRow, spaceBlockCol);
        }

        if (spaceBlockCol < n - 1) {
            // Space is NOT in the last COLUMN - swap it with a block on the right.

            int rBlockRow = spaceBlockRow; // Block is on the same row.
            int rBlockCol = spaceBlockCol + 1; // Block is one column to the right.

            swapBlockValues(boardBlueprint,
                    rBlockRow, rBlockCol,
                    spaceBlockRow, spaceBlockCol);

            // Create a new board and add it to pq.
            boardStack.push(new Board(boardBlueprint));

            // swap the block back
            swapBlockValues(boardBlueprint,
                    rBlockRow, rBlockCol,
                    spaceBlockRow, spaceBlockCol);
        }


        return boardStack;
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

        // <settings>
        int n = 3;
        int spaceRow = 3;
        int spaceCol = 2;
        // </settings>

        int count = 1; // counter for the blocks

        int[][] testA = new int[n][n];


        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (i == spaceRow && j == spaceCol) {
                    testA[i - 1][j - 1] = 0;
                }
                else {
                    testA[i - 1][j - 1] = count++;
                }
            }
        }

        Board tb = new Board(testA);
        Board tb1 = new Board(testA);

        //@Test
        StdOut.println("Print board 1: \n" + tb.toString());
        StdOut.println("Print board 2: \n" + tb1.toString());

        //@Test
        StdOut.println("Space coordinates: " + tb.spaceBlockRow + ":" + tb.spaceBlockCol);

        //@Test
        StdOut.println("Boards are equal: " + tb.equals(tb1));


        //@Test
        StdOut.println("Manhattan:" + tb.manhattan());

        //@Test
        StdOut.println("Hamming: " + tb.hamming());

        //@Test
        Board twinBoard = tb.twin();
        StdOut.println("Create a twin board: \n" + twinBoard.toString());

        //@Test
        StdOut.println("Boards are equal: " + tb.equals(twinBoard));

        StdOut.println("Printing neighbors: ");
        for (Board b : tb.neighbors()) {
            StdOut.println(b);
        }
    }

}