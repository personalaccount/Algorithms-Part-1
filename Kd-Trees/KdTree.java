import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that uses a 2d-tree to implement PointSET API
 */

public class KdTree {

    private final Node root; // Points to the root of KdTree
    private int numberOfPoints = 0;
    // Iterable Stack object to hold matching points
    private Stack<Point2D> pointsInside;

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
            boolean leftInsert = true; // Helps determine where to insert the new node

            int i = 0; // Node level.

            while (pointer != null) {

                // Check for duplicates
                if (p.equals(pointer.p)) return;

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
                i++; // Increment node level after each iteration.
            }
            // Found an empty spot to make an insert.

            // Create a new node and make an insert.

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
        // Recursively go through each node, comparing each point.
        return contains(p, root, 0);
    }

    // Overload to allow recursion
    private boolean contains(Point2D p, Node n, int level) {
        if (isEmpty()) return false;
        if (n == null) return false;
        if (n.p.equals(p)) return true;

        // Target point is not found.

        // Set the compare flag.
        boolean compareByX = (level % 2 == 0);

        // Increment level for the next iteration.
        level++;

        if (compareByX) {
            // Compare by x; Move left if less and right otherwise.
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
    private void draw(Node n, int level) {

        // Draw a point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();

        // Draw subdivisions
        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            // Vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        }
        else {
            // Horizontal
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }

        // Increment level
        level++;

        if (n.lb != null) draw(n.lb, level);
        if (n.rt != null) draw(n.rt, level);

//        root.rect.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        // Initialize stack
        pointsInside = new Stack<>();

        // Find all the relevant points
        if (!isEmpty()) findPointsInRange(rect, root, 0);

        return pointsInside;
    }

    private void findPointsInRange2(RectHV rect, Node n) {
        // Check if this rectangle intersects with the one corresponding to the node
        if (rect.intersects(n.rect)) {
            addPointsToRange(rect, n);
        }
        else {
            findPointsInRange2(rect, n.lb);
            findPointsInRange2(rect, n.rt);
        }

    }

    private void findPointsInRange(RectHV r, Node n, int level) {
        if (n == null) return;
        // Check if this rectangle intersects with the one corresponding to the node
        if (r.contains(n.p)) pointsInside.push(n.p);

        boolean compareByX = (level % 2 == 0);
        level++; // Increment level for future use

        // Check if r intersects with the splitting line, going through Node's point
        if (compareByX) {
            // Compare with the vertical line
            if (r.xmax() < n.p.x()) {
                // Check the left subtree and avoid the right, since there are no intersections
                findPointsInRange(r, n.lb, level);
            }
            else {
                // Check bot subtrees
                findPointsInRange(r, n.lb, level);
                findPointsInRange(r, n.rt, level);
            }
        }
        else {
            // Compare with the horizontal line
            if (r.ymax() < n.p.y()) {
                // Continue to the left (bottom) subtree, avoiding the right one
                findPointsInRange(r, n.lb, level);
            }
            else {
                // Check both subtrees
                findPointsInRange(r, n.lb, level);
                findPointsInRange(r, n.rt, level);
            }
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
        if (!isEmpty()) return nearest(p, root, root.p.distanceSquaredTo(p), root.p);
        return null;
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

        // Exception for the root node, since distance to root rectangle will always be 0
        if (!n.equals(root)) {
            // Check the distance to the rectangle if it's larger then abort.
            if (n.rect.distanceSquaredTo(p) > closestDistanceYet) return closestPointYet;
        }

        double sqrDistance = p.distanceSquaredTo(n.p); // Distance from query point to node's point

        // If this nodes' point is closer, reassign the values
        if (sqrDistance < closestDistanceYet) {
            closestDistanceYet = sqrDistance;
            closestPointYet = n.p;
        }
        else if (sqrDistance > closestDistanceYet) {
            return closestPointYet;
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
            double rtPointSqrDistance = rtPoint.distanceSquaredTo(p);
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

    public static void main(String[] args) {

        //@Test Create a KdTree object
        KdTree kdtree = new KdTree();
        KdTree kdtree2 = new KdTree();

        //@Test insert
        kdtree2.insert(new Point2D(0.7, 0.2));
        kdtree2.insert(new Point2D(0.5, 0.4));
        kdtree2.insert(new Point2D(0.2, 0.3));
        kdtree2.insert(new Point2D(0.4, 0.7));
        kdtree2.insert(new Point2D(0.9, 0.6));

        //@Test duplicate insert
        kdtree2.insert(new Point2D(0.7, 0.2));
        kdtree2.insert(new Point2D(0.2, 0.3));
        kdtree2.insert(new Point2D(0.2, 0.3));
        kdtree2.insert(new Point2D(0.2, 0.3));
        kdtree2.insert(new Point2D(0.9, 0.6));

        //@Test file insert
        String filename = "kdtree-tests/circle10.txt";
        In in = new In(filename);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        KdTree kT = kdtree;

        //@Test Contains
        Point2D a = new Point2D(0.500000, 1.000000);
        Point2D b = new Point2D(0.7, 0.2);

        StdOut.println("Tree contains: " + a.toString() + " - " + kT.contains(a));
        StdOut.println("Tree contains: " + a.toString() + " - " + kT.contains(b));


        //@Test draw points
        kT.draw();

        //@Test rectangle
        RectHV testRect = new RectHV(0.07, 0.17, 0.69, 0.93);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        testRect.draw();

        StdOut.println("\nPoints in range:");
        for (Point2D p : kT.range(testRect)) {
            StdOut.println(p.toString());
        }

        //@Test nearest neighbor
        Point2D c = new Point2D(0.65, 0.94);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        c.draw();

        Point2D nearest = kT.nearest(c);
        StdOut.println("\nNearest point to: " + c.toString() + " is " + nearest.toString());

        StdDraw.setPenRadius();
        StdDraw.line(c.x(), c.y(), nearest.x(), nearest.y());
    }

}
