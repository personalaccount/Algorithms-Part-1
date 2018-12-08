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
    private int numberOfPoints;

    // Construct an empty set of points
    public KdTree() {
        // Empty array of size 0
        tree = new Point2D[0];
    }

    // Auxiliary method to check if the point is null
    private void exceptionIfNull(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
    }

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
        //        if (!contains(p)) add(p);
        tree[findIndex(p)] = p;
        numberOfPoints++;
    }

    // Auxilliary method used to find correct array index
    private int findIndex(Point2D p) {
        // If this is the first insert increase array size to 5 and insert the point at the root
        if (isEmpty()) {
            resizeTree(5);
            return 0;
        }
        else if (tree.length == numberOfPoints - 2 ) {
            resizeTree(numberOfPoints * 2);
        }

        // Start by comparing with the root [0] element
        return findIndex(p, 0);
    }

    // Method overloaded for recursion (k is the current position in the array)
    private int findIndex(Point2D p, int k) {
        // Default value for the next index to be returned
        int nextK = 2 * k;

        // Determine whether we're doing X or Y comparison
        if (k % 2 == 0) {
            // Check by X to see if the point is to the right and adjust accordingly.
            if (Double.compare(p.x(), tree[k].x()) >= 0) nextK++;
        }
        else {
            // Check by Y to see if the point is on top and adjust accordingly.
            if (Double.compare(p.y(), tree[k].y()) >= 0) nextK++;
        }
        if (tree[nextK] == null) return nextK;
        return findIndex(p, nextK);
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        exceptionIfNull(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : tree) {
            p.draw();
        }
    }

    // Resize bst array
    private void resizeTree(int capacity) {
        Point2D[] treeCopy = new Point2D[][ capacity];

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

        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        exceptionIfNull(p);

    }

}
