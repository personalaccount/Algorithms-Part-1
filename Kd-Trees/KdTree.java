import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that uses a 2d-tree to implement PointSET API
 */

public class KdTree {

    private Node root; // Points to the root of KdTree
    private int numberOfPoints = 0;
    // Iterable Stack object to hold matching points
    Stack<Point2D> pointsInside;


    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
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

//    public void insert(Point2D p) {
//        exceptionIfNull(p);
//        Node n = insert(root, root, p, 0);
//    }
//
//    private Node insert(Node parent, Node pointer, Point2D p, int level) {
//        // Treeset is empty
//        if (pointer == null) {
//            pointer = new Node();
//            pointer.p = p;
////            pointer.rect = new RectHV(0, 0, 1, 1);
//
//            numberOfPoints++;
//
//            return pointer;
//        }
//
//        // Check for duplicates
//        if (pointer.p.equals(p)) throw new IllegalArgumentException(pointer.p.toString() + " == " + p.toString());
//
//        boolean leftBottom = true;
//
//        if (level % 2 == 0) {
//            // Compare X coordinates
//            if (p.x() > pointer.p.x()) leftBottom = false;
//        }
//        else {
//            // Compare Y coordinates
//            if (p.y() > pointer.p.y()) leftBottom = false;
//        }
//
//        if (leftBottom) pointer.lb = insert(parent, pointer.lb, p, ++level);
//        pointer.rt = insert(parent, pointer.rt, p, ++level);
//        return pointer;
//    }

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
                if (p.equals(pointer.p))
                    throw new IllegalArgumentException(pointer.p.toString() + " == " + p.toString());
                ;

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
        if (n.p.equals(p)) return true;

        // Target point is not found

        // Set the compare flag
        Boolean compareByX = (level % 2 == 0);

        // Increment level for the next iteration
        level++;

        if (compareByX) {
            //Compare by x; Move left if less and right otherwise
            if (p.x() < n.p.x()) return contains(p, n.lb, level);
            return contains(p, n.rt, level);
        }
        else {
            if (p.y() < n.p.y()) return contains(p, n.lb, level);
            return contains(p, n.rt, level);
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
            // Horizontal
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

        // Initialize stack
        pointsInside = new Stack<>();

        // Find all the relevant points
        findPointsInRange(rect, root);

        return pointsInside;
    }

    private void findPointsInRange(RectHV rect, Node n) {
        // Check if this rectangle intersects with the one corresponding to the node
        if (rect.intersects(n.rect)) {
            addPointsToRange(rect, n);
        }
        else {
            findPointsInRange(rect, n.lb);
            findPointsInRange(rect, n.rt);
        }

    }

    // Recursevely add all relevant points
    private void addPointsToRange(RectHV r, Node n) {
        if (n == null) return;
        if (r.contains(n.p)) pointsInside.push(n.p);
        addPointsToRange(r, n.lb);
        addPointsToRange(r, n.rt);
    }

    public Point2D nearest(Point2D p) {
        exceptionIfNull(p);

        // Closest distance sofar
        return nearest(p, root, root.p.distanceSquaredTo(p), root.p);
    }

    /**
     * Auxiliary method to recursively go through the tree
     * <p>
     * If the closest point discovered so far is closer than the distance
     * between the query point and the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     * <p>
     * That is, search a node only only if it might contain a point
     * that is closer than the best one found so far.
     */

    private Point2D nearest(Point2D p, Node n, double closestDistanceYet, Point2D closestPointYet) {
        if (n == null) return closestPointYet;
        // Using squared distance to compare the squares of the two distances to avoid the expensive operation of taking square roots.

        // Check the distance to the rectangle if it's larger then abort.
        if (!n.equals(root)) {
            if(n.rect.distanceSquaredTo(p) > closestDistanceYet) return closestPointYet;
        }

        double sqrDistance = p.distanceSquaredTo(n.p); // Distance from query point to node's point

        // If this nodes' point is closer, reassign the values
        if (sqrDistance < closestDistanceYet) {
            closestDistanceYet = sqrDistance;
            closestPointYet = n.p;
        }

        // Descend down to subtrees and determine the closest points if there are any.

        Point2D lbPoint = nearest(p, n.lb, closestDistanceYet, closestPointYet);
        Point2D rtPoint = nearest(p, n.rt, closestDistanceYet, closestPointYet);

        // Compare points from each subtree
        if (lbPoint != null) {
            double lbPointSqrDistance = lbPoint.distanceSquaredTo(p);
            if (lbPointSqrDistance < closestDistanceYet) {
                closestDistanceYet = lbPointSqrDistance;
                closestPointYet = lbPoint;
            }
        }

        if (rtPoint != null) {
            double rtPointSqrDistance = rtPoint.distanceTo(p);
            if (rtPointSqrDistance < closestDistanceYet) {
                return rtPoint;
            }
        }

        return closestPointYet;
    }


    // Auxiliary method to check if the point is null.

    private void exceptionIfNull(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
    }

    public static void main(String args[]) {

        //@Test Create a KdTree object
        KdTree kdtree = new KdTree();
        KdTree kdtree2 = new KdTree();

        kdtree2.insert(new Point2D(.7, .2));
        kdtree2.insert(new Point2D(.5, .4));
        kdtree2.insert(new Point2D(.2, .3));
        kdtree2.insert(new Point2D(.4, .7));
        kdtree2.insert(new Point2D(.9, .6));

        StdOut.println(kdtree2.contains(new Point2D(.4, .7)));

        //@Test Insert points
        String filename = "kdtree-tests/circle10.txt";
        In in = new In(filename);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        //@Test draw points
        kdtree2.draw();

        //@Test Contains
        Point2D a = new Point2D(0.500000, 1.000000);
        Point2D b = new Point2D(0.024472, 0.654508);

        StdOut.println(kdtree.contains(a));
        StdOut.println(kdtree.contains(b));

        //@Test rectangle
        RectHV testRect = new RectHV(0, 0.2, 0.7, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
//        testRect.draw();

        for (Point2D p : kdtree.range(testRect)) {
            StdOut.println(p.toString());
        }

        //@Test nearest neighbor
        Point2D c = new Point2D(0.65, 0.44);

        StdDraw.setPenColor(Color.GREEN);
        StdDraw.setPenRadius(0.01);
        c.draw();

        Point2D nearest = kdtree2.nearest(c);
        StdOut.println(nearest.toString());

        StdDraw.setPenRadius();
        StdDraw.line(c.x(), c.y(), nearest.x(), nearest.y());
    }

}
