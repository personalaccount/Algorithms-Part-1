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
//        int[] a = new int[]{0, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10};
        int[] a = new int[]{0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
//        int[] a = new int[]{0, 34, 30, 29, 27, 25, 17, 16, 19, 22, 24};
//        int[] a = new int[]{0, 30, 27, 23, 17, 16, 15, 13, 14, 18, 11};


        for (int i = 1; i < a.length / 2; i++) {
            if (a[i] < a[i * 2] || a[i] < a[i * 2 + 1]) {
                StdOut.println(" [" + a[i] + "]");
                StdOut.println(" /  \\ ");
                StdOut.println(a[i * 2] + "  " + a[i * 2 + 1]);
                break;
            }
        }
    }
}
