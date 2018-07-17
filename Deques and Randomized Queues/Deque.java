/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 *
 *  A double-ended queue or deque (pronounced “deck”) is a generalization of a stack and a queue that supports adding
 *  and removing items from either the front or the back of the data structure.
 */

import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public boolean isEmpty() { return (dequeSize == 0); }

    // Return the number of items on the deque.
    public int size() { return dequeSize; }

    private boolean lastNode() { return (dequeSize == 1); }
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

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        // Set the item to be returned to the item field of the first node
        Item item = headOfDeque.item;

        // If this is the last node - reset deque.
        if (lastNode()) {
            resetDeque();
        }
        else { // reassign head
            headOfDeque = headOfDeque.next;
            headOfDeque.previous = null;
        }

        dequeSize--;

        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        // Set the item to be returned to the item field of the last node
        Item item = endOfDeque.item;

        // Check to see if this is the last node and set both head and end to null
        if (lastNode()) {
            resetDeque();
        }
        else { // reassign endOfDeque
            endOfDeque = endOfDeque.previous;
            endOfDeque.next = null;
        }

        dequeSize--;

        return item;
    }

    // Return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // Inner class describing the required iterator
    private class ListIterator implements Iterator<Item> {
        private Node current = headOfDeque;

        // include current.item check to account for the requirement to start with an empty queue
        public boolean hasNext() { return (current != null && current.item != null); }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private void printNodes() {
        if (size() <= 0) throw new NoSuchElementException("Queue is empty");

        Node current = headOfDeque;
        while (current != null) {
            StdOut.print(current.item + " ");
            current = current.next;
        }
    }

    public static void main(String[] args) {

        // Create an empty deque, fill it with ints from 1 to 10 and print it out
        Deque<Integer> dInts = new Deque<>();

        StdOut.println("\nTest exception on addFirst:");
        try {
            dInts.addFirst(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println(e);
        }

        StdOut.println("\nTest exception on addLast:");
        try {
            dInts.addLast(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println(e);
        }

        StdOut.println("\nTest adding: ");
        int testSize = 3;

        StdOut.println("\nAdd integers ranging from " + testSize + " to 0 to the end of the deque");
        for (int i = testSize; i >= 0; --i) dInts.addLast(i);
        for (int i : dInts) StdOut.print(i + " ");

        StdOut.println("\nAdd integers ranging from " + (testSize - 1) + " to 0 to the front of the deque");
        for (int i = testSize-1; i >= 0; --i) dInts.addFirst(i);
        dInts.printNodes();

        StdOut.println("\n\nTest removing: ");

        try {

            StdOut.println("\nRemove first item: " + dInts.removeFirst());
            dInts.printNodes();

            StdOut.println("\nRemove last item: " + dInts.removeLast());
            dInts.printNodes();

            for (int i = 1; i <= (testSize); i++) {
                StdOut.println("\nRemove " + i + " items from head of deque");
                for (int j = 0; j < i; j++) dInts.removeFirst();
                dInts.printNodes();
            }

            for (int i = 1; i <= (testSize); i++) {
                StdOut.println("\nRemove " + i + " items from end of deque");
                for (int j = 0; j < i; j++) dInts.removeLast();
                dInts.printNodes();
            }

        }
        catch (NoSuchElementException e) {
            StdOut.println(e);
        }
    }

}
