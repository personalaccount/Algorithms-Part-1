import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

public final class Solver {

    // min number of moves to solve initial board; -1 if unsolvable
    private int minMoves;

    // aux. object to hold and compare searchnodes
    private class SearchNode implements Comparable<SearchNode> {
        private int numMoves; // number of moves to reach the board
        private SearchNode previous; // points to the previous node
        private Board board;

        public int compareTo(SearchNode that) {
            int thisPriority = board.hamming() + numMoves;
            int thatPriority = that.board.hamming() + that.numMoves;

            if (thisPriority < thatPriority) return -1;
            if (thisPriority > thatPriority) return 1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();

        SearchNode initNode = new SearchNode();

        initNode.previous = null;
        initNode.numMoves = 0;
        initNode.board = initial;

        SearchNode pointer = initNode;

    }

    // is the initial board solvable?
    public boolean isSolvable() {

        return true;

    }


    // min number of moves to solve initial board; -1 if unsolvable

    public int moves() {

        return -1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        return null;

    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(new File("8puzzle-tests/puzzle01.txt"));
//        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
//
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
