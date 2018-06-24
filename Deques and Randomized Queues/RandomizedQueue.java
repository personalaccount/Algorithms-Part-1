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
    private int numberOfNodes = 0;  // Number of nodes in the deque.


    // construct an empty randomized queue
    public RandomizedQueue() {
        isEmpty();
    }

    // nested inner class that defines a node
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // if the randomized queue empty, construct an empty queue
    public boolean isEmpty() {
        if (head == null) head = new Node();

        return (size() == 0);
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

        if (size() <= 1) { // If this is the last node - reset head.
            head = null;
        }
        else { // there are at least two nodes in the queue

            for (int i = 1; i < StdRandom.uniform(1, numberOfNodes); i++) current = current.next;

            item = current.item;

            //  when it's the first node reset the head to the next node
            if (current.previous == null) {
                head = current.next;
                head.previous = null;
            }
            else if (current.next == null) { // when it's the last node reassign the end to the one before it and remove it
                Node newEnd = current.previous;
                newEnd.next = null;
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

    /*
     iterator implementation must support operations next() and hasNext() in constant worst-case time; and construction in linear time;
     */
    private class RandomOrderIterator implements Iterator<Item> {

        private Node current = head;
        private int[] nodeOrder;  // holds the order of nodes
        private int pointer = 0; // Pointer to the current element in the nodeOrder array

        public RandomOrderIterator() {

            if (size() >= 1) {
                nodeOrder = new int[numberOfNodes];

                // Fill out the sequence of next operations
                for (int i = 0; i < numberOfNodes; i++) {
                    nodeOrder[i] = i;
                }

                StdRandom.shuffle(nodeOrder);
            }

        }

        // include current.item check to account for the requirement to start with an empty queue
        public boolean hasNext() { return (current != null && current.item != null && (pointer < size())); }
        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
//            if (pointer == numberOfNodes) throw new NoSuchElementException();

            // Start from the beginning
            current = head;
            for (int i = 0; i < nodeOrder[pointer]; i++) current = current.next;
            Item item = current.item;

            pointer++;

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
        int testSeed = 14;
        StdOut.println("\nAdd integers ranging from " + testSeed + " to 1");
        for (int i = testSeed; i > 0; --i) {
            StdOut.print(i);
            ranInts.enqueue(i);
            if (i > 1) StdOut.print(", ");
        }

        try {

            /* @Test */
            StdOut.println("\n\nTest sampling: " + ranInts.sample());


            /* @Test */
            StdOut.println("\nTest iterator 1: ");
            Iterator<Integer> ranQIt1 = ranInts.iterator();
            while (ranQIt1.hasNext()) {
                StdOut.print(ranQIt1.next() + " ");
            }

            /* @Test */
            StdOut.println("\nTest iterator 2: ");
            Iterator<Integer> ranQIt2 = ranInts.iterator();
            while (ranQIt2.hasNext()) {
                StdOut.print(ranQIt2.next() + " ");
            }

            StdOut.println("\nForeach loop iterator 3 test: ");
            for (int i : ranInts) {
                StdOut.print(i + " ");
            }


            StdOut.println("\n\nTest removing: ");

            /* @Test */
            StdOut.println("Remove single item: " + ranInts.dequeue());
            StdOut.println("Constituting nodes: ");
            ranInts.printNodes();

            /* @Test */
            for (int i = 1; i <= (testSeed/2); i++) {
                StdOut.print("\nRemove " + i + " items: ");
                for (int j = 0; j < i; j++) StdOut.print(ranInts.dequeue() + " ");
                if (i % 2 == 0) {
                    StdOut.println("\nConstituting nodes: ");
                    ranInts.printNodes();
                }
                else {
                    StdOut.println("\nForeach loop iterator test: ");
                    for (int k : ranInts) {
                        StdOut.print(k + " ");
                    }
                }
            }
        }
        catch (NoSuchElementException e) {
            StdOut.println(e);
        }

        /* @Test */
        StdOut.println("\nCheck random calls to enqueue(), sample(), and size()");

        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        int n = 1000;

        for (int i = 0; i < n; i++) {
            double ranInt = StdRandom.uniform(0.1,1.0);
            if (ranInt > 0.8) {
                rq.enqueue(i);
                StdOut.println("Enque: [" + i + "]");
            }
            else if (ranInt > 0.5) {
                try {
                    StdOut.println("Sample: [" + rq.sample() + "]");
                } catch (NoSuchElementException e) {
                    StdOut.println(e);
                }
            }
            else {
                StdOut.println("Size: " + rq.size());
            }
        }


    }
}