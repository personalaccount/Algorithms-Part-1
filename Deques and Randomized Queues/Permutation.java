import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Philip Ivanov
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> sequenceOfStrings = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals(" ")) sequenceOfStrings.enqueue(item);
        }

        for (int i = 0; i < k; i++) {
            try {
                StdOut.println(sequenceOfStrings.dequeue());
            }
            catch (NoSuchElementException e) {
                StdOut.println("There are no items at index " + i);
                break;
            }
        }
    }
}