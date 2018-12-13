import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that uses a 2d-tree to implement PointSET API
 */

public class KdTree {

    private Node root;
    private int numberOfPoints = 0;

    // 2d Tree node

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }


    // Construct an empty set of points
    public KdTree() {
        // Empty array of size 0
    }

    // Auxiliary method to check if the point is null
    private void exceptionIfNull(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
    }

    public boolean isEmpty() {
        return (numberOfPoints == 0);
    }

    // Number of points in the set
    public int size() {
        return numberOfPoints;
    }

    // Insert the point into the set (if it is not already in the set)
    public void insert(Point2D p) {
        numberOfPoints++;
    }

    // Method overloaded for recursion (k is the current position in the array)
    private int findIndex(Point2D p, int k) {
        // Default value for the next index to be returned
        int nextK = 2 * k;

        // Determine whether we're doing X or Y comparison
        if (k % 2 == 0) {
            // Check by Y to see if the point is to the right and adjust accordingly.
            if (Double.compare(p.y(), tree[k].y()) >= 0) nextK++;
        }
        else {
            // Check by X to see if the point is on top and adjust accordingly.
            if (Double.compare(p.x(), tree[k].x()) >= 0) nextK++;
        }
        if (tree[nextK] == null) return nextK;
        return findIndex(p, nextK);
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        exceptionIfNull(p);
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : tree) {
            p.draw();
        }
    }

    // Resize bst array
    private void resizeTree(int capacity) {
        Point2D[] treeCopy = new Point2D[capacity];

        for (int i = 0; i < size(); i++) {
            treeCopy[i] = tree[i];
        }

        tree = treeCopy;
//        treeCopy = null;
    }


    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        // Iterable Stack object to hold matching points
        Stack<Point2D> pointsInside = new Stack<>();

        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        exceptionIfNull(p);
        return p;

    }

    public static void main(String args[]) {

        //@Test Create a KdTree object
        KdTree kdt = new KdTree();

        Point2D a = new Point2D(0.4, 0.4);
        Point2D b = new Point2D(0.1, 0.2);
        Point2D c = new Point2D(0.5, 0.2);
        Point2D d = new Point2D(0.6, 0.5);

        kdt.insert(a);
        kdt.insert(b);
        kdt.insert(c);
        kdt.insert(d);

    }

}
