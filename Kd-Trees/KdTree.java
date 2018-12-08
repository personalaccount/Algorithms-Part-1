import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that uses a 2d-tree to implement PointSET API
 */

public class KdTree {

    // Array holding a bst of points.
    private Point2D[] tree;

    // Construct an empty set of points
    public KdTree() {
        // Empty array of size 0
        tree = new Point2D[0];
    }

    // Auxiliary method to check if the point is null
    private void exceptionIfNull(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
    }

    // Auxilliary method to check if the point is to the right of this one

    public boolean isEmpty() {
        return (tree.length == 0);
    }

    // Number of points in the set
    public int size() {
        return tree.length;
    }

    // Insert the point into the set (if it is not already in the set)
    public void insert(Point2D p) {
        exceptionIfNull(p);

        // If this is the first insert increase array size to 3 and insert the point at the root
        if (isEmpty()) {
            resize(3);
            tree[0] = p;
        }
//        if (!contains(p)) add(p);
    }

    private void add(Point2D p) {

    }


    // Does the set contain point p?
    public boolean contains(Point2D p) {
        exceptionIfNull(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // Resize bst array
    private void resize(int capacity) {
        double[] treeCopy = new double[capacity];

        for (int i = 0; i < tree.length; i++) {
            treeCopy[i] = tree[i];
        }

        tree = treeCopy;
        treeCopy = null;
    }


    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        // Iterable Stack object to hold matching points
        Stack<Point2D> pointsInside = new Stack<>();
    }

    public Point2D nearest(Point2D p) {
        exceptionIfNull(p);
    }

}
