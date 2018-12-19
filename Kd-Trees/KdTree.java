import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that uses a 2d-tree to implement PointSET API
 */

public class KdTree {

    private Node root; // Points to the root of KdTree
    private int numberOfPoints = 0;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private Node parent; // parent of the node
    }


    // Construct an empty set of points
    public KdTree() {
        root = new Node();
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
        // Special case for an empty tree
        if (isEmpty()) {
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
        }
        else {
            Node pointer = root; // Pointer for traversing down the tree.
            Node parent = root; // To keep track of the parent.
            Boolean leftInsert = true; // Helps determine where to insert the new node

            int i = 0; // Node level.

            while (pointer != null) {

                // Check for duplicates
                if (pointer.p.equals(p)) return;

                parent = pointer;
                leftInsert = true;

                if (i % 2 == 0) {
                    // Compare x coordinates; if smaller go left, else go right
                    if (p.x() < pointer.p.x()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                        leftInsert = false;
                    }
                }
                else {
                    // Compare y coordinates; if below go left, else go right
                    if (p.y() < pointer.p.y()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                        leftInsert = false;
                    }
                }
                i++; // Increment node level after each iteration
            }
            // Found an empty spot to make an insert

            // Create new node and make an insert

            Node n = new Node();
            n.p = p;

            if (leftInsert) {

                if (i % 2 == 0) {
                    // Bottom of the parent
                    n.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                }
                else {
                    // Left of the parent point)
                    n.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                }

//                // Construct an enclosing rectangle on leftInsert
//                if (i % 2 == 0) {
//                    // Vertical line
//                    n.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), p.x(), parent.rect.ymax());
//                }
//                else {
//                    // Horizontal line
//                    n.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), p.y());
//                }
                parent.lb = n;

            }
            else {

                if (i % 2 == 0) {
                    // Top of the parent
                    n.rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                }
                else {
                    // Right of the parent
                    n.rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
//                // Construct an enclosing rectangle on rightInsert
//                if (i % 2 == 0) {
//                    // Vertical line
//                    n.rect = new RectHV(p.x(), parent.rect.ymax(), parent.rect.xmax(), parent.parent.rect.ymax());
//                }
//                else {
//                    // Horizontal line
//                    n.rect = new RectHV(parent.p.x(), p.y(), parent.rect.xmax(), parent.rect.ymax());
//                }
                parent.rt = n;

            }

        }

        numberOfPoints++;
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        exceptionIfNull(p);
        // Recursively go through each node, comparing each point
        return contains(p, root, 0);
    }

    // Overload to allow recursion
    private boolean contains(Point2D p, Node n, int level) {
        if (n == null) return false;
        if (p.equals(n.p)) return true;
        if (level % 2 == 0) {
            //Compare by x; Move left if less and right otherwise
            return contains(p, n.lb, level++);
        }
        else {
            return contains(p, n.rt, level++);
        }
    }

    // draw all points to standard draw
    public void draw() {
        if (!isEmpty()) draw(root, 0);
    }

    // Recursevely go through each branch until null
    private void draw(Node root, int level) {

        // Draw a point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        root.p.draw();

        // Draw subdivisions

        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            // Vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(root.p.x(), root.rect.ymin(), root.p.x(), root.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(root.rect.xmin(), root.p.y(), root.rect.xmax(), root.p.y());
        }

        // Increment level
        level++;

        if (root.lb != null) draw(root.lb, level);
        if (root.rt != null) draw(root.rt, level);

//        root.rect.draw();
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

    // Auxiliary method to check if the point is null
    private void exceptionIfNull(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
    }

    public static void main(String args[]) {

        //@Test Create a KdTree object
        KdTree kdt = new KdTree();

        //@Test Insert points
        kdt.insert(new Point2D(0.7, 0.2));
        kdt.insert(new Point2D(0.7, 0.2));
        kdt.insert(new Point2D(0.7, 0.2));
        kdt.insert(new Point2D(0.7, 0.2));
        kdt.insert(new Point2D(0.5, 0.4));
        kdt.insert(new Point2D(0.2, 0.3));
        kdt.insert(new Point2D(0.4, 0.7));
        kdt.insert(new Point2D(0.9, 0.6));

        //@Test Contains
        Point2D a = new Point2D(0.7, 0.2);
        Point2D b = new Point2D(0.7, 0.3);
        StdOut.println(kdt.contains(a));
        StdOut.println(kdt.contains(b));

        kdt.draw();
    }

}
