import java.util.Iterator;

/**
 * Created by Philip Ivanov
 *
 * A randomized queue is similar to a stack or queue, except that the item removed is chosen
 * uniformly at random from items in the data structure.
 */

public class RandomizedQueue<Item> implements Iterable<Item> {

    // construct an empty randomized queue
    public RandomizedQueue() {

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        boolean empty = (size() == 0 );
        return empty;
    }

    // return the number of items on the randomized queue
    public int size() {

    }

    // add the item
    public void enqueue(Item item) {

    }

    // remove and return a random item
    public Item dequeue() {

    }

    // return a random item (but do not remove it)
    public Item sample() {

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {

    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}