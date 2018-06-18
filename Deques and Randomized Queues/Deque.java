import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Philip Ivanov
 *
 *  A double-ended queue or deque (pronounced “deck”) is a generalization of a stack and a queue that supports adding
 *  and removing items from either the front or the back of the data structure.
 */

public class Deque<Item> implements Iterable<Item> {

    private Node headOfDeque, endOfDeque; // Links to head and end nodes of the deque.
    private int dequeSize; // Number of items in the deque.

    // Nested inner class that defines a node.
    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // Construct an empty deque.
    public Deque() {
        dequeSize = 0;
        isEmpty();
    }

    // Is the deque empty?
    public boolean isEmpty() {
        boolean empty = (size() == 0);

        if(headOfDeque == null) {
            headOfDeque = new Node();
            endOfDeque = headOfDeque;
        }

        return empty;
    }

    // Return the number of items on the deque.
    public int size() { return dequeSize; }

    private void verifyItemIsNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    // Add the item to the front.
    public void addFirst(Item item) {
        verifyItemIsNotNull(item);

        // If deque is empty, then fill out the item field of the empty node
        if (isEmpty()) {
            headOfDeque.item = item;
        } else {
            // Save a link to the current head of deque
            Node previousHeadOfDeck = headOfDeque;

            // Create a new Node and set it's values
            headOfDeque = new Node();
            headOfDeque.item = item;
            headOfDeque.next = previousHeadOfDeck;

            // Maintain a backtrack link for removeLast
            previousHeadOfDeck.previous = headOfDeque;
        }

        dequeSize++;
    }

    // Add the item to the end.
    public void addLast(Item item) {
        verifyItemIsNotNull(item);

        // If this is the first entry, then fill out the item property of the only empty node
        if (isEmpty()) {
            endOfDeque.item = item;
        } else {
            // Create a variable that points to the end of deque.
            Node previousEndOfDeck = endOfDeque;

            // Point endofDeque to a new node and set the values
            endOfDeque = new Node();
            endOfDeque.item = item;
            endOfDeque.next = null;
            endOfDeque.previous = previousEndOfDeck;

            previousEndOfDeck.next = endOfDeque;
        }

        dequeSize++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        // Set the item to be returned to the item field of the first node
        Item item = headOfDeque.item;

        // If the next node exists, point headOfDeque to it and set its previous to null
        if (headOfDeque.next != null) {
            headOfDeque = headOfDeque.next;
            headOfDeque.previous = null;
        } else {
            headOfDeque = null;
        }

        dequeSize--;

        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        // Set the item to be returned to the item field of the last node
        Item item = endOfDeque.item;

        // If previous node exists, then point endOfDeque to it and set its next to null
        if (endOfDeque.previous != null) {
            endOfDeque = endOfDeque.previous;
            endOfDeque.next = null;
        } else {
            endOfDeque = null;
        }

        dequeSize--;

        return item;
    }

    // Return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new FrontToEndIterator();
    }

    // Inner class describing the required iterator
    private class FrontToEndIterator implements Iterator<Item> {
        private Node current = headOfDeque;
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if(!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String args[]) {

        // Create an empty deque, fill it with ints from 1 to 10 and print it out
        Deque<Integer> dInts = new Deque<>();

        StdOut.println("\nTest exception on addFirst:");
        try {
            dInts.addFirst(null);
        } catch (IllegalArgumentException e) {
            StdOut.println(e);
        }

        StdOut.println("\nTest exception on addLast:");
        try {
            dInts.addLast(null);
        } catch (IllegalArgumentException e) {
            StdOut.println(e);
        }

        StdOut.println("\nTest adding: ");

        int testSize = 23;
        StdOut.println("\nAdd integers ranging from " + testSize + " to 0 to the end of the deque");
        for (int i = testSize; i >= 0; --i ) { dInts.addLast(i); }
        for (int item : dInts) { StdOut.print(item + " "); }

        StdOut.println("\nAdd integers ranging from " + (testSize - 1) + " to 0 to the front of the deque");
        for (int i = testSize-1; i >= 0; --i ) { dInts.addFirst(i); }
        for (int item : dInts) { StdOut.print(item + " "); }

        StdOut.println("\n\nTest removing: ");

        try {

            StdOut.println("\nRemove first item: " + dInts.removeFirst());
            for (int item : dInts) {
                StdOut.print(item + " ");
            }

            StdOut.println("\nRemove last item: " + dInts.removeLast());
            for (int item : dInts) {
                StdOut.print(item + " ");
            }

            for (int i=1; i <= (testSize); i++) {
                StdOut.println("\nRemove " + i + " items from head of deque");
                for(int j = 0; j < i; j++) { dInts.removeFirst(); }
                for (int item : dInts) {
                    StdOut.print(item + " ");
                }
            }

            for (int i=1; i <= (testSize); i++) {
                StdOut.println("\nRemove " + i + " items from end of deque");
                for(int j = 0; j < i; j++) { dInts.removeLast(); }
                for (int item : dInts) {
                    StdOut.print(item + " ");
                }
            }

        }catch (NoSuchElementException e){
            StdOut.println(e);
        }
    }

}
