import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */
public class Board {

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public Board(int[][] blocks) {

    }

//    public int dimension() { // board dimension n
//
//    }
//
//    public int hamming() { // number of blocks out of place
//
//    }
//
//    public int manhattan() { // sum of Manhattan distances between blocks and goal
//
//    }
//
//    public boolean isGoal() { // is this board the goal board?
//
//    }
//
//    public Board twin() { // a board that is obtained by exchanging any pair of blocks
//
//    }
//
//    public boolean equals(Object y) {      // does this board equal y?
//
//    }
//
//    public Iterable<Board> neighbors() {   // all neighboring boards
//
//    }

    public static void main(String[] args) {
        int[] a = new int[]{34, 30, 29, 27, 25, 17, 16, 19, 22, 24};
        Arrays.sort(a);
        for (int item : a) {
            StdOut.print(item + ", ");
        }

        StdOut.println();

        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i] + " -> " + (2 * a[i]) + " -> " + (2 * a[i] + 1));
        }

    }
}
