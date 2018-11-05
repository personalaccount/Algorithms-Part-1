import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

public final class Solver {

    // min number of moves to solve initial board; -1 if unsolvable
    private SearchNode currentMinNode;
    private boolean isSolvable;

    // aux. object to hold and compare searchnodes
    private final class SearchNode implements Comparable<SearchNode> {
        private final int manhattanSum;
        private final Board board; // board itself
        private final int numMoves; // number of moves to reach the board
        private final SearchNode previous; // points to the predecessor search node

        public SearchNode(Board b, int n, SearchNode p) {
            board = b;
            numMoves = n;
            previous = p;
            manhattanSum = board.manhattan();
        }

        public int ms() {
            return manhattanSum;
        }

        public int compareTo(SearchNode that) {
            if (manhattanSum < that.ms()) return -1;
            if (manhattanSum > that.ms()) return 1;
            return 0;
        }
    }

    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        // First, insert the initial search node.
        SearchNode initNode = new SearchNode(initial, 0, null);


        pq.insert(initNode);

        // Delete from the priority queue the search node with the minimum priority
        currentMinNode = pq.delMin();

        while (!currentMinNode.board.isGoal()) {
            // Insert onto the priority queue all neighboring search nodes
            for (Board neighborBoard : currentMinNode.board.neighbors()) {
                // If the predecessor is not null, make sure that it's board is not included as a neighbor
                if (currentMinNode.previous != null && neighborBoard.equals(currentMinNode.previous.board)) {
                    continue;
                }
                pq.insert(new SearchNode(neighborBoard, currentMinNode.numMoves + 1, currentMinNode));
            }
            currentMinNode = pq.delMin();
        }

        // Update solvable marker
        isSolvable = true;
    }

    // is the initial board solvable?
    public boolean isSolvable() {

        return isSolvable;

    }


    // min number of moves to solve initial board; -1 if unsolvable

    public int moves() {

        if (isSolvable) {
            return currentMinNode.numMoves;
        }

        return -1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        // Fill in the stack of boards that lead to the solution
        Stack<Board> sb = new Stack<Board>();

        // If the initial board was the solution all along, simply push it down the stack
        if (moves() == 0) {
            sb.push(currentMinNode.board);
        }
        else {
            // Retraces the steps from the solution node to the initial
            SearchNode pointer = currentMinNode;

            while (pointer.previous != null) {
                sb.push(pointer.board);
                pointer = pointer.previous;
            }

        }

        return sb;

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
