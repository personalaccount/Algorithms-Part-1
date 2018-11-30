import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;
import java.util.TreeSet;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that represents a set of points in the unit square
 */

public class PointSET {

    private TreeSet<Point2D> points;

    // Construct an empty set of points
    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    // Is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // Number of points in the set
    public int size() {
        return points.size();
    }

    // Add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!contains(p)) points.add(p);
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // All points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) throw new IllegalArgumentException();

        Stack<Point2D> pointsInside = new Stack<>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointsInside.push(p);
            }
        }

        return pointsInside;
    }
//
//    // a nearest neighbor in the set to point p; null if the set is empty
//    // find a closest point to a query point
//    public Point2D nearest(Point2D p) {
//
//    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET ps = new PointSET();
        Point2D a = new Point2D(0.0, 0.0);
        Point2D b = new Point2D(0.1, 0.4);
        Point2D c = new Point2D(0.6, 0.5);

        ps.insert(a);
        ps.insert(b);
        ps.insert(c);

        StdOut.println(ps.isEmpty());
        StdOut.println(ps.contains(a));
        StdOut.println(ps.size());

        RectHV r = new RectHV(0.4, 0.3, 0.8, 0.6);

        StdOut.println("Points inside: ");
        for (Point2D p: ps.range(r)) {
            StdOut.print(p.toString() + " ");
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        StdDraw.setXscale(-0.4, 1.2);
        StdDraw.setYscale(-0.4, 1.2);

        ps.draw();
        r.draw();
    }
}