import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

// immutable data type Board

public final class Board {

    private final int n; // board dimension n
    private final int[][] blocks;
    private final int totalBlocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public Board(int[][] blocks) {
        if (blocks == null || blocks.length < 1) throw new IllegalArgumentException();

        n = blocks.length;
        this.blocks = new int[n][n];
        totalBlocks = this.n * this.n;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                this.blocks[row][col] = blocks[row][col];
            }
        }

    }

    // board dimension n
    public int dimension() {
        return this.n;
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
            if (getBlockValue(i) != i) numOfBlocksWPos++;
        }

        return numOfBlocksWPos;
    }

    // returns the value of the block given a consequetive count
    private int getBlockValue(int count) {

        if (count > 0 && count <= totalBlocks) {

            // Determine the row, by rounding up
            int row = (int) (Math.ceil(((double) count / this.n)));

            // Determine the column; if remainder is 0 then it is the last column
            int column = count % this.n;
            column = (column == 0) ? this.n : column;

            return blocks[row - 1][column - 1];
        }
        throw new IllegalArgumentException("Count has to be between 1 and " + totalBlocks + ". Your entry is " + count);
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {

    }
//
//    public boolean isGoal()                // is this board the goal board?
//
//    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
//
//    public boolean equals(Object y)        // does this board equal y?
//
//    public Iterable<Board> neighbors()     // all neighboring boards
//
//    public String toString()               // string representation of this board (in the output format specified below)


    public static void main(String[] args) {
        int col = 4 % 3;
        col = (col == 0) ? (3) : col;
        StdOut.println(col);
    }
}
