import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

public final class Solver {

    private final boolean isSolvable;
    private final int numberOfMoves;
    private final Stack<Board> solutions;

    // Auxiliary object used to represent and compare search Nodes
    private final class SearchNode implements Comparable<SearchNode> {
        private final Board board; // Game board
        private final int numMoves; // number of moves to reach the board
        private final SearchNode previous; // points to the predecessor search node
        private final int priority; // priority of the node

        public SearchNode(Board b, int n, SearchNode p) {
            board = b;
            numMoves = n;
            previous = p;
            priority = board.manhattan() + numMoves;
        }

        public int priority() {
            return priority;
        }

        public int compareTo(SearchNode that) {
            if (priority < that.priority()) return -1;
            if (priority > that.priority()) return 1;
            return 0;
        }
    }

    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> pq = new MinPQ<>();

        // First, insert the initial search node and it's twin.
        pq.insert(new SearchNode(initial, 0, null));
        pq.insert(new SearchNode(initial.twin(), 0, null));

        // Delete from the priority queue the search node with the minimum priority
        SearchNode currentMinNode = pq.delMin();

        while (!currentMinNode.board.isGoal()) {
            // Insert onto the priority queue all neighboring search nodes
            for (Board neighborBoard : currentMinNode.board.neighbors()) {
                // If the predecessor is not null, make sure that it's board is not included as a neighbor
                if (currentMinNode.previous != null && neighborBoard.equals(currentMinNode.previous.board)) {
                    continue;
                }
                // Add a new search node to the priority queue.
                pq.insert(new SearchNode(neighborBoard, currentMinNode.numMoves + 1, currentMinNode));
            }
            // Remove the node with the smallest priority.
            currentMinNode = pq.delMin();
        } // When the while() is done currentMinNode contains the goal board

        numberOfMoves = currentMinNode.numMoves;

        // For the sake of convenience create a pointer variable for the solution trace.
        SearchNode pointer = currentMinNode;

        solutions = new Stack<>();
        while (pointer.previous != null) {
            solutions.push(pointer.board);
            pointer = pointer.previous;
        } // When while() loop is done, pointer is pointing to the initial board.

        solutions.push(pointer.board); // Push the initial board to the stack

        if (pointer.board.equals(initial)) {
            isSolvable = true;
        }
        else {
            isSolvable = false;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }


    // min number of moves to solve initial board; -1 if unsolvable

    public int moves() {

        if (isSolvable) {
            return numberOfMoves;
        }

        return -1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        if (!isSolvable) return null;

        // Fill in the stack of boards that lead to the solution
        Stack<Board> reverseStack = new Stack<>();

        while (!solutions.isEmpty()) {
            reverseStack.push(solutions.pop());
        }

        return reverseStack;

    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
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
