import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Arrays;

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

                    if (spaceCount > 1) throw new IllegalArgumentException();
                }

                this.blocks[row][col] = block;
            }
        }

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
        if (val < 1 || val > totalBlocks) throw new IllegalArgumentException();

        // Determine the row, by rounding up
        return (int) (Math.ceil(((double) val / this.n)));
    }

    // returns block's column, based on the input value (assuming columns start with 1)
    private int getBlockCol(int val) {
        if (val < 1 || val > totalBlocks) throw new IllegalArgumentException();

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
        for (int i = 0; i < 2; i++) {
            int randomRow;
            int randomCol;

            do {
                randomRow = StdRandom.uniform(this.n);
                randomCol = StdRandom.uniform(this.n);
            } while (randomRow == spaceBlock[0] && randomCol == spaceBlock[1]);

            randBlock[i][0] = randomRow;
            randBlock[i][1] = randomCol;
        }

        // Exchange block values
        int swap = duplicateBlocks[randBlock[0][0]][randBlock[0][1]];
        duplicateBlocks[randBlock[0][0]][randBlock[0][1]] = duplicateBlocks[randBlock[1][0]][randBlock[1][1]];
        duplicateBlocks[randBlock[1][0]][randBlock[1][1]] = swap;

        return new Board(duplicateBlocks);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
//        return this.manhattan() == ((Board) y).manhattan();
        return Arrays.equals(this.blocks, that.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        // Start by inspecting the row location of the space
        if (spaceBlock[0] == 0) {
            // There's no way to move blocks from the top
        }

        MinPQ<Board> pq = new MinPQ<Board>();
        pq.insert(this);

        return pq;
    }
//
//    public String toString()               // string representation of this board (in the output format specified below)


    public static void main(String[] args) {
        int col = 4 % 3;
//        col = (col == 0) ? (3) : col;
        StdOut.println(col);
    }
}
