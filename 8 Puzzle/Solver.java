import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */

public final class Solver {

    private final boolean isSolvable;
    private final int numberOfMoves;
    private final Deque<Board> solutions;

    // Auxiliary object used to represent and compare search Nodes
    private final class SearchNode implements Comparable<SearchNode> {
        private final int manhattanSum;
        private final Board board; // Game board
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

    // Auxiliary class for the solutions iterator.
    public class Deque<Item> implements Iterable<Item> {

        private Node headOfDeque, endOfDeque; // Links to head and end nodes of the deque.
        private int dequeSize = 0; // Number of items in the deque.

        // Nested inner class that defines a node.
        private class Node {
            private Item item;
            private Node next;
            private Node previous;
        }

        // Construct an empty deque.
        public Deque() {
            resetDeque();
        }

        private void resetDeque() {
            headOfDeque = new Node();
            endOfDeque = headOfDeque;
        }

        // Is the deque empty?
        public boolean isEmpty() {
            return (dequeSize == 0);
        }

        // Return the number of items on the deque.
        public int size() {
            return dequeSize;
        }

        private boolean lastNode() {
            return (dequeSize == 1);
        }

        private void verifyItemIsNotNull(Item item) {
            if (item == null) throw new IllegalArgumentException();
        }

        // Add an item to the front.
        public void addFirst(Item item) {
            verifyItemIsNotNull(item);

            // If deque is not empty, then link a new node and point headOfDeque to it
            if (!isEmpty()) {
                Node previousHeadOfDeck = headOfDeque;

                // Create a new Node and set it's values
                headOfDeque = new Node();
                headOfDeque.next = previousHeadOfDeck;

                // Maintain a backtrack link for removeLast
                previousHeadOfDeck.previous = headOfDeque;
            }
            // assign the value to the head node item
            headOfDeque.item = item;

            dequeSize++;
        }

        // Add the item to the end.
        public void addLast(Item item) {
            verifyItemIsNotNull(item);

            // If deque is not empty, then link a new node and point endOfDeque to it
            if (!isEmpty()) {
                // Create a variable that points to the end of deque.
                Node previousEndOfDeck = endOfDeque;

                // Point endOfDeque to a new node and set the values
                endOfDeque = new Node();
                endOfDeque.next = null;
                endOfDeque.previous = previousEndOfDeck;

                previousEndOfDeck.next = endOfDeque;
            }
            endOfDeque.item = item;
            dequeSize++;
        }

        // Return an iterator over items in order from front to end
        public Iterator<Item> iterator() {
            return new ListIterator();
        }

        // Inner class describing the required iterator
        private class ListIterator implements Iterator<Item> {
            private Node current = headOfDeque;

            // include current.item check to account for the requirement to start with an empty queue
            public boolean hasNext() {
                return (current != null && current.item != null);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }
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

        solutions = new Deque<Board>();
        while (pointer != null) {
            solutions.addFirst(pointer.board);
            pointer = pointer.previous;
        }

        isSolvable = true;
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

        // Fill in the stack of boards that lead to the solution
        return solutions;

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
