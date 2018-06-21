/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 *
 * A randomized queue is similar to a stack or queue, except that the item removed is chosen
 * uniformly at random from items in the data structure.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node head; // Link to head of queue.
    private int numberOfNodes;  // Number of nodes in the deque.


    // construct an empty randomized queue
    public RandomizedQueue() {
        numberOfNodes = 0;
        isEmpty();
    }

    // nested inner class that defines a node
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        boolean empty = (size() == 0);

        if (head == null) head = new Node();

        return empty;
    }

    // return the number of items on the randomized queue
    public int size() {
        return numberOfNodes;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        // If queue is empty, then fill out the item field of the empty node
        if (isEmpty()) {
            head.item = item;
        }
        else {
            // save a link to the current head of the queue
            Node previousHead = head;

            // Create a new Node object and set it's values
            head = new Node();
            head.item = item;
            head.next = previousHead;

            previousHead.previous = head;
        }

        numberOfNodes++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        // link pointing to the head of the rand queue
        Node current = head;
        Item item = current.item;

        if (size() <= 1) {
            head = null;
        }
        else { // there are at least two nodes in the queue

            // create a random counter
            int randCount = StdRandom.uniform(1, numberOfNodes);
            // follow the next link to the random node
            for (int i = 1; i < randCount; i++) {
                current = current.next;
            }
            // updated the item
            item = current.item;

            /**
             * Now deque the node and link the queue back.
             * Prior to doing so, check to see if the node is in the beginning or the end of the queue,
             * since these are the two special cases.
             */


            //  when it's the first node reset the head to the next node
            if (current.previous == null) {
                head = current.next;
                head.previous = null;

            }
            else if (current.next == null) { // when it's the last node reassign the end to the one before it and remove it
                Node beforeCurrent = current.previous;
                beforeCurrent.next = null;
            }
            else {

                // node is somewhere in between, so link the two adjacent nodes together
                Node beforeCurrent = current.previous;
                Node afterCurrent = current.next;

                beforeCurrent.next = afterCurrent;
                afterCurrent.previous = beforeCurrent;
            }

        }

        numberOfNodes--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return iterator().next();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {

        return new RandomOrderIterator();
    }

    private class RandomOrderIterator implements Iterator<Item> {
        private Node current = head;
        public boolean hasNext() { return current.item != null; }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;

            // if there are more nodes than one, then return a random node
            if (size() > 1) {
                int randomNumber = StdRandom.uniform(1, size());
                for (int i = 1; i < randomNumber; i++) current = current.next;
                item = current.item;
            }

            return item;
        }
    }

    private void printNodes() {
        if (size() <= 0) throw new NoSuchElementException("Queue is empty");

        Node current = head;
        while (current != null) {
            StdOut.print(current.item + " ");
            current = current.next;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {

        /* @Test Create an empty randomized queue */
        RandomizedQueue<Integer> ranInts = new RandomizedQueue<>();

        /* @Test */
        StdOut.println("\nTest exception on enqueue:");
        try {
            ranInts.enqueue(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println(e);
        }

        StdOut.print("\nTest adding: ");

        /* @Test */
        int testSeed = 10;
        StdOut.println("\nAdd integers ranging from " + testSeed + " to 0 to the end of the deque");
        for (int i = testSeed; i > 0; --i) {
            StdOut.print(i);
            ranInts.enqueue(i);
            if (i > 1) StdOut.print(",");
        }
        StdOut.println("\nConstituting nodes: ");
        ranInts.printNodes();

        try {

            /* @Test */
            StdOut.println("\n\nTest sampling: " + ranInts.sample());
            StdOut.println("Constituting nodes: ");
            ranInts.printNodes();

            StdOut.println("\n\nTest removing: ");

            /* @Test */
            StdOut.println("Remove single item: " + ranInts.dequeue());
            StdOut.println("Constituting nodes: ");
            ranInts.printNodes();

//            /* @Test */
            for (int i = 1; i <= (testSeed/2); i++) {
                StdOut.print("\nRemove " + i + " items: ");
                for (int j = 0; j < i; j++) StdOut.print(ranInts.dequeue() + " ");
                StdOut.println("\nConstituting nodes: ");
                ranInts.printNodes();
            }
        }
        catch (NoSuchElementException e) {
            StdOut.println(e);
        }

    }
}