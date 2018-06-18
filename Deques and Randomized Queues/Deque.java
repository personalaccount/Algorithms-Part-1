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
        headOfDeque = new Node();
        headOfDeque.next = null;
        headOfDeque.previous = null;

        endOfDeque = headOfDeque;

        dequeSize = 0;
    }

    // Is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // Return the number of items on the deque.
    public int size() { return dequeSize; }

    private void checkForNullArgument (Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    // Add the item to the front.
    public void addFirst(Item item) {
        checkForNullArgument(item);

        // If this is the first entry, then fill out the item property of the only empty node
        if (isEmpty()) {
            headOfDeque.item = item;
        } else {
            // Create a link that points to the current head of deque
            Node previousHeadOfDeck = headOfDeque;

            // Point headOfDeque to a new Node and set it's values
            headOfDeque = new Node();
            headOfDeque.item = item;
            headOfDeque.next = previousHeadOfDeck;

            // Maintain a backlink for removeLast
            previousHeadOfDeck.previous = headOfDeque;
        }

        dequeSize++;
    }

    // Add the item to the end.
    public void addLast(Item item) {
        checkForNullArgument(item);

        // If this is the first entry, then fill out the item property of the only empty node
        if (isEmpty()) {
            if (headOfDeque == null) headOfDeque = new Node();
            headOfDeque.item = item;
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

    // If this is the last element maintain an empty node
    private void resetOnEmpty() {
        if(isEmpty()){
            headOfDeque = new Node();
            endOfDeque = headOfDeque;
        }

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = headOfDeque.item;
        headOfDeque = headOfDeque.next;
        if (headOfDeque != null) {
            headOfDeque.previous = null;
        } else {
            resetOnEmpty();
        }

        dequeSize--;

        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = endOfDeque.item;
        endOfDeque = endOfDeque.previous;
        endOfDeque.next = null;
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
        Deque<Integer> dInts = new Deque<Integer>();

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

//        StdOut.println("\nTest exception on removeFirst:");
//        try {
//            dInts.addLast(1);
//            dInts.addLast(2);
//            dInts.removeLast();
//            dInts.removeLast();
//            dInts.removeLast();
//        }catch (NoSuchElementException e){
//            StdOut.println(e);
//        }

        StdOut.println("\nAdd integers ranging from 10 to 0 to the end of the deque");
        for (int i = 10; i >= 0; --i ) { dInts.addLast(i); }
        for (int item : dInts) { StdOut.print(item + ","); }

        StdOut.println("\nAdd integers ranging from 9 to 0 to the front of the deque");
        for (int i = 9; i >= 0; --i ) { dInts.addFirst(i); }
        for (int item : dInts) { StdOut.print(item + ","); }

        StdOut.println("\nRemove first 10 items");
        for (int i = 0; i < 10; i++ ) { dInts.removeFirst(); }
        for (int item : dInts) { StdOut.print(item + ","); }

        StdOut.println("\nRemove last 3 items");
        for (int i = 0; i < 3; i++ ) { dInts.removeLast(); }
        for (int item : dInts) { StdOut.print(item + ","); }

        StdOut.println("\nRemove last 3 items");
        for (int i = 0; i < 3; i++ ) { dInts.removeLast(); }
        for (int item : dInts) { StdOut.print(item + ","); }

        StdOut.println("\nRemove first 10 items");
        try {

            for (int i = 0; i < 10; i++) {
                dInts.removeFirst();
            }
        }catch (NoSuchElementException e){
            for (int item : dInts) { StdOut.print(item + ","); }
        }
    }

}
