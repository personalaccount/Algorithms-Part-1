import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

public final class Solver {

    // find a solution to the initial board (using the A* algorithm)

    private class SearchNode {

        private SearchNode previous; // points to the previous node
        private int numMoves; // number of moves to reach the board
        private Board board;

    }

    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();

        int numMoves = 0;

//        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
//
//        // Points to the current node
//        SearchNode pointer = new SearchNode(null, initial, numMoves);
//
//        pq.insert(pointer);
//
//        // Continue until the goal board is reached
//        while (!pointer.board.isGoal()) {
//
//            numMoves++; // if not increment the number of moves
//
//            // For each neighbor check if it is the previous board
//            // if it is not, then insert it into the priority queue
//
//            for (Board b : pointer.board.neighbors()) {
//                if (!b.equals(pointer.previousNode.board)) {
//                    pq.insert(new SearchNode(pointer, b, numMoves));
//                }
//            }
//
//            pointer = new SearchNode();
//        }

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

        args[0] = "";
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
