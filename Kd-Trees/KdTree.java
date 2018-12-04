import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Stack;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 * <p>
 * Mutable data type that uses a 2d-tree to implement PointSET API
 */

public class KdTree {

    KdTree points;

    // construct an empty set of points
    public KdTree() {

    }

    private void exceptionIfNull(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    // Number of points in the set
    public int size() {
        return points.size();
    }

    // Add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        exceptionIfNull(p);
        if (!contains(p)) points.add(p);
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        exceptionIfNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
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
